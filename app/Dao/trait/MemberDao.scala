package Dao.`trait`

import models.Member

import scala.concurrent.Future

/**
  * author: dulei
  * date: 2018-05-17
  * desc: 
  */
trait MemberDao {
  //添加会员
  def add(member: Member): Future[String]
  //删除会员
  def delete(id: Long): Future[Int]
  //获取指定id的会员
  def get(id: Long): Future[Option[Member]]
  //按照电话号码查询会员
  def listAll(args: String*): Future[Seq[Member]]
  //更新会员信息
  def update(memberName: String,memberPhone: String,memberId: Long): Future[String]
  //获取会员数量
  def getCount():Int
}
