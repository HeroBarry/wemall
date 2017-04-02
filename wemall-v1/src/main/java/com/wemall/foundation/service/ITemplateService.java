package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Template;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface ITemplateService {
    public abstract boolean save(Template paramTemplate);

    public abstract Template getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(Template paramTemplate);

    public abstract List<Template> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract Template getObjByProperty(String paramString, Object paramObject);
}




