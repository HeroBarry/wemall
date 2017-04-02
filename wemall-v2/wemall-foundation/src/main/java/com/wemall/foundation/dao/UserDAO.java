package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.User;
import org.springframework.stereotype.Repository;

@Repository("userDAO")
public class UserDAO extends GenericDAO<User> {
}

