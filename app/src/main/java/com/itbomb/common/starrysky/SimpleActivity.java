package com.itbomb.common.starrysky;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.itbomb.space.permission.PermissionCheckActivity;


public class SimpleActivity extends ListActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.数据源
        String[] data = getResources().getStringArray(R.array.simple_list_name);
        //2.适配器
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        //3.绑定
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position){
            case 0:
                PermissionCheckActivity.checkPermission(this, Manifest.permission.CAMERA);
                break;
            case 1:
                ARouter.getInstance().build("/StarrySky1/ShapeActivity").navigation(SimpleActivity.this);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionCheckActivity.REQUEST_CODE){
            if (resultCode == PermissionCheckActivity.RESULT_CODE_SUCCESS){
                Toast.makeText(this, "权限请求成功", Toast.LENGTH_SHORT).show();
            }else  if (resultCode == PermissionCheckActivity.RESULT_CODE_FAIL){
                Toast.makeText(this, "权限请求失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
