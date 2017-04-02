package com.wemall.manage.admin.tools;

import com.wemall.foundation.domain.Area;
import com.wemall.foundation.service.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 地区管理工具组件
 */
@Component
public class AreaManageTools {
    @Autowired
    private IAreaService areaService;

    public String generic_area_info(Area area){
        String area_info = "";
        if (area != null){
            area_info = area.getAreaName() + " ";
            if (area.getParent() != null){
                String info = generic_area_info(area.getParent());
                area_info = info + area_info;
            }
        }

        return area_info;
    }
}




