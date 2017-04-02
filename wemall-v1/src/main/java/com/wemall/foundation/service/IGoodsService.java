package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Goods;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IGoodsService {
    public abstract boolean save(Goods paramGoods);

    public abstract Goods getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(Goods paramGoods);

    public abstract List<Goods> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract Goods getObjByProperty(String paramString, Object paramObject);
}




