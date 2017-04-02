package com.wemall.web.action;

import com.wemall.foundation.domain.Area;
import com.wemall.foundation.service.IAreaService;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �ջ����������
 */
@Controller
public class LoadAction {
    @Autowired
    private IAreaService areaService;

    @RequestMapping({"/load_area.htm"})
    public void load_area(HttpServletRequest request, HttpServletResponse response, String pid){
        Map params = new HashMap();
        params.put("pid", Long.valueOf(Long.parseLong(pid)));
        List<Area> areas = this.areaService.query(
                               "select obj from Area obj where obj.parent.id=:pid", params,
                               -1, -1);
        List list = new ArrayList();
        for (Area area : areas){
            Map map = new HashMap();
            map.put("id", area.getId());
            map.put("areaName", area.getAreaName());
            list.add(map);
        }
        String temp = Json.toJson(list, JsonFormat.compact());
        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "no-cache");
        try {request.setCharacterEncoding("UTF-8");} catch (java.io.UnsupportedEncodingException e1) {e1.printStackTrace();}
        try {
            PrintWriter writer = response.getWriter();
            writer.print(temp);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}




