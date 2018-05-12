package controllers

import javax.inject._
import play.api.mvc._
import services.DBService

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
  def stafflist() = Action.async{ implicit request: Request[AnyContent] =>
    //权限获取
    val authority = request.session.get("authority").get
    dBService.staff_DB.listAllUser().map(
      res => {
        authority.charAt(6) match {//权限验证
          case '1' => Ok("")//TODO 添加View
          case _ => BadRequest("Sorry,you don't have authority")
        }

      }
    )
  }


}
