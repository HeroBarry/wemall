package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.GroupArea;
import com.wemall.foundation.service.IGroupAreaService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroupAreaServiceImpl
    implements IGroupAreaService {
    @Resource(name = "groupAreaDAO")
    private IGenericDAO<GroupArea> groupAreaDao;

    public boolean save(GroupArea groupArea){
        try {
            this.groupAreaDao.save(groupArea);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public GroupArea getObjById(Long id){
        GroupArea groupArea = (GroupArea)this.groupAreaDao.get(id);
        if (groupArea != null){
            return groupArea;
        }

        return null;
    }

    public boolean delete(Long id){
        try {
            this.groupAreaDao.remove(id);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean batchDelete(List<Serializable> groupAreaIds){
        for (Serializable id : groupAreaIds){
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
        GenericPageList pList = new GenericPageList(GroupArea.class, query,
                params, this.groupAreaDao);
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

    public boolean update(GroupArea groupArea){
        try {
            this.groupAreaDao.update(groupArea);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public List<GroupArea> query(String query, Map params, int begin, int max){
        return this.groupAreaDao.query(query, params, begin, max);
    }
}




