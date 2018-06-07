package Dao.`trait`

import models.Goods

import scala.concurrent.Future

/**
  * author: dulei
  * date: 18-6-7
  * desc: 
  */
trait PayDao {
  def get(barcode: String): Future[Option[Goods]]
}
