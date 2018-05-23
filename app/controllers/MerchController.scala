package controllers

import javax.inject._
import models._
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
	/** ****************************商品类别管理 ******************************/
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

	/** ****************************商品信息管理 ******************************/
	def merchManage() = Action { implicit request: Request[AnyContent] =>
		//权限获取
		val authority = request.session.get("authority").get
		authority.charAt(2) match {
			case '1' => Ok(views.html.merch.merch_manage("商品管理"))
			case _ => Redirect("/noAuthority")
		}
	}

	def merchPage() = Action.async { implicit request: Request[AnyContent] =>
		import untils.JsonFormats.merchPageFormat
		val ajaxForm = Form(tuple("start" -> nonEmptyText, "limit" -> nonEmptyText, "searchValue" -> text))
		val (start, limit, searchValue) = ajaxForm.bindFromRequest().get
		val counts = dBService.merch_DB.count(searchValue)
		dBService.merch_DB.getPage(start, limit, searchValue).map(
			res => {
				val page = Json.toJson[MerchPage](MerchPage(start.toInt, limit.toInt, res.toList, counts))
				Ok(page)
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

	def merchAdd() = Action.async { implicit request: Request[AnyContent] =>
		merchForm.bindFromRequest().fold(
			hasErrors => Future.successful(BadRequest("No data")),
			data => {
				val newMerch = Merch(0,
					data.merchTypeId,
					data.merchName,
					data.barcode,
					0, 0, 0,
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

	def merchDelete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
		dBService.merch_DB.deleteMerch(id).map(
			res => Redirect("/merch_manage")
		)
	}

	val updateForm = Form(
		tuple(
			"merchId" -> longNumber,
			"merchTypeId" -> longNumber,
			"merchName" -> nonEmptyText,
			"barcode" -> nonEmptyText,
			"cautionNum" -> number,
			"selfPreNum" -> number,
			"merchPrice" -> nonEmptyText,
			"salePrice" -> nonEmptyText,
			"planNum" -> number,
			"provideId" -> longNumber
		)
	)

	def merchUpdate() = Action.async { implicit request: Request[AnyContent] =>
		val data = updateForm.bindFromRequest().get
		val updateData = UpdateMerch(
			data._1, //merchId
			data._2, //merchTypeId
			data._3, //merchName
			data._4, //barcode
			data._5, //cautionNum
			data._6, //selfPreNum
			data._7.toFloat, //merchPrice
			data._8.toFloat, //salePrice
			data._9, //planNum
			data._10 //provideId
		)
		dBService.merch_DB.updateMerch(updateData).map(
			res => Redirect("/merch_manage")
		)
	}

	def merchGet() = Action.async { implicit request: Request[AnyContent] =>
		import untils.JsonFormats.merchFormat
		val id: Option[String] = request.body
			.asFormUrlEncoded
			.flatMap(m => m.get("id")
				.flatMap(_.headOption))
		dBService.merch_DB.getMerch(id.get.toLong).map(
			res => Ok(Json.toJson[Merch](res.get))
		)
	}

}
