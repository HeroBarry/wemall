package com.wemall.view.web.tools;

import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.Accessory;
import com.wemall.foundation.service.IAccessoryService;
import com.wemall.foundation.service.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 相册工具组件
 */
@Component
public class AlbumViewTools {
    @Autowired
    private IAlbumService albumService;

    @Autowired
    private IAccessoryService accessoryService;

    public List<Accessory> query_album(String id){
        List list = new ArrayList();
        if ((id != null) && (!id.equals(""))){
            Map params = new HashMap();
            params.put("album_id", CommUtil.null2Long(id));
            list = this.accessoryService
                   .query(
                       "select obj from Accessory obj where obj.album.id=:album_id",
                       params, -1, -1);
        }else{
            list = this.accessoryService.query(
                       "select obj from Accessory obj where obj.album.id is null",
                       null, -1, -1);
        }

        return list;
    }
}




