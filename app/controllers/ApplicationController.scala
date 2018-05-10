package controllers

import javax.inject._

import models.LoginFormData
import play.api.mvc._
import services.{DBService, StaffDBService}
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class ApplicationController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */


  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginFormData.apply)(LoginFormData.unapply)
  )

  def login() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login())
  }

  def checklogin() = Action.async { implicit request: Request[AnyContent] =>
    //获取表单数据的第一种方法
    val name: Option[String] = request.body.asFormUrlEncoded
      .flatMap(m => m.get("username").flatMap(_.headOption))
    println("Login info From body:" + name.get)

    //获取表单数据的第二种方法
    loginForm.bindFromRequest().fold(
      hasErrors => Future.successful(BadRequest(s"No data,$hasErrors")),
      data => {
        dBService.staff_DB.checkIsUser(data.username, data.password).map(
          res =>
            if (res) {
              Ok(views.html.index(data.username))
            } else {
              BadRequest("No user")
            }

        )
      }
    )
  }
}
