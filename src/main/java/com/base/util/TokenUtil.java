package com.base.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class TokenUtil {

    public static String APP_TOKEN = "5471978WBdSL9933";
    final static Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * 校验请求参数中的tk值是否合法
     *
     * @param tk
     * @return
     */
    public static boolean checkTK(String tk) {
        // 配置文件中的token必须大于4个字符
        if (StringUtils.isEmpty(tk)) {
            return false;
        }

        String ntk = judgeRequest(tk);
        byte[] b = Base64Coder.decode(ntk);
        if (StringUtils.isEmpty(b)) {
            return false;
        }
        ntk = new String(b);
        if (!checkAppTK(ntk)) {
            return false;
        }
        // log.info("解析后的token：" + ntk + ",配置文件：" + appToken);
        if (!judgeMark(ntk, APP_TOKEN)) {
            return false;
        }
        return true;
    }

    /**
     * 对tk解密
     *
     * @return notk 时间戳13位
     */
    public static String judgeRequest(String tk) {
        String str = "";
        // 对Loginkey进行解密处理
        char[] cha = tk.toCharArray();
        for (char c : cha) {
            str = str + LoginKeyDecode.ReplaceNum(c);
        }
        return str;
    }

    /**
     * 检查接口过来的加密字符串在解密后是否包含配置文件中给定的特定字符串
     *
     * @param ntk
     * @return
     */
    private static boolean checkAppTK(String ntk) {
        if (ntk.lastIndexOf(APP_TOKEN) < 0) {
            return false;
        }

        return true;
    }

    /**
     * 检查解密后的时间戳是否在给定范围
     *
     * @param notk   解密后的完整tk，固定串+时间戳
     * @param capptk 配置文件中的apptk
     * @return
     */
    public static boolean judgeMark(String notk, String capptk) {
        String inputime = notk.replace(capptk, "");

		/*
         * if(inputime.length() <= 10){ inputime += "000"; }
		 */
        // 时间戳在补全后必须是13位
        if (inputime.length() != 13) {
            return false;
        }

        long d = new Date().getTime();
        try {
            if (Math.abs(d - Long.valueOf(inputime)) > 60000) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("token检查中时间比对出错...");
            logger.error(e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis() + "" + APP_TOKEN);
        String baseStr = Base64Coder.encode((System.currentTimeMillis() + "" + APP_TOKEN).getBytes());
        System.out.println(baseStr);
        String lkDcdStr = judgeRequest(baseStr);
        System.out.println(lkDcdStr);
        String ddstr = judgeRequest(lkDcdStr);
        System.out.println(ddstr);
        System.out.println(new String(Base64Coder.decode(ddstr)));
        System.out.println(new String(Base64Coder.decode(lkDcdStr)));
        //13位时间戳+原串 Base64编码然后再数字转换
    }

}
