package services

import Dao.implementation.MerchTypeDaoImpl
import javax.inject.Inject
import models.MerchType

import scala.concurrent.Future

/**
	* author: dulei
	* date: 2018-05-20
	* desc: 
	*/
class MerchTypeDBService @Inject()(merchTypeDaoImpl: MerchTypeDaoImpl) {
	def addType(merchType: MerchType): Future[String] = {
		merchTypeDaoImpl.add(merchType)
	}

	def deleteType(id: Long):Future[Int] = {
		merchTypeDaoImpl.delete(id)
	}

	def updateType(newName: String, id: Long):Future[String] = {
		merchTypeDaoImpl.update(newName,id)
	}

	def listAllType():Future[Seq[MerchType]] = {
		merchTypeDaoImpl.listAll()
	}
}
