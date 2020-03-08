package com.brocode.saviour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DiseaseDetection extends AppCompatActivity {

    TextView tv_d,tv_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detection);
        tv_d=findViewById(R.id.tvd);
        tv_a=findViewById(R.id.tva);

        String disease=getIntent().getStringExtra("disease");
        String accuracy=getIntent().getStringExtra("accuracy");

        tv_d.setText(disease);
        tv_a.setText(accuracy);

    }
}
