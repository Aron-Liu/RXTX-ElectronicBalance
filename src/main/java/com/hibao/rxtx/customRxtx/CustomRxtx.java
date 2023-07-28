package com.hibao.rxtx.customRxtx;

import com.hibao.rxtx.util.PageData;
import gnu.io.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * 串口通讯
 *
 * @author Liu Wei Fan
 * @date 2021/7/12
 */
@Component
@Slf4j
public class CustomRxtx implements SerialPortEventListener {

    // 纵横 电子秤的 指令
    private static final String[] command = new String[]{"=0", ",0", ": !", ":!!", "=1", ",1"};

    // 重量
    private String weight = "0";

    //串口
    private static SerialPort serialPort = null;
    private static CommPortIdentifier comm = null;
    private static BufferedInputStream bufferStream;

    /**
     * /**
     * 开启串口
     *
     * @author Liu Wei Fan
     * @date 2021/7/12
     */
    public void startRxtx(String commName, int baudRate) throws NoSuchPortException, PortInUseException, IOException {
        try {
            //获取通信端口标识符
            comm = Serial.getCommPortIdentifierByName(commName);
            //打开串口
            serialPort = (SerialPort) comm.open("Read", 5000);
            //设置串口的参数 波特率 数据位 停止位 奇偶效验
            serialPort.setSerialPortParams(baudRate, Serial.getDataBits(), Serial.getStopBits(), Serial.getParity());
            //如果串口不为空的话 则监听获取信息 否则报错
            OutputStream output = null;

            if (serialPort != null) {
                try {
                    //获取输入输出流
                    bufferStream = new BufferedInputStream(serialPort.getInputStream());
                    output = serialPort.getOutputStream();
                    //添加监听
                    serialPort.addEventListener(this);
                    serialPort.notifyOnDataAvailable(true);
                    Serial.sendData(output, Protocal.getDataHead(), "52 52 52 01", Protocal.getCRC(), Protocal.getDataTail());
                } catch (TooManyListenersException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("串口获取失败-serialPort为null");
            }
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭串口
     *
     * @author Liu Wei Fan
     * @date 2021/7/12
     */
    public void closeRxtx() {
        if (serialPort != null) {
            try {
                serialPort.close();
                serialPort = null;
                weight = null;
                bufferStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 监听串口
     *
     * @param ev 事件
     * @author Liu Wei Fan
     * @date 2021/7/12
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent ev) {
        //判断数据类型
        switch (ev.getEventType()) {
            //通讯中断 Break Interrupt
            case SerialPortEvent.BI:
                System.out.println("BI");
                break;
            //溢位错误 Overrun Error
            case SerialPortEvent.OE:
                System.out.println("OE");
                break;
            //传帧错误 Framing Error
            case SerialPortEvent.FE:
                System.out.println("FE");
                break;
            //效验错误 Parity Error
            case SerialPortEvent.PE:
                System.out.println("PE");
                break;
            //载波检测 Carrier Detect
            case SerialPortEvent.CD:
                System.out.println("CD");
                break;
            //清除发送 Clear To Send
            case SerialPortEvent.CTS:
                System.out.println("CTS");
                break;
            //数据设备就绪 Data Set Ready
            case SerialPortEvent.DSR:
                System.out.println("DSR");
                break;
            //响铃指示 Ring Indicator
            case SerialPortEvent.RI:
                System.out.println("RI");
                break;
            //输出缓冲区清空 Output Buffer Empty
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                System.out.println("output");
                break;
            //端口有可用数据 Data Available
            case SerialPortEvent.DATA_AVAILABLE:
                // 读取数据
                readComm();
                break;
        }
    }

    /**
     * 读取串口返回信息
     *
     * @author Liu Wei Fan
     * @date 2021/7/20
     * @return: void
     */
    public void readComm() {
        try {
            byte[] bytes = null;
            byte[] tempBytes = new byte[1024];
            int len = -1;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (CustomRxtx.class) {
                len = bufferStream.read(tempBytes);
                bytes = new byte[len];

                for (int i = 0; i < bytes.length; i++) {
                    if (i == 100) {
                        break;
                    }
                    bytes[i] = tempBytes[i];
                }

                String unHandle = new String(bytes, StandardCharsets.UTF_8);
                log.debug("unHandle -----------> {}", unHandle);
                String[] split = unHandle.split("\\s+");

                for (String s : split) {
                    log.debug("split -----------> {}", s);
                    if (s.matches("^[0-9]+(\\.[0-9]{1,9})?$")) {
                        log.debug("称重数据 -----------> 【{}】", s);
                        setWeight(s);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取电脑串口
     *
     * @return
     * @throws Exception RXTX
     */
    public List<PageData> listAllCom() {
        List<PageData> listCom = new ArrayList<>();
        CommPortIdentifier portId;
        Enumeration<?> portList;
        try {
            portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                PageData car = new PageData();
                portId = (CommPortIdentifier) portList.nextElement();
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    car.put("value", portId.getName());
                    listCom.add(car);
                }
            }
            if (listCom.size() == 0) {
                PageData car = new PageData();
                car.put("result", "未找到串口!");
                listCom.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCom;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
