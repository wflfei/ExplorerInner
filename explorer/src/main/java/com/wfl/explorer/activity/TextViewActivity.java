package com.wfl.explorer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfl.explorer.R;
import com.wfl.explorer.base.BaseActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextViewActivity extends BaseActivity {
    private static final String TAG = "TextViewActivity";
    public static final String TEXT = "image";
    TextView textView;
    String filePath;
    Uri textUri;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);initData(getIntent());

        initData(getIntent());
        
        initViews();

    }

    private void initData(Intent intent) {
        if (!intent.hasExtra(TEXT)) {
            finish();
            return;
        }
        filePath = intent.getStringExtra(TEXT);
        if (filePath == null) {
            textUri = intent.getParcelableExtra(TEXT);
            if (textUri == null) {
                finish();
                return;
            }
            filePath = textUri.getPath();
        }
        content = readTextFile(filePath);
        
    }

    private void initViews() {
        textView = findView(R.id.view_text_tv);
        textView.setText(content);
    }
    
    private String readTextFile(String filePath) {
        String path = filePath;
        String content = "";
        File file = new File(path);
        if (file.isDirectory())
        {
            Log.d(TAG, "This File is Directory");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    while (( line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                Log.d(TAG, "This File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.d(TAG, e.getMessage());
            }
        }
        return content;
    }
}
