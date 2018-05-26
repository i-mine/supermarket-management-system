package Dao.implementation

import Dao.`trait`.InHouseDao
import javax.inject.Inject
import models.InHouseView
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
/**
	* author: dulei
	* date: 2018-05-25
	* desc: 
	*/
class InHouseDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with InHouseDao{
	override protected val dbConfig = dbConfigProvider.get[JdbcProfile]

	import dbConfig.profile.api._
	implicit val getInHouseResult = GetResult(r => InHouseView(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
	override def get(id: Long):Future[Option[InHouseView]] = {
		val sql = sql"""select * from view_merch_stock_in where instock_id = $id""".as[InHouseView]
		db.run(sql.headOption)
	}

	override def listAll(args: String*):Future[Seq[InHouseView]] = {
			val start = args.apply(0).toInt
			val limit = args.apply(1).toInt
			val listSql = sql"""select * from view_merch_stock_in where in_date = '1970-01-01' limit $start,$limit""".as[InHouseView]
		if(args.length ==3){
			val arg = args.apply(2)
			if(arg.equals("")){
				return db.run(listSql)
			}
			val likeArg = "%" + arg + "%"
			val likeSql = sql"""select * from view_merch_stock_in where barcode like $likeArg or merch_name like $likeArg limit $start,$limit""".as[InHouseView]
			db.run(likeSql)
		}else{
			db.run(listSql)
		}

	}

	override def update(instockId: Long):Future[String] = {
		//这里只更新状态，出货数量更新由触发器完成
		val updateSql = sqlu"""update stock_in_record set in_state = 1,in_date=current_date where instock_id = $instockId"""
		db.run(updateSql).map(
			res => "update inhouse state successfully"
		).recover{
			case ex: Exception => ex.getCause.getMessage
		}
	}

	override def getCount(searchArg: String):Int = {
		if (searchArg.equals("")) {
			Await.result(db.run(sql"""select COUNT(*) from view_merch_stock_in where in_date = '1970-01-01'""".as[Int]), Duration.Inf).headOption.get

		}else{
			val likeArg = "%"+ searchArg + "%"
			val sql = sql"""select COUNT(*) from view_merch_stock_in where barcode like $likeArg or merch_name like $likeArg"""
			Await.result(db.run(sql.as[Int]), Duration.Inf).headOption.get
		}
	}
}
