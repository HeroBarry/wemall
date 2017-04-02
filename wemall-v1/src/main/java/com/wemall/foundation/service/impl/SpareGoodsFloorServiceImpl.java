package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.SpareGoodsFloor;
import com.wemall.foundation.service.ISpareGoodsFloorService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpareGoodsFloorServiceImpl
    implements ISpareGoodsFloorService {
    @Resource(name = "spareGoodsFloorDAO")
    private IGenericDAO<SpareGoodsFloor> spareGoodsFloorDao;

    public boolean save(SpareGoodsFloor spareGoodsFloor){
        try {
            this.spareGoodsFloorDao.save(spareGoodsFloor);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public SpareGoodsFloor getObjById(Long id){
        SpareGoodsFloor spareGoodsFloor = (SpareGoodsFloor)this.spareGoodsFloorDao.get(id);
        if (spareGoodsFloor != null){
            return spareGoodsFloor;
        }

        return null;
    }

    public boolean delete(Long id){
        try {
            this.spareGoodsFloorDao.remove(id);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean batchDelete(List<Serializable> spareGoodsFloorIds){
        for (Serializable id : spareGoodsFloorIds){
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
        GenericPageList pList = new GenericPageList(SpareGoodsFloor.class, query,
                params, this.spareGoodsFloorDao);
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

    public boolean update(SpareGoodsFloor spareGoodsFloor){
        try {
            this.spareGoodsFloorDao.update(spareGoodsFloor);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public List<SpareGoodsFloor> query(String query, Map params, int begin, int max){
        return this.spareGoodsFloorDao.query(query, params, begin, max);
    }
}




