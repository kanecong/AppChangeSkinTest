package com.example.changeskintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button changeSkin;
    private Button resetSkin;
    private ImageView imageSkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeSkin = findViewById(R.id.changeBtn);
        resetSkin = findViewById(R.id.resetBtn);
        imageSkin = findViewById(R.id.imageSkin);
        changeSkin.setOnClickListener(this);
        resetSkin.setOnClickListener(this);
    }

    public void startChangeSkin() {
        try (
            // 创建AssetManager
            AssetManager assetManager = AssetManager.class.newInstance()
        ) {
            // 反射调用 创建AssetManager#addAssetPath
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);
//            // 获取到当前apk在手机中的路径
//            String path = getApplicationContext().getPackageResourcePath();
//            Log.i("gaorui", "startChangeSkin - path = " + path);

            String path = getFilesDir().getPath() + File.separator + "skin_package-debug.apk";

            /// 反射执行方法
            method.invoke(assetManager, path);
            // 创建自己的Resources
            Resources resources = new Resources(assetManager, createDisplayMetrics(), createConfiguration());

//            // 根据id来获取图片
//            Drawable drawable = resources.getDrawable(R.drawable.ic_launcher_background, null);
//            // 设置图片
//            imageSkin.setImageDrawable(drawable);

            /// 加载drawable
            int drawableId = resources.getIdentifier("image", "drawable", "com.example.skin_package");

            imageSkin.setImageDrawable(resources.getDrawable(drawableId, null));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetOriginSkin() {
        imageSkin.setImageDrawable(getResources().getDrawable(R.drawable.image));
    }

    // 这些关于屏幕的就用原来的就可以
    public DisplayMetrics createDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    public Configuration createConfiguration() {
        return getResources().getConfiguration();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changeBtn:
                startChangeSkin();
                break;
            case R.id.resetBtn:
                resetOriginSkin();
                break;
        }
    }
}