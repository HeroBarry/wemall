package com.wemall.foundation.domain.query;

import org.springframework.web.servlet.ModelAndView;

import com.wemall.core.query.QueryObject;

public class FloorGoodsClassQueryObject extends QueryObject {
    public FloorGoodsClassQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType){
        super(currentPage, mv, orderBy, orderType);
    }

    public FloorGoodsClassQueryObject(){
    }
}




