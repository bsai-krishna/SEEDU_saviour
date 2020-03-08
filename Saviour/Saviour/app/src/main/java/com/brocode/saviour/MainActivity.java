package com.brocode.saviour;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),this);
        ViewPager viewPager=findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
