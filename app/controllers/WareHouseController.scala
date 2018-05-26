package controllers

import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import services.DBService
import utils.Page

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
	* This controller creates an `Action` to handle HTTP requests to the
	* application's home page.
	*/
@Singleton
class WareHouseController @Inject()(cc: ControllerComponents, dBService: DBService) extends AbstractController(cc) {
	//**************************入库管理********************************
	def inHouseManage() = Action { implicit request: Request[AnyContent] =>
		//权限获取
		val authority = request.session.get("authority").get
		authority.charAt(4) match {
			case '1' => Ok(views.html.warehouse.inhouse_manage("入库清单"))
			case _ => Redirect("/noAuthority")
		}
	}

	def inHousePage() = Action.async{implicit request: Request[AnyContent] =>
		import utils.JsonFormats.inhousePageFormat
		val ajaxForm = Form(tuple("start" -> nonEmptyText, "limit" -> nonEmptyText, "searchValue" -> text))
		val (start, limit, searchValue) = ajaxForm.bindFromRequest().get
		val counts = dBService.house_DB.inHouseCount(searchValue)
		dBService.house_DB.getInhousePage(start,limit, searchValue).map(
			res => {
				val page = Json.toJson[Page[InHouseView]](Page(start.toInt, limit.toInt,res.toList,counts))
				Ok(page)
			}
		)
	}

	def inHouseUpdate(id: Long) = Action.async{implicit request: Request[AnyContent] =>
		dBService.house_DB.updateState(id).map(
			res => Redirect("/inhouse_manage")
		)
	}

	//**************************出库管理********************************
	def outHouseManage() = Action { implicit request: Request[AnyContent] =>
		//权限获取
		val authority = request.session.get("authority").get
		authority.charAt(4) match {
			case '1' => Ok(views.html.warehouse.outhouse_manage("出库清单"))
			case _ => Redirect("/noAuthority")
		}
	}

	def outHousePage1() = Action.async{implicit request: Request[AnyContent] =>
		import utils.JsonFormats.outshelfPageFormat
		val ajaxForm = Form(tuple("start" -> nonEmptyText, "limit" -> nonEmptyText))
		val (start, limit) = ajaxForm.bindFromRequest().get
		val counts = dBService.house_DB.outHouseCount()
		dBService.house_DB.listShelf(start,limit).map(
			res => {
				val page1 = Json.toJson[Page[OutShelfView]](Page(start.toInt, limit.toInt,res.toList,counts))
				Ok(page1)
			}
		)
	}

	def outHousePage2() = Action.async{implicit request: Request[AnyContent] =>
		import utils.JsonFormats.outhousePageFormat
		val ajaxForm = Form(tuple("start" -> nonEmptyText, "limit" -> nonEmptyText,"searchValue" -> text))
		val (start, limit, searchValue) = ajaxForm.bindFromRequest().get
		val counts = dBService.house_DB.outHouseCount(searchValue)
		dBService.house_DB.listOutHouse(start, limit, searchValue).map(
			res => {
				val page2 = Json.toJson[Page[OutHouseView]](Page(start.toInt, limit.toInt,res.toList,counts))
				Ok(page2)
			}
		)
	}

	def insertOutHouse(barcode: String,merchNum:Int) = Action.async{
		dBService.house_DB.insertOutHouse(barcode,merchNum).map(
			res => Redirect("/outhouse_manage")
		)
	}

	def outHouseUpdate(outStockId: Long) = Action.async{
		dBService.house_DB.updateOutHouse(outStockId).map(
			res => Redirect("/outhouse_manage")
		)
	}
}
