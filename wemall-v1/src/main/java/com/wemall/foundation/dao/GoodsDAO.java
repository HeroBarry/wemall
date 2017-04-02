package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Goods;
import org.springframework.stereotype.Repository;

@Repository("goodsDAO")
public class GoodsDAO extends GenericDAO<Goods> {
}

