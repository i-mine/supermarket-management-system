package models

import slick.jdbc.JdbcProfile

/**
  * author: dulei
  * date: 18-5-10
  * desc: 供应商表对应的Bean对象
  * +-----------------+--------------+------+-----+---------+-------+
  * | Field           | Type         | Null | Key | Default | Extra |
  * +-----------------+--------------+------+-----+---------+-------+
  * | provide_id      | int(10)      | NO   | PRI | NULL    |       |
  * | provide_name    | varchar(50)  | NO   |     | NULL    |       |
  * | provide_address | varchar(250) | NO   |     | NULL    |       |
  * | provide_phone   | varchar(13)  | NO   |     | NULL    |       |
  * +-----------------+--------------+------+-----+---------+-------+
  *
  */
case class Provide(provideId: Long,
                   provideName: String,
                   provideAddress: String,
                   providePhone: String)
case class ProvideFormData(provideName: String,
                           provideAddress: String,
                           providePhone: String)
trait ProvideInfoSchema {
  protected val driver: JdbcProfile

  import driver.api._

  class ProvideInfoTable(tag: Tag) extends Table[Provide](tag, "provide_info") {
    def provideId = column[Long]("provide_id", O.PrimaryKey)

    def provideName = column[String]("provide_name")

    def provideAddress = column[String]("provide_address")

    def providePhone = column[String]("provide_phone")

    def * = (provideId, provideName, provideAddress, providePhone) <> (Provide.tupled, Provide.unapply)
  }

}
