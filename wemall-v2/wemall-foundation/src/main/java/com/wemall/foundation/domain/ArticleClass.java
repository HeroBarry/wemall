package com.wemall.foundation.domain;

import com.wemall.core.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章分类
 * @author
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "wemall_articleclass")
public class ArticleClass extends IdEntity {
    /**
     * UID
     */
    private static final long serialVersionUID = -8266109906001216423L;
    //类名称
    private String className;
    //序列
    private int sequence;
    //等级
    private int level;
    //标记
    private String mark;
    //是否系统文章
    private boolean sysClass;

    //父类
    @ManyToOne(fetch = FetchType.LAZY)
    private ArticleClass parent;

    //子类
    @OneToMany(mappedBy = "parent", cascade = { CascadeType.REMOVE })
    private List<ArticleClass> childs = new ArrayList<ArticleClass>();

    //文章
    @OneToMany(mappedBy = "articleClass", cascade = { CascadeType.REMOVE })
    @OrderBy("addTime desc")
    private List<Article> articles = new ArrayList<Article>();

    public int getSequence(){
        return this.sequence;
    }

    public void setSequence(int sequence){
        this.sequence = sequence;
    }

    public String getClassName(){
        return this.className;
    }

    public void setClassName(String className){
        this.className = className;
    }

    public int getLevel(){
        return this.level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public boolean isSysClass(){
        return this.sysClass;
    }

    public void setSysClass(boolean sysClass){
        this.sysClass = sysClass;
    }

    public ArticleClass getParent(){
        return this.parent;
    }

    public void setParent(ArticleClass parent){
        this.parent = parent;
    }

    public List<ArticleClass> getChilds(){
        return this.childs;
    }

    public void setChilds(List<ArticleClass> childs){
        this.childs = childs;
    }

    public String getMark(){
        return this.mark;
    }

    public void setMark(String mark){
        this.mark = mark;
    }

    public List<Article> getArticles(){
        return this.articles;
    }

    public void setArticles(List<Article> articles){
        this.articles = articles;
    }
}
