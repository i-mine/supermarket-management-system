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
class SaleController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {

  def promotionManage() = Action { implicit request: Request[AnyContent] =>
    //权限获取
    val authority = request.session.get("authority").get
    authority.charAt(7) match {
      case '1' => Ok(views.html.sale.promotion_manage("促销管理"))
      case _ => Redirect("/noAuthority")
    }
  }

  def promotionPage() = Action.async{implicit request: Request[AnyContent] =>
    import utils.JsonFormats.promotionPageFormat
    val ajaxForm = Form(tuple("start" -> nonEmptyText, "limit" -> nonEmptyText, "searchValue" -> text))
    val (start, limit, searchValue) = ajaxForm.bindFromRequest().get
    println(searchValue)
    val counts = dBService.promotion_DB.count(searchValue)
    dBService.promotion_DB.getPage(start, limit, searchValue).map(
      res => {
        val page = Json.toJson[Page[Promotion]](Page(start.toInt, limit.toInt, res.toList, counts))
        Ok(page)
      }
    )
  }

  def strategyGet() = Action.async{implicit request: Request[AnyContent] =>
    import utils.JsonFormats.strategyFormat
    val id: Option[String] = request.body
      .asFormUrlEncoded
      .flatMap(m => m.get("id")
        .flatMap(_.headOption))
    dBService.promotion_DB.getStrategy(id.get.toLong).map(
      res => Ok(Json.toJson[Strategy](res.get))
    )
  }

  val updateForm = Form(
    mapping(
      "strategyId" -> longNumber,
      "barcode" -> nonEmptyText,
      "startDate"-> nonEmptyText,
      "endDate" -> nonEmptyText,
      "discount"-> nonEmptyText
    )(Strategy.apply)(Strategy.unapply)
  )

  def strategyUpdate() = Action.async{implicit request: Request[AnyContent] =>
    val data = updateForm.bindFromRequest().get
    dBService.promotion_DB.updateStrategy(data).map(
      res => Redirect("/promotion_manage")
    )
  }


  case class StrategyFormData(barcode: String, startDate: String, endDate:String, discount: String)

  val addForm = Form(
    mapping(
      "barcode"-> nonEmptyText,
      "startDate"-> nonEmptyText,
      "endDate" -> nonEmptyText,
      "discount"-> nonEmptyText
    )(StrategyFormData.apply)(StrategyFormData.unapply)
  )
  def strategyAdd() = Action.async{implicit request: Request[AnyContent] =>
    addForm.bindFromRequest().fold(
      hasErrors => Future.successful(BadRequest("No data")),
      data => {
        val strategy = Strategy(0, data.barcode, data.startDate, data.endDate, data.discount)
        dBService.promotion_DB.addPromotion(strategy).map(
          res => Redirect("/promotion_manage")
        )
      }
    )
  }

  def strategyDelete(id: Long) = Action.async{
    dBService.promotion_DB.deletePromotion(id).map(
      res => Redirect("/promotion_manage")
    )
  }



}
