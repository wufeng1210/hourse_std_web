package com.hourse.web.model;

/**
 * Created by dell on 2017/4/16.
 */
public class ActivityInfo {

    private int activityId;
    private String activityTitle;
    private String activityImagePath;
    private String activityImageUrl;
    private String target;
    private String STATUS;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityImagePath() {
        return activityImagePath;
    }

    public void setActivityImagePath(String activityImagePath) {
        this.activityImagePath = activityImagePath;
    }

    public String getActivityImageUrl() {
        return activityImageUrl;
    }

    public void setActivityImageUrl(String activityImageUrl) {
        this.activityImageUrl = activityImageUrl;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
