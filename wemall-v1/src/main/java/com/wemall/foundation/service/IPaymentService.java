package com.wemall.foundation.service;

import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Payment;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface IPaymentService {
    public abstract boolean save(Payment paramPayment);

    public abstract Payment getObjById(Long paramLong);

    public abstract boolean delete(Long paramLong);

    public abstract boolean batchDelete(List<Serializable> paramList);

    public abstract IPageList list(IQueryObject paramIQueryObject);

    public abstract boolean update(Payment paramPayment);

    public abstract List<Payment> query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    public abstract Payment getObjByProperty(String paramString1, String paramString2);
}




