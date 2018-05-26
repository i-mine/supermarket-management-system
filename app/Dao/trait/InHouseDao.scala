package Dao.`trait`

import models.InHouseView

import scala.concurrent.Future

/**
	* author: dulei
	* date: 2018-05-25
	* desc: 
	*/
trait InHouseDao {
	def get(id: Long): Future[Option[InHouseView]]
	//列出所有或者符合条件的入库信息
	def listAll(args: String*): Future[Seq[InHouseView]]
	//更新入库信息
	def update(instockId: Long): Future[String]
	//获取不同搜索条件下的记录数量
	def getCount(searchArg: String):Int
}
