package com.wemall.core.filter;


import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecondDomainFilter
    implements Filter {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISysConfigService configService;

    public void destroy(){
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        if (this.configService.getSysConfig().isSecond_domain_open()){
            Cookie[] cookies = request.getCookies();
            String id = "";
            if (cookies != null){
                for (Cookie cookie : cookies){
                    if (cookie.getName().equals("wemall_user_session")){
                        id = CommUtil.null2String(cookie.getValue());
                    }
                }
                User user = this.userService.getObjById(CommUtil.null2Long(id));
                if (user != null)
                    request.getSession(false).setAttribute("user", user);
            }
        }
        chain.doFilter(req, res);
    }

    public void init(FilterConfig config)
    throws ServletException {
    }
}