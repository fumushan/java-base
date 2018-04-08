package com.base.util;


/**
 * 手机设备码的解密规则
 *
 * @author Administrator
 */
public class LoginKeyDecode {
	
    public static String Replace(char a) {

        String str = String.valueOf(a);
        if (a >= 48 && a <= 57) {
            if ("1".equals(str)) {
                str = str.replaceAll("1", "7");
            } else if ("2".equals(str)) {
                str = str.replaceAll("2", "5");
            } else if ("3".equals(str)) {
                str = str.replaceAll("3", "8");
            } else if ("4".equals(str)) {
                str = str.replaceAll("4", "0");
            } else if ("5".equals(str)) {
                str = str.replaceAll("5", "2");
            } else if ("6".equals(str)) {
                str = str.replaceAll("6", "9");
            } else if ("7".equals(str)) {
                str = str.replaceAll("7", "1");
            } else if ("8".equals(str)) {
                str = str.replace("8", "3");
            } else if ("9".equals(str)) {
                str = str.replace("9", "6");
            } else if ("0".equals(str)) {
                str = str.replace("0", "4");
            }
        } else if (a >= 65 && a <= 90) {
            char b = (char) (a - 3);
            if (b < 65) {
                int i = 65 - b;
                b = (char) (91 - i);
            }
            str = String.valueOf((char) (b + 32));
        } else if (a >= 97 && a <= 122) {
            char b = (char) (a - 3);
            if (b < 97) {
                int i = 97 - b;
                b = (char) (123 - i);
            }
            str = String.valueOf((char) (b - 32));
        }
        return str;
    }

    public static String endcodeReplace(byte[] a) {
        String s = "";
        for (byte b : a) {
            s += encodeReplace(b);
        }
        return s;
    }

    private static String encodeReplace(byte a) {
        String str = String.valueOf((char) a);
        if (a >= 48 && a <= 57) {
            if ("1".equals(str)) {
                str = str.replaceAll("1", "7");
            } else if ("2".equals(str)) {
                str = str.replaceAll("2", "5");
            } else if ("3".equals(str)) {
                str = str.replaceAll("3", "8");
            } else if ("4".equals(str)) {
                str = str.replaceAll("4", "0");
            } else if ("5".equals(str)) {
                str = str.replaceAll("5", "2");
            } else if ("6".equals(str)) {
                str = str.replaceAll("6", "9");
            } else if ("7".equals(str)) {
                str = str.replaceAll("7", "1");
            } else if ("8".equals(str)) {
                str = str.replace("8", "3");
            } else if ("9".equals(str)) {
                str = str.replace("9", "6");
            } else if ("0".equals(str)) {
                str = str.replace("0", "4");
            }
        } else if (a >= 65 && a <= 90) {
            //大写转小写 +32
            char b = (char) (a + 32);
            b = (char) (b + 3);
            if (b > 122) {
                b = (char) (b - 26);
            }
            str = String.valueOf(b);
        } else if (a >= 97 && a <= 122) {
            char b = (char) (a - 32);
            b = (char) (b + 3);
            if (b > 90) {
                b = (char) (b - 26);
            }
            str = String.valueOf(b);
        }
        return str;
    }

    public static String ReplaceNum(char a) {
        String str = String.valueOf(a);
        if ("1".equals(str)) {
            str = str.replaceAll("1", "7");
        } else if ("2".equals(str)) {
            str = str.replaceAll("2", "5");
        } else if ("3".equals(str)) {
            str = str.replaceAll("3", "8");
        } else if ("4".equals(str)) {
            str = str.replaceAll("4", "0");
        } else if ("5".equals(str)) {
            str = str.replaceAll("5", "2");
        } else if ("6".equals(str)) {
            str = str.replaceAll("6", "9");
        } else if ("7".equals(str)) {
            str = str.replaceAll("7", "1");
        } else if ("8".equals(str)) {
            str = str.replace("8", "3");
        } else if ("9".equals(str)) {
            str = str.replace("9", "6");
        } else if ("0".equals(str)) {
            str = str.replace("0", "4");
        }
        return str;
    }
}
