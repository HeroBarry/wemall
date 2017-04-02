package com.wemall.foundation.domain;

import com.wemall.core.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "wemall_goods_returnitem")
public class GoodsReturnItem extends IdEntity {

    private static final long serialVersionUID = 3006242082958970206L;
    //商品
    @OneToOne(fetch = FetchType.LAZY)
    private Goods goods;
    //返回商品
    @ManyToOne(fetch = FetchType.LAZY)
    private GoodsReturn gr;

    @ManyToMany(cascade = {javax.persistence.CascadeType.ALL})
    @JoinTable(name = "wemall_return_gsp", joinColumns = {@javax.persistence.JoinColumn(name = "item_id")}, inverseJoinColumns = {@javax.persistence.JoinColumn(name = "gsp_id")})
    private List<GoodsSpecProperty> gsps = new ArrayList<GoodsSpecProperty>();

    //spec信息
    @Lob
    @Column(columnDefinition = "LongText")
    private String spec_info;
    //数量
    private int count;

    public Goods getGoods(){
        return this.goods;
    }

    public void setGoods(Goods goods){
        this.goods = goods;
    }

    public GoodsReturn getGr(){
        return this.gr;
    }

    public void setGr(GoodsReturn gr){
        this.gr = gr;
    }

    public List<GoodsSpecProperty> getGsps(){
        return this.gsps;
    }

    public void setGsps(List<GoodsSpecProperty> gsps){
        this.gsps = gsps;
    }

    public String getSpec_info(){
        return this.spec_info;
    }

    public void setSpec_info(String spec_info){
        this.spec_info = spec_info;
    }

    public int getCount(){
        return this.count;
    }

    public void setCount(int count){
        this.count = count;
    }
}




