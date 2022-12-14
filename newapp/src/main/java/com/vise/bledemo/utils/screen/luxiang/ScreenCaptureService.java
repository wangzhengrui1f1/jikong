package com.vise.bledemo.utils.screen.luxiang;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.vise.bledemo.R;
import com.vise.bledemo.chezai.fragment.SoneFragment1;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;

import static com.vise.bledemo.utils.ViewBean.SPFilename;

public class ScreenCaptureService extends Service {

    public static final String TAG = "ScreenCaptureService";

    private MyBinder mBinder = new MyBinder();
    private int mScreenDensity;
    private int mScreenWidth;
    private int mScreenHeight;
    private static final String MIME_TYPE = "video/avc";
    private int mVideoTrackIndex;
    private int framerate = 60;
    private boolean isRun = false;
    MediaCodec.BufferInfo info;
    MediaFormat format;
    MediaCodec mediaCodec;
    private boolean VERBOS = true;
    private Surface mSurface;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MyThread myThread;

    private MediaMuxer mMediaMuxer;
    private static final String SDCARD_PATH  = Environment.getExternalStorageDirectory().getPath();
    private String FileName;
    ByteBuffer encodedData;
    private long totalSize = 0;
    int isxie = 0;
    public ScreenCaptureService() {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        Notification.Builder builder = new Notification.Builder(this);
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("??????????????????????????????");
        builder.setContentTitle("????????????????????????");
        builder.setContentText("????????????????????????????????????");
        Notification notification = builder.build();
        startForeground(1,notification);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi*2;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        Log.v(TAG,"mScreenWidth is :"+mScreenWidth+";mScreenHeight is :"+mScreenHeight);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    public class MyBinder extends Binder {
        public void StartScreenCapture(){
            //setUpMediaProjection();

            //??????????????????????????????????????????prepareEncoder();???virtualDisplay();
            prepareEncoder();
            virtualDisplay();

            myThread = new MyThread();
            myThread.start();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isxie = 1;
                }
            },10000);
            Log.v(TAG,"StartScreenCapture()");
            return;
        }

        public void StopScreenCapture(){
            isRun = false;
            Log.v(TAG,"StopScreenCapture()");
        }

    }
    class MyThread extends Thread{
        public void run(){
            try {
                Log.v("doNext","process()");
                process();
                //Thread.sleep(100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected boolean process() throws IOException {
        Log.v("process()","process() start");
        isRun = true;
        File destDir = new File(SDCARD_PATH+"/ScreenCaptureA/");
        Log.e("aapppatha",SDCARD_PATH+"/ScreenCapture/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        FileName = sDateFormat.format(new java.util.Date());
        SPFilename = destDir.toString()+"/"+FileName+".mp4";
        Log.v(TAG,"destDir is :"+destDir.toString());
        mMediaMuxer = new MediaMuxer(SPFilename, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

        try{
            while(isRun){
                Log.e("aaaawzraaa","111111111111");
                drainEncoder(false);
                Thread.sleep(100);
            }
            drainEncoder(true);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            Log.v(TAG,"totalSize is :"+totalSize);
            releaseEncoder();
        }

        return true;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void prepareEncoder(){
        info = new MediaCodec.BufferInfo();
        format = MediaFormat.createVideoFormat(MIME_TYPE,mScreenWidth,mScreenHeight);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 10*1920*1080);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        try {
            mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        mSurface = mediaCodec.createInputSurface();//mSurface?????????OpenGL????????? lockCanvas?????????
        mediaCodec.start();
        Log.d(TAG, "encoder output format not changed: " + format);
        //????????????format???SPS???PPS?????????format?????????
        //???????????????
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void releaseEncoder() {
        Log.d(TAG, "releasing encoder objects");
        if (mMediaMuxer != null) {
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mMediaMuxer = null;
        }
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }

        if(mVirtualDisplay!=null){
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        if(mMediaProjection!=null){
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void drainEncoder(boolean endOfStream) {
        final int TIMEOUT_USEC = 10000;
        //Log.d(TAG, "drainEncoder(" + endOfStream + ")");

        if (endOfStream) {
            Log.d(TAG, "sending EOS to encoder");
            mediaCodec.signalEndOfInputStream();
        }
        /*encoderOutputBuffers?????????ByteBuffer?????????
        * ByteBuffer?????????????????????
        * ??????encoderOutputBuffers?????????????????????
        * ???????????????????????????mediaCodec?????????????????????????????????
        * */
        ByteBuffer[] encoderOutputBuffers = mediaCodec.getOutputBuffers();
        while (true) {

            int encoderStatus = mediaCodec.dequeueOutputBuffer(info, TIMEOUT_USEC);//dequeue?????????encoderStatus???????????????????????????????????????
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                Log.v("drainEncoder","encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER");
                //-1
                // no output available yet
                if (!endOfStream) {
                    break;      // out of while
                } else {
                    Log.d(TAG, "no output available, spinning to await EOS");
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                //Log.v("drainEncoder","encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED");
                // not expected for an encoder
                //-3
                encoderOutputBuffers = mediaCodec.getOutputBuffers();
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                //Log.v("drainEncoder","encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED");
                /*
                * MediaCodec??????????????????dequeueOutputBuffer()??????????????????INFO_OUTPUT_FORMAT_CHANGED?????????
                * ??????????????????????????????MediaCodec???format???????????????MediaMuxer??????
                * ???????????????????????????????????????
                * */
                MediaFormat newFormat = mediaCodec.getOutputFormat();
                Log.d(TAG, "encoder output format changed: " + newFormat);

                /*
                //???newFormat?????????SPS???PPS????????????format
                //???????????????????????????format???format????????????????????????????????????
                //newFormat?????????
                ByteBuffer csd0 = newFormat.getByteBuffer("csd-0");
                ByteBuffer csd1 = newFormat.getByteBuffer("csd-1");
                format.setByteBuffer("csd-0",csd0);
                format.setByteBuffer("csd-1",csd1);
                mVideoTrackIndex = mMediaMuxer.addTrack(format);
                */
                mVideoTrackIndex = mMediaMuxer.addTrack(newFormat);//??????????????????newFormat
                mMediaMuxer.start();
                // mMuxerStarted = true;
            } else if (encoderStatus < 0) {
                //Log.v("drainEncoder","encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED");
                Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " +
                        encoderStatus);
                // let's ignore it
            } else {
                //Log.v("drainEncoder","encoderStatus > 0");
                /*encodedData???????????????encoderOutputBuffers?????????encoderStatus???
                *
                * */
                encodedData = encoderOutputBuffers[encoderStatus];
                if (encodedData == null) {
                    throw new RuntimeException("encoderOutputBuffer " + encoderStatus +
                            " was null");
                }

                if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    // The codec config data was pulled out and fed to the muxer when we got
                    // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
                    Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                    info.size = 0;
                }

                if (info.size != 0) {
                    // adjust the ByteBuffer values to match BufferInfo (not needed?)
//                    encodedData.position(info.offset);
//                    encodedData.limit(info.offset + info.size);
                    mMediaMuxer.writeSampleData(mVideoTrackIndex, encodedData, info);
                    //???encodedData????????????????????????,???????????????H.264??????????????????
                    //Log.d(TAG, "sent " + info.size + " bytes to muxer");
                }
                totalSize+=info.size;
                mediaCodec.releaseOutputBuffer(encoderStatus, false);

                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    //??????
                    if (!endOfStream) {
                        Log.w(TAG, "reached end of stream unexpectedly");
                    } else {
                        Log.d(TAG, "end of stream reached");
                    }
                    break;      // out of while
                }
            }
        }
    }
    @SuppressLint("NewApi")
    private void virtualDisplay() {
        mMediaProjection = SoneFragment1.mMediaProjection;
        if( mMediaProjection == null){
            Log.v("ScreenCaptureService","mMediaProjection is null");
        }
        Log.v(TAG, "virtual displayed");

        mMediaProjection.createVirtualDisplay("????????????",mScreenWidth, mScreenHeight,
                mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mSurface,null,null);

    }


}
