package me.loppis.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import me.loppis.R;
import me.loppis.dao.DBTools;

public class StartUpUI extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stratup_ui);
		
		DBTools db = new DBTools(this);
		
		// 启动闪屏
        new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(StartUpUI.this,MainUI.class);
				startActivity(intent);
				StartUpUI.this.finish();
			}
        }, 3000); //3秒后切换
      
        
	}
}
