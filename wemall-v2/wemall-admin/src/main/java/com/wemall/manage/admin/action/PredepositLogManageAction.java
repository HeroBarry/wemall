package com.wemall.manage.admin.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.query.PredepositLogQueryObject;
import com.wemall.foundation.service.IPredepositLogService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 预存款明细管理控制器
 */
@Controller
public class PredepositLogManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IPredepositLogService predepositlogService;

    @SecurityMapping(display = false, rsequence = 0, title = "预存款明细列表", value = "/admin/predepositlog_list.htm*", rtype = "admin", rname = "预存款明细", rcode = "predeposit", rgroup = "会员")
    @RequestMapping({"/admin/predepositlog_list.htm"})
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String userName){
        ModelAndView mv = new JModelAndView(
            "admin/blue/predepositlog_list.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        if (this.configService.getSysConfig().isDeposit()){
            String url = this.configService.getSysConfig().getAddress();
            if ((url == null) || (url.equals(""))){
                url = CommUtil.getURL(request);
            }
            String params = "";
            PredepositLogQueryObject qo = new PredepositLogQueryObject(
                currentPage, mv, orderBy, orderType);
            if (!CommUtil.null2String(userName).equals("")){
                qo.addQuery("obj.pd_log_user.userName",
                            new SysMap("userName",
                                       userName), "=");
            }
            IPageList pList = this.predepositlogService.list(qo);
            CommUtil.saveIPageList2ModelAndView(url +
                                                "/admin/predepositlog_list.htm", "", params, pList, mv);
            mv.addObject("userName", userName);
        }else{
            mv = new JModelAndView("admin/blue/error.html", this.configService
                                   .getSysConfig(), this.userConfigService.getUserConfig(), 0,
                                   request, response);
            mv.addObject("op_title", "系统未开启预存款");
            mv.addObject("list_url", CommUtil.getURL(request) +
                         "/admin/operation_base_set.htm");
        }

        return mv;
    }
}




