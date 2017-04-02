package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Favorite;
import org.springframework.stereotype.Repository;

@Repository("favoriteDAO")
public class FavoriteDAO extends GenericDAO<Favorite> {
}

