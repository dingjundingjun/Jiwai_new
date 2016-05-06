package com.sounuo.jiwai.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by dj on 2015/7/18.
 * email:dingjun0225@gmail.com
 */
public class TabView extends ImageButton{
    private final int NORMAL = 0;
    private final int SELECTOR = 1;
    private final int NORMAL_FLAG = 2;
    private final int SELECTOR_FLAG = 3;
    private boolean bFlag = false;
    public TabView(Context context) {
        super(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSelector(boolean bs)
    {
        if(bFlag)
        {

        }
        else
        {
            this.setSelected(bs);
        }
    }
}
