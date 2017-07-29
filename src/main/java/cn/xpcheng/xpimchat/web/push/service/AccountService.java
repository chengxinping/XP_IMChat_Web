package cn.xpcheng.xpimchat.web.push.service;

import cn.xpcheng.xpimchat.web.push.bean.api.account.AccountRspModel;
import cn.xpcheng.xpimchat.web.push.bean.api.account.LoginModel;
import cn.xpcheng.xpimchat.web.push.bean.api.account.RegisterModel;
import cn.xpcheng.xpimchat.web.push.bean.api.base.ResponseModel;
import cn.xpcheng.xpimchat.web.push.bean.db.User;
import cn.xpcheng.xpimchat.web.push.factory.UserFactory;
import com.google.common.base.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author 哎呦哥哥
 * @time 2017年7月1日 10:56:43
 */

//实际访问路径  127.0.0.1/api/account
@Path("/account")
public class AccountService extends BaseService {

    @POST
    @Path("/login")
    //传入
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        if (LoginModel.check(model))
            return ResponseModel.buildParameterError();
        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if (user != null) {
            //如果有携带pushId
            if (!Strings.isNullOrEmpty(model.getPushId()))
                return bind(user, model.getPushId());

            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            return ResponseModel.buildLoginError();
        }
    }

    @POST
    @Path("/register")
    //传入
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        if (RegisterModel.check(model))
            return ResponseModel.buildParameterError();
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            //已经有账户
            return ResponseModel.buildHaveAccountError();
        }
        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            return ResponseModel.buildHaveNameError();
        }
        //开始注册逻辑
        user = UserFactory.register(model.getAccount(), model.getPassword(), model.getName());
        if (user != null) {
            //如果有携带pushId
            if (!Strings.isNullOrEmpty(model.getPushId()))
                return bind(user, model.getPushId());

            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            return ResponseModel.buildRegisterError();
        }
    }

    @POST
    @Path("/bind/{pushId}")
    //传入
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //从请求头中回去token字段
    //pushId从url地址获取
    public ResponseModel<AccountRspModel> bind(@PathParam("pushId") String pushId) {

        if (Strings.isNullOrEmpty(pushId))
            return ResponseModel.buildParameterError();

        User user = getSelf();
        return bind(user, pushId);
    }

    /**
     * 绑定的操作
     *
     * @param self   自己
     * @param pushId pushId
     * @return User
     */
    private ResponseModel<AccountRspModel> bind(User self, String pushId) {
        User user = UserFactory.bindPushId(self, pushId);
        if (self == null)
            return ResponseModel.buildServiceError();

        //返回当前用户 并且已经绑定
        AccountRspModel model = new AccountRspModel(user, true);
        return ResponseModel.buildOk(model);
    }
}
