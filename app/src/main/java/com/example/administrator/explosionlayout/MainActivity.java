package com.example.administrator.explosionlayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView explosionLayout;
    private TextView clickEventLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        explosionLayout = findViewById(R.id.explosionLayout);
        clickEventLayout = findViewById(R.id.clickEventLayout);
        explosionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ExplosionLayoutActivity.class));
            }
        });
        clickEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ClickEventLayoutActivity.class));
            }
        });
    }
}
