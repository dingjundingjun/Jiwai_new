package com.sounuo.jiwai.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sounuo.jiwai.R;

@SuppressLint("NewApi")
public class ItemLinearLayout extends LinearLayout {

	private TextView itemName;
	private TextView itemNum;
	private ImageView itemIv;
    private boolean bFlag = false;

	public ItemLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		View inflate = LayoutInflater.from(context).inflate(R.layout.item_linear_layout, this, true);
		itemName = (TextView) inflate.findViewById(R.id.item_name);
		itemNum = (TextView) inflate.findViewById(R.id.item_num);
		itemIv = (ImageView) inflate.findViewById(R.id.item_iv);
        // 获取设置的图标
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ItemLinearLayout);
		
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {

            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ItemLinearLayout_item_name_text:
            		CharSequence nameText = a.getText(attr);
            		if (nameText != null)
            			itemName.setText(nameText);
                    break;
                case R.styleable.ItemLinearLayout_item_num_text:
            		CharSequence numText = a.getText(attr);
            		if (numText != null)
            			itemNum.setText(numText); 
                    break;
                case R.styleable.ItemLinearLayout_item_select:
                	Boolean select = a.getBoolean(attr, false);
                	if (select != null)
                		itemIv.setSelected(select); 
                	break;
            }
        }
        
		a.recycle();
	}

	public ItemLinearLayout(Context context) {
		super(context);
	}
	
//    /**
//     * Call this to force a view to update its drawable state. This will cause
//     * drawableStateChanged to be called on this view. Views that are interested
//     * in the new state should call getDrawableState.
//     *
//     * @see #drawableStateChanged
//     * @see #getDrawableState
//     */
//    public void refreshDrawableState() {
//        mPrivateFlags |= PFLAG_DRAWABLE_STATE_DIRTY;
//        drawableStateChanged();
//
//        ViewParent parent = mParent;
//        if (parent != null) {
//            parent.childDrawableStateChanged(this);
//        }
//    }
	
	public void setItemNameText(String text) {    
		itemName.setText(text);   
	}    
	
	public void setSelected(Boolean selected) {    
		itemIv.setSelected(selected);
	}    
	
    public void setSelector(boolean bs)
    {
        if(bFlag)
        {

        }
        else
        {
        	itemIv.setSelected(bs);
        }
    }

	public void setItemNumText(String text) {    
		itemNum.setText(text);    
	} 

}
