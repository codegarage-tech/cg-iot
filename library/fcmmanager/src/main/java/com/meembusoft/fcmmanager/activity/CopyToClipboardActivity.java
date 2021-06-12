package com.meembusoft.fcmmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.meembusoft.fcmmanager.util.FcmUtil;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CopyToClipboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FcmUtil.copyToClipboard(CopyToClipboardActivity.this, getIntent().getStringExtra(Intent.EXTRA_TEXT));
        finish();
    }
}