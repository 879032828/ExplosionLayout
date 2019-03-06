package com.example.administrator.explosionlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.explosionlayout.interfaces.DoubleClickCallback;
import com.example.administrator.explosionlayout.interfaces.SingleClickCallback;
import com.example.administrator.explosionlayout.widget.ClickEventLayout;

public class ClickEventLayoutActivity extends AppCompatActivity {

    private static final String TAG = "ClickEventLayout";

    public ClickEventLayout clickEventLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_event_layout);
        initView();
        initListner();
    }

    private void initView() {
        clickEventLayout = findViewById(R.id.clicklayout);
    }

    private void initListner() {
        clickEventLayout.setDoubleClickCallback(new DoubleClickCallback() {
            @Override
            public void onDoubleClick(View view, float x, float y) {
                Log.d(TAG, "双击事件回调");
            }
        });

        clickEventLayout.setSingleClickCallback(new SingleClickCallback() {
            @Override
            public void onSingleClick(View view) {
                Log.d(TAG, "单击事件回调");
            }
        });
    }
}
