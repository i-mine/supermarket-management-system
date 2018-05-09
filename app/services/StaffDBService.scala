package services

import javax.inject.Inject

import Dao.implementation.StaffDaoImpl

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-5-8
  * desc: 为Staff表的操作提供服务
  */
class StaffDBService @Inject()(staffDaoImpl: StaffDaoImpl) {
  def checkIsUser(username:String,password:String):Future[Boolean]={
    println(username,password)
    return staffDaoImpl.check(username,password)
  }
}
