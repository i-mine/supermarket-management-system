package Dao.implementation

import Dao.`trait`.MemberDao
import javax.inject.Inject
import models.{Member, MemberInfoSchema}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * author: dulei
  * date: 2018-05-17
  * desc: 
  */
class MemberDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with MemberInfoSchema with MemberDao{
  override protected val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.profile.api._

  val memberInfos = TableQuery[MemberInfoTable]

  override def add(member: Member): Future[String] = {
    db.run(memberInfos += member).map(
      res => "member info added successfully"
    ).recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Long):Future[Int] ={
    db.run(memberInfos.filter(_.memberId === id).delete)
  }

  override def get(id: Long): Future[Option[Member]] = {
    db.run(memberInfos.filter(_.memberId === id).result.headOption)
  }

  override def listAll(args: String*): Future[Seq[Member]] = {
    if(args.length == 1){
      //实现search查询,args这里只有一个，即电话号码
      val arg = args.apply(0)
      return db.run(memberInfos.filter(_.memberPhone === arg).result)
    }
    //实现分页查询
    if(args.length == 2){
     val (start,limit) = (args.apply(0).toInt,args.apply(1).toInt)
      return db.run(memberInfos.drop(start).take(limit).result)
    }
    //全部查询
    db.run(memberInfos.result)
  }

  override def update(memberName: String,memberPhone: String,memberId: Long): Future[String] = {
    db.run(memberInfos.filter(_.memberId === memberId).map(res=> (res.memberName,res.memberPhone)).update(memberName,memberPhone)).map(
      res=> "member updated successfully"
    ).recover{
      case ex: Exception => ex.getCause.getMessage
    }

  }

  override def getCount():Int = {
    val res = Await.result(db.run(sql"""select COUNT(*) from member_info""".as[Int]),Duration.Inf).headOption
    res.get
  }
}
