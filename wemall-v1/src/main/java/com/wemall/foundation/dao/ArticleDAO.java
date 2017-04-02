package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Article;
import org.springframework.stereotype.Repository;

@Repository("articleDAO")
public class ArticleDAO extends GenericDAO<Article> {
}
