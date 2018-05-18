package models


import java.sql.Date

import slick.jdbc.JdbcProfile

/**
  * author: dulei
  * date: 2018-05-16
  * desc: 会员表对应的Bean对象
  * +--------------+-------------+------+-----+---------+----------------+
  * | Field        | Type        | Null | Key | Default | Extra          |
  * +--------------+-------------+------+-----+---------+----------------+
  * | member_id    | int(10)     | NO   | PRI | NULL    | auto_increment |
  * | member_name  | varchar(10) | NO   |     | NULL    |                |
  * | member_phone | varchar(13) | NO   |     | NULL    |                |
  * | integral     | int(10)     | YES  |     | 0       |                |
  * | totalcost    | int(10)     | YES  |     | 0       |                |
  * | reg_date     | date        | NO   |     | NULL    |                |
  * | last_date    | date        | YES  |     | NULL    |                |
  * +--------------+-------------+------+-----+---------+----------------+
  */
case class Member(memberId: Long,
                  memberName: String,
                  memberPhone: String,
                  integral: Long,
                  totalCost: Long,
                  regDate: Date,
                  lastDate: Date)

/**
  * desc: 用于会员显示分页
  * @param start 分页开始记录索引
  * @param limit 单页记录数
  * @param data 单页所有记录对象
  * @param total 数据表中所有记录数量
  */
case class MemberPage(start: Int, limit: Int, data: List[Member], total: Int)

/**
  * desc:用于添加或修改会员信息需要填写的表单
  * @param memberName 会员名字
  * @param memberPhone 会员电话
  */
case class MemberFormData(memberName: String,memberPhone: String)


trait MemberInfoSchema {
  protected val driver: JdbcProfile
  import driver.api._

  class MemberInfoTable(tag: Tag) extends Table[Member](tag, "member_info") {
    def memberId = column[Long]("member_id", O.PrimaryKey,O.AutoInc)

    def memberName = column[String]("member_name")

    def memberPhone = column[String]("member_phone")

    def integral = column[Long]("integral")

    def totalCost = column[Long]("totalcost")

    def regDate = column[Date]("reg_date")

    def lastDate = column[Date]("last_date")

    def * = (memberId, memberName, memberPhone, integral,totalCost,regDate,lastDate) <> (Member.tupled,Member.unapply)
  }
}
