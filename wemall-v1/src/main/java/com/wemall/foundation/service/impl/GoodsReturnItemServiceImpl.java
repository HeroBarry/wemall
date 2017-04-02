package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.GoodsReturnItem;
import com.wemall.foundation.service.IGoodsReturnItemService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GoodsReturnItemServiceImpl
    implements IGoodsReturnItemService {
    @Resource(name = "goodsReturnItemDAO")
    private IGenericDAO<GoodsReturnItem> goodsReturnItemDao;

    public boolean save(GoodsReturnItem goodsReturnItem){
        try {
            this.goodsReturnItemDao.save(goodsReturnItem);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public GoodsReturnItem getObjById(Long id){
        GoodsReturnItem goodsReturnItem = (GoodsReturnItem)this.goodsReturnItemDao.get(id);
        if (goodsReturnItem != null){
            return goodsReturnItem;
        }

        return null;
    }

    public boolean delete(Long id){
        try {
            this.goodsReturnItemDao.remove(id);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean batchDelete(List<Serializable> goodsReturnItemIds){
        for (Serializable id : goodsReturnItemIds){
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
        GenericPageList pList = new GenericPageList(GoodsReturnItem.class, query,
                params, this.goodsReturnItemDao);
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

    public boolean update(GoodsReturnItem goodsReturnItem){
        try {
            this.goodsReturnItemDao.update(goodsReturnItem);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public List<GoodsReturnItem> query(String query, Map params, int begin, int max){
        return this.goodsReturnItemDao.query(query, params, begin, max);
    }
}




