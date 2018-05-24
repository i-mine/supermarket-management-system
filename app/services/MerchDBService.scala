package services

import Dao.implementation.MerchDaoImpl
import javax.inject.Inject
import models.{Merch, UpdateMerch}

import scala.concurrent.Future

/**
	* author: dulei
	* date: 2018-05-21
	* desc: 
	*/
class MerchDBService @Inject()(merchDaoImpl: MerchDaoImpl) {
	def addMerch(merch: Merch): Future[String] = {
		merchDaoImpl.add(merch)
	}

	def deleteMerch(id: Long): Future[Int] = {
		merchDaoImpl.delete(id)
	}

	def getMerch(id: Long): Future[Option[Merch]] = {
		merchDaoImpl.get(id)
	}

	def listMerch(): Future[Seq[Merch]] = {
		merchDaoImpl.listAll()
	}

	def getPage(start:String,limit: String, searchValue: String ): Future[Seq[Merch]] = {
		merchDaoImpl.listAll(start,limit, searchValue)
	}

	def updateMerch(updateMerch: UpdateMerch): Future[String] = {
		merchDaoImpl.update(updateMerch)
	}
	def count(searchValue: String): Int = {
		merchDaoImpl.getCount(searchValue)
	}


}
