package com.sounuo.jiwai.guide.fragment;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.guide.slidingtutorial.PageFragment;
import com.sounuo.jiwai.guide.slidingtutorial.TransformItem;

public class SecondCustomPageFragment extends PageFragment {

	@Override
	protected int getLayoutResId() {
		return R.layout.fragment_page_second;
	}

	@Override
	protected TransformItem[] provideTransformItems() {
		return new TransformItem[]{
				new TransformItem(R.id.ivFirstImage, true, 20),
//				new TransformItem(R.id.ivSecondImage, false, 6),
//				new TransformItem(R.id.ivThirdImage, true, 8),
//				new TransformItem(R.id.ivFourthImage, false, 10),
//				new TransformItem(R.id.ivFifthImage, false, 3),
//				new TransformItem(R.id.ivSixthImage, false, 9),
//				new TransformItem(R.id.ivSeventhImage, false, 14),
//				new TransformItem(R.id.ivEighthImage, false, 7)
		};
	}
}
