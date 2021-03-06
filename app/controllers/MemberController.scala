package controllers


import java.sql.Date

import javax.inject._
import models.{Member, MemberPage}
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
	def memberManage() = Action { implicit request: Request[AnyContent] =>
		val authority = request.session.get("authority").get
		authority.charAt(1) match {
			case '1' => Ok(views.html.member.member_manage("会员管理"))
				case _ => Redirect("/noAuthority")
		}
	}

	//返回分页的member数据
	def memberPage() = Action.async { implicit request: Request[AnyContent] =>
		import utils.JsonFormats.memberPageFormat
		val ajaxForm = Form(tuple("start" -> number, "limit" -> number))
		val (start, limit) = ajaxForm.bindFromRequest().get //获取分页数
	val counts = dBService.member_DB.count //获取会员总数
		dBService.member_DB.getPage(start, limit).map(
			res => {
						val page = Json.toJson[MemberPage](MemberPage(start, limit, res.toList, counts))
						Ok(page)
			}
		)
	}

	def memberAdd() = Action.async { implicit request: Request[AnyContent] =>
		val memberForm = Form(tuple("memberName" -> nonEmptyText, "memberPhone" -> nonEmptyText))
		val (memberName, memberPhone) = memberForm.bindFromRequest().get
		val regDate = new Date(System.currentTimeMillis())
		val member = Member(0, memberName, memberPhone, 0, 0, regDate, regDate)
		dBService.member_DB.addMember(member).map(
			res => Redirect("/member_manage")
		)
	}

	def memberDelete(id: Long) = Action.async{implicit request: Request[AnyContent] =>
		dBService.member_DB.deleteMember(id).map{
			res => Redirect("/member_manage")
		}
	}

	def memberGet() = Action.async{implicit request: Request[AnyContent] =>
		val memberIdForm = Form(single("id" -> nonEmptyText))
		import utils.JsonFormats.memberFormat
		val memberId = memberIdForm.bindFromRequest().get
		dBService.member_DB.getMember(memberId.toLong).map(
			res => Ok(Json.toJson[Member](res.get))
		)
	}

	def memberCheck() = Action.async{implicit request: Request[AnyContent] =>
		val seachForm= Form(single("member_phone" -> text))
		val memberPhone = seachForm.bindFromRequest().get
		dBService.member_DB.search(memberPhone).map(
			res=> if(res.isEmpty){
				Ok("false")
			}else{
				Ok("true")
			}
		)

	}

	def memberUpdate() = Action.async{implicit request: Request[AnyContent] =>
		val memberUpdateForm = Form(
			tuple(
				"memberName" -> nonEmptyText,
				"memberPhone" -> nonEmptyText,
				"memberId" -> nonEmptyText
			)
		)

		val (memberName,memberPhone,memberId) = memberUpdateForm.bindFromRequest().get
		dBService.member_DB.updateMember(memberName,memberPhone,memberId.toLong).map(
			res => Redirect("/member_manage")
		)
	}

	def memberSearch() = Action.async{implicit request: Request[AnyContent] =>
		val searchForm = Form(single("memberPhone" -> text))
		val memberPhone = searchForm.bindFromRequest().get
		dBService.member_DB.search(memberPhone).map(
			res => Ok(views.html.member.member_detail(res))
		)
	}

}
