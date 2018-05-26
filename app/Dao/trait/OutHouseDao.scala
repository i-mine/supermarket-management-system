package Dao.`trait`

import scala.concurrent.Future

/**
	* author: dulei
	* date: 2018-05-25
	* desc: 
	*/
trait OutHouseDao{
	//选择出库，插入出库记录
	def insert(barcode: String, merchNum: Int): Future[String]
	//更新出库状态和出库数量
	def update(outStockId: Long): Future[Int]
	//列出所有或者符合条件的出库信息
	def listAll(args: String*): Future[Seq[Any]]
	//获取记录条数
	def getCount(searchArg: String*): Int
}
