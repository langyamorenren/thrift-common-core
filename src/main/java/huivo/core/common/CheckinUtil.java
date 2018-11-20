package huivo.core.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by langya on 2014/11/17.
 */
public class CheckinUtil {


    /**
     * 获取时间点列表
     *
     * @return
     */
    public static List<String> getTimeNode() {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("00");
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 4; j++) {
                list.add(df.format(i) + ":" + df.format(j * 15));
            }
        }
        return list;
    }

    /**
     * 得到当前时间最近的时间点
     *
     * @return
     */
    public static String getCurrentNearTime() {
        String result = "";
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("00");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String time = format.format(new Date());
        String times[] = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minutes = Integer.parseInt(times[1]);
        if (minutes != 0 && minutes != 15 && minutes != 30 && minutes != 45) {
            if (minutes > 0 && minutes < 15) {
                minutes = 0;
            } else if (minutes > 15 && minutes < 30) {
                minutes = 15;
            } else if (minutes > 30 && minutes < 45) {
                minutes = 30;
            } else if (minutes > 45) {
                minutes = 45;
            }
            result = df.format(hour) + ":" + df.format(minutes);
        } else {
            result = time;
        }
        return result;
    }

    public static void main(String args[]) {
        System.out.println(getCurrentNearTime());
    }

    /**
     * 根据参数time获取最接近的15分钟以后的时间点
     *
     * @param time 格式为：hh:ss
     * @return
     */
    public static String getNearTime(String time) {
        String times[] = time.split(":");
        int tempi = 8;
        int tempj = 0;
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.applyPattern("00");
        if (times != null && times.length >= 2) {
            int hours = Integer.parseInt(times[0]);
            int minutes = Integer.parseInt(times[1]);
            for (int i = 0; i < 24; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i == hours) {
                        if (minutes >= (j - 1) * 15 && minutes <= j * 15) {
                            if (j == 3) {
                                tempi = (i + 1) % 24;
                                tempj = 0;
                                break;
                            } else {
                                tempi = i % 24;
                                tempj = j + 1;
                                break;
                            }
                        } else if (minutes > j * 15) {
                            tempi = (i + 1) % 24;
                            tempj = 1;
                        }
                    }

                }
            }
        }
        return df.format(tempi) + ":" + df.format(tempj * 15);
    }

}
