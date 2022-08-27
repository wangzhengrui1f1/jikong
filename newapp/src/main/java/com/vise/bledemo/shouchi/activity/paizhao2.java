package com.vise.bledemo.shouchi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vise.bledemo.R;

import java.io.File;
import java.io.IOException;


public class paizhao2 extends Activity {
    // 调用相机拍照的请求码
    public static final int REQUEST_TAKE_PHOTO_CODE = 1;
    public static final int REQUEST_TAKE_PHOTO_CODE2 = 2;
    // 拍照后显示图片的ImageView
    private ImageView imageView;
    // 保存图片的文件
    private File imageFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpaizhao);

        imageView = (ImageView) findViewById(R.id.imageView11);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照后获取返回值，这里获取到的是原始图片
        if (requestCode == REQUEST_TAKE_PHOTO_CODE
                && resultCode == Activity.RESULT_OK) {
            // 获取到了拍照后的图片文件，从文件解码出Bitmap对象
            if (imageFile.exists()) {
                // 这里直接decode了图片，没有判断图片大小，没有对可能出现的OOM做处理
                Bitmap bm = BitmapFactory.decodeFile(imageFile
                        .getAbsolutePath());
                // 显示图片
                imageView.setImageBitmap(bm);
            } else {
                Toast.makeText(this, "图片文件不存在", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == REQUEST_TAKE_PHOTO_CODE2
                && resultCode == Activity.RESULT_OK){
            //这里获取到的是缩放后的图片，不是原始图片
            Bundle b = data.getExtras();
            if(b != null){
                Bitmap bm = (Bitmap) b.get("data");
                if(bm != null){
                    imageView.setImageBitmap(bm);
                }
            }else{
                Toast.makeText(this, "没有获取数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 判断是否有SD卡
     *
     * @return 有SD卡返回true，否则false
     */
    private boolean hasSDCard() {
        // 获取外部存储的状态
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 有SD卡
            return true;
        }
        return false;
    }

    /**
     * 初始化存储图片的文件
     *
     * @return 初始化成功返回true，否则false
     */
    private boolean initImageFile() {
        // 有SD卡时才初始化文件
        if (hasSDCard()) {
            // 构造存储图片的文件的路径，文件名为当前时间
            String filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/"
                    + System.currentTimeMillis()
                    + ".png";
            imageFile = new File(filePath);
            if (!imageFile.exists()) {// 如果文件不存在，就创建文件
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 跳转到系统相机拍照
     */
    private void startTakePhoto() {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置拍照后保存的图片存储在文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        // 启动activity并获取返回数据
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
    }

    private void startTakePhoto2() {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 启动activity并获取返回数据
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE2);
    }

    // 点击按钮后调用相机拍照
    public void btnclick(View view) {
        // 如果初始化文件成功，则调用相机
        if (initImageFile()) {
            //下面用第一种方式获取图片，还可以调用startTakePhoto2()方法获取图片
            //       startTakePhoto();
            startTakePhoto2();
        } else {
            Toast.makeText(this, "初始化文件失败，无法调用相机拍照！", Toast.LENGTH_SHORT)
                    .show();
        }
    }

}