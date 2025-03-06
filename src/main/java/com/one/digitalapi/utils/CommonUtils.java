package com.one.digitalapi.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils
{
	public static String getSystemDateInString(String pattern){
		String dateStringPattern = pattern!=null?pattern:"MM/dd/yyyy HH:mm:ss";
		DateFormat df = new SimpleDateFormat(dateStringPattern);
		return df.format(getSystemDate());
	}
	
	public static Date getSystemDate()
	{
		Calendar calCurrent = Calendar.getInstance();

		return calCurrent.getTime();
	}
}