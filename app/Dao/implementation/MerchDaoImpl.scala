package Dao.implementation

import Dao.`trait`.MerchDao
import javax.inject.Inject
import models.{Merch, MerchInfoSchema, UpdateMerch}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
/**
	* author: dulei
	* date: 2018-05-21
	* desc: 
	*/
class MerchDaoImpl @Inject()()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with MerchInfoSchema with MerchDao {
	override protected val dbConfig = dbConfigProvider.get[JdbcProfile]

	import dbConfig.profile.api._

	val merchInfos = TableQuery[MerchInfoTable]

	override def add(merch: Merch): Future[String] = {
		db.run(merchInfos += merch).map(
			res => "merch info added successfully"
		).recover {
			case ex: Exception => ex.getCause.getMessage
		}
	}

	override def delete(id: Long): Future[Int] = {
		db.run(merchInfos.filter(_.merchId === id).delete)
	}

	override def get(id: Long): Future[Option[Merch]] = {
		db.run(merchInfos.filter(_.merchId === id).result.headOption)
	}

	/**
		* desc:列出所有符合条件的商品信息
		*
		* @param args args[0],args[1]表示分页参数start和limit,arg[2]表示查询参数
		*             目前可以是merch_name，barcode，merchtype_id，provide_id
		* @return
		*/
	override def listAll(args: String*): Future[Seq[Merch]] = {
		if (args.length == 3) {
			implicit val getMerchResult = GetResult(r => Merch(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
			val start = args.apply(0).toInt
			val limit = args.apply(1).toInt
			val arg = args.apply(2)
			if(arg.equals("")){//初次加载页面或者没有输入搜索参数时，返回所有
				return db.run(merchInfos.result)
			}
			val likeArg = "%"+ arg + "%"
			val sql = sql"""select * from merch_info where merch_name like $likeArg or barcode = $arg or merchtype_id = $arg or provide_id = $arg  limit $start,$limit"""
			db.run(sql.as[Merch])
		} else {
			db.run(merchInfos.result)
		}
	}

	override def update(updateData: UpdateMerch): Future[String] = {
		db.run(merchInfos.filter(_.merchId === updateData.merchId).map(
			res => (res.merchTypeId, res.merchName, res.barcode, res.cautionNum, res.selfPreNum, res.merchPrice, res.salePrice, res.planNum, res.provideId)
		).update((updateData.merchTypeId, updateData.merchName, updateData.barcode, updateData.cautionNum, updateData.selfPreNum, updateData.merchPrice, updateData.salePrice, updateData.planNum, updateData.provideId))).map(
			res => "merch info updated successfully"
		).recover {
			case ex: Exception => ex.getCause.getMessage
		}
	}

	override def getCount(searchArg: String): Int = {
		if (searchArg.equals("")) {
			 Await.result(db.run(sql"""select COUNT(*) from merch_info""".as[Int]), Duration.Inf).headOption.get

		}else{
			val likeArg = "%"+ searchArg + "%"
			val sql = sql"""select COUNT(*) from merch_info where merch_name like $likeArg or barcode = $searchArg  or merchtype_id = $searchArg or provide_id = $searchArg"""
			 Await.result(db.run(sql.as[Int]), Duration.Inf).headOption.get
		}
	}
}
