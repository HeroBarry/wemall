package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Document;
import org.springframework.stereotype.Repository;

@Repository("documentDAO")
public class DocumentDAO extends GenericDAO<Document> {
}

