package com.sounuo.jiwai.data;

import java.io.Serializable;
import java.util.List;

/**
 * 个人信息
 */
public class PersonalInfoPojo implements Serializable {
	private static final long serialVersionUID = 1L;
	//	账户Id
    public String accountId;
    //	用户密码
    public String password;
    //	昵称
    public String nickName;
    //	性别
    public String sex;
    //	年级
    public String grade;
    //	头像保存路径
    public String photoPath;
    //	收藏的图片

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	@Override
	public String toString() {
		return "PersonalInfoPojo [password=" + password + ", nickName="
				+ nickName + ", sex=" + sex + ", grade=" + grade
				+ ", photoPath=" + photoPath + "]";
	}


}




