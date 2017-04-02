package com.wemall.web.tools;

import com.wemall.foundation.domain.Navigation;
import com.wemall.foundation.service.IActivityService;
import com.wemall.foundation.service.IArticleService;
import com.wemall.foundation.service.IGoodsClassService;
import com.wemall.foundation.service.INavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导航工具组件
 */
@Component
public class NavViewTools {
    @Autowired
    private INavigationService navService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private IGoodsClassService goodsClassService;

    public List<Navigation> queryNav(int location, int count){
        List navs = new ArrayList();
        Map params = new HashMap();
        params.put("display", Boolean.valueOf(true));
        params.put("location", Integer.valueOf(location));
        params.put("type", "sparegoods");
        navs = this.navService
               .query("select obj from Navigation obj where obj.display=:display and obj.location=:location and obj.type!=:type order by obj.sequence asc", params, 0, count);

        return navs;
    }
}




