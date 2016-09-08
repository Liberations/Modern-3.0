package com.android.wenxiangli.modern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Liberation on 2016/9/4.
 */
public class ChangeLogActivty extends Activity {
    TextView tv1, tv2;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changelog);
        initView();
        initData();


    }

    private void initData() {

        String log = "";
        String all = ReadTxtFile("/etc/change.log");
        if (all.length() > 10) {
            String arr[] = all.split("#");
            tv1.setText(arr[0]);

            for (int i = 1; i < arr.length; i++) {
                log = log + arr[i];
            }
        } else {
            log = "未知";
        }

        tv2.setText(log);

    }

    private void initView() {
        tv1 = (TextView) findViewById(R.id.tv_version);
        tv2 = (TextView) findViewById(R.id.tv_change_log);
        iv = (ImageView) findViewById(R.id.iv);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1);
        animation.setDuration(2000);
        iv.startAnimation(animation);
    }

    private String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; // 文件内容字符串
        // 打开文件
        File file = new File(path);
        // 如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {

                if (file.exists()) {

                    BufferedReader buffreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                    String line;
                    // 分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n#";
                    }
                    buffreader.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }
}
