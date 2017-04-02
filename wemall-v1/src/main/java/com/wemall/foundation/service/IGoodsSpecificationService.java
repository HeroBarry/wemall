package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.GoodsSpecification;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IGoodsSpecificationService {
    public abstract boolean save(GoodsSpecification paramGoodsSpecification);

    public abstract GoodsSpecification getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(GoodsSpecification paramGoodsSpecification);

    public abstract List<GoodsSpecification> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}




