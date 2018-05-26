package controllers

import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import services.DBService
import utils.Page

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class OrderController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {

  def orderManage() = Action { implicit request: Request[AnyContent] =>
    //权限获取
    val authority = request.session.get("authority").get
    authority.charAt(3) match {
      case '1' => Ok(views.html.order.order_manage("进货管理"))
      case _ => Redirect("/noAuthority")
    }
  }

  def orderPage() = Action.async{implicit request: Request[AnyContent] =>
    import utils.JsonFormats.orderPageFormat
    val ajaxForm = Form(tuple("start" -> nonEmptyText, "limit" -> nonEmptyText, "searchValue" -> text))
    val (start, limit, searchValue) = ajaxForm.bindFromRequest().get
    val counts = dBService.order_DB.count(searchValue)
    dBService.order_DB.getPage(start, limit, searchValue).map(
      res => {
        val page = Json.toJson[Page[Order]](Page(start.toInt, limit.toInt,res.toList,counts))
        Ok(page)
      }
    )
  }

  def orderGet() = Action.async{implicit request: Request[AnyContent] =>
    import utils.JsonFormats.orderFormat
    val id: Option[String] = request.body
      .asFormUrlEncoded
      .flatMap(m => m.get("id")
        .flatMap(_.headOption))
    dBService.order_DB.getOrder(id.get.toLong).map(
      res => Ok(Json.toJson[Order](res.get))
    )
  }
  val updateForm = Form(
    mapping(
      "orderId" -> longNumber,
      "barcode" -> nonEmptyText,
      "merchNum" -> number,
      "merchPrice" -> nonEmptyText,
      "planDate" -> nonEmptyText,
      "arriveState"-> number
    )(Order.apply)(Order.unapply)
  )
  def orderUpdate() = Action.async{implicit request: Request[AnyContent] =>
    val data = updateForm.bindFromRequest().get
    dBService.order_DB.updateOrder(data).map(
      res => Redirect("/order_manage")
    )
  }

  val addForm = Form(
    mapping(
      "barcode" -> nonEmptyText,
      "merchNum" -> number,
      "merchPrice" -> nonEmptyText,
      "planDate" -> nonEmptyText
    )(OrderFormData.apply)(OrderFormData.unapply)
  )

  def orderAdd() = Action.async{implicit request: Request[AnyContent] =>
   addForm.bindFromRequest().fold(
      hasErrors => Future.successful(BadRequest("No data")),
      data => {
        val newOrder = Order(0,data.barcode,data.merchNum,data.merchPrice,data.planDate,0)
        dBService.order_DB.addOrder(newOrder).map(
          res => Redirect("/order_manage")
        )
      }
    )
  }

  def orderDelete(id: Long) = Action.async{
    dBService.order_DB.deleteOrder(id).map(
      res => Redirect("/order_manage")
    )
  }

}
