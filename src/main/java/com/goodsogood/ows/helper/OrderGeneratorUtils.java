package com.goodsogood.ows.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 订单号生成器
 */
public class OrderGeneratorUtils {

    /**
     * 根据uuID 生成
     *
     * @return
     */
    public static String getOrderIdByUUId() {
        Random random = new Random();
        int machineId = random.nextInt(10);//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        //         0 代表前面补充0     
        //         4 代表长度为4     
        //         d 代表参数为正数型
        String code = machineId + "" + String.format("%015d", hashCodeV);

        return code;
    }

    public static String getOrderIdByTime(Long userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result + userId;
    }


}
