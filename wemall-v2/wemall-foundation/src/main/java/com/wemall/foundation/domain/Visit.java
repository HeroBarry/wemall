package com.wemall.foundation.domain;

import com.wemall.core.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "wemall_Visit")
public class Visit extends IdEntity {

    //主页
    @ManyToOne(fetch = FetchType.LAZY)
    private HomePage homepage;

    //用户
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    //浏览时间
    private Date visitTime;

    public HomePage getHomepage(){
        return this.homepage;
    }

    public void setHomepage(HomePage homepage){
        this.homepage = homepage;
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Date getVisitTime(){
        return this.visitTime;
    }

    public void setVisitTime(Date visitTime){
        this.visitTime = visitTime;
    }
}




