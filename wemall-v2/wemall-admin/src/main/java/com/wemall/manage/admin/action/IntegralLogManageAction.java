package com.wemall.manage.admin.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.IntegralLog;
import com.wemall.foundation.domain.SysConfig;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.domain.query.IntegralLogQueryObject;
import com.wemall.foundation.service.IIntegralLogService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import com.wemall.foundation.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 积分明细管理控制器
 */
@Controller
public class IntegralLogManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IIntegralLogService integrallogService;

    @Autowired
    private IUserService userService;

    /**
     * 查询积分明细
     * @param request
     * @param response
     * @param currentPage
     * @param orderBy
     * @param orderType
     * @param userName
     * @return
     */
    @SecurityMapping(display = false, rsequence = 0, title = "积分明细", value = "/admin/integrallog_list.htm*", rtype = "admin", rname = "积分明细", rcode = "user_integral", rgroup = "会员")
    @RequestMapping({"/admin/integrallog_list.htm"})
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userName){
        ModelAndView mv = new JModelAndView("admin/blue/integrallog_list.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))){
            url = CommUtil.getURL(request);
        }
        String params = "";
        IntegralLogQueryObject qo = new IntegralLogQueryObject(currentPage, mv,
                orderBy, orderType);

        if ((userName != null) && (!userName.equals("")))
            qo.addQuery("obj.integral_user.userName",
                        new SysMap("userName",
                                   userName), "=");
        IPageList pList = this.integrallogService.list(qo);
        CommUtil.saveIPageList2ModelAndView(
            url + "/admin/integrallog_list.htm", "", "&userName=" +
            CommUtil.null2String(userName), pList, mv);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "积分管理", value = "/admin/user_integral.htm*", rtype = "admin", rname = "积分管理", rcode = "user_integral", rgroup = "会员")
    @RequestMapping({"/admin/user_integral.htm"})
    public ModelAndView user_integral(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new JModelAndView("admin/blue/user_integral.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        SysConfig config = this.configService.getSysConfig();
        if (!config.isIntegral()){
            mv = new JModelAndView("admin/blue/error.html", this.configService
                                   .getSysConfig(), this.userConfigService.getUserConfig(), 0,
                                   request, response);
            mv.addObject("op_title", "系统未开启积分功能，设置失败");
            mv.addObject("open_url", "admin/operation_base_set.htm");
            mv.addObject("open_op", "积分开启");
            mv.addObject("open_mark", "operation_base_op");
            mv.addObject("list_url", CommUtil.getURL(request) +
                         "/admin/welcome.htm");
        }

        return mv;
    }

    /**
     * 查询会员积分
     * @param request
     * @param response
     * @param userName
     */
    @SecurityMapping(display = false, rsequence = 0, title = "积分动态获取", value = "/admin/verify_user_integral.htm*", rtype = "admin", rname = "积分管理", rcode = "user_integral", rgroup = "会员")
    @RequestMapping({"/admin/verify_user_integral.htm"})
    public void verify_user_integral(HttpServletRequest request, HttpServletResponse response, String userName){
        User user = this.userService.getObjByProperty("userName", userName);
        int ret = -1;
        if (user != null){
            ret = user.getIntegral();
        }
        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "no-cache");
        try {request.setCharacterEncoding("UTF-8");} catch (java.io.UnsupportedEncodingException e1) {e1.printStackTrace();}
        try {
            PrintWriter writer = response.getWriter();
            writer.print(ret);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 保存积分
     * @param request
     * @param response
     * @param userName
     * @param operate_type
     * @param integral
     * @param content
     * @return
     */
    @SecurityMapping(display = false, rsequence = 0, title = "积分管理保存", value = "/admin/user_integral_save.htm*", rtype = "admin", rname = "积分管理", rcode = "user_integral", rgroup = "会员")
    @RequestMapping({"/admin/user_integral_save.htm"})
    public ModelAndView user_integral_save(HttpServletRequest request, HttpServletResponse response, String userName, String operate_type, String integral, String content){
        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);

        User user = this.userService.getObjByProperty("userName", userName);
        if (operate_type.equals("add")){
            user.setIntegral(user.getIntegral() + CommUtil.null2Int(integral));
        }else if (user.getIntegral() > CommUtil.null2Int(integral)){
            user.setIntegral(user.getIntegral() - CommUtil.null2Int(integral));
        }else{
            user.setIntegral(0);
        }

        this.userService.update(user);

        IntegralLog log = new IntegralLog();
        log.setAddTime(new Date());
        log.setContent(content);
        if (operate_type.equals("add"))
            log.setIntegral(CommUtil.null2Int(integral));
       else{
            log.setIntegral(-CommUtil.null2Int(integral));
        }
        log.setOperate_user(SecurityUserHolder.getCurrentUser());
        log.setIntegral_user(user);
        log.setType("system");
        this.integrallogService.save(log);
        mv.addObject("list_url", CommUtil.getURL(request) +
                     "/admin/user_integral.htm");
        mv.addObject("op_title", "操作用户积分成功");

        return mv;
    }
}




