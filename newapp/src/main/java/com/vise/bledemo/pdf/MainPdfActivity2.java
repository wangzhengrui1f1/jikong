package com.vise.bledemo.pdf;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.vise.bledemo.Base.BaseActivity;
import com.vise.bledemo.R;
import com.vise.bledemo.chezai.bean.ShouDongQuZheng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.vise.bledemo.utils.url.imaurl;
import static com.vise.bledemo.utils.url.pdfList;

public class MainPdfActivity2 extends BaseActivity implements OnClickListener {


	private Button btn_pdf; // 生成文件按钮

	private ProgressDialog myDialog; // 保存进度框
	private PopupWindow ppw_lookpdf; //查看PDF文件弹出框

	private static final int PDF_SAVE_START = 1;// 保存PDF文件的开始意图
	private static final int PDF_SAVE_RESULT = 2;// 保存PDF文件的结果开始意图

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainpdf2);
		init();

	}


	private Handler handler = new Handler(new Callback() {

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
				Toast.makeText(MainPdfActivity2.this, "保存成功", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			return false;
		}
	});

	/**
	 * 初始化操作
	 */
	private void init() {
		initView();
		initProgress();
	}


	/**
	 * 初始化识别进度框
	 */
	private void initProgress() {
		myDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		myDialog.setIndeterminateDrawable(getResources().getDrawable(
				R.drawable.progress_ocr));
		myDialog.setMessage("正在保存PDF文件...");
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.setCancelable(false);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		btn_pdf = (Button) findViewById(R.id.btn_pdf);
		btn_pdf.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pdf: //生成pdf
			if(pdfList.size()==0){
				Toast.makeText(this, "没有导出的信息.", Toast.LENGTH_SHORT).show();
			}else {
				turnToPdf();
			}
			break;
		}
	}
	



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
					createPdf(pdf_address,pdfList);
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
	 * @param pdfPath 生成的PDF文件的路径
	 * @param imagePathList 待生成PDF文件的图片集合
	 * @throws IOException 可能出现的IO操作异常
	 * @throws DocumentException PDF生成异常
	 */
	private void createPdf(String pdfPath, List<ShouDongQuZheng> imagePathList) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

//		//设置pdf背景
//		PdfBackground event = new PdfBackground();
//		writer.setPageEvent(event);

		document.open();
		for (int i = 0; i < imagePathList.size(); i++) {
			document.newPage();
			Image img = Image.getInstance(imagePathList.get(i).getImagePath());
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


}
