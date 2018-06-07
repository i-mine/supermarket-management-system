package services

import Dao.implementation.PayDaoImpl
import javax.inject.Inject
import models.Goods

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-6-7
  * desc: 
  */
class PayDBService @Inject()(payDaoImpl: PayDaoImpl) {
  def getGoods(barcode: String):Future[Option[Goods]] = {
    payDaoImpl.get(barcode)
  }
}
