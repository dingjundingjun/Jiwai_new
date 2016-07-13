package com.sounuo.jiwai;

import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity implements OnClickListener{
	private Button mTestCatalogBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Util.AccoutRequest(null, AppConstant.UPDATE_TOKEN_EXPTIME, null);
			}
		}).start();
//        进入到主界面的话，开异步线程
    }
    
    private void init() {
    	mTestCatalogBtn = (Button)findViewById(R.id.test_catalog_btn);
    	mTestCatalogBtn.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.test_catalog_btn: {
			startCatalogActivity();
			break;
		}
		}
	}
	
	private void startCatalogActivity() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, MainCatalogActivity.class);
		startActivity(intent);
//		overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
	}
}
