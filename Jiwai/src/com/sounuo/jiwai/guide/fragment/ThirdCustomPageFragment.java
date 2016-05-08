package com.sounuo.jiwai.guide.fragment;

import android.view.View;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.guide.GuideListener;
import com.sounuo.jiwai.guide.slidingtutorial.PageFragment;
import com.sounuo.jiwai.guide.slidingtutorial.TransformItem;

public class ThirdCustomPageFragment extends PageFragment {
	private GuideListener listener;

	public void setGuideListener( GuideListener l ) {
		this.listener = l;
	}

	@Override
	public void onStart( ) {
		super.onStart();
		getRootView( ).findViewById( R.id.ivSixthImage ).setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick( View v ) {
				if( listener != null ) {
					listener.needFinish( );
				}
			}
		} );
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.fragment_page_third;
	}

	@Override
	protected TransformItem[] provideTransformItems() {
		return new TransformItem[]{
				new TransformItem(R.id.ivFirstImage, true, 20),
//				new TransformItem(R.id.ivSecondImage, false, 6),
//				new TransformItem(R.id.ivThirdImage, true, 8),
//				new TransformItem(R.id.ivFourthImage, false, 10),
//				new TransformItem(R.id.ivFifthImage, false, 3),
				new TransformItem(R.id.ivSixthImage, false,10),
//				new TransformItem(R.id.ivSeventhImage, false, 14),
		};
	}
}
