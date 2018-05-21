package Dao.implementation

import Dao.`trait`.MerchTypeDao
import javax.inject.Inject
import models.{MerchType, MerchTypeSchema}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
	* author: dulei
	* date: 2018-05-20
	* desc: 
	*/
class MerchTypeDaoImpl @Inject()()(dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] with MerchTypeSchema with  MerchTypeDao {
	override protected val dbConfig = dbConfigProvider.get[JdbcProfile]
	import dbConfig.profile.api._

	val merchTypeInfos = TableQuery[MerchTypeTable]
	override def add(merchType: MerchType):Future[String] = {
		db.run(merchTypeInfos += merchType).map(
			res => "merch type info added successfully"
		).recover{
			case ex: Exception => ex.getCause.getMessage
		}
	}

	override def delete(id: Long): Future[Int] = {
		db.run(merchTypeInfos.filter(_.merchTypeId === id).filter(_.merchTypeNum === 0).delete)
	}

	override def listAll():Future[Seq[MerchType]] = {
		db.run(merchTypeInfos.result)
	}

	override def update(newName: String,id: Long):Future[String] = {
		db.run(merchTypeInfos.filter(_.merchTypeId === id).map(_.merchTypeName).update(newName)).map(
			res => "merch type info updated successfully"
		).recover{
			case ex: Exception => ex.getCause.getMessage
		}
	}
}
