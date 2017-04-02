package com.wemall.manage.admin.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.foundation.domain.GroupPriceRange;
import com.wemall.foundation.domain.query.GroupPriceRangeQueryObject;
import com.wemall.foundation.service.IGroupPriceRangeService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 团购价格区间管理控制器
 */
@Controller
public class GroupPriceRangeManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IGroupPriceRangeService grouppricerangeService;

    @SecurityMapping(display = false, rsequence = 0, title = "团购价格区间列表", value = "/admin/group_price_list.htm*", rtype = "admin", rname = "团购管理", rcode = "group_admin", rgroup = "运营")
    @RequestMapping({"/admin/group_price_list.htm"})
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType){
        ModelAndView mv = new JModelAndView("admin/blue/group_price_list.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))){
            url = CommUtil.getURL(request);
        }
        String params = "";
        GroupPriceRangeQueryObject qo = new GroupPriceRangeQueryObject(
            currentPage, mv, orderBy, orderType);

        IPageList pList = this.grouppricerangeService.list(qo);
        CommUtil.saveIPageList2ModelAndView(
            url + "/admin/group_range_list.htm", "", params, pList, mv);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "团购价格区间列表", value = "/admin/group_price_add.htm*", rtype = "admin", rname = "团购管理", rcode = "group_admin", rgroup = "运营")
    @RequestMapping({"/admin/group_price_add.htm"})
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, String currentPage){
        ModelAndView mv = new JModelAndView("admin/blue/group_price_add.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        mv.addObject("currentPage", currentPage);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "团购价格区间列表", value = "/admin/group_price_edit.htm*", rtype = "admin", rname = "团购管理", rcode = "group_admin", rgroup = "运营")
    @RequestMapping({"/admin/group_price_edit.htm"})
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage){
        ModelAndView mv = new JModelAndView("admin/blue/group_price_add.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))){
            GroupPriceRange grouppricerange = this.grouppricerangeService
                                              .getObjById(Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", grouppricerange);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "团购价格区间保存", value = "/admin/group_price_save.htm*", rtype = "admin", rname = "团购管理", rcode = "group_admin", rgroup = "运营")
    @RequestMapping({"/admin/group_price_save.htm"})
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String cmd){
        WebForm wf = new WebForm();
        GroupPriceRange grouppricerange = null;
        if (id.equals("")){
            grouppricerange = (GroupPriceRange)wf.toPo(request, GroupPriceRange.class);
            grouppricerange.setAddTime(new Date());
        }else{
            GroupPriceRange obj = this.grouppricerangeService.getObjById(
                                      Long.valueOf(Long.parseLong(id)));
            grouppricerange = (GroupPriceRange)wf.toPo(request, obj);
        }

        if (id.equals(""))
            this.grouppricerangeService.save(grouppricerange);
        else
            this.grouppricerangeService.update(grouppricerange);
        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        mv.addObject("list_url", CommUtil.getURL(request) +
                     "/admin/group_price_list.htm");
        mv.addObject("op_title", "保存价格区间成功");
        mv.addObject("add_url", CommUtil.getURL(request) +
                     "/admin/group_price_add.htm" + "?currentPage=" + currentPage);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "团购价格区间删除", value = "/admin/group_price_del.htm*", rtype = "admin", rname = "团购管理", rcode = "group_admin", rgroup = "运营")
    @RequestMapping({"/admin/group_price_del.htm"})
    public String delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage){
        String[] ids = mulitId.split(",");
        for (String id : ids){
            if (!id.equals("")){
                GroupPriceRange grouppricerange = this.grouppricerangeService
                                                  .getObjById(Long.valueOf(Long.parseLong(id)));
                this.grouppricerangeService.delete(Long.valueOf(Long.parseLong(id)));
            }
        }

        return "redirect:group_price_list.htm?currentPage=" + currentPage;
    }
}




