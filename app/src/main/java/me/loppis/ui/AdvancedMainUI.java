package me.loppis.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.loppis.R;

public class AdvancedMainUI extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_main);
	}
	
	public void toContent(View view){
		String url = view.getContentDescription().toString() ;
		Intent intent = new Intent(AdvancedMainUI.this,ContentUI.class);
		intent.putExtra("filename", url);
		intent.putExtra("messionName", "进阶部分");
		startActivity(intent);
	}

}
