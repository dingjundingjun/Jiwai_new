package com.sounuo.jiwai.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.data.ReadTitleData;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.SharedPrefUtil;
import com.sounuo.jiwai.views.ChangeColorTextView;
import com.sounuo.jiwai.views.ItemLinearLayout;

/**
 * 展示收藏的图片
 * @author gq
 *
 */
public class MePicFragment extends Fragment {
	
	private boolean Test=true;
	private GridView GridViewPicCollection;
	private ImageView ImageviewNoPic;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View inflate = inflater.inflate(R.layout.me_fragment_pic, null);
		
		initViews(inflate);
		
		getPicFromDb();

		return inflate;
	}


	private void getPicFromDb() {
		if (Test) {
			// 木有照片
			GridViewPicCollection.setVisibility(View.GONE);
			ImageviewNoPic.setVisibility(View.VISIBLE);
		}
		
	}

	private void initViews(View inflate) {
		GridViewPicCollection = (GridView) inflate.findViewById(R.id.me_pic_collection_grid);
		ImageviewNoPic = (ImageView) inflate.findViewById(R.id.me_pic_collection_no_pic);
	}
	
	public void initTestDate() {
		ArrayList<String> Urls = new ArrayList<String>();
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic2.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic1.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic3.jpg ");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic4.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic5.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic7.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic6.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic8.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic9.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic10.jpg");
		Urls.add("http://game.sounuo.net/sinaapp/watchworld/update/20151212/pic11.jpg");
		
	}
}
