package com.vise.bledemo.aliyun;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.vise.bledemo.utils.url.alyima1;
import static com.vise.bledemo.utils.url.alyima2;

public class ALiUploadManager {
    public static final String ACCESS_ID = "LTAI5tPg3MjRLkfUJHpjWc9B";                                  //阿里云ID
    public static final String ACCESS_KEY = "4fOW348MFfa2nig0Qb6ZCE6qPbc7sh";                           //阿里云KEY
    public static final String ACCESS_BUCKET_NAME = "vectorset";
    public static final String ACCESS_ENDPOINT = "https://oss-cn-beijing.aliyuncs.com/";
    public static final String ACCESS_DOMAINNAME = "http:xxxxx";

//    public static final String ACCESS_ID = "LTAI5t5nHEUsjSY5EnZTjzBD";                                  //阿里云ID
//        public static final String ACCESS_KEY = "L3r0Fnd2VbVTNYcXAAIJ1N6YI2AEmI";                           //阿里云KEY
//        public static final String ACCESS_BUCKET_NAME = "lingjiedian";
//        public static final String ACCESS_ENDPOINT = "https://oss-cn-beijing.aliyuncs.com/";
//        public static final String ACCESS_DOMAINNAME = "http:xxxxx";

    public static OSSClient ossClient = null;
    private static ALiUploadManager instance = null;

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

    /**
     * 初始化阿里云SDK
     *
     * @param context
     */
    public static void initAlyun(Context context) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESS_ID, ACCESS_KEY);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000);               // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000);                   // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8);                    // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(3);                           // 失败后最大重试次数，默认2次
        // oss为全局变量，OSS_ENDPOINT是一个OSS区域地址
        ossClient = new OSSClient(context, ACCESS_ENDPOINT, credentialProvider, conf);
    }

    /**
     * 上传图片到阿里云
     *
     * @param filePath 本地图片地址
     */
    public OSSAsyncTask uploadFile(String filePath, final ALiCallBack callBack) {
        // 构造上传请求
        final String key = getObjectPortraitKey(filePath);
        Log.e("aaakey:", key);
        Log.e("aliy", "uploadFile");
        PutObjectRequest put = new PutObjectRequest(ACCESS_BUCKET_NAME, key, filePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if (callBack != null) {
                    callBack.process(currentSize, totalSize);
                }
            }
        });

        //  Process: com.vise.bledemo, PID: 21194
        //    java.lang.NullPointerException: Attempt to invoke virtual method 'com.alibaba.sdk.android.oss.internal.OSSAsyncTask com.alibaba.sdk.android.oss.OSSClient.asyncPutObject(com.alibaba.sdk.android.oss.model.PutObjectRequest, com.alibaba.sdk.android.oss.callback.OSSCompletedCallback)' on a null object reference
        OSSAsyncTask task = null;
        try {
            task = ossClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
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
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                    }
                    if (callBack != null) {
                        callBack.onError(request, clientExcepion, serviceException);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("aliy--Exception", "aaa");
        }
        Log.e("aliy--Exception", "bbb");
        return task;
    }

    //格式: portrait/201805/sfdsgfsdvsdfdsfs.jpg
    private static String getObjectPortraitKey(String path) {
        Random random = new Random();
        String fileMd5 = "LJD" + random.nextInt(90000) + "t" + random.nextInt(90000) + "s" + disposeTime3();
        String dateString = getDateString();
        String uppath = String.format("jikong/%s/%s.jpg", dateString, fileMd5);
        Log.e("asshangchuan", uppath);
        if (alyima1.equals("create")) {
            alyima1 = uppath;
        } else {
            alyima2 = uppath;
        }
        Log.e("assttttttttt", alyima1 + "---" + alyima2);
        Log.e("aliy", "getObjectPortraitKey");
        return uppath;
    }

    private static String getObjectPortraitKeymp4(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.mp4", dateString, fileMd5);
    }

    /**
     * 获取时间
     *
     * @return 时间戳 例如:201805
     */
    private static String getDateString() {
        return DateFormat.format("yyyy/MM/dd", new Date()).toString();
    }

    //todo 加载时间
    public static String disposeTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(c.getTime());
        return time;
    }

    //todo 加载时间
    public static String disposeTime3() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
        String time = sdf.format(c.getTime());
        return time;
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
