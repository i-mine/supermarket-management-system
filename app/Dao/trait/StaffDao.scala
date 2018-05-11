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
  def get(username: String):Future[Option[Staff]]
}
