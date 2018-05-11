package Dao.implementation
import Dao.`trait`.ProvideDao
import javax.inject.Inject
import models.{Provide, ProvideInfoSchema}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
/**
  * author: dulei
  * date: 18-5-10
  * desc: 
  */
class ProvideDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with ProvideInfoSchema with ProvideDao{
  override protected val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.profile.api._

  val provideInfos = TableQuery[ProvideInfoTable]

  override def add(provide: Provide): Future[String] = {
    db.run(provideInfos += provide).map(res => "provide info added successfully").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Long): Future[Int] = {
    db.run(provideInfos.filter(_.provideId === id).delete)
  }

  override def get(id: Long): Future[Option[Provide]] = {
    db.run(provideInfos.filter(_.provideId === id).result.headOption)
  }

  override def listAll(args: String*): Future[Seq[Provide]] = {
    if(args.length == 1){
      //实现查询search的逻辑，arg在这里最多只有一个
      for(arg <-args){
        return db.run(provideInfos.filter(_.provideName.like(s"%$arg%")).result)
      }
    }
    db.run(provideInfos.result)
  }

  override def update(newProvide: Provide): Future[String] = {
    db.run(provideInfos.filter(_.provideId === newProvide.provideId).update(newProvide)).map(
      res => "provide successfully updated"
    ).recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }
}
