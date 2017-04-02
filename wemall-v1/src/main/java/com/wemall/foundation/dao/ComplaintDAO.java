package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Complaint;
import org.springframework.stereotype.Repository;

@Repository("complaintDAO")
public class ComplaintDAO extends GenericDAO<Complaint> {
}

