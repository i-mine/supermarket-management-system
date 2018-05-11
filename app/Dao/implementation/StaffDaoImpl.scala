package Dao.implementation

import javax.inject.Inject
import Dao.`trait`.StaffDao
import models.{Staff, StaffInfoSchema}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.JdbcProfile

import scala.concurrent.Future


/**
  * author: dulei
  * date: 18-5-8
  * desc: 
  */
class StaffDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with StaffDao with StaffInfoSchema {
  override val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.profile.api._

  val staffInfos = TableQuery[StaffInfoTable]

  override def check(username: String, password: String): Future[Boolean] = {
    db.run(staffInfos.filter(staff => staff.staffName === username && staff.password === password).result.headOption).map(
      res => {
        if (res.get.staffId > 0) true
        else false
      }
    )
  }

  override def get(username: String): Future[Option[Staff]] = {
    db.run(staffInfos.filter(staff => staff.staffName === username).result.headOption)
  }

  override def get(id: Long): Future[Option[Staff]] = {
    db.run(staffInfos.filter(_.staffId === id).result.headOption)
  }

  override def listAll(args: String*): Future[Seq[Staff]] = {
    if(args.length == 1){
      for(arg <- args){
        return db.run(staffInfos.filter(_.staffName.like(s"%$arg%")).result)
      }
    }
    db.run(staffInfos.result)
  }

  override def update(newStaff: Staff): Future[String] = {
    db.run(staffInfos.filter(_.staffId === newStaff.staffId).update(newStaff)
    ).map(res => "staff info update successfully").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def add(staff: Staff): Future[String] = {
    db.run(staffInfos += staff).map(res=>"staff info add successfully").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  override def delete(id: Long): Future[Int] = {
    db.run(staffInfos.filter(_.staffId === id).delete)
  }
}
