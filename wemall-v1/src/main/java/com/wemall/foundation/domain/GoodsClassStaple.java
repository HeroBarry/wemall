package com.wemall.foundation.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wemall.core.domain.IdEntity;

/**
 * 主要产品
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "wemall_goodsclassstaple")
public class GoodsClassStaple extends IdEntity {
    private static final long serialVersionUID = 1L;

    //名称
    private String name;

    //商品类型
    @ManyToOne(fetch = FetchType.LAZY)
    private GoodsClass gc;

    //店铺
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public GoodsClass getGc(){
        return this.gc;
    }

    public void setGc(GoodsClass gc){
        this.gc = gc;
    }

    public Store getStore(){
        return this.store;
    }

    public void setStore(Store store){
        this.store = store;
    }
}
