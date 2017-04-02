package com.wemall.manage.seller.Tools;

import org.nutz.json.Json;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * �˵��������
 */
@Component
public class MenuTools {
    public List<Map> generic_seller_quick_menu(String menu_json){
        List list = new ArrayList();
        if ((menu_json != null) && (!menu_json.equals(""))){
            list = (List)Json.fromJson(List.class, menu_json);
        }

        return list;
    }
}




