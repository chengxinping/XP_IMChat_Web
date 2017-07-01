package cn.xpcheng.xpimchat.web.push;

import cn.xpcheng.xpimchat.web.push.service.AccountService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * @author 哎呦哥哥
 * @time 2017年7月1日 10:51:01
 */
public class Application extends ResourceConfig {
    public Application() {
        // 注册逻辑处理的包名
        packages(AccountService.class.getPackage().getName());

        // 注册Json解析器
        register(JacksonJsonProvider.class);

        // 注册日志打印输出
        register(Logger.class);

    }
}
