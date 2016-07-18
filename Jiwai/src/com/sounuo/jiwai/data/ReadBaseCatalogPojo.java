package com.sounuo.jiwai.data;

import java.util.ArrayList;
import java.util.List;

public class ReadBaseCatalogPojo extends BasePojo{
	private ArrayList<ReadCatalogPojo> msg;
	private String data;
	private int code;
	public ArrayList<ReadCatalogPojo> getmsg() {
		return msg;
	}
	public void setmsg(ArrayList<ReadCatalogPojo> message) {
		this.msg = message;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
}
