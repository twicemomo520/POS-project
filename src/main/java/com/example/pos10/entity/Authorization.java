package com.example.pos10.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authorization")
public class Authorization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 自動產生流水號
    @Column(name = "authorization_id", nullable = false, updatable = false)
    private Integer authorizationId;

    @Column(name = "authorization_name", nullable = false, unique = true, length = 45)
    private String authorizationName;

    @Column(name = "authorization_item", length = 90)
    private String authorizationItem;

    // Getter and Setter methods

    public Integer getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(Integer authorizationId) {
        this.authorizationId = authorizationId;
    }

    public String getAuthorizationName() {
        return authorizationName;
    }

    public void setAuthorizationName(String authorizationName) {
        this.authorizationName = authorizationName;
    }

    public String getAuthorizationItem() {
        return authorizationItem;
    }

    public void setAuthorizationItem(String authorizationItem) {
        this.authorizationItem = authorizationItem;
    }
}
