package models

/**
	* author: dulei
	* date: 2018-05-25
	* desc: 入库清单对应的数据库视图Bean对象
	* +------------+-------------+------+-----+---------+-------+
	* | Field      | Type        | Null | Key | Default | Extra |
	* +------------+-------------+------+-----+---------+-------+
	* | instock_id | int(10)     | NO   |     | 0       |       |
	* | barcode    | varchar(13) | NO   |     | NULL    |       |
	* | merch_name | varchar(50) | NO   |     | NULL    |       |
	* | merch_num  | int(4)      | NO   |     | NULL    |       |
	* | in_date    | date        | YES  |     | NULL    |       |
	* | in_state   | varchar(4)  | YES  |     | NULL    |       |
	* +------------+-------------+------+-----+---------+-------+
	*/
case class InHouseView(instockId: Long, barcode: String, merchName: String, merchNum: Int, inDate:String, inState: String)
