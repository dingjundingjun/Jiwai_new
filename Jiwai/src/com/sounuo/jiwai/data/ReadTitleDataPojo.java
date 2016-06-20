package com.sounuo.jiwai.data;

import java.util.ArrayList;

/**
 * 这个是阅读的分类
 * */
public class ReadTitleDataPojo extends BasePojo{
	private ArrayList<ReadTitleData> message;
	public ArrayList<ReadTitleData> getMessage() {
		return message;
	}
	public void setMessage(ArrayList<ReadTitleData> message) {
		this.message = message;
	}
}
