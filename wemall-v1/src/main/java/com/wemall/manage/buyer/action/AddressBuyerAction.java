package com.wemall.manage.buyer.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.core.tools.database.DatabaseTools;
import com.wemall.foundation.domain.Address;
import com.wemall.foundation.domain.Area;
import com.wemall.foundation.domain.query.AddressQueryObject;
import com.wemall.foundation.service.IAddressService;
import com.wemall.foundation.service.IAreaService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 买家收货地址控制器
 */
@Controller
public class AddressBuyerAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private DatabaseTools databaseTools;

    @SecurityMapping(display = false, rsequence = 0, title = "收货地址列表", value = "/buyer/address.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping({"/buyer/address.htm"})
    public ModelAndView address(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType){
        ModelAndView mv = new JModelAndView("user/default/usercenter/address.html", this.configService.getSysConfig(),
                                            this.userConfigService.getUserConfig(), 0, request, response);
        String wemall_view_type = CommUtil.null2String(request.getSession(false).getAttribute("wemall_view_type"));
        if((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))){
            mv = new JModelAndView("wap/address.html", this.configService.getSysConfig(),
                                   this.userConfigService.getUserConfig(), 1, request, response);
        }
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))){
            url = CommUtil.getURL(request);
        }
        String params = "";
        AddressQueryObject qo = new AddressQueryObject(currentPage, mv, orderBy, orderType);
        qo.addQuery("obj.user.id", new SysMap("user_id", SecurityUserHolder.getCurrentUser().getId()), "=");
        IPageList pList = this.addressService.list(qo);
        CommUtil.saveIPageList2ModelAndView(url + "/buyer/address.htm", "", params, pList, mv);
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        mv.addObject("areas", areas);

        return mv;
    }
    /**
     * 新增收货地址页面
     * @param request
     * @param response
     * @param currentPage
     * @return
     */
    @SecurityMapping(display = false, rsequence = 0, title = "新增收货地址", value = "/buyer/address_add.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping({"/buyer/address_add.htm"})
    public ModelAndView address_add(HttpServletRequest request, HttpServletResponse response, String currentPage){
        ModelAndView mv = new JModelAndView("user/default/usercenter/address_add.html", this.configService.getSysConfig(),
                                            this.userConfigService.getUserConfig(), 0, request, response);
        String wemall_view_type = CommUtil.null2String(request.getSession().getAttribute("wemall_view_type"));
        if((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))){
            mv = new JModelAndView("wap/address_add.html", this.configService.getSysConfig(),
                                   this.userConfigService.getUserConfig(), 1, request, response);
        }
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        mv.addObject("areas", areas);
        mv.addObject("currentPage", currentPage);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "新增收货地址", value = "/buyer/address_edit.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping({"/buyer/address_edit.htm"})
    public ModelAndView address_edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage){
        ModelAndView mv = new JModelAndView("user/default/usercenter/address_add.html", this.configService.getSysConfig(),
                                            this.userConfigService.getUserConfig(), 0, request, response);
        String wemall_view_type = CommUtil.null2String(request.getSession().getAttribute("wemall_view_type"));
        if((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))){
            mv = new JModelAndView("wap/address_add.html", this.configService.getSysConfig(),
                                   this.userConfigService.getUserConfig(), 1, request, response);
        }
        List areas = this.areaService.query("select obj from Area obj where obj.parent.id is null", null, -1, -1);
        Address obj = this.addressService.getObjById(CommUtil.null2Long(id));
        mv.addObject("obj", obj);
        mv.addObject("areas", areas);
        mv.addObject("currentPage", currentPage);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "收货地址保存", value = "/buyer/address_save.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping({"/buyer/address_save.htm"})
    public String address_save(HttpServletRequest request, HttpServletResponse response, String id, String area_id, String currentPage){
        WebForm wf = new WebForm();
        Address address = null;
        if (id.equals("")){
            address = (Address)wf.toPo(request, Address.class);
            address.setAddTime(new Date());
        }else{
            Address obj = this.addressService.getObjById(Long.valueOf(Long.parseLong(id)));
            address = (Address)wf.toPo(request, obj);
        }
        address.setUser(SecurityUserHolder.getCurrentUser());
        Area area = this.areaService.getObjById(CommUtil.null2Long(area_id));
        address.setArea(area);
        if (id.equals(""))
            this.addressService.save(address);
        else
            this.addressService.update(address);

        return "redirect:address.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "收货地址删除", value = "/buyer/address_del.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping({"/buyer/address_del.htm"})
    public String address_del(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage){
        String[] ids = mulitId.split(",");
        for (String id : ids){
            if (!id.equals("")){
                Address address = this.addressService.getObjById(
                                      Long.valueOf(Long.parseLong(id)));
                this.addressService.delete(Long.valueOf(Long.parseLong(id)));
            }
        }

        return "redirect:address.htm?currentPage=" + currentPage;
    }
}




