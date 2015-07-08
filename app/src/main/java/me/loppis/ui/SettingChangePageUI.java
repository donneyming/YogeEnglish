package me.loppis.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.loppis.R;
import me.loppis.dao.LocalStorage;

public class SettingChangePageUI extends Activity {
	
	LocalStorage storage  ;
	private String editTip = "";
	private String title = "编辑";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_change_page);
		
		Intent intent = getIntent() ;
		
		final int content = (int) intent.getIntExtra("content", 1);
		if(content == 1){
			editTip = "编辑你想要显示的昵称。" ;
			title = "编辑昵称" ;
		}else if(content == 2){
			editTip = "编辑你想要显示的签名。" ;
			title = "编辑签名" ;
		}
		
		//改变这个Activity的提示文字
		TextView tip = (TextView) findViewById(R.id.setting_change_page_tip);
		TextView title = (TextView) findViewById(R.id.setting_change_page_title);
		tip.setText(editTip);
		title.setText(this.title);
		
		storage = new LocalStorage(SettingChangePageUI.this);
		
		//获取到侧滑的组件
        LayoutInflater layout = this.getLayoutInflater();
        View view = layout.inflate(R.layout.sliding_menu, null);
        final TextView username = (TextView)view.findViewById(R.id.sliding_menu_username);
        final TextView intro = (TextView) view.findViewById(R.id.sliding_menu_intro);
        
        //绑定到确定按钮
		Button submit = (Button) findViewById(R.id.setting_change_page_submit);
		submit.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				EditText et = (EditText) findViewById(R.id.setting_change_page_text);
				String inputVal = et.getText().toString();
				if(inputVal.equals("")){	
					Toast.makeText(getApplicationContext(), "请输入!", Toast.LENGTH_SHORT).show();
					return ;
				}
				switch (content) {
					case 1:
						storage.putString("username",inputVal);
						username.setText(inputVal);
						break;
					case 2 :
						storage.putString("userintro", inputVal);
						intro.setText(inputVal);
						break;
				}
				storage.commitEditor();
				SettingChangePageUI.this.finish();
			}
		}); //submit button end 
	
	}
}
