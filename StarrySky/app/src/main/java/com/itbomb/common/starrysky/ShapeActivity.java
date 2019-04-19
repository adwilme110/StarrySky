package com.itbomb.common.starrysky;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.itbomb.space.backgroundlib.BackgroundLibrary;

/**
 * @author A.D.Wilme
 * @date on 2019/4/18  13:32
 * @email A.D.Wilme@gmail.com
 * @describe BackgroundLibrary
 */

@Route(path="/StarrySky1/ShapeActivity")
public class ShapeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);
    }
}
