package com.android.wenxiangli.modern;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends ActionBarActivity {
    TextView tv_product, tv_model, tv_storage, tv_ram, tv_build_version,
            tv_androidversion, tv_kernel_version, tv_firmware_version,
            tv_usb_debug, tv_android_version;
    String product, model, storage, ram, build_version, androidversion,
            kernel_version, firmware_version;
    public static int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 设置窗口风格为顶部显示Actionbar
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);
        // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，

        try {
            ViewConfiguration config = ViewConfiguration.get(this);

            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("这是啥");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
        initView();
        getData();
        setData();
        setListen();
    }

    private void setListen() {
        tv_usb_debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "咦，终于被你发现了~",
                        Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent("android.intent.action.MAIN");
                ComponentName comp = new ComponentName("com.android.settings",
                        "com.android.settings.DevelopmentSettings");
                mIntent.setComponent(comp);
                mIntent.addCategory("android.intent.category.LAUNCHER");
                startActivity(mIntent);// 启动
            }
        });
        tv_android_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "咦，终于被你发现了~",
                        Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent("android.intent.action.MAIN");
                ComponentName comp = new ComponentName("com.android.settings",
                        "com.android.settings.DevelopmentSettings");
                mIntent.setComponent(comp);
                mIntent.addCategory("android.intent.category.LAUNCHER");
                startActivity(mIntent);// 启动
            }
        });
    }

    private void setData() {
        tv_product.setText(product);
        tv_model.setText(model);
        tv_storage.setText(storage);
        tv_ram.setText(ram);
        tv_build_version.setText(build_version);
        tv_androidversion.setText(androidversion);
        tv_kernel_version.setText(kernel_version);
        tv_firmware_version.setText(firmware_version);
    }

    private void getData() {
        product = getProuduct();
        model = getModel();
        storage = getStorage();
        ram = getRam();
        build_version = getModernVersion();
        // patchlevel = getPatchLevel();
        androidversion = getVersion();
        kernel_version = getKernelVersion();
        firmware_version = getBaseBand();
    }

    private String getVersion() {
        return Build.VERSION.RELEASE;
    }

    private String getRam() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(getBaseContext(), mi.totalMem);
    }

    private String getModel() {
        return Build.MODEL;
    }

    private String getProuduct() {
        return Build.MANUFACTURER;
    }

    private void initView() {
        tv_product = (TextView) findViewById(R.id.tv_device_name);
        tv_model = (TextView) findViewById(R.id.tv_model);
        tv_storage = (TextView) findViewById(R.id.tv_storage_memory);
        tv_ram = (TextView) findViewById(R.id.tv_ram_memory);
        tv_build_version = (TextView) findViewById(R.id.tv_build_version);
        tv_androidversion = (TextView) findViewById(R.id.tv_android_version);
        tv_kernel_version = (TextView) findViewById(R.id.tv_kernel_version);
        tv_firmware_version = (TextView) findViewById(R.id.tv_firmware_version);
        tv_usb_debug = (TextView) findViewById(R.id.usb_debug);
        tv_android_version = (TextView) findViewById(R.id.tv_android_version);
    }

    public void Status(View view) {
        Intent mIntent = new Intent("android.intent.action.MAIN");
        ComponentName comp = new ComponentName("com.android.settings",
                "com.android.settings.deviceinfo.Status");
        mIntent.setComponent(comp);
        mIntent.addCategory("android.intent.category.LAUNCHER");
        startActivity(mIntent);// 启动
    }

    public String getStorage() {
        // 获得手机内部存储控件的状态
        File dataFileDir = Environment.getDataDirectory();
        String dataMemory = getMemoryInfo(dataFileDir);
        return dataMemory;
    }

    private String getMemoryInfo(File path) {
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize(); // 获得一个扇区的大小
        long totalBlocks = stat.getBlockCount(); // 获得扇区的总数
        // 总空间
        String totalMemory = Formatter.formatFileSize(this, totalBlocks
                * blockSize);
        return totalMemory;
    }

    public String getModernVersion() {
        String all = ReadTxtFile("/etc/change.log");
        if (all.length() > 10) {
            String arr[] = all.split("#");
            return arr[0].substring(6, arr[0].length()).trim();
        } else {
            return "未知";
        }

    }

    public String getKernelVersion() {
        String kernelVersion = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return kernelVersion;
        }
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream), 8 * 1024);
        String info = "";
        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                info += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (info != "") {
                final String keyword = "version ";
                int index = info.indexOf(keyword);
                line = info.substring(index + keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    public String getBaseBand() {
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class,
                    String.class});
            Object result = m.invoke(invoker, new Object[]{
                    "gsm.version.baseband", "no message"});
            // System.out.println(">>>>>>><<<<<<<" +(String)result);
            Version = (String) result;
        } catch (Exception e) {
        }
        return Version.substring(3, Version.length());
    }

    public void getChanglog(View view) {
        Intent intent = new Intent(this, ChangeLogActivty.class);
        startActivity(intent);

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

    public void getAboutModern(View view) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("http://www.modernmod.cn/"));
        intent.setAction(Intent.ACTION_VIEW);
        this.startActivity(intent); // 启动浏览器访问modern官网
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void rename(View view) {
        // 向sharedpresf写入当前CheckBox的状态
        // 1.获取sharedprefs对象实例
        EditText text = (EditText) view;
        text.setText(((TextView) view).getText().toString());

    }
}
