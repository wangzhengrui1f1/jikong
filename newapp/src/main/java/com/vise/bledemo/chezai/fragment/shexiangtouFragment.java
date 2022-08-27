package com.vise.bledemo.chezai.fragment;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.INT_PTR;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_JPEGPARA;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.NET_DVR_VOD_PARA;
import com.hikvision.netsdk.PTZCommand;
import com.hikvision.netsdk.PlaybackControlCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.ByteByReference;
import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.chezai.bean.SxtBean;
import com.vise.bledemo.chezai.bean.aa2;
import com.vise.bledemo.haikang.HCNetSDKByJNA;

import org.MediaPlayer.PlayM4.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.vise.bledemo.utils.url.SAVE_PATH;
import static com.vise.bledemo.utils.url.SAVE_PATH2;
import static com.vise.bledemo.utils.url.SAVE_PATH3;


public class shexiangtouFragment extends BaseFragment {
    View view;
    private SurfaceView m_osurfaceView = null;

    private String m_oIPAddr = "192.168.1.64";
    private String m_oPort = "8000";
    private String m_oUser = "admin";
    private String m_oPsd = "abcd1234";

    private static NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;

    private int m_iLogID = -1; // return by NET_DVR_Login_v30
    private int m_iPlayID = -1; // return by NET_DVR_RealPlay_V30
    private int m_iPlaybackID = -1; // return by NET_DVR_PlayBackByTime

    private int m_iPort = -1; // play port
    private int m_iStartChan = 0; // start channel no
    private int m_iChanNum = 0; // channel number
    //private static PlaySurfaceView[] playView = new PlaySurfaceView[4];

    private final String TAG = "DemoActivity";

    private boolean m_bTalkOn = false;
    private boolean m_bPTZL = false;
    private boolean m_bMultiPlay = false;

    private boolean m_bNeedDecode = true;
    private boolean m_bSaveRealData = false;
    private boolean m_bStopPlayback = false;
    private ImageView da,xiao,yuan,jin;

    List<SxtBean> aadataList = new ArrayList<>();
    int aaindex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shipin, container, false);
       // init();
        return view;
    }

    public void init() {

        if (!initeSdk()) {

        }

        m_osurfaceView = (SurfaceView)view.findViewById(R.id.Sur_Player);
        da = view.findViewById(R.id.imageView8);
        xiao = view.findViewById(R.id.imageView9);

        da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startZoom2(1);
            }
        });
        da.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startZoom2(1);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        stopZoom2(1);
                        break;
                    default:
                        break;
                }
                return true;//true说明事件已经完成了，不会再被其他事件监听器调用
            }
        });
        xiao.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startZoom2(-1);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        stopZoom2(-1);
                        break;
                    default:
                        break;
                }
                return true;//true说明事件已经完成了，不会再被其他事件监听器调用
            }
        });




       // Zhuce();
        Zhuce();

        Bofang();
        final NET_DVR_TIME startTime = new NET_DVR_TIME();
        startTime.dwYear = 2022;
        startTime.dwMonth = 8;
        startTime.dwDay = 1;
        startTime.dwHour = 2;
        startTime.dwMinute = 28;
        startTime.dwSecond = 40;
        final NET_DVR_TIME stopTime = new NET_DVR_TIME();
        stopTime.dwYear = 2022;
        stopTime.dwMonth = 8;
        stopTime.dwDay = 1;
        stopTime.dwHour = 2;
        stopTime.dwMinute = 28;
        stopTime.dwSecond = 50;

        m_osurfaceView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
              //  backPlay(startTime,stopTime);
                HCNetSDK.getInstance()
                        .NET_DVR_GetFileByTime(m_iLogID, m_oNetDvrDeviceInfoV30.byStartChan,
                                startTime, stopTime, new String("/sdcard/a1aaaaaRecordFile.H264"));
               // Test_GetFileByTime(m_iLogID);
                //Test_GetFileByName(m_iLogID);
                Log.e("sxtbofang","aa"+m_oNetDvrDeviceInfoV30.byStartChan);
                //Test_GetFileByTime(m_iLogID);
                return false;
            }
        });
