package models

/**
	* author: dulei
	* date: 2018-05-25
	* desc: 出库清单对应的数据库视图Bean对象
	*
	* +---------------+-------------+------+-----+---------+-------+
	* | Field         | Type        | Null | Key | Default | Extra |
	* +---------------+-------------+------+-----+---------+-------+
	* | barcode       | varchar(13) | NO   |     | NULL    |       |
	* | merch_name    | varchar(50) | NO   |     | NULL    |       |
	* | shelf_num     | int(4)      | YES  |     | 0       |       |
	* | shelf_pre_num | int(4)      | YES  |     | 0       |       |
	* +---------------+-------------+------+-----+---------+-------+
	*/
case class OutShelfView(barcode: String, merchName: String, shelfNum: Int, shelfPreNum:Int)
/**
	* OutHouseView:
	* +-------------+-------------+------+-----+---------+-------+
	* | Field       | Type        | Null | Key | Default | Extra |
	* +-------------+-------------+------+-----+---------+-------+
	* | outstock_id | int(10)     | NO   |     | 0       |       |
	* | barcode     | varchar(13) | NO   |     | NULL    |       |
	* | merch_name  | varchar(50) | NO   |     | NULL    |       |
	* | merch_num   | int(4)      | NO   |     | NULL    |       |
	* | out_date    | date        | NO   |     | NULL    |       |
	* | out_state   | varchar(4)  | YES  |     | NULL    |       |
	* +-------------+-------------+------+-----+---------+-------+
 */
case class OutHouseView(outstockId: Long, barcode: String, merchName: String, merchNum: Int, outDate: String, outState: String)