package com.hibao.rxtx.customRxtx;

/**
 * -协议工具类
 *
 * @author Shendi
 */
public class Protocal {
    //数据头 数据尾
    private static String dataHead = "7B 01 00 16 31 33 39 31 34 30 30 30 31 37 39 42 42 42 42 42 42 42 42 42 53";
    private static String dataTail = "45";
    private static String CRC = "40 55 ";//CRC效验码 默认为 40 55

    /**
     * -将字符串转换为byte数组 16进制
     *
     * @param str 要转换的字符串
     * @return 如果为null 则转换失败 否则返回byte数组
     */
    public static byte[] switchStringToHexadecimal(String str) {
        //将空格清除
        str = str.replaceAll(" ", "");
        //将字符串按照2个一组拆分 存入byte中
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(str.substring(i + i, i + i + 2), 16);
            System.out.print(bytes[i] + " ");
        }
        System.out.println("\n");
        return bytes;
    }

    public static String switchByteToHexadecimal(byte[] bytes) {
        StringBuffer data = new StringBuffer();
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0; i < bytes.length; i++) {
            int num = bytes[i];
            String str = "";
            if (num == 0) {
                str = "00";
            } else {
                while (num != 0) {
                    str = hex[num % 16] + str;
                    num /= 16;
                }
            }
            //如果字符串长度不等于2的话 则添加一位0
            if (str.length() < 2) {
                str = "0" + str;
            }
            //获取16进制
            data.append(str + " ");
        }
        return data.toString();
    }

    public static String getDataHead() {
        return dataHead;
    }

    public static String getDataTail() {
        return dataTail;
    }

    public static String getCRC() {
        return CRC;
    }
}