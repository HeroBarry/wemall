package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.ComplaintSubject;
import com.wemall.foundation.service.IComplaintSubjectService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComplaintSubjectServiceImpl
    implements IComplaintSubjectService {
    @Resource(name = "complaintSubjectDAO")
    private IGenericDAO<ComplaintSubject> complaintSubjectDao;

    public boolean save(ComplaintSubject complaintSubject){
        try {
            this.complaintSubjectDao.save(complaintSubject);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public ComplaintSubject getObjById(Long id){
        ComplaintSubject complaintSubject = (ComplaintSubject)this.complaintSubjectDao.get(id);
        if (complaintSubject != null){
            return complaintSubject;
        }

        return null;
    }

    public boolean delete(Long id){
        try {
            this.complaintSubjectDao.remove(id);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean batchDelete(List<Serializable> complaintSubjectIds){
        for (Serializable id : complaintSubjectIds){
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
        GenericPageList pList = new GenericPageList(ComplaintSubject.class, query,
                params, this.complaintSubjectDao);
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

    public boolean update(ComplaintSubject complaintSubject){
        try {
            this.complaintSubjectDao.update(complaintSubject);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public List<ComplaintSubject> query(String query, Map params, int begin, int max){
        return this.complaintSubjectDao.query(query, params, begin, max);
    }
}




