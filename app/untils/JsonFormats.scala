package untils

import models.{Member, MemberPage, Provide}
import play.api.libs.json._

/**
  * author: dulei
  * date: 18-5-10
  * desc: 
  */
object JsonFormats {
  implicit val provideFormat = Json.format[Provide]
  implicit val memberFormData =Json.format[Member]
  implicit val memberPageFormat = Json.format[MemberPage]
}
