package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.TransArea;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface ITransAreaService {
    public abstract boolean save(TransArea paramTransArea);

    public abstract TransArea getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(TransArea paramTransArea);

    public abstract List<TransArea> query(String paramString, Map paramMap, int paramInt1, int paramInt2);
}




