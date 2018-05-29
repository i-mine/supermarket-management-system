package Dao.implementation

import Dao.`trait`.PromotionDao
import javax.inject.Inject
import models.{Promotion, Strategy}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * author: dulei
  * date: 18-5-29
  * desc: 
  */
class PromotionDaoImpl @Inject()()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with PromotionDao{
  override protected val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.profile.api._

  implicit val getPromotionResult = GetResult(r => Promotion(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  implicit val getStrategyResult = GetResult(r => Strategy(r.<<, r.<<, r.<<, r.<<, r.<<))

  override def add(strategy: Strategy): Future[String] = {
    val (barcode,startDate,endDate,discount) = (strategy.barcode, strategy.startDate, strategy.endDate, strategy.discount)
    val insertSql = sqlu"""INSERT INTO supermarket_management_system.sale_strategy
        (barcode, sale_pro_start, sale_pro_stop, discount)
         VALUES($barcode, $startDate, $endDate, $discount )"""
    db.run(insertSql).map(
      res => "add strategy successfully"
    ).recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Long): Future[Int] = {
    db.run(sqlu"""delete from sale_strategy where strategy_id = $id""")
  }

  override def get(id: Long): Future[Option[Strategy]] = {
    db.run(sql"""select * from sale_strategy where strategy_id = $id""".as[Strategy].headOption)
  }

  override def listAll(args: String*): Future[Seq[Promotion]] = {
    //默认至少两个参数
    val start = args.apply(0).toInt
    val limit = args.apply(1).toInt
    val promotionListSql = sql"""select strategy_id,merch_name,sale_strategy.barcode,sale_pro_start,sale_pro_stop,discount from sale_strategy,merch_info where sale_strategy.barcode = merch_info.barcode limit $start,$limit""".as[Promotion]
    if(args.length == 3){
      val arg = args.apply(2)
      if (arg.equals("")) {
        return db.run(promotionListSql)
      }
      val likeArg = "%" + arg + "%"
      val likeSql = sql"""select strategy_id,merch_name,sale_strategy.barcode,sale_pro_start,sale_pro_stop,discount from sale_strategy inner join merch_info on sale_strategy.barcode = merch_info.barcode where sale_strategy.barcode like $likeArg or merch_name like $likeArg limit $start,$limit""".as[Promotion]
      db.run(likeSql)
    }else{
      db.run(promotionListSql)
    }
  }

  override def getCount(searchArg: String): Int = {
    if (searchArg.equals("")) {
      Await.result(db.run(sql"""select COUNT(*) from sale_strategy""".as[Int]), Duration.Inf).headOption.get

    }else{
      val likeArg = "%"+ searchArg + "%"
      val sql = sql"""select COUNT(*) from sale_strategy inner join merch_info on sale_strategy.barcode = merch_info.barcode  where sale_strategy.barcode like $likeArg or merch_name like $likeArg"""
      Await.result(db.run(sql.as[Int]), Duration.Inf).headOption.get
    }
  }

  override def update(newStragegy: Strategy): Future[String] = {
    val id = newStragegy.strategyId
    val (barcode,startDate,endDate,discount) = (newStragegy.barcode, newStragegy.startDate, newStragegy.endDate, newStragegy.discount)
    db.run(sqlu"update sale_strategy set barcode=$barcode, sale_pro_start=$startDate, sale_pro_stop=$endDate,discount=$discount where strategy_id = $id").map(
      res => "update strategy successfully"
    ).recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }
}
