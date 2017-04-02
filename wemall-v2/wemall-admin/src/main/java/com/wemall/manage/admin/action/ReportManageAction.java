package com.wemall.manage.admin.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.Goods;
import com.wemall.foundation.domain.Report;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.domain.query.ReportQueryObject;
import com.wemall.foundation.service.*;
import com.wemall.lucene.LuceneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

/**
 * 举报管理控制器
 */
@Controller
public class ReportManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IReportService reportService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IUserService userService;

    @SecurityMapping(display = false, rsequence = 0, title = "未处理举报列表", value = "/admin/report_list.htm*", rtype = "admin", rname = "举报管理", rcode = "report_manage", rgroup = "交易")
    @RequestMapping({"/admin/report_list.htm"})
    public ModelAndView report_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String goods_name, String userName){
        ModelAndView mv = new JModelAndView("admin/blue/report_list.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))){
            url = CommUtil.getURL(request);
        }
        String params = "";
        ReportQueryObject qo = new ReportQueryObject(currentPage, mv, orderBy,
                orderType);
        if (!CommUtil.null2String(goods_name).equals("")){
            qo.addQuery("obj.goods.goods_name",
                        new SysMap("goods_name", "%" +
                                   goods_name + "%"), "like");
            mv.addObject("goods_name", goods_name);
        }
        if (!CommUtil.null2String(userName).equals("")){
            qo.addQuery("obj.user.userName", new SysMap("userName", userName),
                        "=");
            mv.addObject("userName", userName);
        }
        qo.addQuery("obj.status", new SysMap("status", Integer.valueOf(0)), "=");
        IPageList pList = this.reportService.list(qo);
        CommUtil.saveIPageList2ModelAndView(url + "/admin/report_list.htm", "",
                                            params, pList, mv);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "已处理举报列表", value = "/admin/report_handle_list.htm*", rtype = "admin", rname = "举报管理", rcode = "report_manage", rgroup = "交易")
    @RequestMapping({"/admin/report_handle_list.htm"})
    public ModelAndView report_handle_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String goods_name, String userName){
        ModelAndView mv = new JModelAndView(
            "admin/blue/report_handle_list.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))){
            url = CommUtil.getURL(request);
        }
        String params = "";
        ReportQueryObject qo = new ReportQueryObject(currentPage, mv, orderBy,
                orderType);
        if (!CommUtil.null2String(goods_name).equals("")){
            qo.addQuery("obj.goods.goods_name",
                        new SysMap("goods_name", "%" +
                                   goods_name + "%"), "like");
            mv.addObject("goods_name", goods_name);
        }
        if (!CommUtil.null2String(userName).equals("")){
            qo.addQuery("obj.user.userName", new SysMap("userName", userName),
                        "=");
            mv.addObject("userName", userName);
        }
        qo.addQuery("obj.status", new SysMap("status", Integer.valueOf(1)), "=");
        IPageList pList = this.reportService.list(qo);
        CommUtil.saveIPageList2ModelAndView(url + "/admin/report_list.htm", "",
                                            params, pList, mv);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "举报处理", value = "/admin/report_handle.htm*", rtype = "admin", rname = "举报管理", rcode = "report_manage", rgroup = "交易")
    @RequestMapping({"/admin/report_handle.htm"})
    public ModelAndView report_handle(HttpServletRequest request, HttpServletResponse response, String id, String currentPage){
        ModelAndView mv = new JModelAndView("admin/blue/report_handle.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        Report obj = this.reportService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        mv.addObject("currentPage", currentPage);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "举报处理", value = "/admin/report_handle_save.htm*", rtype = "admin", rname = "举报管理", rcode = "report_manage", rgroup = "交易")
    @RequestMapping({"/admin/report_handle_save.htm"})
    public ModelAndView report_handle_save(HttpServletRequest request, HttpServletResponse response, String id, int result, String handle_info, String currentPage){
        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        Report obj = this.reportService.getObjById(CommUtil.null2Long(id));
        obj.setResult(result);
        obj.setStatus(1);
        obj.setHandle_info(handle_info);
        obj.setHandle_Time(new Date());
        this.reportService.update(obj);
        if (obj.getResult() == 1){
            Goods goods = obj.getGoods();
            goods.setGoods_status(-3);
            this.goodsService.update(goods);

            String goods_lucene_path = (new StringBuilder(String.valueOf(System.getProperty("wemall.root")))).append(File.separator).append("lucene").append(File.separator).append("goods").toString();
            File file = new File(goods_lucene_path);
            if (!file.exists()){
                CommUtil.createFolder(goods_lucene_path);
            }
            LuceneUtil lucene = LuceneUtil.instance();
            LuceneUtil.setIndex_path(goods_lucene_path);
            lucene.delete_index(CommUtil.null2String(goods.getId()));
        }
        if (obj.getResult() == -2){
            User user = obj.getUser();
            user.setReport(-1);
            this.userService.update(user);
        }
        mv.addObject("op_title", "处理举报成功");
        mv.addObject("list_url", CommUtil.getURL(request) +
                     "/admin/report_list.htm?currentPage=" + currentPage);

        return mv;
    }
}




