package com.solarexsoft.solarexbutterknifedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.solarexsoft.BindView;
import com.solarexsoft.solarexbutterknife.InjectView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectView.bind(this);
        Log.d("Solarex", "onCreate: tv = " + tv );
    }
}
