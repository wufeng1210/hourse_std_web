package com.hourse.web.model;

/**
 * Created by dell on 2017/4/16.
 */
public class UserAuth {
    private int authId;
    private String authName;
    private String authPath;
    private String parentId;
    private String authDescription;
    private String state;

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthPath() {
        return authPath;
    }

    public void setAuthPath(String authPath) {
        this.authPath = authPath;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAuthDescription() {
        return authDescription;
    }

    public void setAuthDescription(String authDescription) {
        this.authDescription = authDescription;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
