package Dao.`trait`

import models.{Order, StockIn}

import scala.concurrent.Future

/**
	* author: dulei
	* date: 2018-05-24
	* desc: 
	*/
trait OrderDao {
	//添加订单信息
	def add(order: Order): Future[String]
	//取消指定id的订单信息：将计划日期置空
	def delete(id: Long): Future[String]
	//获取指定id的订单信息
	def get(id: Long): Future[Option[Order]]
	//列出所有或者符合条件的订单信息
	def listAll(args: String*): Future[Seq[Order]]
	//更新订单信息
	def update(newOrder: Order): Future[String]
	//获取不同搜索条件下的记录数量
	def getCount(searchArg: String):Int
}
