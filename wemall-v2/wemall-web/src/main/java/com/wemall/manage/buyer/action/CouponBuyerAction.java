package com.wemall.manage.buyer.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.query.CouponInfoQueryObject;
import com.wemall.foundation.service.ICouponInfoService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 买家优惠券控制器
 */
@Controller
public class CouponBuyerAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private ICouponInfoService couponInfoService;

    @SecurityMapping(display = false, rsequence = 0, title = "买家优惠券列表", value = "/buyer/coupon.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping({"/buyer/coupon.htm"})
    public ModelAndView coupon(HttpServletRequest request, HttpServletResponse response, String reply, String currentPage){
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/buyer_coupon.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        CouponInfoQueryObject qo = new CouponInfoQueryObject(currentPage, mv,
                "addTime", "desc");
        qo.addQuery("obj.user.id",
                    new SysMap("user_id",
                               SecurityUserHolder.getCurrentUser().getId()), "=");
        IPageList pList = this.couponInfoService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);

        return mv;
    }
}




