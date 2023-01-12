package com.hibao.rxtx.customRxtx;

import com.hibao.rxtx.config.AbstractServerInfos;
import com.hibao.rxtx.config.LinuxServerInfos;
import com.hibao.rxtx.config.WindowsServerInfos;
import com.hibao.rxtx.util.PageData;
import com.hibao.rxtx.util.R;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 定时获取秤的接口
 *
 * @author LiuWeiFan
 * @date 2021/7/18
 */
@RestController
@RequestMapping("/serialPort")
public class RxtxController {

    @Autowired
    private CustomRxtx customRxtx;

    /**
     * 获取数据
     */
    @GetMapping("/timingGet")
    public R timingGet() {
        return R.success().data("weight", customRxtx.getWeight());
    }

    @PostMapping("getMacAddress")
    public R getMacAddress() {
        //操作系统类型
        String osName = System.getProperty("os.name").toLowerCase();
        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        }else{//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }
        return R.success().data("macAddress",abstractServerInfos.getServerInfos().getMacAddress().get(0));
    }

    /**
     * 启动端口
     *
     * @param commName 提供COM口名称，例如：COM5
     * @param baudRate 提供波特率
     */
    @GetMapping("/startRxtx")
    public R startRxtx(String commName, int baudRate) {
        try {
            customRxtx.startRxtx(commName, baudRate);
        } catch (NoSuchPortException e) {
            return R.success().data("msg", "请连接" + commName + "!!!");
        } catch (PortInUseException e) {
            return R.success().data("msg", "端口正在被使用!!!");
        } catch (IOException e) {
            return R.success().data("msg", "COM已断开!!!");
        }
        return R.success();
    }

    /**
     * 关闭端口
     */
    @GetMapping("/closeRxtx")
    public R closeRxtx() {
        customRxtx.closeRxtx();
        return R.success();
    }

    /**
     * 获取电脑串口
     *
     * @author Liu Wei Fan
     * @date 2021/7/21
     */
    @GetMapping("/listAllCom")
    public R listAllCom() {
        List<PageData> list = customRxtx.listAllCom();
        return R.success().data("data", list);
    }

}
