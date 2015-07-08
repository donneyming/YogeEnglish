package me.loppis.ui;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import me.loppis.R;
import me.loppis.dao.DBTools;

public class ContentUI extends Activity {
	
	DBTools db ;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_ui);
		
		db = new DBTools(this);
		
		WebView webNavi = (WebView) findViewById(R.id.webNavi);
		webNavi.setWebChromeClient(new WebChromeClient());
		webNavi.setWebViewClient(new WebViewClient());
		
		//获取WebView设置对象
		WebSettings setting = webNavi.getSettings() ;
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(false);
		setting.setBuiltInZoomControls(false);
		
		final Intent sourceIntent = getIntent();
		String filename = (String) sourceIntent.getCharSequenceExtra("filename");
		String jqmFilePath = "file:///android_asset/" + filename + ".html" ;
		
		webNavi.addJavascriptInterface(this, "YoGe");
		webNavi.loadUrl(jqmFilePath);
		
		//绑定按钮：
		Button returnBtn = (Button) findViewById(R.id.returnBtn);
		returnBtn.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				ContentUI.this.finish();
			}
		});
		
		Button finish = (Button) findViewById(R.id.finishedRead);
		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
				String time = formatter.format(new GregorianCalendar().getTime());
				String id = String.valueOf(System.currentTimeMillis());	
				String name = sourceIntent.getCharSequenceExtra("messionName").toString();
				db.add(id, name, time, DBTools.FINISH);
				ContentUI.this.finish();
			}
		});
			
	}

	@android.webkit.JavascriptInterface
	public void log(String msg) {
		Toast.makeText(ContentUI.this, msg, Toast.LENGTH_SHORT).show();
	}
	
}
