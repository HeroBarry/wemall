package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.StoreSlide;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IStoreSlideService {
    public abstract boolean save(StoreSlide paramStoreSlide);

    public abstract StoreSlide getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(StoreSlide paramStoreSlide);

    public abstract List<StoreSlide> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}




