package cn.itcast.utils;

import java.util.Calendar;
public class TimestampUtils {
	
		public static Long timestamp(int minute){
			Calendar time = Calendar.getInstance();
			time.add(Calendar.MINUTE, +minute);
			System.out.println(time.getTimeInMillis());
			System.out.println(time.getTimeInMillis()/1000);
			return time.getTimeInMillis();
		}

		
		public static String nextMonth(int year, int month, int k){
			Calendar time = Calendar.getInstance();
			time.set(Calendar.YEAR, year);
			time.set(Calendar.MONTH, month+1);
			time.add(Calendar.MONTH, k);
			return (time.get(Calendar.YEAR)+1900)+"-"+(time.get(Calendar.MONTH)+1);
		}
		
		
	

	
	
}
