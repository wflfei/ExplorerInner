package com.wfl.explorer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.wfl.explorer.R;
import com.wfl.explorer.base.BaseActivity;

import java.io.File;

public class ImageViewActivity extends BaseActivity {
    public static final String IMAGE = "image";
    ImageView imageView;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        
        initData(getIntent());
        
        initViews();
        
    }
    
    private void initData(Intent intent) {
        if (!intent.hasExtra(IMAGE)) {
            finish();
            return;
        }
        imageUri = intent.getParcelableExtra(IMAGE);
        if (imageUri == null) {
            String path = intent.getStringExtra(IMAGE);
            if (TextUtils.isEmpty(path)) {
                finish();
                return;
            }
            imageUri = Uri.fromFile(new File(path));
        }
    }

    private void initViews() {
        imageView = findView(R.id.view_image_img);
        imageView.setImageURI(imageUri);
    }


}
