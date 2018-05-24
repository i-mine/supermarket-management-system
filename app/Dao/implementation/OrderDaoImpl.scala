package Dao.implementation

import java.sql.Date

import Dao.`trait`.OrderDao
import javax.inject.Inject
import models.{Order, StockIn, StockInInfoSchema}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.{GetResult, JdbcProfile}
import utils.TimeUtil

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
/**
	* author: dulei
	* date: 2018-05-24
	* desc: 
	*/
class OrderDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile]
	with StockInInfoSchema with OrderDao {
	override protected val dbConfig = dbConfigProvider.get[JdbcProfile]

	import dbConfig.profile.api._

	val stockInInfoTable = TableQuery[StockInInfoTable]
	implicit val getStockResult = GetResult(r => StockIn(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<,r.<<, r.<<))
	implicit val getOrderResult = GetResult(r => Order(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

	override def add(order: Order): Future[String] = {
		val barcode = order.barcode
		val merchNum = order.merchNum
		val merchPrice = order.merchPrice
		val planDate = order.planDate
		val insertSql =
			sqlu"""INSERT INTO stock_in_record
		(barcode, merch_num, merch_price, in_date, plan_date, arrive_state, in_state)
		VALUES($barcode, $merchNum, $merchPrice, '1970-01-01', $planDate, 0, 0)"""

		db.run(insertSql).map(
			res => "add order successfully"
		).recover {
			case ex: Exception => ex.getCause.getMessage
		}

	}

	/**
		* desc: 取消指定id的订单，其实是指将该订单的计划日期置空，并不删除该条记录
		*
		* @param id
		* @return
		*/
	override def delete(id: Long): Future[String] = {
		db.run(stockInInfoTable.filter(_.instockId === id).map(_.planDate).update(new Date(0))).map(
			res => "update planDate to null successfully"
		).recover {
			case ex: Exception => ex.getCause.getMessage
		}
	}

	override def get(id: Long): Future[Option[Order]] = {
		db.run(sql"""select instock_id,barcode,merch_num,merch_price,plan_date,arrive_state from stock_in_record where instock_id = $id """.as[Order].headOption)
	}

	/**
		* desc: 按照分页参数返回指定分页的订单信息
		*
		* @param args args[0]:start,args[1]:limit, args[2]:searchValue
		* @return
		*/
	override def listAll(args: String*): Future[Seq[Order]] = {
		val orderListSql = sql"""select instock_id,barcode,merch_num,merch_price,plan_date,arrive_state from stock_in_record where plan_date <> "null" and arrive_state = 0 and plan_date > '1970-01-01'""".as[Order]
		if (args.length == 3) {
			val start = args.apply(0).toInt
			val limit = args.apply(1).toInt
			val arg = args.apply(2)
			if (arg.equals("")) {
				return db.run(orderListSql)
			}
			val likeArg = "%" + arg + "%"
			val likeSql = sql"""select instock_id,barcode,merch_num,merch_price,plan_date,arrive_state from stock_in_record where barcode like $likeArg and plan_date <> "null" limit $start,$limit""".as[Order]
			db.run(likeSql)
		} else {
			db.run(orderListSql)
		}
	}

	override def update(newOrder: Order):Future[String] = {
		val planDate = TimeUtil.StringToDate(newOrder.planDate)
		db.run(stockInInfoTable.filter(_.instockId === newOrder.orderId).map(
			res =>(res.barcode, res.merchNum, res.merchPrice, res.planDate, res.arriveState)
		).update((newOrder.barcode, newOrder.merchNum, newOrder.merchPrice.toFloat, planDate, newOrder.arriveState))).map(
			res => "update order info sucessfully"
		).recover{
			case ex: Exception => ex.getCause.getMessage
		}
	}

	override def getCount(searchArg: String) = {
		if (searchArg.equals("")) {
			Await.result(db.run(sql"""select COUNT(*) from stock_in_record where plan_date<>"null"""".as[Int]), Duration.Inf).headOption.get

		}else{
			val likeArg = "%"+ searchArg + "%"
			val sql = sql"""select COUNT(*) from stock_in_record where barcode like $likeArg and plan_date <> "null""""
			Await.result(db.run(sql.as[Int]), Duration.Inf).headOption.get
		}
	}
}
