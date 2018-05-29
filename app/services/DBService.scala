package services

import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.JdbcProfile

/**
  * author: dulei
  * date: 18-5-8
  * desc: 提供操作各个Bean数据的Service
  */
@Singleton
class DBService @Inject()(staffDBService: StaffDBService,
                          provideDBService: ProvideDBService,
                          memberDBService: MemberDBService,
                          merchTypeDBService: MerchTypeDBService,
                          merchDBService: MerchDBService,
                          orderDBService: OrderDBService,
                         warehouseDBService:WareHouseDBService,
                          promotionDBService: PromotionDBService)  {
  val staff_DB = staffDBService
  val provide_DB = provideDBService
  val member_DB = memberDBService
  val merchType_DB = merchTypeDBService
  val merch_DB = merchDBService
  val order_DB = orderDBService
  val house_DB = warehouseDBService
  val promotion_DB = promotionDBService
}
