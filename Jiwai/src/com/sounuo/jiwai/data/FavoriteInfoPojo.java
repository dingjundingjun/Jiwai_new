package com.sounuo.jiwai.data;

import java.io.Serializable;
import java.util.List;

/**
 * 收藏信息
 */
public class FavoriteInfoPojo implements Serializable {
	private static final long serialVersionUID = 1L;
    String title ;
    String username;
    String description;
    String domain;
    String url;
    long dateline;
    int id;
    int share;
    int comment;
    int like;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getDateline() {
		return dateline;
	}
	public void setDateline(long dateline) {
		this.dateline = dateline;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public FavoriteInfoPojo(String title, String username, String description,
			String domain, String url, long dateline, int id, int share,
			int comment, int like) {
		super();
		this.title = title;
		this.username = username;
		this.description = description;
		this.domain = domain;
		this.url = url;
		this.dateline = dateline;
		this.id = id;
		this.share = share;
		this.comment = comment;
		this.like = like;
	}
	@Override
	public String toString() {
		return "FavoriteInfoPojo [title=" + title + ", username=" + username
				+ ", description=" + description + ", domain=" + domain
				+ ", url=" + url + ", dateline=" + dateline + ", id=" + id
				+ ", share=" + share + ", comment=" + comment + ", like="
				+ like + "]";
	}
    
    

}




