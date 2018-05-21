package controllers

import javax.inject._
import models.{Merch, MerchFormData, MerchPage, MerchType}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import services.DBService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
	* This controller creates an `Action` to handle HTTP requests to the
	* application's home page.
	*/
@Singleton
class MerchController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {
	/******************************商品类别管理******************************/
	def merchTypeList() = Action.async { implicit request: Request[AnyContent] =>
		//权限获取
		val authority = request.session.get("authority").get
		dBService.merchType_DB.listAllType().map(
			res => {
				authority.charAt(2) match {
					case '1' => Ok(views.html.merch.merch_type(res))
					case _ => Redirect("/noAuthority")
				}
			}
		)
	}

	def merchTypeAdd() = Action.async { implicit request: Request[AnyContent] =>
		val typeForm = Form(single("merchTypeName" -> nonEmptyText))
		val newTypeName = typeForm.bindFromRequest().get
		val newType = MerchType(0, newTypeName, 0)
		dBService.merchType_DB.addType(newType).map(
			res => Redirect("/merchType_list")
		)
	}

	def merchTypeUpdate() = Action.async { implicit request: Request[AnyContent] =>
		val typeForm = Form(tuple("merchTypeName" -> nonEmptyText, "merchTypeId" -> nonEmptyText))
		val (newTypeName, typeId) = typeForm.bindFromRequest().get
		dBService.merchType_DB.updateType(newTypeName, typeId.replace(" ", "").toLong).map(
			res => Redirect("/merchType_list")
		)
	}

	def merchTypeDelete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
		dBService.merchType_DB.deleteType(id).map(
			res => Redirect("/merchType_list")
		)
	}
/******************************商品信息管理******************************/
	def merchManage() = Action { implicit request: Request[AnyContent] =>
		Ok(views.html.merch.merch_manage("商品管理"))
	}

	def merchPage() = Action.async { implicit request: Request[AnyContent] =>
		import untils.JsonFormats.merchPageFormat
		//权限获取
		val authority = request.session.get("authority").get
		val ajaxForm = Form(tuple("start" -> nonEmptyText, "limit" -> nonEmptyText, "searchValue" -> nonEmptyText))
//		val (start, limit, searchValue) = ajaxForm.bindFromRequest().get
		//TODO 修复搜索栏需要null值的bug 将查询和进入管理页面的分页逻辑分开
		//TODO 修复不能按照商品名查询的bug
		val start = ajaxForm.bindFromRequest().get._1
		val limit = ajaxForm.bindFromRequest().get._2
		val searchValue = ajaxForm.bindFromRequest().get._3
		val counts = dBService.merch_DB.count(searchValue)
		dBService.merch_DB.getPage(start, limit, searchValue).map(
			res => {
				authority.charAt(2) match {
					case '1' => {
						val page =  Json.toJson[MerchPage](MerchPage(start.toInt,limit.toInt,res.toList,counts))
						Ok(page)
					}
					case _ => Redirect("/noAuthority")
				}
			}
		)
	}
	val merchForm = Form(
		mapping(
			"merchTypeId" -> longNumber,
			"merchName" -> nonEmptyText,
			"barcode" -> nonEmptyText,
			"merchPrice" -> nonEmptyText,
			"salePrice" -> nonEmptyText,
			"selfPreNum" -> number,
			"planNum" -> number,
			"provideId" -> longNumber
		)(MerchFormData.apply)(MerchFormData.unapply)
	)
	def merchAdd() = Action.async{implicit request: Request[AnyContent] =>
		merchForm.bindFromRequest().fold(
			hasErrors => Future.successful(BadRequest("No data")),
			data => {
				val newMerch = Merch(0,
					data.merchTypeId,
					data.merchName,
					data.barcode,
					0,0,0,
					data.selfPreNum,
					data.merchPrice.toFloat,
					data.salePrice.toFloat,
					data.planNum,
					true,
					data.provideId
					)
				dBService.merch_DB.addMerch(newMerch).map(
					res => Redirect("/merch_manage")
				)
			}
		)
	}

	def merchDelete(id: Long) = Action.async{implicit request: Request[AnyContent] =>
		dBService.merch_DB.deleteMerch(id).map(
			res => Redirect("/merch_manage")
		)
	}



}
