package com.vise.bledemo.chezai.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.adapter.ChejingBaoAdapter;
import com.vise.bledemo.baiduMap.baiduMap.view.WelcomeActivity;
import com.vise.bledemo.chezai.bean.ShouDongQuZheng;
import com.vise.bledemo.pdf.MainPdfActivity;
import com.vise.bledemo.pdf.MainPdfActivity2;
import com.vise.bledemo.pdf.PdfUtils;
import com.vise.bledemo.sqlite.MyDatabase;
import com.vise.bledemo.utils.OkHttpUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vise.bledemo.sqlite.sqliteMethod.dels;
import static com.vise.bledemo.sqlite.sqliteMethod.getCheZaiBaoJing;
import static com.vise.bledemo.sqlite.sqliteMethod.lessUid;
import static com.vise.bledemo.utils.getTime.disposeTime;
import static com.vise.bledemo.utils.url.SQLITE_NAME;
import static com.vise.bledemo.utils.url.SaveAlert;
import static com.vise.bledemo.utils.url.delList;
import static com.vise.bledemo.utils.url.imaurl;
import static com.vise.bledemo.utils.url.pdfList;
import static com.vise.bledemo.utils.url.viewsa;

public class SthreeFragmentchezai extends BaseFragment {
    View view;
    List<ShouDongQuZheng> ls = new ArrayList<ShouDongQuZheng>();
    ChejingBaoAdapter adapter;
    ListView listView;
    private ImageView back;
    private EditText starttime, endtime, shebeiid, caozuoyuan;
    private LinearLayout daochu;
    String time1 = "-1", time2 = "-1";
    private Button del;
    private CheckBox delcheckBox;
    int index = 0;
    Activity activitys = getActivity();
    View ScrccView = null;
    public Thread dataCheckThread;
    public Handler dataCheckHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //刷新重绘view
            if (msg.what == 30) {

            }
            if (msg.what == 31) {
                tips2(pdfList.get(index));
                index++;
            }
        }
    };
    int startFalg = 0;
    int finishFlag = 0;
    List<String> daochuList = new ArrayList<>();
    ImageView imaswzr;

    //todo 导出pdf
    private Button btn_pdf; // 生成文件按钮

    private ProgressDialog myDialog; // 保存进度框
    private PopupWindow ppw_lookpdf; //查看PDF文件弹出框

    private static final int PDF_SAVE_START = 1;// 保存PDF文件的开始意图
    private static final int PDF_SAVE_RESULT = 2;// 保存PDF文件的结果开始意图

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment33, container, false);
        activitys = getActivity();
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);
        init();
        start();
        Log.e("aazz", "0");
        initProgress();
        return view;
    }


    /**
     * 初始化识别进度框
     */
    private void initProgress() {
        myDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        myDialog.setIndeterminateDrawable(getResources().getDrawable(
                R.drawable.progress_ocr));
        myDialog.setMessage("正在保存PDF文件...");
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setCancelable(false);
    }

    private void start() {
        if (dataCheckThread == null) {
            dataCheckThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            //休眠500ms
                            sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (startFalg == 0) {
                            continue;
                        }

                        if (index >= pdfList.size()) {
                            startFalg = 0;
                            finishFlag = 1;
                        } else {
                            if (finishFlag == 0) {
                                finishFlag = 1;
                                dataCheckHandler.sendEmptyMessage(31);
                            }

                        }


                        Log.e("xianchen", "start");

                    }
                }
            };
            dataCheckThread.start();
        }
    }

    private void init() {
        delcheckBox = (CheckBox) view.findViewById(R.id.checkBox2);
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        del = (Button) view.findViewById(R.id.button8);
        starttime = (EditText) view.findViewById(R.id.starttime);
        endtime = (EditText) view.findViewById(R.id.endtime);
        starttime.setFocusable(false);
        starttime.setFocusableInTouchMode(false);
        endtime.setFocusable(false);
        endtime.setFocusableInTouchMode(false);
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettimes(starttime);
            }
        });
        daochu = f(view, R.id.daochu);
        daochu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   intent(MainPdfActivity.class);
                if (pdfList.size() == 0) {
                    toastShow("无导出数据");
                } else {
                    daochuList.clear();
                    //todo 开始
                    index = 0;
                    startFalg = 1;
                    Log.e("sizeaa", String.valueOf(pdfList.size()));
                    // dataCheckHandler.sendEmptyMessage(31);
                }

            }
        });
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettimes(endtime);
            }
        });

        shebeiid = view.findViewById(R.id.shebeiid);
        caozuoyuan = view.findViewById(R.id.caozuoyuan);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips_del();

            }
        });

        shebeiid.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ls.clear();
                ls = getCheZaiBaoJing(myDatebaseHelper, time1, time2, shebeiid.getText().toString(),
                        caozuoyuan.getText().toString());
                Log.e("ttatta-操作人员", caozuoyuan.getText().toString());
                InitListview(0);
            }
        });
        caozuoyuan.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ls.clear();
                ls = getCheZaiBaoJing(myDatebaseHelper, time1, time2, shebeiid.getText().toString(),
                        caozuoyuan.getText().toString());
                Log.e("ttatta-操作人员", caozuoyuan.getText().toString());
                InitListview(0);
            }
        });

        delcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    delList.clear();
                    ls.clear();
                    ls = getCheZaiBaoJing(myDatebaseHelper, "-1", "-1", "-1", "-1");
                    InitListview(1);
                } else {
                    delList.clear();
                    ls.clear();
                    ls = getCheZaiBaoJing(myDatebaseHelper, "-1", "-1", "-1", "-1");
                    InitListview(0);
                }
            }
        });

    }

    public void gettimes(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String m, r;
                m = String.valueOf(month + 1);
                r = String.valueOf(dayOfMonth);
                if (Integer.valueOf(m) < 10) {
                    m = "0" + m;
                }
                if (Integer.valueOf(r) < 10) {
                    r = "0" + r;
                }
                if (starttime == editText) {
                    time1 = "" + year + (m) + r;
                    String text = "你选择了：" + year + "年" + (m) + "月" + r + "日";
                    toastShow(text);
                }

                if (endtime == editText) {
                    time2 = "" + year + (m) + r;
                    String text = "你选择了：" + year + "年" + (m) + "月" + r + "日";
                    toastShow(text);
                }

                editText.setText(year + "年" + (m) + "月" + r + "日");
                if (!endtime.getText().toString().equals("") && !starttime.getText().toString().equals("")) {
                    ls.clear();
                    ls = getCheZaiBaoJing(myDatebaseHelper, time1, time2, shebeiid.getText().toString(),
                            caozuoyuan.getText().toString());
                    InitListview(0);
                    Log.e("wwzzertime1", String.valueOf(time1));
                    Log.e("wwzzertime2", String.valueOf(time2));
                }
            }
        }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //TODO 可见操作
            ls.clear();
            ls = getCheZaiBaoJing(myDatebaseHelper, "-1", "-1", "-1", "-1");
            InitListview(0);
            //上传
            List<ShouDongQuZheng> shangchuan = new ArrayList<>();
            shangchuan = getCheZaiBaoJing(myDatebaseHelper, "-1", "-1", "-1", "-1");
            if (shangchuan.size() > 0) {
                for (int i = 0; i < shangchuan.size(); i++) {
                    if (shangchuan.get(i).getUid().contains("AA")) {

                    } else {
                        //上传
                        SendPost2(shangchuan.get(i));
                    }
                }
            }

        } else {
            //TODO 不可见操作
        }
    }

    private void InitListview(int del) {
        adapter = new ChejingBaoAdapter(getActivity(), ls, del) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                LinearLayout root = view.findViewById(R.id.t1);
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tips(ls.get(position));
                        toastShow(ls.get(position).getUid());
                    }
                });
                return view;
            }
        };
        listView = (ListView) view.findViewById(R.id.listview1);
        listView.setAdapter(adapter);
    }

    //todo
    private void tips(ShouDongQuZheng shouDongQuZheng) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_shouchi3, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final ImageView exit = view.findViewById(R.id.exit);
        final ImageView imaswzr = view.findViewById(R.id.imaswzr);
        final TextView info10 = view.findViewById(R.id.info10);
        final TextView info11 = view.findViewById(R.id.info11);
        final TextView info12 = view.findViewById(R.id.info12);
        final TextView info13 = view.findViewById(R.id.info13);
        final TextView info14 = view.findViewById(R.id.info14);
        final TextView info15 = view.findViewById(R.id.info15);
        final TextView info16 = view.findViewById(R.id.info16);
        final TextView info17 = view.findViewById(R.id.info17);
        final TextView info18 = view.findViewById(R.id.info18);
        final TextView info19 = view.findViewById(R.id.info19);
        final LinearLayout rootview = view.findViewById(R.id.rootview);
        info10.setText(" " + shouDongQuZheng.getUsername());
        info11.setText(" " + shouDongQuZheng.getType());
        info12.setText(" " + shouDongQuZheng.getDidian());
        info13.setText(" " + shouDongQuZheng.getEndtime());
        info14.setText(" " + shouDongQuZheng.getNongdu());
        info15.setText(" " + shouDongQuZheng.getWendu());
        info16.setText(" " + shouDongQuZheng.getGuangqiang());
        info17.setText(" " + shouDongQuZheng.getChesu());
        info18.setText(" " + shouDongQuZheng.getStarttime() + " 至 " + shouDongQuZheng.getEndtime());
        info19.setText(" " + shouDongQuZheng.getBeizhu());

        if (shouDongQuZheng.getImagePath() != null) {
            imaurl = shouDongQuZheng.getImagePath();
            Glide.with(getActivity()).load(shouDongQuZheng.getImagePath()).into(imaswzr);
        }

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void tips2(ShouDongQuZheng shouDongQuZheng) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_shouchi3, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final ImageView exit = view.findViewById(R.id.exit);
        imaswzr = view.findViewById(R.id.imaswzr);
        final TextView info10 = view.findViewById(R.id.info10);
        final TextView info11 = view.findViewById(R.id.info11);
        final TextView info12 = view.findViewById(R.id.info12);
        final TextView info13 = view.findViewById(R.id.info13);
        final TextView info14 = view.findViewById(R.id.info14);
        final TextView info15 = view.findViewById(R.id.info15);
        final TextView info16 = view.findViewById(R.id.info16);
        final TextView info17 = view.findViewById(R.id.info17);
        final TextView info18 = view.findViewById(R.id.info18);
        final TextView info19 = view.findViewById(R.id.info19);
        final LinearLayout rootview = view.findViewById(R.id.rootview);
        Log.e("aazz", "3");
        info10.setText(" " + shouDongQuZheng.getUsername());
        info11.setText(" " + shouDongQuZheng.getType());
        info12.setText(" " + shouDongQuZheng.getDidian());
        info13.setText(" " + shouDongQuZheng.getEndtime());
        info14.setText(" " + shouDongQuZheng.getNongdu());
        info15.setText(" " + shouDongQuZheng.getWendu());
        info16.setText(" " + shouDongQuZheng.getGuangqiang());
        try {
            if (shouDongQuZheng.getChesu() == null) {
                info17.setText(" " + "0");
            } else {
                info17.setText(" " + shouDongQuZheng.getChesu());
            }
        } catch (Exception e) {

        }
        info18.setText(" " + shouDongQuZheng.getStarttime() + " 至 " + shouDongQuZheng.getEndtime());
        info19.setText(" " + shouDongQuZheng.getBeizhu());
        ScrccView = rootview;
        Log.e("aazz", "4");
        if (shouDongQuZheng.getImagePath() != null) {
            imaurl = shouDongQuZheng.getImagePath();
            Glide.with(getActivity()).load(shouDongQuZheng.getImagePath()).into(imaswzr);
        }
        Log.e("aazz", "5");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //截图
                saveIma(rootview);
                //dataCheckHandler.sendEmptyMessage(31);
            }
        }, 1600);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index == pdfList.size()) {
                    if (pdfList.size() == 0) {
                        Toast.makeText(getActivity(), "没有导出的信息.", Toast.LENGTH_SHORT).show();
                    } else {
                        turnToPdf();
                    }
                }
                finishFlag = 0;
                dialog.dismiss();
            }
        }, 1900);

        dialog.show();

    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public String saveIma(View view) {
        String path = "";

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "手动取证" + disposeTime() + ".jpg");
        // Bitmap bitmap = screenShot(getActivity());

