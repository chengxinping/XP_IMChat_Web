package cn.xpcheng.xpimchat.web.push.provider;

import cn.xpcheng.xpimchat.web.push.bean.api.base.ResponseModel;
import cn.xpcheng.xpimchat.web.push.bean.db.User;
import cn.xpcheng.xpimchat.web.push.factory.UserFactory;
import com.google.common.base.Strings;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * @author 哎呦哥哥
 *         jersey拦截器  所有请求接口的过滤和拦截
 *         Created by qq972 on 2017/7/29.
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //检查是否是登录注册接口
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login") || relationPath.startsWith("account/register"))
            return;
        String token = ((ContainerRequest) requestContext).getHeaders().getFirst("token");
        if (Strings.isNullOrEmpty(token)) {
            final User self = UserFactory.findByToken(token);
            if (self != null) {
                //给当前请求添加一个上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    //主体部分
                    @Override
                    public Principal getUserPrincipal() {
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        //判断用户的权限  role 是权限
                        //可以管理管理员权限等
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                //写入山下文就返回
                return;
            }
        }
        //直接返回一个账户需要登录的Model
        ResponseModel model = ResponseModel.buildAccountError();
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();
        //停止请求继续分发，不会走到Service去
        requestContext.abortWith(response);
    }
}
