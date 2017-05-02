package com.yd.org.sellpopularizesystem.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity {
    private JCVideoPlayerStandard playerStandard;
    private ProductDetailBean.ResultBean prs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        playerStandard= (JCVideoPlayerStandard) findViewById(R.id.video);
        Bundle bundle = getIntent().getExtras();
        prs= (ProductDetailBean.ResultBean) bundle.getSerializable("prs");
       playerStandard.setUp((String) prs.getVideo_url(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"123");
        //playerStandard.thumbImageView.setImageURI(Uri.parse());
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()){
                return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
