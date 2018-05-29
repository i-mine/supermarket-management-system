package models

/**
  * author: dulei
  * date: 18-5-29
  * desc: 营销策略表对应的Bean对象
  * +----------------+-------------+------+-----+---------+----------------+
  * | Field          | Type        | Null | Key | Default | Extra          |
  * +----------------+-------------+------+-----+---------+----------------+
  * | strategy_id    | int(10)     | NO   | PRI | NULL    | auto_increment |
  * | barcode        | varchar(13) | NO   |     | NULL    |                |
  * | sale_pro_start | date        | NO   |     | NULL    |                |
  * | sale_pro_stop  | date        | NO   |     | NULL    |                |
  * | discount       | float(4,2)  | NO   |     | NULL    |                |
  * +----------------+-------------+------+-----+---------+----------------+
  */
case class Strategy(strategyId: Long, barcode: String, startDate: String, endDate: String, discount: String)
case class Promotion(strategyId: Long, merchName: String, barcode: String, startDate: String, endDate: String, discount: String)

