package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.pj.service.XMLService;

public class TimeUtils {
	@Resource
	private static XMLService xmlService;

	public static long getDeltaTime(String startTime, String endTime) {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH-mm-ss");
		try {
			Date start = format.parse(startTime);
			Date end = format.parse(endTime);
			long time = (end.getTime() - start.getTime()) / 1000;
			return time;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return 0;
		}

	}

	public static long getSumTime(String startTime, String endTime, String dept) {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH-mm-ss");
		try {
			Date start = format.parse(startTime);
			Date end = format.parse(endTime);
			long time = (end.getTime() - start.getTime()) / 1000 / 60 / 60 / 24;
			int hour = Integer.parseInt(xmlService.readDept(dept));
			time = time * hour * 60 * 60;
			return time;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return 0;
		}
	}

	public static String getMinTime(String time1, String time2) {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH-mm-ss");
		try {
			Date start = format.parse(time1);
			Date end = format.parse(time2);
			if (start.getTime() > end.getTime()) {
				return time2;
			} else {
				return time1;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String getMaxTime(String time1, String time2) {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH-mm-ss");
		try {
			Date start = format.parse(time1);
			Date end = format.parse(time2);
			if (start.getTime() > end.getTime()) {
				return time1;
			} else {
				return time2;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
