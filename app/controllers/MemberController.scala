package controllers


import javax.inject._
import models.MemberPage
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import services.DBService
import scala.concurrent.ExecutionContext.Implicits.global

/**
	* This controller creates an `Action` to handle HTTP requests to the
	* application's home page.
	*/
@Singleton
class MemberController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {
	//跳转到会员管理页面
	def memberManage() = Action {implicit request: Request[AnyContent] =>
		Ok(views.html.member.member_manage("会员管理"))
	}
	//返回分页的member数据
	def memberPage() = Action.async { implicit request: Request[AnyContent] =>
		import untils.JsonFormats.memberPageFormat
		val authority = request.session.get("authority").get
		val ajaxForm = Form(tuple("start" -> number, "limit" -> number))
		val (start, limit) = ajaxForm.bindFromRequest().get //获取分页数
		val counts = dBService.member_DB.count //获取会员总数
		dBService.member_DB.getPage(start, limit).map(
			res => {
				authority.charAt(1) match {
					case '1' => {
						val page = Json.toJson[MemberPage](MemberPage(start, limit, res.toList, counts))
						Ok(page)
					}
					case _ => Redirect("/noAuthority")
				}
			}
		)
	}



}
