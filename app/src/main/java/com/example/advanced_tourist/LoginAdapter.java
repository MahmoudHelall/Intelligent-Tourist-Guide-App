package com.example.advanced_tourist;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    int tabs;
    public LoginAdapter(FragmentManager fm,Context context, int tabs) {
        super(fm);
        this.context = context;
        this.tabs = tabs;
    }
    @Override
    public int getCount() {
        return tabs;
    }

    public Fragment getItem(int Position)
    {
        switch (Position)
        {
            case 0:
                LoginFragment loginFragment=new LoginFragment();
                return loginFragment;
            case 1:
                RegisterFragment registerFragment=new RegisterFragment();
                return registerFragment;
            default:
                return null;
        }
    }
}
