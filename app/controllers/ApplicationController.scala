package controllers

import javax.inject._

import models.LoginFormData
import play.api.mvc._
import services.{DBService, StaffDBService}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.JsValue

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class ApplicationController @Inject()(cc: ControllerComponents, dBService: DBService, staffDBService: StaffDBService) extends AbstractController(cc) {
  val db = dBService.DB
  val dbConfig = dBService.dbConfig
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  import dbConfig.profile.api._

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
    println("From body:"+name)
    //获取表单数据的第二种方法
    loginForm.bindFromRequest().fold(
      hasErrors => Future.successful(BadRequest(s"No data,$hasErrors")),
      data => {
        staffDBService.checkIsUser(data.username, data.password).map(
          res =>
            res match {
              case true => Ok(views.html.index(data.username))
              case _ => BadRequest("No user")
            }

        )
      }
    )
  }
}
