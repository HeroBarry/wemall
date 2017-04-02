package com.wemall.manage.buyer.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.foundation.domain.PredepositCash;
import com.wemall.foundation.domain.PredepositLog;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.domain.query.PredepositCashQueryObject;
import com.wemall.foundation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 买家提现控制器
 */
@Controller
public class PredepositCashBuyerAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IPredepositCashService predepositCashService;

    @Autowired
    private IPredepositLogService predepositLogService;

    @Autowired
    private IUserService userService;

    @SecurityMapping(display = false, rsequence = 0, title = "提现管理", value = "/buyer/buyer_cash.htm*", rtype = "buyer", rname = "预存款管理", rcode = "predeposit_set", rgroup = "用户中心")
    @RequestMapping({"/buyer/buyer_cash.htm"})
    public ModelAndView buyer_cash(HttpServletRequest request, HttpServletResponse response, String id){
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/buyer_cash.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        if (!this.configService.getSysConfig().isDeposit()){
            mv = new JModelAndView("error.html", this.configService.getSysConfig(),
                                   this.userConfigService.getUserConfig(), 1, request,
                                   response);
            mv.addObject("op_title", "系统未开启预存款");
            mv.addObject("url", CommUtil.getURL(request) + "/buyer/index.htm");
        }else{
            mv.addObject("availableBalance",
                         Double.valueOf(CommUtil.null2Double(this.userService.getObjById(
                                            SecurityUserHolder.getCurrentUser().getId())
                                        .getAvailableBalance())));
        }

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "提现管理保存", value = "/buyer/buyer_cash_save.htm*", rtype = "buyer", rname = "预存款管理", rcode = "predeposit_set", rgroup = "用户中心")
    @RequestMapping({"/buyer/buyer_cash_save.htm"})
    public ModelAndView buyer_cash_save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage){
        ModelAndView mv = new JModelAndView("success.html", this.configService
                                            .getSysConfig(), this.userConfigService.getUserConfig(), 1,
                                            request, response);
        WebForm wf = new WebForm();
        PredepositCash obj = (PredepositCash)wf.toPo(request, PredepositCash.class);
        obj.setCash_sn("cash" +
                       CommUtil.formatTime("yyyyMMddHHmmss", new Date()) +
                       SecurityUserHolder.getCurrentUser().getId());
        obj.setAddTime(new Date());
        obj.setCash_user(SecurityUserHolder.getCurrentUser());
        User user = this.userService.getObjById(
                        SecurityUserHolder.getCurrentUser().getId());

        if (CommUtil.null2Double(obj.getCash_amount()) <=
                CommUtil.null2Double(user.getAvailableBalance())){
            this.predepositCashService.save(obj);

            PredepositLog log = new PredepositLog();
            log.setAddTime(new Date());
            log.setPd_log_amount(obj.getCash_amount());
            log.setPd_log_info("申请提现");
            log.setPd_log_user(obj.getCash_user());
            log.setPd_op_type("提现");
            log.setPd_type("可用预存款");
            this.predepositLogService.save(log);
            mv.addObject("op_title", "提现申请成功");
        }else{
            mv = new JModelAndView("error.html", this.configService.getSysConfig(),
                                   this.userConfigService.getUserConfig(), 1, request,
                                   response);
            mv.addObject("op_title", "提现金额大于用户余额，提现失败");
        }

        mv.addObject("url", CommUtil.getURL(request) + "/buyer/buyer_cash.htm");

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "提现管理", value = "/buyer/buyer_cash_list.htm*", rtype = "buyer", rname = "预存款管理", rcode = "predeposit_set", rgroup = "用户中心")
    @RequestMapping({"/buyer/buyer_cash_list.htm"})
    public ModelAndView buyer_cash_list(HttpServletRequest request, HttpServletResponse response, String currentPage){
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/buyer_cash_list.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        if (!this.configService.getSysConfig().isDeposit()){
            mv = new JModelAndView("error.html", this.configService.getSysConfig(),
                                   this.userConfigService.getUserConfig(), 1, request,
                                   response);
            mv.addObject("op_title", "系统未开启预存款");
            mv.addObject("url", CommUtil.getURL(request) + "/buyer/index.htm");
        }else{
            PredepositCashQueryObject qo = new PredepositCashQueryObject(
                currentPage, mv, "addTime", "desc");
            qo.addQuery("obj.cash_user.id",
                        new SysMap("user_id",
                                   SecurityUserHolder.getCurrentUser().getId()), "=");
            IPageList pList = this.predepositCashService.list(qo);
            CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        }

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "会员提现详情", value = "/buyer/buyer_cash_view.htm*", rtype = "buyer", rname = "预存款管理", rcode = "predeposit_set", rgroup = "用户中心")
    @RequestMapping({"/buyer/buyer_cash_view.htm"})
    public ModelAndView buyer_cash_view(HttpServletRequest request, HttpServletResponse response, String id){
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/buyer_cash_view.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        if (this.configService.getSysConfig().isDeposit()){
            PredepositCash obj = this.predepositCashService.getObjById(
                                     CommUtil.null2Long(id));
            mv.addObject("obj", obj);
        }else{
            mv = new JModelAndView("error.html", this.configService.getSysConfig(),
                                   this.userConfigService.getUserConfig(), 1, request,
                                   response);
            mv.addObject("op_title", "系统未开启预存款");
            mv.addObject("url", CommUtil.getURL(request) + "/buyer/index.htm");
        }

        return mv;
    }
}




