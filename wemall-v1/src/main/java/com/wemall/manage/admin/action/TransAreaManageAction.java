package com.wemall.manage.admin.action;

import com.easyjf.beans.BeanUtils;
import com.easyjf.beans.BeanWrapper;
import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.foundation.domain.TransArea;
import com.wemall.foundation.domain.query.AreaQueryObject;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.ITransAreaService;
import com.wemall.foundation.service.IUserConfigService;
import org.apache.commons.lang.StringUtils;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 运费地区管理控制器
 */
@Controller
public class TransAreaManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private ITransAreaService transareaService;

    @SecurityMapping(display = false, rsequence = 0, title = "运费地区列表", value = "/admin/trans_area_list.htm*", rtype = "admin", rname = "运费区域", rcode = "admin_trans_area", rgroup = "设置")
    @RequestMapping({"/admin/trans_area_list.htm"})
    public ModelAndView trans_area_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String pid, String orderBy, String orderType){
        ModelAndView mv = new JModelAndView("admin/blue/trans_area_list.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))){
            url = CommUtil.getURL(request);
        }
        String params = "";
        AreaQueryObject qo = null;
        if ((pid == null) || (pid.equals(""))){
            qo = new AreaQueryObject(currentPage, mv, orderBy, orderType);
            qo.addQuery("obj.parent.id is null", null);
        }else{
            qo = new AreaQueryObject(currentPage, mv, orderBy, orderType);
            qo.addQuery("obj.parent.id",
                        new SysMap("pid", Long.valueOf(Long.parseLong(pid))), "=");
            params = "&pid=" + pid;
            TransArea parent = this.transareaService.getObjById(
                                   Long.valueOf(Long.parseLong(pid)));
            mv.addObject("parent", parent);
            if (parent.getLevel() == 0){
                Map map = new HashMap();
                map.put("pid", parent.getId());
                List seconds = this.transareaService
                               .query(
                                   "select obj from TransArea obj where obj.parent.id=:pid",
                                   map, -1, -1);
                mv.addObject("seconds", seconds);
                mv.addObject("first", parent);
            }
            if (parent.getLevel() == 1){
                Map map = new HashMap();
                map.put("pid", parent.getId());
                List thirds = this.transareaService
                              .query(
                                  "select obj from TransArea obj where obj.parent.id=:pid",
                                  map, -1, -1);
                map.clear();
                map.put("pid", parent.getParent().getId());
                List seconds = this.transareaService
                               .query(
                                   "select obj from TransArea obj where obj.parent.id=:pid",
                                   map, -1, -1);
                mv.addObject("thirds", thirds);
                mv.addObject("seconds", seconds);
                mv.addObject("second", parent);
                mv.addObject("first", parent.getParent());
            }
            if (parent.getLevel() == 2){
                Map map = new HashMap();
                map.put("pid", parent.getParent().getId());
                List thirds = this.transareaService
                              .query(
                                  "select obj from TransArea obj where obj.parent.id=:pid",
                                  map, -1, -1);
                map.clear();
                map.put("pid", parent.getParent().getParent().getId());
                List seconds = this.transareaService
                               .query(
                                   "select obj from TransArea obj where obj.parent.id=:pid",
                                   map, -1, -1);
                mv.addObject("thirds", thirds);
                mv.addObject("seconds", seconds);
                mv.addObject("third", parent);
                mv.addObject("second", parent.getParent());
                mv.addObject("first", parent.getParent().getParent());
            }
        }
        WebForm wf = new WebForm();
        wf.toQueryPo(request, qo, TransArea.class, mv);
        IPageList pList = this.transareaService.list(qo);
        CommUtil.saveIPageList2ModelAndView(url + "/admin/trans_area_list.htm", "",
                                            params, pList, mv);
        List areas = this.transareaService.query(
                         "select obj from TransArea obj where obj.parent.id is null",
                         null, -1, -1);
        mv.addObject("areas", areas);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "运费地区保存", value = "/admin/trans_area_save.htm*", rtype = "admin", rname = "运费区域", rcode = "admin_trans_area", rgroup = "设置")
    @RequestMapping({"/admin/trans_area_save.htm"})
    public ModelAndView trans_area_save(HttpServletRequest request, HttpServletResponse response, String areaId, String pid, String count, String list_url, String currentPage){
        if (areaId != null){
            String[] ids = areaId.split(",");
            int i = 1;
            for (String id : ids){
                String areaName = request.getParameter("areaName_" + i);
                TransArea area = this.transareaService.getObjById(
                                     Long.valueOf(Long.parseLong(request.getParameter("id_" + i))));
                area.setAreaName(areaName);
                area.setSequence(CommUtil.null2Int(request
                                                   .getParameter("sequence_" + i)));
                this.transareaService.update(area);
                i++;
            }

        }

        TransArea parent = null;
        if (!pid.equals(""))
            parent = this.transareaService.getObjById(Long.valueOf(Long.parseLong(pid)));
        for (int i = 1; i <= CommUtil.null2Int(count); i++){
            TransArea area = new TransArea();
            area.setAddTime(new Date());
            String areaName = request.getParameter("new_areaName_" + i);
            int sequence = CommUtil.null2Int(request
                                             .getParameter("new_sequence_" + i));
            if (parent != null){
                area.setLevel(parent.getLevel() + 1);
                area.setParent(parent);
            }
            area.setAreaName(areaName);
            area.setSequence(sequence);
            this.transareaService.save(area);
        }

        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        mv.addObject("op_title", "更新配送区域成功");
        mv.addObject("list_url", list_url + "?currentPage=" + currentPage +
                     "&pid=" + pid);

        return mv;
    }

    private Set<Long> genericIds(TransArea obj){
        Set ids = new HashSet();
        ids.add(obj.getId());
        for (TransArea child : obj.getChilds()){
            Set<Long> cids = genericIds(child);
            for (Long cid : cids){
                ids.add(cid);
            }
            ids.add(child.getId());
        }

        return ids;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "运费地区删除", value = "/admin/trans_area_del.htm*", rtype = "admin", rname = "运费区域", rcode = "admin_trans_area", rgroup = "设置")
    @RequestMapping({"/admin/trans_area_del.htm"})
    public String trans_area_del(HttpServletRequest request, String mulitId, String currentPage, String pid){
        String[] ids = mulitId.split(",");
        for (String id : ids){
            if (!id.equals("")){
                Set list = genericIds(this.transareaService
                                      .getObjById(Long.valueOf(Long.parseLong(id))));
                Map params = new HashMap();
                params.put("ids", list);
                List<TransArea> objs = this.transareaService.query(
                                           "select obj from TransArea obj where obj.id in (:ids)",
                                           params, -1, -1);
                for (TransArea obj : objs){
                    obj.setParent(null);
                    this.transareaService.delete(obj.getId());
                }
            }
        }

        return "redirect:trans_area_list.htm?pid=" + pid + "&currentPage=" +
               currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "运费地区Ajax更新", value = "/admin/trans_area_ajax.htm*", rtype = "admin", rname = "运费区域", rcode = "admin_trans_area", rgroup = "设置")
    @RequestMapping({"/admin/trans_area_ajax.htm"})
    public void trans_area_ajax(HttpServletRequest request, HttpServletResponse response, String id, String fieldName, String value) throws ClassNotFoundException {
        TransArea obj = this.transareaService.getObjById(Long.valueOf(Long.parseLong(id)));
        Field[] fields = TransArea.class.getDeclaredFields();
        BeanWrapper wrapper = new BeanWrapper(obj);
        Object val = null;
        for (Field field : fields){
            if (field.getName().equals(fieldName)){
                Class clz = Class.forName("java.lang.String");
                if (field.getType().getName().equals("int")){
                    clz = Class.forName("java.lang.Integer");
                }
                if (field.getType().getName().equals("boolean")){
                    clz = Class.forName("java.lang.Boolean");
                }
                if (!value.equals(""))
                    val = BeanUtils.convertType(value, clz);
               else{
                    val = Boolean.valueOf(
                              !CommUtil.null2Boolean(wrapper
                                                     .getPropertyValue(fieldName)));
                }
                wrapper.setPropertyValue(fieldName, val);
            }
        }
        this.transareaService.update(obj);
        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "no-cache");
        try {request.setCharacterEncoding("UTF-8");} catch (java.io.UnsupportedEncodingException e1) {e1.printStackTrace();}
        try {
            PrintWriter writer = response.getWriter();
            writer.print(val.toString());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * ajax根据parentId查询下属配送区域
     * @param parentId
     */
    @RequestMapping(value = "/get_trans_area.htm", method = RequestMethod.POST)
    public void get_trans_area(HttpServletRequest request, HttpServletResponse response, String parentId){
        if(StringUtils.isEmpty(parentId)){
            parentId = "100001,100002,100003,100004,100005,100006,100007,100008,100009";
        }
        String[] parentIds = parentId.split(",");
        List list = new ArrayList();
        int i = 0;
        while (i < parentIds.length){
            TransArea transArea = this.transareaService.getObjById(Long.valueOf(Long.parseLong(parentIds[i])));
            for (TransArea area : transArea.getChilds()){
                Map map = new HashMap();
                map.put("id", area.getId());
                map.put("areaName", area.getAreaName());
                map.put("level", area.getLevel());
                list.add(map);
            }
            i++;
        }

        String result = Json.toJson(list, JsonFormat.compact());

        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache");
        try {request.setCharacterEncoding("UTF-8");} catch (java.io.UnsupportedEncodingException e1) {e1.printStackTrace();}
        try {
            PrintWriter writer = response.getWriter();
            writer.print(result);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}




