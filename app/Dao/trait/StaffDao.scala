package Dao.`trait`

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-5-8
  * desc: 
  */
trait StaffDao {
  def check(username:String,password:String):Future[Boolean]
}
