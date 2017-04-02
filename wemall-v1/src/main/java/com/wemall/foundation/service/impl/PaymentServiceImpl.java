package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Payment;
import com.wemall.foundation.service.IPaymentService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentServiceImpl
    implements IPaymentService {
    @Resource(name = "paymentDAO")
    private IGenericDAO<Payment> paymentDao;

    public boolean save(Payment payment){
        try {
            this.paymentDao.save(payment);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public Payment getObjById(Long id){
        Payment payment = (Payment)this.paymentDao.get(id);
        if (payment != null){
            return payment;
        }

        return null;
    }

    public boolean delete(Long id){
        try {
            this.paymentDao.remove(id);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean batchDelete(List<Serializable> paymentIds){
        for (Serializable id : paymentIds){
            delete((Long)id);
        }

        return true;
    }

    public IPageList list(IQueryObject properties){
        if (properties == null){
            return null;
        }
        String query = properties.getQuery();
        Map params = properties.getParameters();
        GenericPageList pList = new GenericPageList(Payment.class, query,
                params, this.paymentDao);
        if (properties != null){
            PageObject pageObj = properties.getPageObj();
            if (pageObj != null)
                pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj
                             .getCurrentPage().intValue(), pageObj.getPageSize() == null ? 0 :
                             pageObj.getPageSize().intValue());
        }else{
            pList.doList(0, -1);
        }

        return pList;
    }

    public boolean update(Payment payment){
        try {
            this.paymentDao.update(payment);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public List<Payment> query(String query, Map params, int begin, int max){
        return this.paymentDao.query(query, params, begin, max);
    }

    public Payment getObjByProperty(String propertyName, String value){
        return (Payment)this.paymentDao.getBy(propertyName, value);
    }
}




