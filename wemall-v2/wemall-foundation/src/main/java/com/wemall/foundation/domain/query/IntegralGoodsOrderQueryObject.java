package com.wemall.foundation.domain.query;

import com.wemall.core.query.QueryObject;
import org.springframework.web.servlet.ModelAndView;

public class IntegralGoodsOrderQueryObject extends QueryObject {
    public IntegralGoodsOrderQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType){
        super(currentPage, mv, orderBy, orderType);
    }

    public IntegralGoodsOrderQueryObject(){
    }
}




