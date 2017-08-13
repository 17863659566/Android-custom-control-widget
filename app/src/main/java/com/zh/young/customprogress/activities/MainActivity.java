package com.zh.young.customprogress.activities;

import android.media.AudioManager;
import android.media.VolumeProvider;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.zh.young.customprogress.R;
import com.zh.young.customprogress.widget.CircleProgressView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private CircleProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = (CircleProgressView) findViewById(R.id.cpv);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return progressView.dispatchKeyEvent(event);
    }
}
