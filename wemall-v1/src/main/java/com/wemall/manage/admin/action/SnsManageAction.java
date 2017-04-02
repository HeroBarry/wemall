package com.wemall.manage.admin.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.Dynamic;
import com.wemall.foundation.domain.query.DynamicQueryObject;
import com.wemall.foundation.service.IDynamicService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import com.wemall.view.web.tools.StoreViewTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 会员动态管理控制器
 */
@Controller
public class SnsManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IDynamicService dynamicService;

    @Autowired
    private StoreViewTools storeViewTools;

    @SecurityMapping(display = false, rsequence = 0, title = "会员动态列表", value = "/admin/sns_user.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/sns_user.htm"})
    public ModelAndView sns_user(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String condition, String userName){
        ModelAndView mv = new JModelAndView("admin/blue/sns_user.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        DynamicQueryObject qo = new DynamicQueryObject(currentPage, mv,
                orderBy, orderType);
        qo.addQuery("obj.dissParent.id is null", null);
        if ((userName != null) && (!userName.equals(""))){
            qo.addQuery("obj.user.userName",
                        new SysMap("obj_userName", "%" +
                                   userName.trim() + "%"), "like");
            mv.addObject("userName", userName);
        }
        qo.setPageSize(Integer.valueOf(10));
        qo.setOrderBy("addTime");
        qo.setOrderType("desc");
        IPageList pList = this.dynamicService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "会员动态删除", value = "/admin/sns_del.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/sns_del.htm"})
    public String sns_user_del(HttpServletRequest request, HttpServletResponse response, String currentPage, String mulitId){
        String[] ids = mulitId.split(",");
        for (String id : ids){
            Dynamic obj = this.dynamicService
                          .getObjById(CommUtil.null2Long(id));
            this.dynamicService.delete(CommUtil.null2Long(id));
        }
        String url = "redirect:/admin/sns_user.htm?currentPage=" + currentPage;

        return url;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "店铺动态列表", value = "/admin/sns_store.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/sns_store.htm"})
    public ModelAndView sns_store(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String condition, String store_name){
        ModelAndView mv = new JModelAndView("admin/blue/sns_store.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        DynamicQueryObject qo = new DynamicQueryObject(currentPage, mv,
                orderBy, orderType);
        qo.addQuery("obj.dissParent.id is null", null);
        if ((store_name != null) && (!store_name.equals(""))){
            qo.addQuery("obj.store.store_name",
                        new SysMap("obj_store_name",
                                   "%" + store_name.trim() + "%"), "like");
            mv.addObject("store_name", store_name);
        }
        qo.addQuery("obj.store.id is not null", null);
        qo.setPageSize(Integer.valueOf(10));
        qo.setOrderBy("addTime");
        qo.setOrderType("desc");
        IPageList pList = this.dynamicService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        mv.addObject("storeViewTools", this.storeViewTools);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "店铺动态删除", value = "/admin/sns_store_del.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/sns_store_del.htm"})
    public String sns_store_del(HttpServletRequest request, HttpServletResponse response, String currentPage, String mulitId){
        String[] ids = mulitId.split(",");
        for (String id : ids){
            Dynamic obj = this.dynamicService
                          .getObjById(CommUtil.null2Long(id));
            this.dynamicService.delete(CommUtil.null2Long(id));
        }
        String url = "redirect:/admin/sns_store.htm?currentPage=" + currentPage;

        return url;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "sns动态设置可见度", value = "/admin/sns_set_display.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/sns_set_display.htm"})
    public String sns_set_display(HttpServletRequest request, HttpServletResponse response, String currentPage, String mulitId, String type, String mark){
        String[] ids = mulitId.split(",");
        for (String id : ids){
            Dynamic obj = this.dynamicService
                          .getObjById(CommUtil.null2Long(id));
            if ((obj != null) && (!obj.equals(""))){
                if (type.equals("show")){
                    if (!obj.isDisplay()){
                        obj.setDisplay(true);
                    }
                }else if (obj.isDisplay()){
                    obj.setDisplay(false);
                }

                this.dynamicService.update(obj);
            }
        }
        String url = "redirect:/admin/sns_" + mark + ".htm?currentPage=" +
                     currentPage;

        return url;
    }
}




