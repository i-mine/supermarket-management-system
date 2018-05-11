package services

import javax.inject.{Inject, Singleton}
import Dao.implementation.StaffDaoImpl
import models.Staff

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-5-8
  * desc: 为Staff表的操作提供服务
  */
@Singleton
class StaffDBService @Inject()(staffDaoImpl: StaffDaoImpl) {
  def checkIsUser(username: String, password: String): Future[Boolean] = {
    println(username, password)
    return staffDaoImpl.check(username, password)
  }

  def getUser(username: String): Future[Option[Staff]] = {
    staffDaoImpl.get(username)
  }

  def getUser(id: Long): Future[Option[Staff]] = {
    staffDaoImpl.get(id)
  }

  def listAllUser(): Future[Seq[Staff]] = {
    staffDaoImpl.listAll()
  }

  def deleteUser(id: Long): Future[Int] = {
    staffDaoImpl.delete(id)
  }

  def updateUser(newStaff: Staff): Future[String] = {
    staffDaoImpl.update(newStaff)
  }

  def search(staffName: String): Future[Seq[Staff]] = {
    staffDaoImpl.listAll(staffName)
  }

}
