package com.example.advanced_tourist;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
public class Login extends AppCompatActivity {
    TabLayout t;
    ViewPager page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        t = findViewById(R.id.tab);
        page = findViewById(R.id.pager);
        t.addTab(t.newTab().setText("Login"));
        t.addTab(t.newTab().setText("Register"));
        t.setTabGravity(t.GRAVITY_FILL);
        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, t.getTabCount());
        page.setAdapter(adapter);
        page.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(t));


    }

}