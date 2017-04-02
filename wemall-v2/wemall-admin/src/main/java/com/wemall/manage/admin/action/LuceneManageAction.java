package com.wemall.manage.admin.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.Goods;
import com.wemall.foundation.domain.Store;
import com.wemall.foundation.domain.SysConfig;
import com.wemall.foundation.service.*;
import com.wemall.lucene.LuceneThread;
import com.wemall.lucene.LuceneVo;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * lucene全文检索管理控制器
 */
@Controller
public class LuceneManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IStoreService storeService;

    @Autowired
    private IArticleService articleService;

    @SecurityMapping(display = false, rsequence = 0, title = "全文检索设置", value = "/admin/lucene.htm*", rtype = "admin", rname = "全文检索", rcode = "lucene_manage", rgroup = "工具")
    @RequestMapping({ "/admin/lucene.htm" })
    public ModelAndView lucene(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new JModelAndView("admin/blue/lucene.html", this.configService.getSysConfig(), this.userConfigService.getUserConfig(), 0, request, response);
        String path = (new StringBuilder(String.valueOf(System.getProperty("wemall.root")))).append("lucene").toString();
        File file = new File(path);
        if(!file.exists()){
            CommUtil.createFolder(path);
        }
        mv.addObject("lucene_disk_size", Double.valueOf(CommUtil.fileSize(file)));
        mv.addObject("lucene_disk_path", path);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "全文检索关键字保存", value = "/admin/lucene_hot_save.htm*", rtype = "admin", rname = "全文检索", rcode = "lucene_manage", rgroup = "工具")
    @RequestMapping({ "/admin/lucene_hot_save.htm" })
    public void lucene_hot_save(HttpServletRequest request, HttpServletResponse response, String id, String hotSearch){
        SysConfig obj = this.configService.getSysConfig();
        boolean ret = true;
        if(id.equals("")){
            obj.setHotSearch(hotSearch);
            obj.setAddTime(new Date());
            ret = this.configService.save(obj);
        }else{
            obj.setHotSearch(hotSearch);
            ret = this.configService.update(obj);
        }
        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "no-cache");
        try {request.setCharacterEncoding("UTF-8");} catch (java.io.UnsupportedEncodingException e1) {e1.printStackTrace();}
        try {
            PrintWriter writer = response.getWriter();
            writer.print(ret);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @SecurityMapping(display = false, rsequence = 0, title = "全文检索更新", value = "/admin/lucene_update.htm*", rtype = "admin", rname = "全文检索", rcode = "lucene_manage", rgroup = "工具")
    @RequestMapping({ "/admin/lucene_update.htm" })
    public void lucene_update(HttpServletRequest request, HttpServletResponse response, String id, String hotSearch){
        Map params = new HashMap();
        params.put("goods_status", Integer.valueOf(0));
        List<Goods> goods_list = this.goodsService.query("select obj from Goods obj where obj.goods_status=:goods_status", params, -1, -1);
        params.clear();
        params.put("store_status", Integer.valueOf(2));
        List<Store> store_list = this.storeService.query("select obj from Store obj where obj.store_status=:store_status", params, -1, -1);
        params.clear();
        params.put("display", Boolean.valueOf(true));
        List article_list = this.articleService.query("select obj from Article obj where obj.display=:display", params, -1, -1);
        String goods_lucene_path = (new StringBuilder(String.valueOf(System.getProperty("wemall.root")))).append(File.separator).append("lucene").append(File.separator).append("goods").toString();
        String store_lucene_path = (new StringBuilder(String.valueOf(System.getProperty("wemall.root")))).append(File.separator).append("lucene").append(File.separator).append("store").toString();
        File file = new File(goods_lucene_path);
        if(!file.exists()){
            CommUtil.createFolder(goods_lucene_path);
        }
        file = new File(store_lucene_path);
        if(!file.exists()){
            CommUtil.createFolder(store_lucene_path);
        }
        List<LuceneVo> goods_vo_list = new ArrayList();
        LuceneVo vo;
        for(Goods goods : goods_list){
            vo = new LuceneVo();
            vo.setVo_id(goods.getId());
            vo.setVo_title(goods.getGoods_name());
            vo.setVo_content(goods.getGoods_details());
            vo.setVo_type("goods");
            vo.setVo_store_price(CommUtil.null2Double(goods.getStore_price()));
            vo.setVo_add_time(goods.getAddTime().getTime());
            vo.setVo_goods_salenum(goods.getGoods_salenum());
            goods_vo_list.add(vo);
        }
        List<LuceneVo> store_vo_list = new ArrayList();
        for(Store store : store_list){
            LuceneVo vo1 = new LuceneVo();
            vo1.setVo_id(store.getId());
            vo1.setVo_title(store.getStore_name());
            vo1.setVo_content(store.getStore_address() + store.getStore_seo_description() + store.getStore_seo_keywords() + store.getStore_info());
            store_vo_list.add(vo1);
        }
        LuceneThread goods_thread = new LuceneThread(goods_lucene_path, goods_vo_list);
        LuceneThread store_thread = new LuceneThread(store_lucene_path, goods_vo_list);
        Date d1 = new Date();
        goods_thread.run();
        store_thread.run();
        Date d2 = new Date();
        String path = (new StringBuilder(String.valueOf(System.getProperty("wemall.root")))).append("lucene").toString();
        Map map = new HashMap();
        map.put("run_time", Long.valueOf(d2.getTime() - d1.getTime()));
        map.put("file_size", Double.valueOf(CommUtil.fileSize(new File(path))));
        map.put("update_time", CommUtil.formatLongDate(new Date()));
        SysConfig config = this.configService.getSysConfig();
        config.setLucene_update(new Date());
        this.configService.update(config);
        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "no-cache");
        try {request.setCharacterEncoding("UTF-8");} catch (java.io.UnsupportedEncodingException e1) {e1.printStackTrace();}
        try {
            PrintWriter writer = response.getWriter();
            writer.print(Json.toJson(map, JsonFormat.compact()));
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
