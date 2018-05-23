package Dao.`trait`

import models.{Merch, UpdateMerch}

import scala.concurrent.Future

/**
	* author: dulei
	* date: 2018-05-21
	* desc: 
	*/
trait MerchDao {
	//添加商品信息
	def add(member: Merch): Future[String]
	//删除商品信息
	def delete(id: Long): Future[Int]
	//获取指定id的商品信息
	def get(id: Long): Future[Option[Merch]]
	//列出所有或者符合条件的商品信息
	def listAll(args: String*): Future[Seq[Merch]]
	//更新商品信息
	def update(newMerch: UpdateMerch): Future[String]
	//获取录入商品的数量
	def getCount(searchArg: String):Int
}
