package com.sounuo.jiwai.database;

/**
 * 栏目信息的基类
 * Created by gq on 2016/5/7.
 */
public class ColumnBean {
//    需要获取栏目的标题
    private String title;
//    大圆的背景颜色
    private String url;
//    是否选中
    private boolean isChecked;


	public ColumnBean(String title, boolean isChecked, String url) {
		super();
		this.title = title;
		this.url = url;
		this.isChecked = isChecked;
	}

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

