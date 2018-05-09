package Dao.implementation

import javax.inject.Inject

import Dao.`trait`.StaffDao
import models.StaffInfoSchema
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.JdbcProfile

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * author: dulei
  * date: 18-5-8
  * desc: 
  */
class StaffDaoImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with StaffDao with StaffInfoSchema {
  override val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig.profile.api._

  val staff = TableQuery[StaffInfoTable]

  override def check(username: String, password: String): Future[Boolean] = {
    db.run(staff.filter(staff => staff.staffName === username && staff.password === password).result.headOption).map(
      res => {
        if (res.get.staffId > 0) true
        else false
      }
    )
  }

}
