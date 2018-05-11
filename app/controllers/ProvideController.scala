package controllers

import javax.inject._
import models.{Provide, ProvideFormData}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.DBService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProvideController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {
  //提供供应商添加和更新的的页面,提供Form表单
  def provideAddPage() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.provide.provide_add())
  }

  def provideUpdatePage(id: Long) = Action.async {
    implicit request: Request[AnyContent] =>
      dBService.provide_DB.get(id).map(
        res => Ok(views.html.provide.provide_edit(res.get))
      )
  }

  def providelist = Action.async { implicit request: Request[AnyContent] =>
    //权限获取
    val authority = request.session.get("authority").get
    dBService.provide_DB.listAllProvide.map(
      res => {
        authority.charAt(5) match {//权限验证
          case '1' => Ok(views.html.provide.provide_manage(res))
          case _ => BadRequest("Sorry,you don't have authority")
        }

      }
    )
  }

  val provideForm = Form(
    mapping(
      "provideName" -> nonEmptyText,
      "provideAddress" -> nonEmptyText,
      "providePhone" -> nonEmptyText
    )(ProvideFormData.apply)(ProvideFormData.unapply)
  )

  def provideAdd = Action.async {
    implicit request: Request[AnyContent] =>
      provideForm.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest("No data")),
        data => {
          val newProvide = Provide(0, data.provideName, data.provideAddress, data.providePhone)
          dBService.provide_DB.add(newProvide).map(
            res => Redirect("/provide_list")
          )
        }
      )
  }

  def provideDelete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    dBService.provide_DB.delete(id).map(
      res => Redirect("/provide_list")
    )
  }

  def provideUpdate(id: Long) = Action.async {
    implicit request: Request[AnyContent] =>
      provideForm.bindFromRequest.fold(
        hasErrors => Future.successful(BadRequest("No data")),
        data => {
          val newProvide = Provide(id, data.provideName, data.provideAddress, data.providePhone)
          dBService.provide_DB.update(newProvide).map(
            res => Redirect("/provide_list")
          )
        }
      )
  }

}
