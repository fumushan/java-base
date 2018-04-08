package com.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtil {

    public static BigDecimal double2StringTail2(BigDecimal val, BigDecimal divisor) {
        if (val == null) {
            return new BigDecimal(0);
        }
        BigDecimal d = val.divide(divisor);
        return d.setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * 数字前补0转成字符串
     *
     * @param place
     * @param param
     * @return
     */
    public static String addPreZero(int place, long param) {

        String formart = "";
        String formartData = null;
        for (int i = 0; i < place; i++) {
            formart = "0" + formart;
        }
        formartData = formartNumber(formart, param);
        return formartData;
    }

    /**
     * 格式化数字
     *
     * @param formart
     * @param param
     * @return
     */
    public static String formartNumber(String formart, long param) {

        String formartData = null;
        try {
            DecimalFormat df = new DecimalFormat(formart);
            formartData = df.format(param);
        } catch (Exception e) {
        }
        return formartData;

    }

    /**
     * 格式化数字
     *
     * @param formart
     * @param param
     * @return
     */
    public static String formartDouble(String formart, double param) {

        String formartData = null;
        try {
            DecimalFormat df = new DecimalFormat(formart);
            formartData = df.format(param);
        } catch (Exception e) {
        }
        return formartData;
    }
}
