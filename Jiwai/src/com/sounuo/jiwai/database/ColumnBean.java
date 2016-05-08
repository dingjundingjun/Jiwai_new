package com.sounuo.jiwai.database;

/**
 * 栏目信息的基类
 * Created by gq on 2016/5/7.
 */
public class ColumnBean {
//    需要获取栏目的标题
    private String title;
//    大圆的背景颜色
    private int bigColor;
//    是否选中
    private boolean isChecked;


    public ColumnBean(String title,int bigColor,boolean isChecked){
        this.bigColor=bigColor;
        this.isChecked=isChecked;
        this.title=title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getBigColor() {
        return bigColor;
    }

    public void setBigColor(int bigColor) {
        this.bigColor = bigColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

