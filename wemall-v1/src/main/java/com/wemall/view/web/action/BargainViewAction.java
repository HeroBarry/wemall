package com.wemall.view.web.action;

import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.query.BargainGoodsQueryObject;
import com.wemall.foundation.service.IBargainGoodsService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 今日特价控制器
 */
@Controller
public class BargainViewAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IBargainGoodsService bargainGoodsService;

    @RequestMapping({"/bargain.htm"})
    public ModelAndView bargain(HttpServletRequest request, HttpServletResponse response, String bg_time, String currentPage, String orderBy, String orderType){
        ModelAndView mv = new JModelAndView("bargain.html",
                                            this.configService.getSysConfig(),
                                            this.userConfigService.getUserConfig(), 1, request, response);
        BargainGoodsQueryObject bqo = new BargainGoodsQueryObject(currentPage,
                mv, orderBy, orderType);
        if (CommUtil.null2String(bg_time).equals(""))
            bqo.addQuery(
                "obj.bg_time",
                new SysMap("bg_time", CommUtil.formatDate(
                               CommUtil.formatShortDate(new Date()))), "=");
       else{
            bqo.addQuery("obj.bg_time",
                         new SysMap("bg_time", CommUtil.formatDate(bg_time)), "=");
        }
        bqo.addQuery("obj.bg_status", new SysMap("bg_status", Integer.valueOf(1)), "=");
        bqo.setPageSize(Integer.valueOf(20));
        IPageList pList = this.bargainGoodsService.list(bqo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);

        Map params = new HashMap();
        Calendar cal = Calendar.getInstance();
        if (CommUtil.null2String(bg_time).equals("")){
            bg_time = CommUtil.formatShortDate(new Date());
        }
        cal.setTime(CommUtil.formatDate(bg_time));
        cal.add(6, 1);
        params.put("bg_time",
                   CommUtil.formatDate(CommUtil.formatShortDate(cal.getTime())));
        params.put("bg_status", Integer.valueOf(1));
        List bgs = this.bargainGoodsService
                   .query("select obj from BargainGoods obj where obj.bg_time=:bg_time and obj.bg_status=:bg_status order by audit_time desc",
                          params, 0, 5);
        mv.addObject("bgs", bgs);
        int day_count = this.configService.getSysConfig().getBargain_validity();
        List dates = new ArrayList();
        for (int i = 0; i < day_count; i++){
            cal = Calendar.getInstance();
            cal.add(6, i);
            dates.add(cal.getTime());
        }
        mv.addObject("dates", dates);
        mv.addObject("bg_time", bg_time);

        return mv;
    }
}




