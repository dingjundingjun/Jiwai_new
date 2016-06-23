package com.sounuo.jiwai.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ReadCatalogPojo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int article_id;
	private int classify_id;
	private int status;
	private String title;
	private String url;
	private String src_url;
	private String snapshots;
	private String createtime;
	private String imgs;
	private String remark;
	private String age;	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}
	public int getClassify_id() {
		return classify_id;
	}
	public void setClassify_id(int classify_id) {
		this.classify_id = classify_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSrc_url() {
		return src_url;
	}
	public void setSrc_url(String src_url) {
		this.src_url = src_url;
	}
	public String getSnapshots() {
		return snapshots;
	}
	public void setSnapshots(String snapshots) {
		this.snapshots = snapshots;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
