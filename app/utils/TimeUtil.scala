package utils

import java.sql.Date
import java.text.SimpleDateFormat

/**
	* author: dulei
	* date: 2018-05-24
	* desc: 
	*/
object TimeUtil {
	def StringToDate(str: String): Date ={
		val fm = new SimpleDateFormat("yyyy-MM-dd")
		val timestamp = fm.parse(str).getTime
		new Date(timestamp)
	}
}
