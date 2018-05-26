package Dao.implementation

import java.sql.Date

import Dao.`trait`.OutHouseDao
import javax.inject.Inject
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
/**
	* author: dulei
	* date: 2018-05-26
	* desc: 
	*/
class OutHouseDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile]
	with OutHouseDao with StockOutInfoSchema{
	override protected val dbConfig = dbConfigProvider.get[JdbcProfile]

	import dbConfig.profile.api._

	val stockOutInfos = TableQuery[StockOutTable]

	implicit val getOutShelfResult = GetResult(r => OutShelfView(r.<<, r.<<, r.<<, r.<<))
	implicit val getOutHouseResult = GetResult(r => OutHouseView(r.<<, r.<<, r.<<, r.<<, r <<, r.<<))

	override def insert(barcode: String, merchNum: Int) = {
		db.run(stockOutInfos += StockOut(0, barcode, merchNum, new Date(System.currentTimeMillis()), 0)).map(
			res => "insert new stock out record successfully"
		).recover {
			case ex: Exception => ex.getCause.getMessage
		}
	}

	override def update(outStockId: Long) = {
		//这里只更新状态，出货数量更新由触发器完成
		db.run(stockOutInfos.filter(_.outstockId === outStockId).map(res => (res.outDate, res.outState)).update(new Date(System.currentTimeMillis()), 1))
	}

	override def listAll(args: String*): Future[Seq[Any]] = {
			val start = args.apply(0).toInt
			val limit = args.apply(1).toInt
		if (args.length == 3) {
			//出库清单第二栏为将要出库的商品，来自视图view_merch_stock_out,采取分页查询;
			val listSql = sql"""select * from view_merch_stock_out where out_state='未出库' limit $start,$limit""".as[OutHouseView]
			val arg = args.apply(2)
			if (arg.equals("")) {
				return db.run(listSql)
			}
			val likeArg = "%" + arg + "%"
			val likeSql = sql"""select * from view_merch_stock_out where merch_name like $likeArg or barcode like $likeArg limit $start,$limit""".as[OutHouseView]
			return db.run(likeSql)
		}else{
			//出库清单第一栏为需要出库的商品，来自视图view_merch_shelf_out，并且只查询未准备出库的商品信息，返回分页结果
			val pageSql = sql"""select * from view_merch_shelf_out as a where a.barcode not in (select barcode from view_merch_stock_out)  limit $start,$limit""".as[OutShelfView]
			db.run(pageSql)
		}
	}

	override def getCount(searchArg: String*) = {
		if (searchArg.equals("")) {
			Await.result(db.run(sql"""select COUNT(*) from view_merch_stock_out""".as[Int]), Duration.Inf).headOption.get

		}else{
			val likeArg = "%"+ searchArg + "%"
			val sql = sql"""select COUNT(*) from view_merch_stock_out where barcode like $likeArg or merch_name like $likeArg"""
			Await.result(db.run(sql.as[Int]), Duration.Inf).headOption.get
		}
		Await.result(db.run(sql"""select count(*) from view_merch_shelf_out""".as[Int]),Duration.Inf).headOption.get
	}
}
