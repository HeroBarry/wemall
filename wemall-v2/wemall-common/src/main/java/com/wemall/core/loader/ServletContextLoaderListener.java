package com.wemall.core.loader;


import com.wemall.core.security.SecurityManager;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

public class ServletContextLoaderListener
    implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent){
        ServletContext servletContext = servletContextEvent.getServletContext();
        SecurityManager securityManager =
            getSecurityManager(servletContext);
        Map urlAuthorities = securityManager
                             .loadUrlAuthorities();
        servletContext.setAttribute("urlAuthorities", urlAuthorities);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent){
        servletContextEvent.getServletContext().removeAttribute(
            "urlAuthorities");
    }

    protected SecurityManager getSecurityManager(ServletContext servletContext){
        return (SecurityManager)
               WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(
                   "securityManager");
    }
}