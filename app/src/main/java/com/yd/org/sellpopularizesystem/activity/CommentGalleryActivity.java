package com.yd.org.sellpopularizesystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bosong.commentgallerylib.CommentGallery;
import com.bosong.commentgallerylib.CommentGalleryContainer;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;

public class CommentGalleryActivity extends AppCompatActivity {
    private CommentGallery mGallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_gallery);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        mGallery= (CommentGallery) findViewById(R.id.comment_gallery);
        mGallery.setData((CommentGalleryContainer) getIntent().getSerializableExtra(BuildingPlanActivity.COMMENT_LIST),
                getIntent().getExtras().getInt(BuildingPlanActivity.CLICK_INDEX));
    }
}
