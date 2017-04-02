package com.wemall.view.web.tools;

import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.*;
import com.wemall.foundation.service.IGoodsClassService;
import com.wemall.foundation.service.IGoodsService;
import com.wemall.foundation.service.IUserGoodsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 商品工具组件
 */
@Component
public class GoodsViewTools {
    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IGoodsClassService goodsClassService;

    @Autowired
    private IUserGoodsClassService userGoodsClassService;

    /**
     * 根据商品id生成该商品的多规格
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<GoodsSpecification> generic_spec(String id){
        List specs = new ArrayList();
        if ((id != null) && (!id.equals(""))){
            Goods goods = this.goodsService.getObjById(Long.valueOf(Long.parseLong(id)));
            for (GoodsSpecProperty gsp : goods.getGoods_specs()){
                GoodsSpecification spec = gsp.getSpec();
                if (!specs.contains(spec)){
                    specs.add(spec);
                }
            }
        }
        Collections.sort(specs, new Comparator(){
            public int compare(Object gs1, Object gs2){
                return (((GoodsSpecification)gs1).getSequence()) - (((GoodsSpecification)gs2).getSequence());
            }
        });

        return specs;
    }

    public List<UserGoodsClass> query_user_class(String pid){
        List list = new ArrayList();
        if ((pid == null) || (pid.equals(""))){
            Map map = new HashMap();
            map.put("uid", SecurityUserHolder.getCurrentUser().getId());
            list = this.userGoodsClassService
                   .query("select obj from UserGoodsClass obj where obj.parent.id is null and obj.user.id = :uid order by obj.sequence asc",
                          map, -1, -1);
        }else{
            Map params = new HashMap();
            params.put("pid", Long.valueOf(Long.parseLong(pid)));
            params.put("uid", SecurityUserHolder.getCurrentUser().getId());
            list = this.userGoodsClassService
                   .query("select obj from UserGoodsClass obj where obj.parent.id=:pid and obj.user.id = :uid order by obj.sequence asc",
                          params, -1, -1);
        }

        return list;
    }

    public List<Goods> query_with_gc(String gc_id, int count){
        List list = new ArrayList();
        GoodsClass gc = this.goodsClassService.getObjById(
                            CommUtil.null2Long(gc_id));
        if (gc != null){
            Set ids = genericIds(gc);
            Map params = new HashMap();
            params.put("ids", ids);
            params.put("goods_status", Integer.valueOf(0));
            list = this.goodsService
                   .query("select obj from Goods obj where obj.gc.id in (:ids) and obj.goods_status=:goods_status order by obj.goods_click desc",
                          params, 0, count);
        }

        return list;
    }

    private Set<Long> genericIds(GoodsClass gc){
        Set ids = new HashSet();
        ids.add(gc.getId());
        for (GoodsClass child : gc.getChilds()){
            Set<Long> cids = genericIds(child);
            for (Long cid : cids){
                ids.add(cid);
            }
            ids.add(child.getId());
        }

        return ids;
    }

    public List<Goods> sort_sale_goods(String store_id, int count){
        List list = new ArrayList();
        Map params = new HashMap();
        params.put("store_id", CommUtil.null2Long(store_id));
        params.put("goods_status", Integer.valueOf(0));
        list = this.goodsService
               .query("select obj from Goods obj where obj.goods_store.id=:store_id and obj.goods_status=:goods_status order by obj.goods_salenum desc",
                      params, 0, count);

        return list;
    }

    public List<Goods> sort_collect_goods(String store_id, int count){
        List list = new ArrayList();
        Map params = new HashMap();
        params.put("store_id", CommUtil.null2Long(store_id));
        params.put("goods_status", Integer.valueOf(0));
        list = this.goodsService
               .query("select obj from Goods obj where obj.goods_store.id=:store_id and obj.goods_status=:goods_status order by obj.goods_collect desc",
                      params, 0, count);

        return list;
    }

    public List<Goods> query_combin_goods(String id){
        return this.goodsService.getObjById(CommUtil.null2Long(id))
               .getCombin_goods();
    }
}




