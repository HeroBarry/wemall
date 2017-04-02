package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.SpareGoods;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface ISpareGoodsService {
    public abstract boolean save(SpareGoods paramSpareGoods);

    public abstract SpareGoods getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(SpareGoods paramSpareGoods);

    public abstract List<SpareGoods> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}




