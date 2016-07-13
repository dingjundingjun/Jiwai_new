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
    public String phone;
    //	昵称
    public String nickName;
    //	性别
    public String sex;
    //	年级
    public String grade;
    //	头像保存路径
    public String photoPath;
    
    public String token;
    //	收藏的图片
    
//    "accountId": 8,
//    "loginName": "18676275291",
//    "phone": "18676275291",
//    "status": 0,
//    "nickName": "牛牛",
//    "sex": null,
//    "grade": null,
//    "photoPath": null,
//    "regTime": "2016-07-06T01:59:57.000Z",
//    "imUser": null,
//    "imPassword": null,
//    "introduce": null,
//    "token": "3082_04463039608e1864c5372ddbbeb9f6cb"
    
    public String getPhone() {
		return phone;
	}
    
    public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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


	public PersonalInfoPojo(String accountId, String phone, String nickName,
			String sex, String grade, String photoPath, String token) {
		super();
		this.accountId = accountId;
		this.phone = phone;
		this.nickName = nickName;
		this.sex = sex;
		this.grade = grade;
		this.photoPath = photoPath;
		this.token = token;
	}

	@Override
	public String toString() {
		return "PersonalInfoPojo [accountId=" + accountId + ", phone=" + phone
				+ ", nickName=" + nickName + ", sex=" + sex + ", grade="
				+ grade + ", photoPath=" + photoPath + "]";
	}



}




