package com.wemall.view.web.tools;

import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.GroupGoods;
import com.wemall.foundation.service.IGroupGoodsService;
import com.wemall.foundation.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 团购工具组件
 */
@Component
public class GroupViewTools {
    @Autowired
    private IGroupService groupService;

    @Autowired
    private IGroupGoodsService groupGoodsService;

    public List<GroupGoods> query_goods(String group_id, int count){
        List list = new ArrayList();
        Map params = new HashMap();
        params.put("group_id", CommUtil.null2Long(group_id));
        list = this.groupGoodsService
               .query(
                   "select obj from GroupGoods obj where obj.group.id=:group_id order by obj.addTime desc",
                   params, 0, count);

        return list;
    }
}




