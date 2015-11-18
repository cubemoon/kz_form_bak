package com.kuaizhan.form.common;

import java.util.Calendar;

public class DateUtil {

	public static int getDateInt(long utc) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(utc);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		return Integer.parseInt(String.format("%04d%02d%02d", year, month, day));
	}

}
