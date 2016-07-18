package com.sounuo.jiwai.data;

import java.util.ArrayList;

/**
 * 这个是阅读的分类
 * */
public class ReadTitleDataPojo extends BasePojo{
	private ArrayList<ReadTitleData> msg;
	public ArrayList<ReadTitleData> getmsg() {
		return msg;
	}
//	public void setMessage(ArrayList<ReadTitleData> message) {
//		this.message = message;
//	}
	
	public void setmsg(ArrayList<ReadTitleData> message) {
		this.msg = message;
	}
}
