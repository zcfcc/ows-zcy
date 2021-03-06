package com.goodsogood.ows.helper;

import java.time.Instant;

/**
 * @Description: 时间日期工具类

 */
public final class DateUtil {

    private DateUtil(){}

    /**
     * 获取当前时间戳(秒级,10位数字)
     *
     * @return 时间戳
     */
    public static String getTimeStampSecond(){
        Instant timestamp = Instant.now();

        return String.valueOf(timestamp.toEpochMilli()).substring(0,10);
    }
}
