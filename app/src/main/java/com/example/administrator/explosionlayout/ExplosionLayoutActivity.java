package com.example.administrator.explosionlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.explosionlayout.manager.ClickEffectsManager;
import com.example.administrator.explosionlayout.widget.ExplosionLayout;

public class ExplosionLayoutActivity extends AppCompatActivity {

    private static final String TAG = "ExplosionLayout";
    protected ClickEffectsManager effectsManager;
    private TextView click;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explosion_layout);
        click = findViewById(R.id.click);

        ExplosionLayout explosionLayout = (ExplosionLayout) findViewById(R.id.explosionLayout);
        effectsManager = ClickEffectsManager.newInstance(explosionLayout);


        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Log.d(TAG, "点击事件" + i);
                effectsManager.onClickEvent(v);
            }
        });
    }
}
