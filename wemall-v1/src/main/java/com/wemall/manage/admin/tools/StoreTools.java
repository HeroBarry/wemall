package com.wemall.manage.admin.tools;

import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.*;
import com.wemall.foundation.service.IGoodsClassService;
import com.wemall.foundation.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

/**
 * 店铺工具组件
 */
@Component
public class StoreTools {
    @Autowired
    private IGoodsClassService goodsClassService;

    @Autowired
    private IStoreService storeService;

    public String genericProperty(GoodsSpecification spec){
        String val = "";
        for (GoodsSpecProperty gsp : spec.getProperties()){
            val = val + "," + gsp.getValue();
        }
        if (!val.equals("")){
            return val.substring(1);
        }

        return "";
    }

    public String createUserFolder(HttpServletRequest request, SysConfig config, Store store){
        String path = "";
        String uploadFilePath = config.getUploadFilePath();
        if (config.getImageSaveType().equals("sidImg")){
            path = request.getSession().getServletContext().getRealPath("/") +
                   uploadFilePath + File.separator + "store" +
                   File.separator + store.getId();
        }

        if (config.getImageSaveType().equals("sidYearImg")){
            path = request.getSession().getServletContext().getRealPath("/") +
                   uploadFilePath + File.separator + "store" +
                   File.separator + store.getId() + File.separator +
                   CommUtil.formatTime("yyyy", new Date());
        }
        if (config.getImageSaveType().equals("sidYearMonthImg")){
            path = request.getSession().getServletContext().getRealPath("/") +
                   uploadFilePath + File.separator + "store" +
                   File.separator + store.getId() + File.separator +
                   CommUtil.formatTime("yyyy", new Date()) + File.separator +
                   CommUtil.formatTime("MM", new Date());
        }
        if (config.getImageSaveType().equals("sidYearMonthDayImg")){
            path = request.getSession().getServletContext().getRealPath("/") +
                   uploadFilePath + File.separator + "store" +
                   File.separator + store.getId() + File.separator +
                   CommUtil.formatTime("yyyy", new Date()) + File.separator +
                   CommUtil.formatTime("MM", new Date()) + File.separator +
                   CommUtil.formatTime("dd", new Date());
        }
        CommUtil.createFolder(path);

        return path;
    }

    public String createUserFolderURL(SysConfig config, Store store){
        String path = "";
        String uploadFilePath = config.getUploadFilePath();
        if (config.getImageSaveType().equals("sidImg")){
            path = uploadFilePath + "/store/" + store.getId().toString();
        }

        if (config.getImageSaveType().equals("sidYearImg")){
            path = uploadFilePath + "/store/" + store.getId() + "/" +
                   CommUtil.formatTime("yyyy", new Date());
        }
        if (config.getImageSaveType().equals("sidYearMonthImg")){
            path = uploadFilePath + "/store/" + store.getId() + "/" +
                   CommUtil.formatTime("yyyy", new Date()) + "/" +
                   CommUtil.formatTime("MM", new Date());
        }
        if (config.getImageSaveType().equals("sidYearMonthDayImg")){
            path = uploadFilePath + "/store/" + store.getId() + "/" +
                   CommUtil.formatTime("yyyy", new Date()) + "/" +
                   CommUtil.formatTime("MM", new Date()) + "/" +
                   CommUtil.formatTime("dd", new Date());
        }

        return path;
    }

    public String generic_goods_class_info(GoodsClass gc){
        if (gc != null){
            String goods_class_info = generic_the_goods_class_info(gc);
            return goods_class_info.substring(0, goods_class_info.length() - 1);
        }

        return "";
    }

    private String generic_the_goods_class_info(GoodsClass gc){
        if (gc != null){
            String goods_class_info = gc.getClassName() + ">";
            if (gc.getParent() != null){
                String class_info = generic_the_goods_class_info(gc.getParent());
                goods_class_info = class_info + goods_class_info;
            }
            return goods_class_info;
        }

        return "";
    }

    public int query_store_with_user(String user_id){
        int status = 0;
        Store store = this.storeService.getObjByProperty("user.id",
                      CommUtil.null2Long(user_id));
        if (store != null)
            status = 1;

        return status;
    }
}




