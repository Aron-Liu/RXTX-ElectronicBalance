package com.hibao.rxtx.customRxtx;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;

/**
 * -串口通讯工具类
 *
 * @author Shendi
 */
public class Serial {

    private static int dataBits = SerialPort.DATABITS_8;//数据位 默认为8
    private static int stopBits = SerialPort.STOPBITS_1;//停止位 默认为1
    private static int parity = SerialPort.PARITY_NONE;//奇偶效验 默认为无

    public static int getDataBits() {
        return dataBits;
    }

    public static void setDataBits(int dataBits) {
        Serial.dataBits = dataBits;
    }

    public static int getStopBits() {
        return stopBits;
    }

    public static void setStopBits(int stopBits) {
        Serial.stopBits = stopBits;
    }

    public static int getParity() {
        return parity;
    }

    public static void setParity(int parity) {
        Serial.parity = parity;
    }

    /**
     * -获取通信端口标识符通过端口名
     *
     * @param commName 端口名
     * @return 实例通讯端口标识符
     * @throws NoSuchPortException
     */
    public static CommPortIdentifier getCommPortIdentifierByName(String commName) throws NoSuchPortException {
        //如果名称为空 则返回null
        if ("".equals(commName)) {
            return null;
        }
        CommPortIdentifier comm = CommPortIdentifier.getPortIdentifier(commName);
        return comm;
    }

    /**
     * -发送数据
     *
     * @param serialPortOutput 输出流
     * @param dataHead         数据头
     * @param data             数据
     * @param CRC              校验位
     * @param dataTail         数据尾
     */
    public static void sendData(OutputStream serialPortOutput, String dataHead, String data, String CRC, String dataTail) {
        //组合字符串 数据头+数据+校验位+数据尾
        StringBuffer str = new StringBuffer();
        if (dataHead != null) {
            str.append(dataHead);
        }
        if (data != null) {
            str.append(data);
        }
        if (CRC != null) {
            str.append(CRC);
        }
        if (dataTail != null) {
            str.append(dataTail);
        }
        System.out.println("发送数据:" + str.toString());
        //将字符串变成16进制存进byte中
        byte[] bytes = Protocal.switchStringToHexadecimal(str.toString());
        //发送数据
        try {
            serialPortOutput.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}