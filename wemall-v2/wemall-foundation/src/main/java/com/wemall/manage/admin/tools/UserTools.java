package com.wemall.manage.admin.tools;

import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.concurrent.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员工具组件
 */
@Component
public class UserTools {
    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private IUserService userSerivce;

    public List<User> query_user(){
        List users = new ArrayList();
        Object[] objs = this.sessionRegistry.getAllPrincipals();
        for (int i = 0; i < objs.length; i++){
            User user = this.userSerivce.getObjByProperty("userName",
                        CommUtil.null2String(objs[i]));

            users.add(user);
        }

        return users;
    }

    public boolean userOnLine(String userName){
        boolean ret = false;
        List<User> users = query_user();
        for (User user : users){
            if ((user != null) && (user.getUsername().equals(userName.trim()))){
                ret = true;
            }
        }

        return ret;
    }
}




