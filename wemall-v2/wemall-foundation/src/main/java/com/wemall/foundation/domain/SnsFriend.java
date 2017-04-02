package com.wemall.foundation.domain;

import com.wemall.core.domain.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "wemall_user_friend")
public class SnsFriend extends IdEntity {

    //来源用户
    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;

    //目标用户
    @ManyToOne(fetch = FetchType.LAZY)
    private User toUser;

    public User getFromUser(){
        return this.fromUser;
    }

    public void setFromUser(User fromUser){
        this.fromUser = fromUser;
    }

    public User getToUser(){
        return this.toUser;
    }

    public void setToUser(User toUser){
        this.toUser = toUser;
    }
}




