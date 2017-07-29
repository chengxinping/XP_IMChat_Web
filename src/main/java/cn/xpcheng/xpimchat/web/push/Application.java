package cn.xpcheng.xpimchat.web.push;

import cn.xpcheng.xpimchat.web.push.provider.AuthRequestFilter;
import cn.xpcheng.xpimchat.web.push.provider.GsonProvider;
import cn.xpcheng.xpimchat.web.push.service.AccountService;
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

        //注册全局拦截器
        register(AuthRequestFilter.class);
        // 注册Json解析器
//        register(JacksonJsonProvider.class);
        //替换解析器
        register(GsonProvider.class);

        // 注册日志打印输出
        register(Logger.class);

    }
}
