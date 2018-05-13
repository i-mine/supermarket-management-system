package controllers

import javax.inject._
import models.{Staff, StaffFormData}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import services.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class StaffController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def stafflist() = Action.async { implicit request: Request[AnyContent] =>
    //权限获取
    val authority = request.session.get("authority").get
    dBService.staff_DB.listAllUser().map(
      res => {
        authority.charAt(6) match {
          //权限验证
          case '1' => Ok(views.html.staff.staff_manage(res))
          case _ => BadRequest("Sorry,you don't have authority")
        }

      }
    )
  }

  def staffDelete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    dBService.staff_DB.deleteUser(id).map(
      res => Redirect("/staff_list")
    )
  }

  val staffForm = Form(
    mapping(
      "staffName" -> nonEmptyText,
      "position" -> nonEmptyText,
      "password" -> nonEmptyText,
      "gender" -> nonEmptyText,
      "teleNumber" -> nonEmptyText,
      "address" -> nonEmptyText
    )(StaffFormData.apply)(StaffFormData.unapply)
  )

  def staffAdd() = Action.async { implicit request: Request[AnyContent] =>
    val positionMap = Map("超市管理员" -> 1, "系统管理员" -> 2, "仓库管理员" -> 3, "收银员" -> 4)
    val authroityMap = Map("超市管理员" -> "111111111", "收银员" -> "110000000")
    staffForm.bindFromRequest.fold(
      hasErrors => Future.successful(BadRequest("No data")),
      data => {
        val posiitonId = positionMap.get(data.position).get
        val authority = authroityMap.get(data.position).get
        val newStaff = Staff(0, posiitonId, data.staffName, data.gender.charAt(0), data.teleNumber, data.address, data.password, authority)
        dBService.staff_DB.addUser(newStaff).map(
          res => Redirect("/staff_list")
        )
      }
    )
  }

}
