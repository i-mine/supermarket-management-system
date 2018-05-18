package services

import Dao.implementation.MemberDaoImpl
import javax.inject.Inject
import models.Member

import scala.concurrent.Future

/**
  * author: dulei
  * date: 2018-05-17
  * desc: 
  */
class MemberDBService @Inject()(memberDaoImpl: MemberDaoImpl) {
  def addMember(member: Member): Future[String] = {
    memberDaoImpl.add(member)
  }

  def deleteMember(id: Long): Future[Int] = {
    memberDaoImpl.delete(id)
  }

  def getMember(id: Long): Future[Option[Member]] = {
    memberDaoImpl.get(id)
  }

  def listMember(): Future[Seq[Member]] = {
    memberDaoImpl.listAll()
  }

  def search(memberPhone: String): Future[Seq[Member]] = {
    memberDaoImpl.listAll(memberPhone)
  }

  def getPage(start: Int, limit: Int): Future[Seq[Member]] = {
    memberDaoImpl.listAll(start.toString, limit.toString)
  }

  def updateMember(newMember: Member): Future[String] = {
    memberDaoImpl.update(newMember)
  }

  def count:Int = {
    memberDaoImpl.getCount()
  }
}
