package com.wemall.manage.seller.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.IdEntity;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.foundation.domain.Accessory;
import com.wemall.foundation.domain.GoodsBrand;
import com.wemall.foundation.domain.query.GoodsBrandQueryObject;
import com.wemall.foundation.service.IAccessoryService;
import com.wemall.foundation.service.IGoodsBrandService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 卖家品牌控制器
 */
@Controller
public class GoodsBrandSellerAction extends IdEntity {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IGoodsBrandService goodsBrandService;

    @Autowired
    private IAccessoryService accessoryService;

    @SecurityMapping(display = false, rsequence = 0, title = "卖家品牌列表", value = "/seller/usergoodsbrand_list.htm*", rtype = "seller", rname = "品牌申请", rcode = "usergoodsbrand_seller", rgroup = "商品管理")
    @RequestMapping({"/seller/usergoodsbrand_list.htm"})
    public ModelAndView usergoodsbrand_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType){
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/usergoodsbrand_list.html",
            this.configService.getSysConfig(), this.userConfigService
            .getUserConfig(), 0, request, response);
        GoodsBrandQueryObject qo = new GoodsBrandQueryObject(currentPage, mv,
                orderBy, orderType);
        qo.addQuery("obj.user.id",
                    new SysMap("uid",
                               SecurityUserHolder.getCurrentUser().getId()), "=");
        qo.setOrderBy("addTime");
        qo.setOrderType("desc");
        IPageList pList = this.goodsBrandService.list(qo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "卖家品牌申请", value = "/seller/usergoodsbrand_add.htm*", rtype = "seller", rname = "品牌申请", rcode = "usergoodsbrand_seller", rgroup = "商品管理")
    @RequestMapping({"/seller/usergoodsbrand_add.htm"})
    public ModelAndView usergoodsbrand_add(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/usergoodsbrand_add.html",
            this.configService.getSysConfig(), this.userConfigService
            .getUserConfig(), 0, request, response);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "卖家品牌编辑", value = "/seller/usergoodsbrand_edit.htm*", rtype = "seller", rname = "品牌申请", rcode = "usergoodsbrand_seller", rgroup = "商品管理")
    @RequestMapping({"/seller/usergoodsbrand_edit.htm"})
    public ModelAndView usergoodsbrand_edit(HttpServletRequest request, HttpServletResponse response, String id){
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/usergoodsbrand_add.html",
            this.configService.getSysConfig(), this.userConfigService
            .getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))){
            GoodsBrand goodsBrand = this.goodsBrandService.getObjById(
                                        Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", goodsBrand);
        }
        mv.addObject("edit", Boolean.valueOf(true));

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "卖家品牌删除", value = "/seller/usergoodsbrand_dele.htm*", rtype = "seller", rname = "品牌申请", rcode = "usergoodsbrand_seller", rgroup = "商品管理")
    @RequestMapping({"/seller/usergoodsbrand_dele.htm"})
    public String usergoodsbrand_dele(HttpServletRequest request, String id, String currentPage){
        if (!id.equals("")){
            GoodsBrand brand = this.goodsBrandService.getObjById(
                                   Long.valueOf(Long.parseLong(id)));
            if (brand.getAudit() != 1){
                CommUtil.del_acc(request, brand.getBrandLogo());
                this.goodsBrandService.delete(Long.valueOf(Long.parseLong(id)));
            }
        }

        return "redirect:usergoodsbrand_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "卖家品牌保存", value = "/seller/usergoodsbrand_save.htm*", rtype = "seller", rname = "品牌申请", rcode = "usergoodsbrand_seller", rgroup = "商品管理")
    @RequestMapping({"/seller/usergoodsbrand_save.htm"})
    public String usergoodsbrand_save(HttpServletRequest request, HttpServletResponse response, String id, String cmd, String cat_name, String list_url, String add_url){
        WebForm wf = new WebForm();
        GoodsBrand goodsBrand = null;
        if (id.equals("")){
            goodsBrand = (GoodsBrand)wf.toPo(request, GoodsBrand.class);
            goodsBrand.setAddTime(new Date());
            goodsBrand.setAudit(0);
            goodsBrand.setUserStatus(1);
            goodsBrand.setUser(SecurityUserHolder.getCurrentUser());
        }else{
            GoodsBrand obj = this.goodsBrandService.getObjById(
                                 Long.valueOf(Long.parseLong(id)));
            goodsBrand = (GoodsBrand)wf.toPo(request, obj);
        }

        String uploadFilePath = this.configService.getSysConfig()
                                .getUploadFilePath();
        String saveFilePathName = request.getSession().getServletContext()
                                  .getRealPath("/") +
                                  uploadFilePath + File.separator + "brand";
        Map map = new HashMap();
        try {
            String fileName = goodsBrand.getBrandLogo() == null ? "" :
                              goodsBrand.getBrandLogo().getName();
            map = CommUtil.saveFileToServer(request, "brandLogo",
                                            saveFilePathName, fileName, null);
            if (fileName.equals("")){
                if (map.get("fileName") != ""){
                    Accessory photo = new Accessory();
                    photo.setName(CommUtil.null2String(map.get("fileName")));
                    photo.setExt(CommUtil.null2String(map.get("mime")));
                    photo.setSize(CommUtil.null2Float(map.get("fileSize")));
                    photo.setPath(uploadFilePath + "/brand");
                    photo.setWidth(CommUtil.null2Int(map.get("width")));
                    photo.setHeight(CommUtil.null2Int(map.get("height")));
                    photo.setAddTime(new Date());
                    this.accessoryService.save(photo);
                    goodsBrand.setBrandLogo(photo);
                }
            }else if (map.get("fileName") != ""){
                Accessory photo = goodsBrand.getBrandLogo();
                photo.setName(CommUtil.null2String(map.get("fileName")));
                photo.setExt(CommUtil.null2String(map.get("mime")));
                photo.setSize(CommUtil.null2Float(map.get("fileSize")));
                photo.setPath(uploadFilePath + "/brand");
                photo.setWidth(CommUtil.null2Int(map.get("width")));
                photo.setHeight(CommUtil.null2Int(map.get("height")));
                photo.setAddTime(new Date());
                this.accessoryService.update(photo);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        if (id.equals(""))
            this.goodsBrandService.save(goodsBrand);
        else
            this.goodsBrandService.update(goodsBrand);

        return "redirect:usergoodsbrand_list.htm";
    }
}




