package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Advert;
import com.wemall.foundation.service.IAdvertService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdvertServiceImpl
    implements IAdvertService {
    @Resource(name = "advertDAO")
    private IGenericDAO<Advert> advertDao;

    public boolean save(Advert advert){
        try {
            this.advertDao.save(advert);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public Advert getObjById(Long id){
        Advert advert = (Advert)this.advertDao.get(id);
        if (advert != null){
            return advert;
        }

        return null;
    }

    public boolean delete(Long id){
        try {
            this.advertDao.remove(id);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean batchDelete(List<Serializable> advertIds){
        for (Serializable id : advertIds){
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
        GenericPageList pList = new GenericPageList(Advert.class, query,
                params, this.advertDao);
        if (properties != null){
            PageObject pageObj = properties.getPageObj();
            if (pageObj != null) {
                pList.doList(pageObj.getCurrentPage() == null ? 0 : pageObj
                        .getCurrentPage().intValue(), pageObj.getPageSize() == null ? 0 :
                        pageObj.getPageSize().intValue());
            }
        }else{
            pList.doList(0, -1);
        }

        return pList;
    }

    public boolean update(Advert advert){
        try {
            this.advertDao.update(advert);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public List<Advert> query(String query, Map params, int begin, int max){
        return this.advertDao.query(query, params, begin, max);
    }
}




