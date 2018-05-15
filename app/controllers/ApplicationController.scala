package controllers

import javax.inject._
import models._
import play.api.mvc._
import services.DBService
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class ApplicationController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {

  val loginForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginFormData.apply)(LoginFormData.unapply)
  )

  def welcome() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.welcome())
  }

  def noAuthority() = Action {
    implicit request: Request[AnyContent] =>
      Ok(views.html.authority_error())
  }

  /**
    * 跳转到登录界面
    *
    * @return
    */
  def login() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login()).withNewSession
  }


  def checklogin() = Action.async { implicit request: Request[AnyContent] =>

    //    获取表单数据的第一种方法
    //    val name: Option[String] = request.body.asFormUrlEncoded
    //      .flatMap(m => m.get("username").flatMap(_.headOption))
    //    println("Login info From body:" + name.get)

    //获取表单数据的第二种方法
    loginForm.bindFromRequest().fold(
      hasErrors => Future.successful(BadRequest(s"No data,$hasErrors")),
      data => {
        dBService.staff_DB.getUser(data.username)
          .map(_ match {
            case Some(staff) => {
              if (staff.password.equals(data.password)) {
                //判断密码是否正确
                Ok(views.html.index(data.username))
                  .withSession(("authority", staff.authority))
              } else {
                BadRequest(s"user's ${data.username} password not true!")
              }
            }
            case None => {
              BadRequest(s"not exist user named ${data.username}!")
            }
          })
      }
    )
  }

}
