package Dao.`trait`

import models.MerchType

import scala.concurrent.Future

/**
	* author: dulei
	* date: 2018-05-20
	* desc: 
	*/
trait MerchTypeDao {
	//添加商品类型
	def add(merchType: MerchType): Future[String]
	//删除商品类型
	def delete(id: Long): Future[Int]
	//列出所有的商品类型
	def listAll(): Future[Seq[MerchType]]
	//更新商品类型名字
	def update(newName: String,id: Long): Future[String]
}
