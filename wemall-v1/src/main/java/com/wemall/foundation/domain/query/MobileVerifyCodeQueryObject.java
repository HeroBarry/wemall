package com.wemall.foundation.domain.query;

import org.springframework.web.servlet.ModelAndView;

import com.wemall.core.query.QueryObject;

public class MobileVerifyCodeQueryObject extends QueryObject {
    public MobileVerifyCodeQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType){
        super(currentPage, mv, orderBy, orderType);
    }

    public MobileVerifyCodeQueryObject(){
    }
}




