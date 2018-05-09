package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import services.DBService
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {
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
  def index() = Action { implicit request: Request[AnyContent] =>
//    val count:Int = Await.result(db.run(sql"""SELECT COUNT(1) FROM position_info""".as[Int]),Duration.Inf).headOption.get
    Ok(views.html.index("test"))
  }
}
