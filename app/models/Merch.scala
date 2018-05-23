package models

import slick.jdbc.JdbcProfile

/**
	* author: dulei
	* date: 2018-05-20
	* desc: 商品信息表对应的Bean对象
	* +---------------+-------------+------+-----+---------+----------------+
	* | Field         | Type        | Null | Key | Default | Extra          |
	* +---------------+-------------+------+-----+---------+----------------+
	* | merch_id      | int(10)     | NO   | PRI | NULL    | auto_increment |
	* | merchtype_id  | int(4)      | NO   | MUL | NULL    |                |
	* | merch_name    | varchar(50) | NO   |     | NULL    |                |
	* | barcode       | varchar(13) | NO   |     | NULL    |                |
	* | stock_num     | int(4)      | YES  |     | 0       |                |
	* | caution_num   | int(4)      | YES  |     | 0       |                |
	* | shelf_num     | int(4)      | YES  |     | 0       |                |
	* | shelf_pre_num | int(4)      | YES  |     | 0       |                |
	* | merch_price   | float(4,2)  | NO   |     | NULL    |                |
	* | sale_price    | float(4,2)  | NO   |     | NULL    |                |
	* | plan_num      | int(4)      | NO   |     | NULL    |                |
	* | alow_sale     | tinyint(1)  | YES  |     | 1       |                |
	* | provide_id    | int(10)     | NO   | MUL | NULL    |                |
	* +---------------+-------------+------+-----+---------+----------------+
	*/
case class Merch(merchId: Long,
                 merchTypeId: Long,
                 merchName: String,
                 barcode: String,
                 stockNum: Int,
                 cautionNum: Int,
                 selfNum: Int,
                 selfPreNum: Int,
                 merchPrice: Float,
                 salePrice: Float,
                 planNum: Int,
                 alowSale: Boolean,
                 provideId: Long)

/**
	* desc: 用于填写商品信息表单
	*
	* @param merchTypeId 商品类型ID
	* @param merchName   商品名字
	* @param barcode     条形码
	* @param merchPrice  商品进价
	* @param salePrice   商品售价
	* @param selfPreNum  货架预设数量
	* @param planNum     预设进货数量
	* @param provideId   供应商ID
	*/
case class MerchFormData(merchTypeId: Long,
                         merchName: String,
                         barcode: String,
                         merchPrice: String,
                         salePrice: String,
                         selfPreNum: Int,
                         planNum: Int,
                         provideId: Long
                        )

case class UpdateMerch(
	                      merchId: Long,
	                      merchTypeId: Long,
	                      merchName: String,
	                      barcode: String,
	                      cautionNum: Int,
	                      selfPreNum: Int,
	                      merchPrice: Float,
	                      salePrice: Float,
	                      planNum: Int,
	                      provideId: Long
                      )

/**
	* desc: 用于商品信息显示分页
	*
	* @param start 分页开始记录索引
	* @param limit 单页记录数
	* @param data  单页所有记录对象
	* @param total 数据表中所有记录数量
	*/
case class MerchPage(start: Int, limit: Int, data: List[Merch], total: Int)

trait MerchInfoSchema {
	protected val driver: JdbcProfile

	import driver.api._

	class MerchInfoTable(tag: Tag) extends Table[Merch](tag, "merch_info") {
		def merchId = column[Long]("merch_id", O.PrimaryKey, O.AutoInc)

		def merchTypeId = column[Long]("merchtype_id")

		def merchName = column[String]("merch_name")

		def barcode = column[String]("barcode")

		def stockNum = column[Int]("stock_num")

		def cautionNum = column[Int]("caution_num")

		def selfNum = column[Int]("shelf_num")

		def selfPreNum = column[Int]("shelf_pre_num")

		def merchPrice = column[Float]("merch_price")

		def salePrice = column[Float]("sale_price")

		def planNum = column[Int]("plan_num")

		def alowSale = column[Boolean]("alow_sale")

		def provideId = column[Long]("provide_id")

		def * = (merchId, merchTypeId, merchName, barcode, stockNum, cautionNum, selfNum, selfPreNum, merchPrice, salePrice, planNum, alowSale, provideId) <> (Merch.tupled, Merch.unapply)
	}

}
