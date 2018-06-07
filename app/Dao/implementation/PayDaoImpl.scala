package Dao.implementation
import Dao.`trait`.PayDao
import javax.inject.Inject
import models.Goods
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * author: dulei
  * date: 18-6-7
  * desc: 
  */
class PayDaoImpl @Inject()()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with PayDao {
  override protected val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.profile.api._

  implicit val GoodsResult = GetResult(r => Goods(r.<<, r.<<, r.<<, r.<<))
  override def get(barcode: String): Future[Option[Goods]] = {
    val sql = sql"""select barcode,merch_name,sale_price,discount from view_merch_strategy_forpay where barcode=$barcode""".as[Goods].headOption
    db.run(sql)
  }
}
