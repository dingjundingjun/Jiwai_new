package com.sounuo.jiwai.database;

import java.io.Serializable;
import java.util.List;

/**
 * 个人信息
 */
public class PersonalInfoPojo implements Serializable {

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

    //	关注的栏目信息
    public List<ColumnBean> columnBeans;

    public String getPhotoPath() {
        return photoPath;
    }


    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }


    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String userId) {
        this.accountId = userId;
    }


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

    @Override
    public String toString() {
        return "PersonalInfoPojo [userId=" + accountId + ", password="
                + password + ", nickName=" + nickName + ", sex=" + sex
                + ",grade=" + grade + "]";
    }


}




