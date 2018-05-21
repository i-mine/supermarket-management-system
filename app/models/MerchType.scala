package models

import slick.jdbc.JdbcProfile

/**
	* author: dulei
	* date: 2018-05-20
	* desc: 商品类型对应的Bean对象
	* +----------------+-------------+------+-----+---------+----------------+
	* | Field          | Type        | Null | Key | Default | Extra          |
	* +----------------+-------------+------+-----+---------+----------------+
	* | merchtype_id   | int(10)     | NO   | PRI | NULL    | auto_increment |
	* | merchtype_name | varchar(50) | NO   |     | NULL    |                |
	* | merch_num      | int(4)      | YES  |     | 0       |                |
	* +----------------+-------------+------+-----+---------+----------------+
	*/
case class MerchType(merchTypeId: Long, merchTypeName: String, merchTypeNum: Int)

trait MerchTypeSchema {
	protected val driver: JdbcProfile

	import driver.api._

	class MerchTypeTable(tag: Tag) extends Table[MerchType](tag, "merchtype_info") {
		def merchTypeId = column[Long]("merchtype_id", O.PrimaryKey, O.AutoInc)

		def merchTypeName = column[String]("merchtype_name")

		def merchTypeNum = column[Int]("merch_num")

		def * = (merchTypeId, merchTypeName, merchTypeNum) <> (MerchType.tupled, MerchType.unapply)
	}

}