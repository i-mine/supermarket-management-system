package utils

/**
	* author: dulei
	* date: 2018-05-24
	* desc: 用于页面展示分页
	* @param start 分页开始记录索引
	* @param limit 单页记录数
	* @param data  单页所有记录对象
	* @param total 数据表中所有记录数量
	*/
case class  Page[T](start: Int, limit: Int, data: List[T], total: Int)
