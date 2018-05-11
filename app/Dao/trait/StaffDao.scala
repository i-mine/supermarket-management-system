package Dao.`trait`

import models.Staff

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-5-8
  * desc: 
  */
trait StaffDao {
  def check(username:String,password:String):Future[Boolean]
  //根据用户名获取用户
  def get(username: String):Future[Option[Staff]]
  //根据id获取用户
  def get(id: Long):Future[Option[Staff]]
  //根据给出的参数列出所有符合条件的职员
  def listAll(args: String*): Future[Seq[Staff]]
  //更新职员信息
  def update(staff: Staff):Future[String]
  //添加职员
  def add(staff: Staff):Future[String]
  //删除职员
  def delete(id: Long):Future[Int]

}
