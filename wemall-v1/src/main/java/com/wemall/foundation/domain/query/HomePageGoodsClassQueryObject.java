package com.wemall.foundation.domain.query;

import org.springframework.web.servlet.ModelAndView;

import com.wemall.core.query.QueryObject;

public class HomePageGoodsClassQueryObject extends QueryObject {
    public HomePageGoodsClassQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType){
        super(currentPage, mv, orderBy, orderType);
    }

    public HomePageGoodsClassQueryObject(){
    }
}




