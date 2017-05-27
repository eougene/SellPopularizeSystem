package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        /*Bundle bundle = getIntent().getExtras();
        prs= (ProductDetailBean.ResultBean) bundle.getSerializable("prs");*/
      // playerStandard.setUp((String) prs.getVideo_url(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"123");
        //playerStandard.thumbImageView.setImageURI(Uri.parse());
        //视频url
        String videoUrl="http://www.maclandgroup.com/video/2016.10.18_Macland_Video_2016.mp4";
        //设置视频地址
        playerStandard.setUp(videoUrl,JCVideoPlayerStandard.SCREEN_LAYOUT_LIST,getString(R.string.vp));
        //直接进入全屏
        playerStandard.startFullscreen(this, JCVideoPlayerStandard.class, videoUrl, "");
        //点击播放按钮播放
        playerStandard.startButton.performClick();
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
