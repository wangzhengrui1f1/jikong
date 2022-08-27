package com.vise.bledemo.aliyun;

import android.Manifest;
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
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.vise.bledemo.Base.BaseActivity;
import com.vise.bledemo.R;
import com.vise.bledemo.baiduMap.utils.PrefStore;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import static com.vise.bledemo.aliyun.ALiUploadManager.initAlyun;


public class paizhao extends BaseActivity {
    public static final String ACCESS_ID = "LTAI5tPg3MjRLkfUJHpjWc9B";                                  //阿里云ID
    public static final String ACCESS_KEY = "4fOW348MFfa2nig0Qb6ZCE6qPbc7sh";                           //阿里云KEY
    public static final String ACCESS_BUCKET_NAME = "lingjiedian";
    public static final String ACCESS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com/";
    public static final String ACCESS_DOMAINNAME = "http:xxxxx";

//    public static final String ACCESS_ID = "LTAI5t5nHEUsjSY5EnZTjzBD";                                  //阿里云ID
//    public static final String ACCESS_KEY = "L3r0Fnd2VbVTNYcXAAIJ1N6YI2AEmI";                           //阿里云KEY
//    public static final String ACCESS_BUCKET_NAME = "lingjiedian";
//    public static final String ACCESS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com/";
//    public static final String ACCESS_DOMAINNAME = "http:xxxxx";
    List<String> lsaa =new ArrayList<>();
    private OSSClient ossClient = null;
    private static ALiUploadManager instance = null;
    private OSSClient oss;

    public static ALiUploadManager getInstance() {
        if (instance == null) {
            synchronized (ALiUploadManager.class) {
                if (instance == null) {
                    instance = new ALiUploadManager();
                }
            }
        }
        return instance;
    }

    //ImageView
    private ImageView i1;
    private String tupian1;
    //拍照
    private final int OPEN_RESULT = 1;//打开照相机
    private final int CHOOSE_PICTURE = 0; //选择相册图片
    File file55;
    Bitmap bitmap222,b3;
    private String img = "";
    TextView textView;
    String pp;
    int index=0;
    EditText e1,e2,e3;
    String filePaht="";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final OkHttpClient client = new OkHttpClient();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){

            if(msg.what==1){
                String ReturnMessage =  (String)msg.obj;
                Log.i("获取的返回信息",ReturnMessage);
                if(ReturnMessage.equals("成功")){
//                    Toast.makeText(fabu_upload.this, "添加成功！", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(fabu_upload.this,uploadShow.class);
//                    startActivity(intent);
//                    finish();
                }else{
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
        PrefStore prefStore = new PrefStore(paizhao.this);
        prefStore.savePref("awzr","aaaa");
        initAlyun(paizhao.this);
        String url2 = "https://"+ ACCESS_BUCKET_NAME + "." + ACCESS_ENDPOINT + "/" + "aaa";
        Log.e("上传阿里云成功:", url2);
        Log.e("adsad",url2);
        //实例化组件
        initView();
       // final String pdf_address = PdfUtils.ADDRESS + File.separator + "问题归纳.docx";
        TextView wanc = findViewById(R.id.textView24);
        wanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("wzra", lsaa.toString());
                for (int i = 0; i < lsaa.size(); i++) {
                    ALiUploadManager.getInstance().uploadFile(lsaa.get(i), new ALiUploadManager.ALiCallBack() {
                        @Override
                        public void onSuccess(PutObjectRequest request, PutObjectResult result, String url) {
                            Log.e("上传阿里云成功:", url);
                            Log.e("上传阿里云成功:", result.toString());
                            String url2 = "https://"+ ACCESS_BUCKET_NAME + "." + ACCESS_ENDPOINT + "/" + "aaa";
                            Log.e("上传阿里云成功:", url2);
                            TextView textView =findViewById(R.id.textView24);
                            textView.setText(result.toString());
                            Toast.makeText(paizhao.this, ""+url, Toast.LENGTH_SHORT).show();
                            // putImage(url);  //路径回调至服务端
                        }

                        @Override
                        public void onError(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                            Log.e("上传阿里云失败clientExcepion:",
                                    clientExcepion.getMessage() + ",serviceException:" + serviceException);
//                ToastUtil.showShortToast(ImageSubmitActivity.this, "上传失败");
                            Toast.makeText(paizhao.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void process(long currentSize, long totalSize) {
                            Log.e("上传中:", (currentSize * 100) / totalSize + "%");

                        }
                    });

                }
            }
        });



    }
        //组件初始化
    private void initView() {
        //ImageView
        i1 = (ImageView) findViewById(R.id.caipu_i1);
        Glide.with(paizhao.this).load("https://lingjiedian.oss-cn-beijing.aliyuncs.com/portrait/202206/b6069bfe8fea61676fd211780e0ee6ce.jpg").into(i1);
        /**
         * 图片点击弹出自定义选择框
         */
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                startActivityForResult(intent, CHOOSE_PICTURE);
            }
        });
    }

        /**
         *选择图片后上传并显示在imgView
         */
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            switch (requestCode) {
                //拍照获得图片
                case OPEN_RESULT:
                    if (resultCode == RESULT_OK) {
                        try {
                            Bundle bundle = data.getExtras();
                            bitmap222 = (Bitmap) bundle.get("data");
                            i1.setImageBitmap(bitmap222);

                            //将拍照获得的bitmap转换成图片文件并保存到本地
                            File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() +"aa"+ ".jpg");
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
                            filePaht =path;
                            lsaa.add(path);
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
    public Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView s1 = (ImageView) findViewById(R.id.imageView);
        s1.setImageBitmap(bitmap);
        saveImageToGallery(paizhao.this,bitmap);
       // toastShow("成功上传"+index+"张照片！");
        return bitmap;
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "jikong2313321");
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


    /**
     * 上传图片到阿里云
     *
     * @param filePath 本地图片地址
     */
    public OSSAsyncTask uploadFile(String filePath, final ALiUploadManager.ALiCallBack callBack) {
        // 构造上传请求
        final String key = getObjectPortraitKey(filePath);
        Log.e("key:" , key);
        PutObjectRequest put = new PutObjectRequest(ACCESS_BUCKET_NAME, key, filePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if (callBack != null) {
                    callBack.process(currentSize, totalSize);
                    Log.e("yibua","异步");
                }
            }
        });

        final OSSAsyncTask task = ossClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (callBack != null) {
                    //获取可访问的url
                    String url = ossClient.presignPublicObjectURL(ACCESS_BUCKET_NAME, key);
                    callBack.onSuccess(request, result, url);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode" , serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage" , serviceException.getRawMessage());
                }
                if (callBack != null) {
                    callBack.onError(request, clientExcepion, serviceException);
                }
            }
        });
        return task;
    }

    //格式: portrait/201805/sfdsgfsdvsdfdsfs.jpg
    private static String getObjectPortraitKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    /**
     * 获取时间
     *
     * @return 时间戳 例如:201805
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    public interface ALiCallBack {

        /**
         * 上传成功
         *
         * @param request
         * @param result
         */
        void onSuccess(PutObjectRequest request, PutObjectResult result, String url);

        /**
         * 上传失败
         *
         * @param request
         * @param clientExcepion
         * @param serviceException
         */
        void onError(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException);

        /**
         * 上传进度
         *
         * @param currentSize 当前进度
         * @param totalSize   总进度
         */
        void process(long currentSize, long totalSize);

    }

}

