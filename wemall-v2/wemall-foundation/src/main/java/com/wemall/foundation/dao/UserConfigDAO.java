package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.UserConfig;
import org.springframework.stereotype.Repository;

@Repository("userConfigDAO")
public class UserConfigDAO extends GenericDAO<UserConfig> {
}

