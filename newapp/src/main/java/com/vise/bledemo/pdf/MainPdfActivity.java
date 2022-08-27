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

public class MainPdfActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_back; // 退出界面按钮
	private ImageView iv_menu; // 打开菜单按钮
	private EditText et_pdf; // 内容编辑框
	private Button btn_pdf; // 生成文件按钮
	private Button btn_lookPdf; //查看PDF文件按钮
	ImageView imageView;
	private ProgressDialog myDialog; // 保存进度框
	private PopupWindow ppw_lookpdf; //查看PDF文件弹出框

	private static final int PDF_SAVE_START = 1;// 保存PDF文件的开始意图
	private static final int PDF_SAVE_RESULT = 2;// 保存PDF文件的结果开始意图
	Bitmap bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainpdf);
		init();
		final ImageView imageView = findViewById(R.id.ss2);
		Glide.with(MainPdfActivity.this).load(imaurl).into(imageView);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RelativeLayout ls= f(R.id.rl_pdf_top);

				Log.e("1adsa","1");
				bt=convertViewToBitmap(imageView);
				Log.e("1adsa","1");
				ImageView ss =f(R.id.ss2);
				Log.e("1adsa","1");
				ss.setImageBitmap(bt);
			}
		},2500);

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
				Toast.makeText(MainPdfActivity.this, "保存成功", Toast.LENGTH_SHORT)
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
		initPop();
	}

	/**
	 * 初始化popupwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPop() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.dialog_pdf_model, null);
		btn_lookPdf = (Button) view.findViewById(R.id.btn_ocr_look_pdf);
		btn_lookPdf.setOnClickListener(this);
		ppw_lookpdf = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		ppw_lookpdf.setFocusable(true);
		ppw_lookpdf.setOutsideTouchable(true);
		ppw_lookpdf.setBackgroundDrawable(new BitmapDrawable());
		ppw_lookpdf.setTouchable(true);
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
		iv_back = (ImageView) findViewById(R.id.iv_pdf_back);
		iv_menu = (ImageView) findViewById(R.id.iv_pdf_menu);
		et_pdf = (EditText) findViewById(R.id.et_pdf);
		btn_pdf = (Button) findViewById(R.id.btn_pdf);
		iv_back.setOnClickListener(this);
		iv_menu.setOnClickListener(this);
		btn_pdf.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.ss2);
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
		case R.id.iv_pdf_back://退出界面
			this.finish();
			break;
		case R.id.iv_pdf_menu://打开查看pdf
			openPdfDialog(v);
			break;
		case R.id.btn_ocr_look_pdf:
			 lookPdf();
			break;
		}
	}
	
	/**
	 * 查看PDF文件
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void lookPdf() {
		ppw_lookpdf.dismiss();
		PdfCenterDialog dialog = new PdfCenterDialog();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		dialog.show(ft, "");
	}

	/**
	 * 打开进入pdf文件的显示框
	 */
	private void openPdfDialog(View v) {
		ppw_lookpdf.showAsDropDown(v);
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
//				Document doc = new Document();// 创建一个document对象
//				FileOutputStream fos;
//				try {
//					fos = new FileOutputStream(pdf_address); // pdf_address为Pdf文件保存到sd卡的路径
//					PdfWriter.getInstance(doc, fos);
//					ByteArrayOutputStream stream = new ByteArrayOutputStream();
//					Bitmap bitmap = BitmapFactory.decodeResource(
//							getBaseContext().getResources(),R.drawable.as1);
//					zoomImg(bitmap).compress(Bitmap.CompressFormat.PNG, 100 , stream);
//					//comp(bitmap).compress(Bitmap.CompressFormat.PNG, 100 , stream);
//					Image myImg = Image.getInstance(stream.toByteArray());
//					myImg.setAlignment(Image.MIDDLE);
//					ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
//					RelativeLayout ls= f(R.id.roots);
//					//zoomImg(convertViewToBitmap(imageView)).compress(Bitmap.CompressFormat.PNG, 100 , stream2);
//					zoomImage(convertViewToBitmap(imageView),500,250).compress(Bitmap.CompressFormat.JPEG, 90 , stream2);
//					//convertViewToBitmap(imageView).compress(Bitmap.CompressFormat.PNG, 100 , stream2);
//					Image myImg2 = Image.getInstance(stream2.toByteArray());
//					myImg2.setAlignment(Image.UNDERLYING);
//					//getimage(imaurl);
//
//					Log.e("1adsa","1");
//					//bt=convertViewToBitmap(ls);
//					//add image to document
//
//
//					doc.open();
//					doc.setPageCount(1);
//					doc.add(myImg2);
////					doc.add(new Paragraph(et_pdf.getText().toString()+'\n',
////							setChineseFont())); // result为保存的字符串
//												// ,setChineseFont()为pdf字体
//					// 一定要记得关闭document对象
//					doc.close();
//					fos.flush();
//					fos.close();
//					handler.sendEmptyMessage(PDF_SAVE_RESULT);
//				} catch (FileNotFoundException e1) {
//					e1.printStackTrace();
//				} catch (DocumentException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
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
	/**
	 * 设置PDF字体(较为耗时)
	 */
	public Font setChineseFont() {
		BaseFont bf = null;
		Font fontChinese = null;
		try {
			// STSong-Light : Adobe的字体
			// UniGB-UCS2-H : pdf 字体
			bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);
			fontChinese = new Font(bf, 12, Font.NORMAL);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fontChinese;
	}
	public Bitmap zoomImg(Bitmap bm) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
//		Matrix matrix = new Matrix();
//		matrix.setScale(0.15f, 0.7f);
//		// 得到新的图片
//		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);


		Matrix matrix = new Matrix();
		matrix.setScale(1f, 1f);
		int bitMapWidth = bm.getWidth();
		int bitMapHeight = bm.getHeight();
		Bitmap bm2 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), matrix, false);
		Canvas canvas =new Canvas();
		Paint mPaint1 = new Paint();
		canvas.drawBitmap(bm2, (float) ((width - (bitMapWidth * 0.5)) / 2), 230, mPaint1);

		return bm2;
	}


	/**
	 * 图片按比例大小压缩方法
	 * @param srcPath （根据路径获取图片并压缩）
	 * @return
	 */
	public static Bitmap getimage(String srcPath) {

		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}


	/**
	 * 质量压缩方法
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 90;
		while (baos.toByteArray().length / 1024 > 1500) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset(); // 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	/**
	 * View转换为Bitmap图片
	 *
	 * @param view
	 * @return Bitmap
	 */
	public Bitmap convertViewToBitmap(View view) {
		//创建Bitmap,最后一个参数代表图片的质量.
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		//创建Canvas，并传入Bitmap.
		Canvas canvas = new Canvas(bitmap);
		//View把内容绘制到canvas上，同时保存在bitmap.
		view.draw(canvas);
		return bitmap;
	}


	public static Bitmap zoomImage(Bitmap origin, double newWidth, double newHeight) {
		Bitmap returnBitmap = null;
		float width = origin.getWidth();
		float height = origin.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		try {
			returnBitmap = Bitmap.createBitmap(origin, 0, 0, (int) width, (int) height, matrix, true);
		} catch (OutOfMemoryError e) {
			return origin;
		}
		return returnBitmap;
	}

	/**
	 * 根据图片生成PDF
	 *
	 * @param pdfPath 生成的PDF文件的路径
	 * @param imagePathList 待生成PDF文件的图片集合
	 * @throws IOException 可能出现的IO操作异常
	 * @throws DocumentException PDF生成异常
	 */

}
