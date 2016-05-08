package com.sounuo.jiwai;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;
import com.sounuo.jiwai.database.ColumnBean;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.Debug;
import com.sounuo.jiwai.views.ChangeColorTextView;
import com.sounuo.jiwai.views.CircleImageView;

/**
 * 获取用户头像，以及用户关注的栏目
 */
public class RegisterInformActivity extends Activity implements View.OnClickListener {
    //    本地测试数据
    private List<ColumnBean> columnBeans = new ArrayList<ColumnBean>();

    private boolean isEditMode = false;

    private CircleImageView mPhotoView;

    private static String ICON_PATH;
    private Button mEnterGreatWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_inform);
        ICON_PATH = getFilesDir().getAbsolutePath() + File.separator + "head.png";
        Debug.d(ICON_PATH);
        columnBeans.add(new ColumnBean("美景记事", getResources().getColor(R.color.bluish_white), false));
        columnBeans.add(new ColumnBean("历史典故", getResources().getColor(R.color.apricot_yellow), false));
        columnBeans.add(new ColumnBean("奇闻趣事", getResources().getColor(R.color.pea_green), false));
        columnBeans.add(new ColumnBean("校园轶事", getResources().getColor(R.color.reddish_orange), false));
        columnBeans.add(new ColumnBean("美景记事", getResources().getColor(R.color.reddish_orange), false));
        columnBeans.add(new ColumnBean("历史典故", getResources().getColor(R.color.apricot_yellow), false));
        columnBeans.add(new ColumnBean("奇闻趣事", getResources().getColor(R.color.bluish_white), false));
        columnBeans.add(new ColumnBean("校园轶事", getResources().getColor(R.color.pea_green), false));

        GridView gridView = (GridView) findViewById(R.id.column_grid_view);
        mPhotoView = (CircleImageView) findViewById(R.id.civ_user_head);
        mEnterGreatWorld = (Button) findViewById(R.id.btn_enter_great_world);
        mEnterGreatWorld.setOnClickListener(this);
        ColumnAdapter columnAdapter = new ColumnAdapter(columnBeans, this);
        mPhotoView.setOnClickListener(this);
        gridView.setAdapter(columnAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (data != null) {
                beginCrop(data.getData());
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }


    public void setCrop() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 111);
    }

    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == RESULT_OK && result != null) {
            mPhotoView.setImageBitmap(BitmapFactory.decodeFile(ICON_PATH));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(ICON_PATH));
        Intent cropIntent = new Intent();
        cropIntent.setData(source);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
        cropIntent.setClass(this, CropImageActivity.class);
        startActivityForResult(cropIntent, Crop.REQUEST_CROP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_user_head:
                setCrop();
                break;
            case R.id.btn_enter_great_world:
                ActivityHelper.enterMainActivity(this);
                break;
        }
    }

    class ColumnAdapter extends BaseAdapter {
        List<ColumnBean> mColumnBeans;
        Context mContext;

        public ColumnAdapter(List<ColumnBean> columnBeans, Context context) {
            this.mColumnBeans = columnBeans;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mColumnBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return mColumnBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ColumnBean columnBean = mColumnBeans.get(position);
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.column_grid_view_item, null);
            final ChangeColorTextView columnInfoTextView = (ChangeColorTextView) view.findViewById(R.id.column_info_tv);
            columnInfoTextView.setText(columnBean.getTitle());
            columnInfoTextView.setIconColor(columnBean.getBigColor());

            columnInfoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    columnInfoTextView.changeSelectColor();
                }
            });
            return view;
        }
    }

}