//        if(ScrccView!=null){
//
//        }
        Bitmap bitmap = shotActivity(view);
        //Bitmap bitmap = screenShot(activity);

        try {
            if (!file.exists())
                file.createNewFile();

            boolean ret = saveSrc(bitmap, file, Bitmap.CompressFormat.JPEG, true);
            if (ret) {
                path = file.getAbsolutePath();
                daochuList.add(path);
                Toast.makeText(getActivity().getApplicationContext(), "截图已保持至 " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //final ImageView imageView = view.findViewById(R.id.imageView4);
        //        Glide.with(this).load(as).into(imageView);
        return path;
    }

    /**
     * 根据指定的view截图
     *
     * @param v 要截图的view
     * @return Bitmap
     */
    public static Bitmap getViewBitmap(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }

        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 保存图片到文件File。
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return true 成功 false 失败
     */
    public static boolean saveSrc(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src))
            return false;

        OutputStream os;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled())
                src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public static Bitmap capture(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
    }

    public Bitmap shotActivity(View view) {
        // View view = ScrccView;
        //  View view = viewsa;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bp;
    }


    private void tips_del() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_del, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final Button del = view.findViewById(R.id.button5);
        final Button quxiao = view.findViewById(R.id.button9);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dels(myDatebaseHelper, delList);
                ls.clear();
                ls = getCheZaiBaoJing(myDatebaseHelper, "-1", "-1", "-1", "-1");
                InitListview(0);
                dialog.dismiss();
                tips_del2();
            }
        });

        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void tips_del2() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_del2, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final Button queding = view.findViewById(R.id.button5);

        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    //导出的代码

    /**
     * 识别结果转为PDF文件
     */
    private void turnToPdf() {
//		if (et_pdf.getText().toString().equals("")
//				|| et_pdf.getText().toString() == null) {
//			Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
//			return;
//		}
        File file = new File(PdfUtils.ADDRESS);
        if (!file.exists())
            file.mkdirs();
        final String pdf_address = PdfUtils.ADDRESS + File.separator + "集控PDF_"
                + disposeTime() + ".pdf";


        handler.sendEmptyMessage(PDF_SAVE_START);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    createPdf(pdf_address, daochuList);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 根据图片生成PDF
     *
     * @param pdfPath       生成的PDF文件的路径
     * @param imagePathList 待生成PDF文件的图片集合
     * @throws IOException       可能出现的IO操作异常
     * @throws DocumentException PDF生成异常
     */
    private void createPdf(String pdfPath, List<String> imagePathList) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

//		//设置pdf背景
//		PdfBackground event = new PdfBackground();
//		writer.setPageEvent(event);

        document.open();
        for (int i = 0; i < imagePathList.size(); i++) {
            document.newPage();
            Image img = Image.getInstance(imagePathList.get(i));
            //设置图片缩放到A4纸的大小
            img.scaleToFit(PageSize.A4.getWidth() - 50, PageSize.A4.getHeight() - 20);
            //设置图片的显示位置（居中）
            img.setAbsolutePosition((PageSize.A4.getWidth() - img.getScaledWidth()) / 2,
                    (PageSize.A4.getHeight() - img.getScaledHeight()) / 2);
            document.add(img);
        }
        document.close();
        handler.sendEmptyMessage(PDF_SAVE_RESULT);
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PDF_SAVE_START:
                    if (!myDialog.isShowing())
                        myDialog.show();
                    break;

                case PDF_SAVE_RESULT:
                    if (myDialog.isShowing())
                        myDialog.dismiss();
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
            return false;
        }
    });

    //todo 上传记录 http://123.56.230.79:6799/user/JikongSave/SaveAlert?
    // deviceName=321321&userName=321&type=32&longitude=321&latitude=321&
    // gpslongitude=5125&gpslatitude=21424&concentration=321&temperature=3213&
    // lightIntensity=4214&nospeed=5215&starttime=3123&endtime=321312&grade=5215&automatic=312321&uid=3213
    public void SendPost2(ShouDongQuZheng mShouDongQuZheng) {
        final String uid = mShouDongQuZheng.getUid() + "AA";
        final int id = mShouDongQuZheng.getId();
        Log.e("sssendpost2", "1111");
        Map<String, String> hs = new HashMap<>();
        //http://localhost:8080/user/saveShops?latitude=33&longitude=23&placeTime=dsdsad&placeName=909
        hs.put("deviceName", getpre("androidIds"));
        hs.put("userName", getpre("username"));
        hs.put("type", mShouDongQuZheng.getType());
        hs.put("longitude", mShouDongQuZheng.getJingdu());
        hs.put("latitude", mShouDongQuZheng.getWeidu());
        hs.put("gpslongitude", String.valueOf(mShouDongQuZheng.getJingdu()));
        hs.put("gpslatitude", String.valueOf(mShouDongQuZheng.getWeidu()));
        hs.put("concentration", mShouDongQuZheng.getNongdu());
        hs.put("temperature", mShouDongQuZheng.getWendu());
        hs.put("lightIntensity", mShouDongQuZheng.getGuangqiang());
        hs.put("nospeed", mShouDongQuZheng.getChesu());
        hs.put("starttime", mShouDongQuZheng.getStarttime());
        hs.put("endtime", mShouDongQuZheng.getEndtime());
        hs.put("grade", mShouDongQuZheng.getGrade());
        hs.put("automatic", mShouDongQuZheng.getFlag());
        hs.put("uid", mShouDongQuZheng.getUid());
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveAlert, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                toastShow(result);
                if (result.contains("成功")) {
                    lessUid(myDatebaseHelper, uid, id);
                } else {

                }
            }
        });
    }


}
