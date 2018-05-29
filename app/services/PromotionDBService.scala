package services

import Dao.implementation.PromotionDaoImpl
import javax.inject.Inject
import models.{Promotion, Strategy}

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-5-29
  * desc: 
  */
class PromotionDBService @Inject()(promotionDaoImpl: PromotionDaoImpl) {
  def addPromotion(strategy: Strategy): Future[String] = {
    promotionDaoImpl.add(strategy)
  }

  def deletePromotion(id: Long): Future[Int] = {
    promotionDaoImpl.delete(id)
  }

  def getPage(start: String, limit: String, searchValue: String): Future[Seq[Promotion]] = {
    promotionDaoImpl.listAll(start, limit, searchValue)
  }

  def getStrategy(id: Long): Future[Option[Strategy]] = {
    promotionDaoImpl.get(id)
  }

  def updateStrategy(newStrategy: Strategy): Future[String] = {
    promotionDaoImpl.update(newStrategy)
  }

  def count(searchValue: String): Int = {
    promotionDaoImpl.getCount(searchValue)
  }
}
