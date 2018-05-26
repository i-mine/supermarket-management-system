package services

import Dao.implementation.{InHouseDaoImpl, OutHouseDaoImpl}
import javax.inject.Inject
import models.{OutHouseView, OutShelfView}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
	* author: dulei
	* date: 2018-05-25
	* desc: 
	*/
class WareHouseDBService @Inject()(inHouseDaoImpl: InHouseDaoImpl, outHouseDaoImpl: OutHouseDaoImpl) {
	def getInHouse(id: Long) = {
		inHouseDaoImpl.get(id)
	}

	def listInHouse = {
		inHouseDaoImpl.listAll()
	}

	def getInhousePage(start: String, limit: String, searchValue: String) = {
		inHouseDaoImpl.listAll(start, limit, searchValue)
	}

	def updateState(instockId: Long) = {
		inHouseDaoImpl.update(instockId)
	}

	def inHouseCount(searchValue: String) = {
		inHouseDaoImpl.getCount(searchValue)
	}

	def insertOutHouse(barcode: String, merchNum: Int) = {
		outHouseDaoImpl.insert(barcode, merchNum)
	}

	def updateOutHouse(outstockId: Long) = {
		outHouseDaoImpl.update(outstockId)
	}

	def listShelf(start: String, limit: String): Future[Seq[OutShelfView]] = {
		outHouseDaoImpl.listAll(start, limit).map(_.asInstanceOf[Seq[OutShelfView]])
	}

	def listOutHouse(start: String, limit: String, searchValue: String): Future[Seq[OutHouseView]] = {
		outHouseDaoImpl.listAll(start, limit, searchValue).map(_.asInstanceOf[Seq[OutHouseView]])
	}

	def outHouseCount(searchValue: String*) = {
		if (searchValue.isEmpty) {
			outHouseDaoImpl.getCount()
		}else{
			outHouseDaoImpl.getCount(searchValue.apply(0))
		}
	}
}
