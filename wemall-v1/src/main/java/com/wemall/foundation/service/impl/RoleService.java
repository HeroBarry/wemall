package com.wemall.foundation.service.impl;

import com.wemall.core.dao.IGenericDAO;
import com.wemall.core.query.GenericPageList;
import com.wemall.core.query.PageObject;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.query.support.IQueryObject;
import com.wemall.foundation.domain.Role;
import com.wemall.foundation.service.IRoleService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService
    implements IRoleService {
    @Resource(name = "roleDAO")
    private IGenericDAO<Role> roleDAO;

    public boolean delete(Long id){
        try {
            this.roleDAO.remove(id);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public Role getObjById(Long id){
        return (Role)this.roleDAO.get(id);
    }

    public List<Role> query(String query, Map params, int begin, int max){
        return this.roleDAO.query(query, params, begin, max);
    }

    public boolean save(Role role){
        try {
            this.roleDAO.save(role);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(Role role){
        try {
            this.roleDAO.update(role);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public IPageList list(IQueryObject properties){
        if (properties == null){
            return null;
        }
        String query = properties.getQuery();
        Map params = properties.getParameters();
        GenericPageList pList = new GenericPageList(Role.class, query, params,
                this.roleDAO);
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

    public Role getObjByProperty(String propertyName, Object value){
        return (Role)this.roleDAO.getBy(propertyName, value);
    }
}




