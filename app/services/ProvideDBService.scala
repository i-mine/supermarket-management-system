package services

import Dao.implementation.ProvideDaoImpl
import javax.inject.{Inject, Singleton}
import models.Provide

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-5-10
  * desc: 
  */
@Singleton
class ProvideDBService @Inject()(provideDaoImpl: ProvideDaoImpl) {

  def listAllProvide: Future[Seq[Provide]] = {
    provideDaoImpl.listAll()
  }

  def search(provideName: String): Future[Seq[Provide]] = {
    provideDaoImpl.listAll(provideName)
  }

  def add(provide: Provide): Future[String] = {
    provideDaoImpl.add(provide)
  }

  def update(provide: Provide): Future[String] = {
    provideDaoImpl.update(provide)
  }

  def delete(id: Long): Future[Int] = {
    provideDaoImpl.delete(id)
  }

  def get(id: Long):Future[Option[Provide]] = {
    provideDaoImpl.get(id)
  }

}
