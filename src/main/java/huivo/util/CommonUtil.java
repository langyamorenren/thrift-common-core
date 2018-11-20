package huivo.util;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.DataFormatException;

/**
 * Created by langya on 2014/11/13.
 */
public class CommonUtil {
	public static String getEnvironment() {
		String env = "NOMAD_ENV";
		String module = System.getenv(env);
		if (module == null || "".equals(module)) {

			throw new RuntimeException("do not set enviroment ,can not start");
			// module="development";
		}
		return module;
	}

	public static int getTodayDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(format.format(new Date()));
	}

	public static int getMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		return Integer.parseInt(format.format(new Date()));
	}

	public static String getFirstDayOfWeek() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return format.format(calendar.getTimeInMillis());
	}

	public static boolean chkTheSameDay(long timemilles1, long timemilles2){
		DateTime dt1 = new DateTime(timemilles1);
		DateTime dt2 = new DateTime(timemilles2);
		return dt1.getYear() == dt2.getYear() && dt1.getDayOfYear() == dt2.getDayOfYear();
	}

	public static Timestamp getTimestamp(String date, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		long time = 0;
		try {
			time = format.parse(date).getTime();
		} catch (ParseException e) {
			// e.printStackTrace();
		}
		return new Timestamp(time);
	}

	public static Timestamp getTimestamp(String date) {
		return getTimestamp(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static int minusTwoDays(String fromDate, String toDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			Date from = format.parse(fromDate);
			Date to = format.parse(toDate);
			System.out.println(to.getTime());
			System.out.println(from.getTime());
			return (int) ((to.getTime() - from.getTime()) / (24 * 60 * 60 * 1000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getAddDay(String day, int space) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		try {
			calendar.set(Calendar.YEAR, Integer.parseInt(day.substring(0, 4)));
			calendar.set(Calendar.MONTH, Integer.parseInt(day.substring(4, 6)) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.substring(6, 8)));
		} catch (Exception e) {
			calendar.setTimeInMillis(System.currentTimeMillis());
		}
		calendar.add(Calendar.DAY_OF_MONTH, space);
		return format.format(calendar.getTimeInMillis());
	}

	public static String safeString(String param) {
		return param == null ? "" : param;
	}

	public static boolean isProductionEvn() {
		boolean result = false;
		String env = "NOMAD_ENV";
		String module = System.getenv(env);
		if (module != null && module.equals("production")) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public static String getUUID32() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 时间字符串格式化
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateFormatString(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return format.format(date);

	}
	
	/**
	 * 按","连接集合元素
	 * 
	 * @param collection
	 * @return
	 */
	public static String join(Collection<?> collection) {
		return join(collection, ",");
	}
	
	/**
	 * 按","连接集合元素
	 * 
	 * @param objects
	 * @return
	 */
	public static String join(Object[] objects) {
		return join(Arrays.asList(objects), ",");
	}
	
	/**
	 * 按指定分隔符连接集合元素
	 * 
	 * @param objects
	 * @param separator
	 * @return
	 */
	public static String join(Object[] objects, String separator) {
		return join(Arrays.asList(objects), separator);
	}

	/**
	 * 按指定分隔符连接集合元素
	 * 
	 * @param collection
	 * @param separator
	 * @return
	 */
	public static String join(Collection<?> collection, String separator) {

		if (collection == null || collection.isEmpty() || separator == null || separator.isEmpty()) {
			return null;
		}

		StringBuffer strings = new StringBuffer();

		Iterator<?> iterator = collection.iterator();

		while (iterator.hasNext()) {

			Object object = iterator.next();

			strings.append(object.toString());

			if (iterator.hasNext()) {
				strings.append(separator);
			}

		}

		return strings.toString();

	}
	
	public static String joinSql(Collection<?> collection) {

		if (collection == null || collection.isEmpty()) {
			return null;
		}

		StringBuffer strings = new StringBuffer();

		Iterator<?> iterator = collection.iterator();

		while (iterator.hasNext()) {

			Object object = iterator.next();

			strings.append("'" + object.toString() + "'");

			if (iterator.hasNext()) {
				strings.append(",");
			}

		}

		return strings.toString();

	}
	
}
