package cc.uman.generator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 日期处理
 * </p>
 *
 * @author shaohua
 * Email xiaomabenten@gmail.com
 * created by 2018/6/14
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
}
