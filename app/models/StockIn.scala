package models

import java.sql.Date

import slick.jdbc.JdbcProfile

/**
	* author: dulei
	* date: 2018-05-24
	* desc: 进货/入库表对应的Bean对象
	* +--------------+-------------+------+-----+---------+----------------+
	* | Field        | Type        | Null | Key | Default | Extra          |
	* +--------------+-------------+------+-----+---------+----------------+
	* | instock_id   | int(10)     | NO   | PRI | NULL    | auto_increment |
	* | barcode      | varchar(13) | NO   |     | NULL    |                |
	* | merch_num    | int(4)      | NO   |     | NULL    |                |
	* | merch_price  | float(4,2)  | NO   |     | NULL    |                |
	* | in_date      | date        | YES  |     | NULL    |                |
	* | plan_date    | date        | NO   |     | NULL    |                |
	* | arrive_state | tinyint(1)  | NO   |     | NULL    |                |
	* | in_state     | tinyint(1)  | NO   |     | NULL    |                |
	* +--------------+-------------+------+-----+---------+----------------+
	*/
case class StockIn(instockId: Long,
                   barcode: String,
                   merchNum: Int,
                   merchPrice: Float,
                   inDate: Date,
                   planDate: Date,
                   arriveState: Int,
                   inState: Int)

/**
	* desc: 手动填写订单信息
	*
	* @param barcode    条形码
	* @param merchNum   进货数量
	* @param merchPrice 进价
	* @param planDate   进货日期
	*/
case class OrderFormData(barcode: String, merchNum: Int, merchPrice: String, planDate: String)

/**
	* desc: 更新订单信息
	*
	* @param orderId     即instockId,订单号/入库号
	* @param barcode     条形码
	* @param merchNum    进货数量
	* @param merchPrice  进价
	* @param planDate    进货日期
	* @param arriveState 到货状态
	*/
case class Order(orderId: Long, barcode: String, merchNum: Int, merchPrice: String, planDate: String, arriveState: Int)

trait StockInInfoSchema {
	protected val driver: JdbcProfile

	import driver.api._

	class StockInInfoTable(tag: Tag) extends Table[StockIn](tag, "stock_in_record") {
		def instockId = column[Long]("instock_id", O.PrimaryKey, O.AutoInc)

		def barcode = column[String]("barcode")

		def merchNum = column[Int]("merch_num")

		def merchPrice = column[Float]("merch_price")

		def inDate = column[Date]("in_date")

		def planDate = column[Date]("plan_date")

		def arriveState = column[Int]("arrive_state")

		def inState = column[Int]("in_state")

		def * = (instockId, barcode, merchNum, merchPrice, inDate, planDate, arriveState, inState) <> (StockIn.tupled, StockIn.unapply)
	}

}