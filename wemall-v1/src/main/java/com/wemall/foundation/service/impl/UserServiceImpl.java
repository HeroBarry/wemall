package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.service.IUserService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl
    implements IUserService {
    @Resource(name = "userDAO")
    private IGenericDAO<User> userDAO;

    public boolean delete(Long id){
        try {
            this.userDAO.remove(id);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public User getObjById(Long id){
        return (User)this.userDAO.get(id);
    }

    public boolean save(User user){
        try {
            this.userDAO.save(user);
            return true;
        } catch (Exception e){
        }

        return false;
    }

    public boolean update(User user){
        try {
            this.userDAO.update(user);
            return true;
        } catch (Exception e){
        }

        return false;
    }

    public List<User> query(String query, Map params, int begin, int max){
        return this.userDAO.query(query, params, begin, max);
    }

    public IPageList list(IQueryObject properties){
        if (properties == null){
            return null;
        }
        String query = properties.getQuery();
        Map params = properties.getParameters();
        GenericPageList pList = new GenericPageList(User.class, query, params,
                this.userDAO);
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

    public User getObjByProperty(String propertyName, String value){
        return (User)this.userDAO.getBy(propertyName, value);
    }
}




