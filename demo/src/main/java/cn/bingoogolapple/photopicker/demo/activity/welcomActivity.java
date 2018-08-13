package cn.bingoogolapple.photopicker.demo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.bingoogolapple.photopicker.demo.R;

public class welcomActivity extends AppCompatActivity {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView reg;
    ImageView log;
    AnimatorSet set3 = new AnimatorSet();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        startAnimation();
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(welcomActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(welcomActivity.this,RegActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void initViews() {
        mImageView1 = (ImageView) findViewById(R.id.image1);
        mImageView2 = (ImageView) findViewById(R.id.image2);
        mImageView3 = (ImageView) findViewById(R.id.image3);
        log=(ImageView)findViewById(R.id.btn_login);
        reg=(ImageView)findViewById(R.id.btn_register);
    }


    private void startAnimation() {
        ObjectAnimator anim1 = new ObjectAnimator().ofFloat(mImageView1, "alpha", 1f, 0f).setDuration(5000);
        ObjectAnimator anim2 = new ObjectAnimator().ofFloat(mImageView2, "alpha", 0f, 1f).setDuration(5000);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim1, anim2);

        ObjectAnimator anim3 = new ObjectAnimator().ofFloat(mImageView2, "alpha", 1f, 0f).setDuration(5000);
        ObjectAnimator anim4 = new ObjectAnimator().ofFloat(mImageView3, "alpha", 0f, 1f).setDuration(5000);
        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(anim3, anim4);

        ObjectAnimator anim5 = new ObjectAnimator().ofFloat(mImageView3, "alpha", 1f, 0f).setDuration(5000);
        ObjectAnimator anim6 = new ObjectAnimator().ofFloat(mImageView1, "alpha", 0f, 1f).setDuration(5000);
        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(anim5, anim6);
        set3.playSequentially(set, set1, set2);
        set3.addListener(new AnimatorListenerAdapter() {

            private boolean mCanceled;

            @Override
            public void onAnimationStart(Animator animation) {
                mCanceled = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mCanceled) {
                    animation.start();
                }
            }

        });
        set3.start();
    }
}
