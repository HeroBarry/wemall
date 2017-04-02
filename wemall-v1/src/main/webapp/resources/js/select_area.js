function load_area(id,level,type,name){
    if(type=="tab"){//tab标签切换
        $("#area_tab span").removeClass("this");
        $("#area_tab span").eq(level).addClass("this");
        if(level==0)
            id="";
    }else if(type=="first"){//打开地址列表
        $("#area_tab span").removeClass("this");
        $("#area_tab span").eq(parseInt(level)).addClass("this");
    }else if(type=="loading"){//下级地址加载
        $("#area_tab span").removeClass("this");
        $("#area_tab span").eq(level-2).find("a").attr("city_id",id).attr("city_name",name).html(name);
        $("#area_tab span").eq(parseInt(level-2)+1).addClass("this").find("a").html("选择地区");

        if(level==3){
            $("#area_tab span").eq(parseInt(level)+1).find("a").html("选择地区");
        }
    }
    if(level < 4){
        jQuery.ajax({
            type: "POST",
            url: "/get_trans_area.htm",
            data: {parentId:id},
            dataType: "json",
            success:function(data){
                if(id=="")
                    $("#area_tab span").eq(0).addClass("this");
                var text ="";
                var last_id="";
                var last_level="";
                var last_name="";
                var city_id=$("#area_tab span[class='this']").find("a").attr("city_id");
                jQuery.each(data, function(i,item){
                    last_id=item.id;
                    last_level=item.level;
                    last_name=item.areaName;
                    if(item.id==city_id){
                        text=text+"<li><a href='javascript:void(0);' class='this' id="+item.id+" level="+item.level+">"+item.areaName+"</a></li>";
                    }else{
                        text=text+"<li><a href='javascript:void(0);' id="+item.id+" level="+item.level+">"+item.areaName+"</a></li>";
                    }
                });
                if(text!="")
                    $("#citys ul").html(text);
                if(type=="loading"){
                    var obj=$("#citys li");
                    var len =obj.length;
                    if(len==1){
                        load_area(last_id,last_level,"loading",last_name);
                    }
                }
            }
        });
    }else{
        if(type=="loading"||type=="default"){
            if(id!=""){
                go_goods_inventory();
            }
        }
    }
}

function show_areas(){
    var dis =  $('.select_area_list').css("display");
    if(dis=="none"){
        $("#area_tab span").removeClass("this");
        $("#city_info span").each(function(){//地区tab显示
            var index =$(this).index();
            var id = $(this).attr("city_id");
            var name = $(this).html();
            $("#area_tab span").eq(index).find("a").attr("city_id",id).attr("city_name",name).html(name);
        });
        load_area("",0,"first");

        $('.select_area_list').show();
    }
}

function go_goods_inventory(){
    set_area_info();
    $('.select_area_list').hide();
}
//设置区域详情显示信息及详情tab显示信息
function set_area_info(){
    $("#area_tab span").each(function(){//地区详细显示
        var index =$(this).index();
        var name =$(this).find("a").html();
        var id =$(this).find("a").attr("city_id");
        $("#city_info").find("span").eq(index).html(name).attr("city_id",id);
    });
}