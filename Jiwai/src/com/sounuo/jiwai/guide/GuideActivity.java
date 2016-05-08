package com.sounuo.jiwai.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;

import com.sounuo.jiwai.R;
import com.sounuo.jiwai.guide.fragment.CustomPresentationPagerFragment;
import com.sounuo.jiwai.utils.ActivityHelper;
import com.sounuo.jiwai.utils.AppConstant;
import com.sounuo.jiwai.utils.SharedPrefUtil;
import com.sounuo.jiwai.utils.StorageUtil;

/**
 * Created by gq on 2016/5/6.
 */
@SuppressLint("NewApi")
public class GuideActivity extends FragmentActivity {
    private static final int DELAY_TIME = 1000;
    private static final long MIN_SD_SIZE_MB = 40;
    private boolean isRest = false;
    private boolean isEnter = false;
    private boolean isFirstEnter = false;

    @Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstEnter = SharedPrefUtil.getBoolean(this, AppConstant.IS_FIRST_ENTER_APP, true);
        setContentView(R.layout.activity_guide);
    }

    private void initData() {
        // check available space
        if (StorageUtil.getAvailableExternalMemorySize() < MIN_SD_SIZE_MB) {
            return;
        }

        if (isEnter) {
            return;
        }
        if (isFirstEnter) {
            checkEnter();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkEnter();
                }
            }, DELAY_TIME);
        }
    }

    private void checkEnter() {
        if (isRest) {
            return;
        }
        isEnter = true;
        if (isFirstEnter) {
            replaceTutorialFragment();
        } else {
            enterHome();
        }
    }

    private void replaceTutorialFragment() {
        CustomPresentationPagerFragment customPresentationPagerFragment = new CustomPresentationPagerFragment();
        customPresentationPagerFragment.setGuideListener(new GuideListener() {
            @Override
            public void needFinish() {
                SharedPrefUtil.putBoolean(getApplicationContext(), AppConstant.IS_FIRST_ENTER_APP, false);
                enterHome();
            }
        });
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, customPresentationPagerFragment);
        fragmentTransaction.commit();
    }


    private void enterHome() {
        ActivityHelper.enterMainActivity(this);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRest = false;
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRest = true;
    }
}