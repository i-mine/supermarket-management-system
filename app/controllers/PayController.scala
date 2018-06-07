package controllers

import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import services.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class PayController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {
  def payPage() = Action {implicit request: Request[AnyContent] =>
    //权限获取
    val authority = request.session.get("authority").get
    authority.charAt(0) match {
      case '1' => Ok(views.html.pay.pay("收银"))
      case _ => Redirect("/noAuthority")
    }
  }

    val addForm = Form(
      single("barcode" -> nonEmptyText)
    )
  def goodsGet() = Action.async{implicit request: Request[AnyContent] =>
    import utils.JsonFormats.goodsFormat
    val barcode = addForm.bindFromRequest().get
    dBService.pay_DB.getGoods(barcode).map(
      data =>
        Ok(Json.toJson[Goods](data.get))
    )
  }
}
