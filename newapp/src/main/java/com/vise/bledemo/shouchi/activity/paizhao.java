package com.vise.bledemo.shouchi.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.bledemo.Base.BaseActivity;
import com.vise.bledemo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import static com.vise.bledemo.shouchi.activity.paizhao2.REQUEST_TAKE_PHOTO_CODE2;


public class paizhao extends BaseActivity {
    //ImageView
    private ImageView i1;
    private String tupian1;
    //拍照
    private final int OPEN_RESULT = 1;//打开照相机
    private final int CHOOSE_PICTURE = 0; //选择相册图片
    File file55;
    Bitmap bitmap222, b3;
    private String img = "";
    TextView textView;
    String pp;
    int index = 0;
    EditText e1, e2, e3;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final OkHttpClient client = new OkHttpClient();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                Log.i("获取的返回信息", ReturnMessage);
                if (ReturnMessage.equals("成功")) {
//                    Toast.makeText(fabu_upload.this, "添加成功！", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(fabu_upload.this,uploadShow.class);
//                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(paizhao.this, "添加失败,图片过大！", Toast.LENGTH_SHORT).show();
                }
            }

        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ima);
        textView = findViewById(R.id.textView24);
        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        e3 = findViewById(R.id.e3);
        //实例化组件
        initView();
        //permissiongen();
        Button wanc = findViewById(R.id.button3);
        wanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void startTakePhoto2() {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 启动activity并获取返回数据
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE2);
    }

    //组件初始化
    private void initView() {
        //ImageView
        i1 = (ImageView) findViewById(R.id.caipu_i1);

        /**
         * 图片点击弹出自定义选择框
         */
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startTakePhoto2();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                startActivityForResult(intent, OPEN_RESULT);
            }
        });
    }

    /**
     * 选择图片后上传并显示在imgView
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //拍照获得图片
            case OPEN_RESULT:
                if (resultCode == RESULT_OK) {
                    try {
                        Bundle bundle = data.getExtras();
                        bitmap222 = (Bitmap) bundle.get("data");
                        i1.setImageBitmap(bitmap222);

                        //将拍照获得的bitmap转换成图片文件并保存到本地
                        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "aa" + ".jpg");
                        FileOutputStream fileOutStream = new FileOutputStream(file);
                        try {
                            bitmap222.compress(Bitmap.CompressFormat.JPEG, 100, fileOutStream);
                            fileOutStream.flush();
                            fileOutStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("tag", e.getMessage());
                        Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //相册选择图片
            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK) {

                    try {
                        Uri uri = data.getData();
                        b3 = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        bitmaptoString();
                        //  Toast.makeText(this,sa , Toast.LENGTH_SHORT).show();
                        String path = com.example.zhihuixiaoyuan.utils.RealPathFromUriUtils.getRealPathFromUri(this, uri);
                        // Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                        file55 = new File(path);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("tag", e.getMessage());
                        // Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public String bitmaptoString() {
        // i1 = (ImageView) findViewById(R.id.caipu_i1);
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.id.caipu_i1);
        //将Bitmap转换成字符串
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        b3.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        pp = Base64.encodeToString(bytes, Base64.DEFAULT);

        stringtoBitmap(pp);
        //textView.setText(string.length()+"111"+string);
        return pp;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView s1 = (ImageView) findViewById(R.id.imageView);
        s1.setImageBitmap(bitmap);
        saveImageToGallery(paizhao.this, bitmap);
        toastShow("成功上传" + index + "张照片！");
        return bitmap;
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "jikong" + disposeTime());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "内部存储/pictures")));

    }

}

