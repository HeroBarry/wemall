package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Address;
import org.springframework.stereotype.Repository;

@Repository("addressDAO")
public class AddressDAO extends GenericDAO<Address> {
}

