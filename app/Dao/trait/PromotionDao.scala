package Dao.`trait`

import models.{Promotion, Strategy}

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-5-29
  * desc: 
  */
trait PromotionDao {
  //添加销售策略信息
  def add(strategy: Strategy): Future[String]
  //删除销售策略信息
  def delete(id: Long): Future[Int]
  //获取指定id的销售策略信息
  def get(id: Long): Future[Option[Strategy]]
  //列出所有或者符合条件的销售策略信息
  def listAll(args: String*): Future[Seq[Promotion]]
  //更新策略信息
  def update(newStragegy: Strategy): Future[String]
  //获取不同搜索条件下录入数量
  def getCount(searchArg: String): Int
}
