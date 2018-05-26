package models

import java.sql.Date

import slick.jdbc.JdbcProfile

/**
	* author: dulei
	* date: 2018-05-26
	* desc: 出货表对应的Bean对象
	* +-------------+-------------+------+-----+---------+----------------+
	* | Field       | Type        | Null | Key | Default | Extra          |
	* +-------------+-------------+------+-----+---------+----------------+
	* | outstock_id | int(10)     | NO   | PRI | NULL    | auto_increment |
	* | barcode     | varchar(13) | NO   |     | NULL    |                |
	* | merch_num   | int(4)      | NO   |     | NULL    |                |
	* | out_date    | date        | NO   |     | NULL    |                |
	* | out_state   | tinyint(1)  | NO   |     | NULL    |                |
	* +-------------+-------------+------+-----+---------+----------------+
	*/
case class StockOut(outstockId: Long, barcode: String, merchNum:Int, outDate: Date, outState: Int)

trait StockOutInfoSchema{
	protected val driver: JdbcProfile

	import driver.api._

	class StockOutTable(tag: Tag) extends Table[StockOut](tag, "stock_out_record"){
		def outstockId = column[Long]("outstock_id", O.PrimaryKey, O.AutoInc)
		def barcode = column[String]("barcode")
		def merchNum = column[Int]("merch_num")
		def outDate = column[Date]("out_date")
		def outState = column[Int]("out_state")
		def * = (outstockId, barcode, merchNum, outDate, outState) <> (StockOut.tupled, StockOut.unapply)
	}
}