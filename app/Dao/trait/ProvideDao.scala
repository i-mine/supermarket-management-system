package Dao.`trait`

import models.Provide

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-5-10
  * desc: 
  */
trait ProvideDao {
  //添加供应商
  def add(provide: Provide): Future[String]
  //删除供应商
  def delete(id: Long): Future[Int]
  //获取指定id的供应商
  def get(id: Long): Future[Option[Provide]]
  //模糊查询并列出所有符合条件的供应商
  def listAll(args: String*): Future[Seq[Provide]]
  //更新供应商信息
  def update(newProvide: Provide): Future[String]
}
