package models
import slick.jdbc.JdbcProfile


/**
  * author: dulei
  * date: 18-5-8
  * desc: 员工表对应的Bean对象
  * +-------------+--------------+------+-----+---------+----------------+
  * | Field       | Type         | Null | Key | Default | Extra          |
  * +-------------+--------------+------+-----+---------+----------------+
  * | staff_id    | int(10)      | NO   | PRI | NULL    | auto_increment |
  * | position_id | int(10)      | NO   | MUL | NULL    |                |
  * | staff_name  | varchar(10)  | NO   |     | NULL    |                |
  * | gender      | char(1)      | NO   |     | NULL    |                |
  * | teleNumber  | varchar(13)  | NO   |     | NULL    |                |
  * | address     | varchar(250) | NO   |     | NULL    |                |
  * | password    | varchar(18)  | NO   |     | NULL    |                |
  * | authority   | varchar(9)   | NO   |     | NULL    |                |
  * +-------------+--------------+------+-----+---------+----------------+
  */

case class Staff(staffId: Long,
                 positionId: Long,
                 staffName: String,
                 gender: Char,
                 teleNumber: String,
                 address: String,
                 password: String,
                 authority: String
                )

/**
  * desc:用于展示职员特定的信息
  */
case class StaffTable(staffId: String,
                      position: String,
                      staffName: String,
                      gender: String,
                      teleNumber: String,
                      address: String)

/**
  *desc:用于添加或修改职员时需要填写的表单
  */
case class StaffFormData(staffName: String,
                         position: String,
                         password: String,
                         gender: String,
                         teleNumber: String,
                         address: String
                        )

/**
  * desc:用于填写登录表单
  */
case class LoginFormData(username: String, password: String)

trait StaffInfoSchema {
  protected val driver: JdbcProfile

  import driver.api._

  class StaffInfoTable(tag: Tag) extends Table[Staff](tag, "staff_info") {
    def staffId = column[Long]("staff_id", O.PrimaryKey, O.AutoInc)

    def positionId = column[Long]("position_id")

    def staffName = column[String]("staff_name")

    def gender = column[Char]("gender")

    def teleNumber = column[String]("teleNumber")

    def address = column[String]("address")

    def password = column[String]("password")

    def authority = column[String]("authority")

    def * = (staffId, positionId, staffName, gender, teleNumber, address, password, authority) <> (Staff.tupled, Staff.unapply)
  }
}


