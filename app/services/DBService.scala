package services

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.JdbcProfile

/**
  * author: dulei
  * date: 18-5-8
  * desc: 提供操作各个Bean数据的Service
  */
class DBService @Inject()(staffDBService: StaffDBService)  {
  val staff_DB = staffDBService
}