//        public int dwYear;
//        public int dwMonth;
//        public int dwDay;
//        public int dwHour;
//        public int dwMinute;
//        public int dwSecond;

        String fileName = "awzrawzrawzr";




    }

    public static void Test_GetFileByTime(int iUserID)
    {
        NET_DVR_TIME timeStart = new NET_DVR_TIME();
        NET_DVR_TIME timeStop = new NET_DVR_TIME();

        timeStart.dwYear = 2022;
        timeStart.dwMonth = 8;
        timeStart.dwDay = 1;
        timeStart.dwHour = 2;
        timeStart.dwMinute = 22;
        timeStart.dwSecond = 40;
        timeStop.dwYear = 2022;
        timeStop.dwMonth = 8;
        timeStop.dwDay = 1;
        timeStop.dwHour = 2;
        timeStop.dwMinute = 22;
        timeStop.dwSecond = 50;
        int nDownloadHandle = HCNetSDK.getInstance().NET_DVR_GetFileByTime(iUserID,m_oNetDvrDeviceInfoV30.byStartChan,
                timeStart, timeStop, new String("/sdcard/aaaaaaRecordFile.H264"));
        Log.e("NET_DVR_GetFileByTimea1","Nzz:"
                + m_oNetDvrDeviceInfoV30.byStartChan);
        if (-1 == nDownloadHandle)
        {
            System.out.println("NET_DVR_GetFileByTime failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            Log.e("NET_DVR_GetFileByTimea","NET_DVR_GetFileByTime failed! error:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }
        HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(nDownloadHandle, PlaybackControlCommand.NET_DVR_PLAYSTART,
                null, 0, null);
        int nProgress = -1;
        while(true)
        {
            nProgress = HCNetSDK.getInstance().NET_DVR_GetDownloadPos(nDownloadHandle);
            System.out.println("NET_DVR_GetDownloadPos:" + nProgress);
            if(nProgress < 0 || nProgress >= 100)
            {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        HCNetSDK.getInstance().NET_DVR_StopGetFile(nDownloadHandle);
    }

    public static void Test_GetFileByName(int iUserID)
    {
        int nDownloadHandle = HCNetSDK.getInstance().NET_DVR_GetFileByName(iUserID, new String("ch0001_01000000080001900"), new String("/sdcard/RecordFileaaname"));
        if (-1 == nDownloadHandle)
        {
            System.out.println("NET_DVR_GetFileByName failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }
        HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(nDownloadHandle, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null);
        int nProgress = -1;
        while(true)
        {
            nProgress = HCNetSDK.getInstance().NET_DVR_GetDownloadPos(nDownloadHandle);
            System.out.println("NET_DVR_GetDownloadPos:" + nProgress);
            if(nProgress < 0 || nProgress >= 100)
            {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        HCNetSDK.getInstance().NET_DVR_StopGetFile(nDownloadHandle);
    }

    public void Bofang() {
        try {
            if (m_iLogID < 0) {
                Log.e(TAG, "please login on device first");
                return;
            }
            if (m_bNeedDecode) {
                if (m_iChanNum > 1)// preview more than a channel
                {
                    if (!m_bMultiPlay) {
                        m_bMultiPlay = true;
//                        m_oPreviewBtn.setText("停止");
                    } else {
                        stopMultiPreview();
                        m_bMultiPlay = false;
//                        m_oPreviewBtn.setText("播放");
                    }
                } else // preivew a channel
                {
                    if (m_iPlayID < 0) {

                        startSinglePreview();
                    } else {
                        stopSinglePreview();
                      //  m_oPreviewBtn.setText("播放");
                    }
                }
            } else {

            }
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        }
    }

    public void Zhuce() {
        //m_oIPAddr = "192.168.1.64";
        //    private String m_oPort = "8000";
        //    private String m_oUser = "admin";
        //    private String m_oPsd = "abcd1234";
        m_oIPAddr = getpre("e1");
        m_oPort = getpre("e3");
        m_oUser = getpre("e4");
        m_oPsd = getpre("e5");
        try {
            if (m_iLogID < 0) {
                // login on the device
                m_iLogID = loginDevice();
                if (m_iLogID < 0) {
                    Log.e(TAG, "This device logins failed!");
                    return;
                } else {
                    System.out.println("m_iLogID=" + m_iLogID);
                }
                // get instance of exception callback and set
                ExceptionCallBack oexceptionCbf = getExceptiongCbf();
                if (oexceptionCbf == null) {
                    Log.e(TAG, "ExceptionCallBack object is failed!");
                    return;
                }

                if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
                        oexceptionCbf)) {
                    Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
                    return;
                }

             //   m_oLoginBtn.setText("注销账号");
                Log.i(TAG,
                        "Login sucess ****************************1***************************");
            } else {
                // whether we have logout
                if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
                    Log.e(TAG, " NET_DVR_Logout is failed!");
                    return;
                }
             //   m_oLoginBtn.setText("西南摄像机");
                m_iLogID = -1;
            }
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        }
    }

    // @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_osurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        Log.i(TAG, "surface is created" + m_iPort);
        if (-1 == m_iPort) {
            return;
        }
        Surface surface = holder.getSurface();
        if (true == surface.isValid()) {
            if (false == Player.getInstance()
                    .setVideoWindow(m_iPort, 0, holder)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }
    }

    // @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    // @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
        if (-1 == m_iPort) {
            return;
        }
        if (true == holder.getSurface().isValid()) {
            if (false == Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }
    }


    private boolean initeSdk() {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            Log.e(TAG, "HCNetSDK init is failed!");
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",
                true);
        return true;
    }

    private void startSinglePreview() {
        if (m_iPlaybackID >= 0) {
            Log.i(TAG, "Please stop palyback first");
            return;
        }
        RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
        if (fRealDataCallBack == null) {
            Log.e(TAG, "fRealDataCallBack object is failed!");
            return;
        }
        Log.i(TAG, "m_iStartChan:" + m_iStartChan);

        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
        previewInfo.lChannel = m_iStartChan;
        previewInfo.dwStreamType = 0; // substream
        previewInfo.bBlocked = 1;

        m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(m_iLogID,
                previewInfo, fRealDataCallBack);
        if (m_iPlayID < 0) {
            Log.e(TAG, "NET_DVR_RealPlay is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }

        Log.i(TAG,
                "NetSdk Play sucess ***********************3***************************");
      //  m_oPreviewBtn.setText("停止");
    }


    private void stopMultiPreview() {
        int i = 0;
        for (i = 0; i < 4; i++) {
            //playView[i].stopPreview();
        }
        m_iPlayID = -1;
    }

    private void stopSinglePreview() {
        if (m_iPlayID < 0) {
            Log.e(TAG, "m_iPlayID < 0");
            return;
        }

        // net sdk stop preview
        if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID)) {
            Log.e(TAG, "StopRealPlay is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }

        m_iPlayID = -1;
        stopSinglePlayer();
    }

    private void stopSinglePlayer() {
        Player.getInstance().stopSound();
        // player stop play
        if (!Player.getInstance().stop(m_iPort)) {
            Log.e(TAG, "stop is failed!");
            return;
        }

        if (!Player.getInstance().closeStream(m_iPort)) {
            Log.e(TAG, "closeStream is failed!");
            return;
        }
        if (!Player.getInstance().freePort(m_iPort)) {
            Log.e(TAG, "freePort is failed!" + m_iPort);
            return;
        }
        m_iPort = -1;
    }

    /**
     * @fn loginNormalDevice
     * @author zhuzhenlei
     * @brief login on device
     *            [out]
     * @return login ID
     */
    private int loginNormalDevice() {
        // get instance
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
            return -1;
        }
        String strIP = m_oIPAddr;
        int nPort = Integer.parseInt(m_oPort);
        String strUser = m_oUser;
        String strPsd = m_oPsd;
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort,
                strUser, strPsd, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            Log.e(TAG, "NET_DVR_Login is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum
                    + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }
        Log.i(TAG, "NET_DVR_Login is Successful!");

        return iLogID;
    }



    private int loginDevice() {
        int iLogID = -1;

        iLogID = loginNormalDevice();

        return iLogID;
    }

    private ExceptionCallBack getExceptiongCbf() {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                System.out.println("recv exception, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    private RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType,
                                          byte[] pDataBuffer, int iDataSize) {
                bytesToImageFile(pDataBuffer);

//                if(aadataList.size()<3000){
//                    aadataList.add(new SxtBean(iRealHandle,iDataType,pDataBuffer,iDataSize));
//                    // player channel 1
//                    processRealData(1, iDataType, pDataBuffer,
//                            iDataSize, Player.STREAM_REALTIME);
//                }else {
//                    if(aaindex<=2900){
//                        aaindex++;
//                    }else {
//                        aaindex = 0;
//                    }
//                    // player channel 1
//                    processRealData(1, iDataType, aadataList.get(aaindex).getpDataBuffer(),
//                            aadataList.get(aaindex).getiDataSize(), Player.STREAM_REALTIME);
//                }


                processRealData(1, iDataType, pDataBuffer,
                        iDataSize, Player.STREAM_REALTIME);

                try {
                    saveFile("aaaaaaaaaa",pDataBuffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e("aadata",byteToStr(pDataBuffer));
                Log.e("aadata2", String.valueOf(pDataBuffer.length));
                Log.e("aadatai3DataSize", String.valueOf(iDataSize));
                Log.e("aadatai3iDataType", String.valueOf(iDataType));
                Log.e("aadata3",String.valueOf(aaindex)+"----"+String.valueOf(aadataList.size()));
            }
        };
        return cbf;
    }

    public boolean backPlay(NET_DVR_TIME beginDate, NET_DVR_TIME endDate) {
        if (m_iPlayID >= 0) {
            return false;
        }
        NET_DVR_VOD_PARA struVod = new NET_DVR_VOD_PARA();
        struVod.struBeginTime = beginDate;
        struVod.struEndTime = endDate;
        struVod.byStreamType = 0;
        struVod.struIDInfo.dwChannel = m_oNetDvrDeviceInfoV30.byStartChan;
        struVod.hWnd = m_osurfaceView.getHolder().getSurface();
        m_iPlaybackID = HCNetSDK.getInstance().NET_DVR_PlayBackByTime_V40(m_iLogID, struVod);
        if (m_iPlaybackID >= 0) {
            if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(m_iPlaybackID, PlaybackControlCommand.NET_DVR_PLAYSTART,
                    null, 0, null)) {
                return false;
            }
            return true;
        }

        return false;
    }
    /**
     * 将byte数组转换为字符串
     * @param b byte数组
     * @return 字符串
     */
    public static String byteToStr(byte[] b) {
        if (b.length == 0)
            throw new NullPointerException("data is null!");
        int pos = 0;
        for (int i = 0; i < b.length; i++) {
            if (b[i] == 0) {
                pos = i;
                break;
            }
        }
        byte[] bstr = new byte[pos];
        System.arraycopy(b, 0, bstr, 0, pos);
        String str = new String(bstr);
        return str;
    }

    private void bytesToImageFile(byte[] bytes) {
        try {
            String SDCARD_PATH  = Environment.getExternalStorageDirectory().getPath();
            Random random = new Random();
            File file = new File(SDCARD_PATH+"/ScreenCapture/" + random.nextInt(10000)+1+"/facenew1.png");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void toaa(){
        Toast.makeText(getActivity(), "111", Toast.LENGTH_SHORT).show();
    }

    public void processRealData(int iPlayViewNo, int iDataType,
                                byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (!m_bNeedDecode) {
            // Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" +
            // iDataType + ",iDataSize:" + iDataSize);
        } else {
            if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
                if (m_iPort >= 0) {
                    return;
                }
                m_iPort = Player.getInstance().getPort();
                if (m_iPort == -1) {
                    Log.e(TAG, "getPort is failed with: "
                            + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                Log.i(TAG, "getPort succ with: " + m_iPort);
                if (iDataSize > 0) {
                    if (!Player.getInstance().setStreamOpenMode(m_iPort,
                            iStreamMode)) // set stream mode
                    {
                        Log.e(TAG, "setStreamOpenMode failed");
                        return;
                    }
                    if (!Player.getInstance().openStream(m_iPort, pDataBuffer,
                            iDataSize, 2 * 1024 * 1024)) // open stream
                    {
                        Log.e(TAG, "openStream failed");
                        return;
                    }
                    if (!Player.getInstance().play(m_iPort,
                            m_osurfaceView.getHolder())) {
                        Log.e(TAG, "play failed");
                        return;
                    }
                    if (!Player.getInstance().playSound(m_iPort)) {
                        Log.e(TAG, "playSound failed with error code:"
                                + Player.getInstance().getLastError(m_iPort));
                        return;
                    }
                }
            } else {
                if (!Player.getInstance().inputData(m_iPort, pDataBuffer,
                        iDataSize)) {
                    // Log.e(TAG, "inputData failed with: " +
                    // Player.getInstance().getLastError(m_iPort));
                    for (int i = 0; i < 4000 && m_iPlaybackID >= 0
                            && !m_bStopPlayback; i++) {
                        if (Player.getInstance().inputData(m_iPort,
                                pDataBuffer, iDataSize)) {
                            break;

                        }

                        if (i % 100 == 0) {
                            Log.e(TAG, "inputData failed with: "
                                    + Player.getInstance()
                                    .getLastError(m_iPort) + ", i:" + i);
                        }

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }
                    }
                }

            }
        }

    }

    /**
     * 开始变焦 NET_DVR_PTZControl_Other参数：(播放标记, 通道， 指令码, 开始标记0或停止标记1)
     *
     * @param x
     *            -1近端 1远端
     */
    public void startFocus2(int x) {
        if (m_iPlayID < 0) {
            return;
        }
        if (x < 0) {
            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, 1,
                    PTZCommand.FOCUS_NEAR, 0);
        } else {
            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, 1,
                    PTZCommand.FOCUS_FAR, 0);
        }
    }

    public void startZoom2(int x) {
        if (m_iPlayID < 0) {
            return;
        }
        Log.e("sxtbofang","aa1");
        if (x < 0) {
            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, 1,
                    PTZCommand.ZOOM_OUT, 0);
        } else {
            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, 1,
                    PTZCommand.ZOOM_IN, 0);
        }
    }

    public void stopZoom2(int x) {
        if (m_iPlayID < 0) {
            return;
        }
        if (x < 0) {
            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, 1,
                    PTZCommand.ZOOM_OUT, 1);
        } else {
            HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, 1,
                    PTZCommand.ZOOM_IN, 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        init();
    }


    /**
     * 将字节流转换成文件
     * @param filename
     * @param data
     * @throws Exception
     */
    public static void saveFile(String filename,byte [] data)throws Exception{
        if(data != null){
            String filepath =SAVE_PATH;
            File file  = new File(filepath);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data,0,data.length);
            fos.flush();
            fos.close();
        }
    }
}
