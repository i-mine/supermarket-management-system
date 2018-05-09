package services

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.JdbcProfile

/**
  * author: dulei
  * date: 18-5-8
  * desc: 提供slick执行需要的db
  */
class DBService @Inject()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] {
  override val dbConfig = dbConfigProvider.get[JdbcProfile]
  val DB = db
}
