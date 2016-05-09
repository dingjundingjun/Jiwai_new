package com.sounuo.jiwai.data;

import java.util.ArrayList;
import java.util.List;

public class ReadBaseCatalogPojo extends BasePojo{
	private ArrayList<ReadCatalogPojo> message;
	public ArrayList<ReadCatalogPojo> getMessage() {
		return message;
	}
	public void setMessage(ArrayList<ReadCatalogPojo> message) {
		this.message = message;
	}
}
