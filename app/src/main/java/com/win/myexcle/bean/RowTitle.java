package com.win.myexcle.bean;

/**
 * Author: wangshuang
 * Time: 2017/2/27
 * Email:xiaoshuang990@sina.com
 */
//row hang   column  lie
public class RowTitle {
    private String dateString;
    private String weekString;
    private int availableRoomCount;

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getWeekString() {
        return weekString;
    }

    public void setWeekString(String weekString) {
        this.weekString = weekString;
    }

    public int getAvailableRoomCount() {
        return availableRoomCount;
    }

    public void setAvailableRoomCount(int availableRoomCount) {
        this.availableRoomCount = availableRoomCount;
    }
}
