package utils

import models._
import play.api.libs.json._

/**
  * author: dulei
  * date: 18-5-10
  * desc: 
  */
object JsonFormats {
  implicit val provideFormat = Json.format[Provide]
  implicit val memberFormat =Json.format[Member]
  implicit val memberPageFormat = Json.format[MemberPage]
  implicit val merchFormat = Json.format[Merch]
  implicit val merchPageFormat = Json.format[MerchPage]
  //新的分页风格
  implicit val orderFormat = Json.format[Order]
  implicit val orderPageFormat = Json.format[Page[Order]]
  implicit val stockinFormat = Json.format[StockIn]

  implicit val inhouseFormat = Json.format[InHouseView]
  implicit val inhousePageFormat = Json.format[Page[InHouseView]]

  implicit val outshelfFormat = Json.format[OutShelfView]
  implicit val outshelfPageFormat = Json.format[Page[OutShelfView]]

  implicit val outhouseFormat = Json.format[OutHouseView]
  implicit val outhousePageFormat = Json.format[Page[OutHouseView]]

  implicit val promotionFormat = Json.format[Promotion]
  implicit val promotionPageFormat = Json.format[Page[Promotion]]
  implicit val strategyFormat = Json.format[Strategy]

  implicit val goodsFormat = Json.format[Goods]
}
