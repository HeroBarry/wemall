package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.MobileVerifyCode;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IMobileVerifyCodeService {
    public abstract boolean save(MobileVerifyCode paramMobileVerifyCode);

    public abstract MobileVerifyCode getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(MobileVerifyCode paramMobileVerifyCode);

    public abstract List<MobileVerifyCode> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract MobileVerifyCode getObjByProperty(String paramString, Object paramObject);
}




