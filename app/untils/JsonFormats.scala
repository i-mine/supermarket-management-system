package untils

import models.Provide
import play.api.libs.json.Json

/**
  * author: dulei
  * date: 18-5-10
  * desc: 
  */
object JsonFormats {
  implicit val provideFormat = Json.format[Provide]
}
