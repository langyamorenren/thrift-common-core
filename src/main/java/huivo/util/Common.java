package huivo.util;

import huivo.core.data.postgresql.PGDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuzhixin on 11/11/14.
 */

public class Common {

    public static Calendar c = Calendar.getInstance();
    static Logger LOG = LoggerFactory.getLogger(Common.class);
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    public static boolean checkStringNull(String str) {
        return null == str || "".equals(str) || "''".equals(str) || str.trim().isEmpty();
    }

    /**
     * @param date
     * @return 1-7数字 对应 星期日 一 二 三 四 五 星期六
     */
    public static String getDayOfWeek(String date) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(c.get(Calendar.DAY_OF_WEEK));
    }

    public static String getStartDate(int week) {
        c.set(Calendar.WEEK_OF_YEAR, Calendar.WEEK_OF_YEAR - 2);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        return sdf.format(c.getTimeInMillis() + 24 * 3600 * 1000 * 7);
    }

    public static String getEndDate(int week) {
        c.set(Calendar.WEEK_OF_YEAR, Calendar.WEEK_OF_YEAR - 2);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        return sdf.format(c.getTimeInMillis() + 24 * 3600 * 1000);
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String neverNull(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static String joinStringToSql(List<String> ids) {
        if (ids.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Iterator<String> iterator = ids.iterator();
        boolean bStart = true;
        while (iterator.hasNext()) {
            if (!bStart) {
                sb.append(",");
            } else {
                bStart = false;
            }
            sb.append("'" + iterator.next() + "'");
        }
        return sb.toString();
    }

    public static String getCurrentTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }

    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm"<br />
     * 如果获取失败，返回null
     *
     * @return
     */
    public static String getUTCTimeStr() {
        StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day);
        UTCTimeBuffer.append(" ").append(hour).append(":").append(minute);
        try {
            format.parse(UTCTimeBuffer.toString());
            return UTCTimeBuffer.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatArrayForSQLIn(Collection<String> objCollection) {
        boolean bFirst = true;
        StringBuilder sb = new StringBuilder();
        for (String obj : objCollection) {
            if (!bFirst) {
                sb.append(',');
            } else {
                bFirst = false;
            }
            sb.append('\'');
            sb.append(obj);
            sb.append('\'');
        }
        return '(' + sb.toString() + ')';

    }

    /**
     * @param infoMap
     * @param table
     * @return
     */
    public static String formatInfoMapForSqlCreate(Map<String, String> infoMap, String table) {
        Set<Map.Entry<String, String>> entrySet = infoMap.entrySet();
        Iterator iterator = entrySet.iterator();
        String sql = "";
        String sqlKey = "(";
        String sqlValue = "(";
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (!Common.checkStringNull(value)) {
                sqlKey += String.format("%s,", key);
                if (Common.isNumeric(value)) {
                    sqlValue += String.format("%d,", Long.parseLong(value));
                } else {
                    sqlValue += String.format("'%s',", Common.escapeQueryChars(value));
                }
            }
        }
        sqlKey = sqlKey.substring(0, sqlKey.length() - 1) + ")";
        sqlValue = sqlValue.substring(0, sqlValue.length() - 1) + ")";
        sql = String.format("INSERT INTO %s " + sqlKey + " VALUES " + sqlValue, table);
        return sql;
    }

    /**
     * @param infoMap
     * @param table
     * @return
     */
    public static String formatInfoMapForSqlUpdate(Map<String, String> infoMap, String table, String column, String id) {
        Set<Map.Entry<String, String>> entrySet = infoMap.entrySet();
        Iterator iterator = entrySet.iterator();
        String sql = "";
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (!Common.checkStringNull(value)) {
                if (Common.isNumeric(value)) {
                    sql += String.format("%s=%d,", key, Long.parseLong(value));
                } else {
                    sql += String.format("%s='%s',", key, Common.escapeQueryChars(value));
                }
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        return String.format("UPDATE %s SET " + sql + " WHERE %s='%s'", table, column, id);
    }


    public static String formatArrayForPGSQL(List<String> objList) {
        System.out.println(objList);
        boolean bFirst = true;
        StringBuilder sb = new StringBuilder();
        for (String obj : objList) {
            if (!bFirst) {
                sb.append(',');
            } else {
                bFirst = false;
            }
            sb.append('"');
            sb.append(obj);
            sb.append('"');
        }
        return "'{" + sb.toString() + "}'";
    }

    public static String formatStringForPGSQL(String obj) {
        return "'{" + '"' + obj + '"' + "}'";
    }

    public static boolean executeTransaction(String[] sqlList) {
        boolean bRet = false;
        Statement st = null;
        Connection conn = null;
        boolean oldAUtoCommit = true;

        LOG.info("begin transaction");
        try {
            st = PGDatabase.getInstance().getStatement();
            conn = st.getConnection();
            oldAUtoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            for (String sql : sqlList) {
                LOG.info("in transaction ,exe sql = {}", sql);
                st.execute(sql);
            }
            conn.commit();
            bRet = true;

        } catch (SQLException e) {
            try {
                conn.rollback();
                LOG.error("transaction exe sql error ,rollback");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            //throw new SQLException("Init error");
        } finally {
            LOG.info("end transaction");
            if (null != st) {
                try {
                    conn.setAutoCommit(oldAUtoCommit);
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return bRet;
    }

    public static boolean executeInsertOrUpdateOne(String sql) {
        boolean bRet = false;
        Statement st = null;
        try {
            st = PGDatabase.getInstance().getStatement();
            if (st.executeUpdate(sql) == 1) {
                bRet = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != st) {
                try {
                    Connection conn = st.getConnection();
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return bRet;
    }

    /**
     * 判断字符型数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 过滤sql中传递的参数中非法字符
     *
     * @param s
     * @return
     */
    public static String escapeQueryChars(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\'') {
                c = '"';
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    /**
     * 获取当前日期的前N天的日期
     * @param days -1表示前一天 1表示后一天
     * @return
     */
    public static String beforeOrAfterCurrentDate(int days) {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, days);    //得到前N天
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }


    /**
     * 时间戳（微秒级）段转换成标准日期段
     *
     * @param startTimestamp
     * @param endTimestamp
     * @return
     */
    public static List combineDate(Long startTimestamp, Long endTimestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List dateList = new LinkedList();
        Long start = c.getTimeInMillis();
        int step = 3600 * 24 * 1000;
        Long end = start - step * 6;
        if (startTimestamp <= 0) {
            startTimestamp = start;
        }
        if (endTimestamp <= 0) {
            endTimestamp = end;
        }
        for (Long i = startTimestamp; i >= endTimestamp; i -= step) {
            String i_date = sdf.format(i);
            dateList.add(i_date);
        }
        return dateList;
    }

    /**
     * 标准时间转换成时间戳（微秒级）
     *
     * @param datetime
     * @return
     */
    public static Long datetimeToTimestamp(String datetime) {
        Long timestamp = System.currentTimeMillis();
        if (Common.checkStringNull(datetime)) {
            return timestamp;
        }
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(datetime);
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 标准日期转换成时间戳（微秒级）
     *
     * @param dateStr
     * @return
     */
    public static Long datetToTimestamp(String dateStr) {
        Long timestamp = System.currentTimeMillis();
        if (Common.checkStringNull(dateStr)) {
            return timestamp;
        }
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 时间戳（微秒级）转换成标准日期
     *
     * @param timestamp
     * @return
     */
    public static String timestampToDate(Long timestamp) {
        Timestamp ts = new Timestamp(timestamp);
        String date = "";
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间戳（微秒级）转换成标准时间
     *
     * @param timestamp
     * @return
     */
    public static String timestampToDatetime(Long timestamp) {
        Timestamp ts = new Timestamp(timestamp);
        String datetime = "";
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            datetime = sdf.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datetime;
    }

    /**
     * 获取上周周日 日期 2015-7-16
     */
    public static String getLastWeekMonday() {
        Calendar date = Calendar.getInstance(Locale.CHINA);
        date.setFirstDayOfWeek(Calendar.MONDAY);
        date.add(Calendar.WEEK_OF_MONTH, -1);
        date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        return sdf.format(date.getTime());
    }

    /**
     * 获取上周日期对象
     */
    public static Calendar getLastWeekMondayForCalendar() {
        Calendar date = Calendar.getInstance(Locale.CHINA);
        date.setFirstDayOfWeek(Calendar.MONDAY);
        date.add(Calendar.WEEK_OF_MONTH, -1);
        date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return date;
    }

    /**
     * 获取上周周一 日期 2015-12-16
     */
    public static String getLastWeekSunday() {
        Calendar date = Calendar.getInstance(Locale.CHINA);
        date.setFirstDayOfWeek(Calendar.SUNDAY);
        date.add(Calendar.WEEK_OF_MONTH, -1);
        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        return sdf.format(date.getTime());
    }

    private static Calendar getCalendarFormYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    /**
     * 通过年和 周数 获取周一时间
     *
     * @param year
     * @param weekNo
     * @return
     */
    public static String getStartDayOfWeekNo(int year, int weekNo) {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setMinimalDaysInFirstWeek(4);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 通过年和 周数 获取周日时间
     *
     * @param year
     * @param weekNo
     * @return
     */
    public static String getEndDayOfWeekNo(int year, int weekNo) {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setMinimalDaysInFirstWeek(4);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 时间unix转换
     *
     * @param timestampString
     * @return
     */
    public static String TimeStampDate(String timestampString, String format) {

        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(format).format(new java.util.Date(timestamp));
        return date;
    }

    /**
     * 将时间unix转换为int类型
     *
     * @param timeString
     * @param format
     * @return
     */
    public static int DateToInt(String timeString, String format) {

        int time = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(timeString);
            String strTime = date.getTime() + "";
            strTime = strTime.substring(0, 10);
            time = Integer.parseInt(strTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     *
     * 返回日期加X天后的日期
     * @param date
     * @param step
     * @return
     */
    public static String combineStepDate(String date, int step) {
        try {
            SimpleDateFormat sdf_date_format = new SimpleDateFormat("yyyy-MM-dd");
            GregorianCalendar gCal = new GregorianCalendar(
                    Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(5, 7)) - 1,
                    Integer.parseInt(date.substring(8, 10)));
            gCal.add(GregorianCalendar.DATE, step);
            return sdf_date_format.format(gCal.getTime());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前传递日期或时间的毫秒数
     * @param date
     * @return
     */
    public static long DateToTime(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date obj = df.parse(date);
            return obj.getTime();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getFirstDayOfWeek() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return format.format(calendar.getTimeInMillis());
    }

    //    public static String getLastDayOfWeek() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//        Calendar calendar = Calendar.getInstance(Locale.CHINA);
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
//        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
//        return format.format(calendar.getTimeInMillis());
//    }
//
    public static String getFirstDayOfWeek(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        try {
            calendar.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));
        } catch (Exception e) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return format.format(calendar.getTimeInMillis());
    }


}
