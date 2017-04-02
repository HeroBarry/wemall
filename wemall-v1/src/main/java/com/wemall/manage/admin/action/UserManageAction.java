package com.wemall.manage.admin.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.Md5Encrypt;
import com.wemall.core.tools.WebForm;
import com.wemall.foundation.domain.*;
import com.wemall.foundation.domain.query.UserQueryObject;
import com.wemall.foundation.service.*;
import com.wemall.manage.admin.tools.StoreTools;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 会员管理控制器
 */
@Controller
public class UserManageAction {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IStoreGradeService storeGradeService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IAlbumService albumService;

    @Autowired
    private IPredepositService predepositService;

    @Autowired
    private IEvaluateService evaluateService;

    @Autowired
    private IGoodsCartService goodsCartService;

    @Autowired
    private IOrderFormService orderFormService;

    @Autowired
    private IOrderFormLogService orderFormLogService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private StoreTools storeTools;

    @SecurityMapping(display = false, rsequence = 0, title = "会员添加", value = "/admin/user_add.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/user_add.htm"})
    public ModelAndView user_add(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new JModelAndView("admin/blue/user_add.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);

        return mv;
    }

    /**
     * 编辑会员资料
     * @param request
     * @param response
     * @param id
     * @param op
     * @return
     */
    @SecurityMapping(display = false, rsequence = 0, title = "会员编辑", value = "/admin/user_edit.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/user_edit.htm"})
    public ModelAndView user_edit(HttpServletRequest request, HttpServletResponse response, String id, String op){
        ModelAndView mv = new JModelAndView("admin/blue/user_add.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        mv.addObject("obj", this.userService.getObjById(Long.valueOf(Long.parseLong(id))));
        mv.addObject("edit", Boolean.valueOf(true));

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "会员列表", value = "/admin/user_list.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/user_list.htm"})
    public ModelAndView user_list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType, String condition, String value){
        ModelAndView mv = new JModelAndView("admin/blue/user_list.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        UserQueryObject uqo = new UserQueryObject(currentPage, mv, orderBy,
                orderType);
        WebForm wf = new WebForm();
        wf.toQueryPo(request, uqo, User.class, mv);
        uqo.addQuery("obj.userRole", new SysMap("userRole", "ADMIN"), "!=");
        if (condition != null){
            if (condition.equals("userName")){
                uqo
                .addQuery("obj.userName",
                          new SysMap("userName", value), "=");
            }
            if (condition.equals("email")){
                uqo.addQuery("obj.email", new SysMap("email", value), "=");
            }
            if (condition.equals("trueName")){
                uqo
                .addQuery("obj.trueName",
                          new SysMap("trueName", value), "=");
            }
        }
        uqo.addQuery("obj.parent.id is null", null);
        IPageList pList = this.userService.list(uqo);
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))){
            url = CommUtil.getURL(request);
        }
        CommUtil.saveIPageList2ModelAndView(url + "/admin/user_list.htm", "",
                                            "", pList, mv);
        mv.addObject("userRole", "USER");
        mv.addObject("storeTools", this.storeTools);

        return mv;
    }

    /**
     * 保存会员资料
     * @param request
     * @param response
     * @param id
     * @param role_ids
     * @param list_url
     * @param add_url
     * @param password
     * @return
     */
    @SecurityMapping(display = false, rsequence = 0, title = "会员保存", value = "/admin/user_save.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/user_save.htm"})
    public ModelAndView user_save(HttpServletRequest request, HttpServletResponse response, String id, String role_ids, String list_url, String add_url, String password){
        WebForm wf = new WebForm();
        User user = null;
        if (id.equals("")){
            user = (User)wf.toPo(request, User.class);
            user.setAddTime(new Date());
        }else{
            User u = this.userService.getObjById(Long.valueOf(Long.parseLong(id)));
            user = (User)wf.toPo(request, u);
        }
        if ((password != null) && (!password.equals(""))){
            user.setPassword(Md5Encrypt.md5(password).toLowerCase());
        }
        if (id.equals("")){// 新增会员
            user.setUserRole("BUYER");
            user.getRoles().clear();
            Map params = new HashMap();
            params.put("type", "BUYER");
            List roles = this.roleService
                         .query("select obj from Role obj where obj.type=:type", params,
                                -1, -1);
            user.getRoles().addAll(roles);
            this.userService.save(user);

            Album album = new Album();
            album.setAddTime(new Date());
            album.setAlbum_default(true);
            album.setAlbum_name("默认相册");
            album.setAlbum_sequence(-10000);
            album.setUser(user);
            this.albumService.save(album);
        }else{// 修改会员资料
            this.userService.update(user);
        }
        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        mv.addObject("list_url", list_url);
        mv.addObject("op_title", "保存用户成功");
        if (add_url != null){
            mv.addObject("add_url", add_url);
        }

        return mv;
    }

    /**
     * 删除会员
     * @param request
     * @param mulitId
     * @param currentPage
     * @return
     */
    @SecurityMapping(display = false, rsequence = 0, title = "会员删除", value = "/admin/user_del.htm*", rtype = "admin", rname = "会员管理", rcode = "user_manage", rgroup = "会员")
    @RequestMapping({"/admin/user_del.htm"})
    public String user_del(HttpServletRequest request, String mulitId, String currentPage){
        String[] ids = mulitId.split(",");
        for (String id : ids){
            if (!id.equals("")){
                User parent = this.userService.getObjById(Long.valueOf(Long.parseLong(id)));
                if (!parent.getUsername().equals("admin")){
                    Long ofid;
                    for (User user : parent.getChilds()){
                        user.getRoles().clear();
                        if (user.getStore() != null){
                            for (Goods goods : user.getStore().getGoods_list()){
                                Map map = new HashMap();
                                map.put("gid", goods.getId());
                                List<GoodsCart> goodCarts = this.goodsCartService
                                                            .query(
                                                                "select obj from GoodsCart obj where obj.goods.id = :gid",
                                                                map, -1, -1);
                                ofid = null;
                                Map map2;
                                for (GoodsCart gc : goodCarts){
                                    ofid = gc.getOf().getId();
                                    this.goodsCartService.delete(gc.getId());
                                    map2 = new HashMap();
                                    map2.put("ofid", ofid);
                                    List goodCarts2 = this.goodsCartService
                                                      .query(
                                                          "select obj from GoodsCart obj where obj.of.id = :ofid",
                                                          map2, -1, -1);
                                    if (goodCarts2.size() == 0){
                                        this.orderFormService.delete(ofid);
                                    }
                                }

                                List<Evaluate> evaluates = goods.getEvaluates();
                                for (Evaluate e : evaluates){
                                    this.evaluateService.delete(e.getId());
                                }
                                goods.getGoods_ugcs().clear();
                                this.goodsService.delete(goods.getId());
                            }
                        }
                        this.userService.delete(user.getId());
                    }
                    parent.getRoles().clear();
                    if (parent.getStore() != null){
                        for (Goods goods : parent.getStore().getGoods_list()){
                            Map map = new HashMap();
                            map.put("gid", goods.getId());
                            List<GoodsCart> goodCarts = this.goodsCartService
                                                        .query(
                                                            "select obj from GoodsCart obj where obj.goods.id = :gid",
                                                            map, -1, -1);
                            Long ofid1 = null;
                            Map map2;
                            for (GoodsCart gc : goodCarts){
                                ofid1 = gc.getOf().getId();
                                this.goodsCartService.delete(gc.getId());
                                map2 = new HashMap();
                                map2.put("ofid", ofid1);
                                List goodCarts2 = this.goodsCartService
                                                  .query(
                                                      "select obj from GoodsCart obj where obj.of.id = :ofid",
                                                      map2, -1, -1);
                                if (goodCarts2.size() == 0){
                                    this.orderFormService.delete(ofid1);
                                }
                            }

                            List<Evaluate> evaluates = goods.getEvaluates();
                            for (Evaluate e : evaluates){
                                this.evaluateService.delete(e.getId());
                            }
                            goods.getGoods_ugcs().clear();
                            this.goodsService.delete(goods.getId());
                        }
                    }
                    this.userService.delete(parent.getId());
                }
            }
        }

        return "redirect:user_list.htm?currentPage=" + currentPage;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "会员通知", value = "/admin/user_msg.htm*", rtype = "admin", rname = "会员通知", rcode = "user_msg", rgroup = "会员")
    @RequestMapping({"/admin/user_msg.htm"})
    public ModelAndView user_msg(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new JModelAndView("admin/blue/user_msg.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        List grades = this.storeGradeService.query(
                          "select obj from StoreGrade obj order by obj.sequence asc",
                          null, -1, -1);
        mv.addObject("grades", grades);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "会员通知发送", value = "/admin/user_msg_send.htm*", rtype = "admin", rname = "会员通知", rcode = "user_msg", rgroup = "会员")
    @RequestMapping({"/admin/user_msg_send.htm"})
    public ModelAndView user_msg_send(HttpServletRequest request, HttpServletResponse response, String type, String list_url, String users, String grades, String content) throws IOException {
        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        List<User> user_list = new ArrayList();
        if (type.equals("all_user")){
            Map params = new HashMap();
            params.put("userRole", "ADMIN");
            user_list = this.userService
                        .query(
                            "select obj from User obj where obj.userRole!=:userRole order by obj.addTime desc",
                            params, -1, -1);
        }
        User user;
        if (type.equals("the_user")){
            List<String> user_names = CommUtil.str2list(users);
            for (String user_name : user_names){
                user = this.userService.getObjByProperty("userName",
                        user_name);
                user_list.add(user);
            }
        }
        if (type.equals("all_store"))
            user_list = this.userService
                        .query(
                            "select obj from User obj where obj.store.id is not null order by obj.addTime desc",
                            null, -1, -1);
        Set store_ids;
        if (type.equals("the_store")){
            Map params = new HashMap();
            store_ids = new TreeSet();
            String[] arrayOfString = grades.split(",");
            int localUser1 = arrayOfString.length;
            for (int i = 0; i < localUser1; i++){
                String grade = arrayOfString[i];
                store_ids.add(Long.valueOf(Long.parseLong(grade)));
            }
            params.put("store_ids", store_ids);
            user_list = this.userService
                        .query(
                            "select obj from User obj where obj.store.id in(:store_ids)",
                            params, -1, -1);
        }
        for (User user1 : user_list){
            Message msg = new Message();
            msg.setAddTime(new Date());
            msg.setContent(content);
            msg.setFromUser(SecurityUserHolder.getCurrentUser());
            msg.setToUser(user1);
            this.messageService.save(msg);
        }
        mv.addObject("op_title", "会员通知发送成功");
        mv.addObject("list_url", list_url);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "会员信用", value = "/admin/user_creditrule.htm*", rtype = "admin", rname = "会员信用", rcode = "user_creditrule", rgroup = "会员")
    @RequestMapping({"/admin/user_creditrule.htm"})
    public ModelAndView user_creditrule(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new JModelAndView("admin/blue/user_creditrule.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "买家信用保存", value = "/admin/user_creditrule_save.htm*", rtype = "admin", rname = "会员信用", rcode = "user_creditrule", rgroup = "会员")
    @RequestMapping({"/admin/user_creditrule_save.htm"})
    public ModelAndView user_creditrule_save(HttpServletRequest request, HttpServletResponse response, String id, String list_url){
        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        SysConfig sc = this.configService.getSysConfig();
        Map map = new HashMap();
        for (int i = 0; i <= 29; i++){
            map.put("creditrule" + i, Integer.valueOf(CommUtil.null2Int(request
                    .getParameter("creditrule" + i))));
        }
        String user_creditrule = Json.toJson(map, JsonFormat.compact());
        sc.setUser_creditrule(user_creditrule);
        if (id.equals(""))
            this.configService.save(sc);
        else
            this.configService.update(sc);
        mv.addObject("list_url", list_url);
        mv.addObject("op_title", "保存会员信用成功");

        return mv;
    }
}




