package untils

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
}
