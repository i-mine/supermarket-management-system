package services

import Dao.implementation.OrderDaoImpl
import javax.inject.Inject
import models.{Order, StockIn}

import scala.concurrent.Future

/**
	* author: dulei
	* date: 2018-05-24
	* desc: 
	*/
class OrderDBService @Inject()(orderDaoImpl: OrderDaoImpl) {
	def addOrder(order: Order): Future[String] = {
		orderDaoImpl.add(order)
	}

	def deleteOrder(id: Long): Future[String] = {
		orderDaoImpl.delete(id)
	}

	def getOrder(id: Long): Future[Option[Order]] = {
		orderDaoImpl.get(id)
	}

	def listOrder(): Future[Seq[Order]] = {
		orderDaoImpl.listAll()
	}

	def getPage(start: String, limit: String, searchValue: String): Future[Seq[Order]] = {
		orderDaoImpl.listAll(start, limit, searchValue)
	}

	def updateOrder(newOrder: Order): Future[String] = {
		orderDaoImpl.update(newOrder)
	}

	def count(searchValue: String): Int = {
		orderDaoImpl.getCount(searchValue)
	}
}
