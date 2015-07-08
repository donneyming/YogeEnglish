package me.loppis.ui;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.loppis.R;
import me.loppis.dao.DataHelper;
import me.loppis.dao.LocalStorage;

public class RegisterOrLoginUI extends Activity {
	String id="";
	String password="";
	String result ="";
	Handler handler;
	LocalStorage storage ;
	Button login ;
	EditText name;
	EditText pass ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_login);
		
		final LocalStorage storage = new LocalStorage(RegisterOrLoginUI.this);
		final Button login = (Button) findViewById(R.id.register_login_submit);
		final EditText name = (EditText) findViewById(R.id.register_login_username);
		final EditText pass = (EditText) findViewById(R.id.register_login_password);

		login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				id =name.getText().toString();
				password =pass.getText().toString();
				if(new DataHelper().isConnect(RegisterOrLoginUI.this)==false)
				{
					Toast.makeText(RegisterOrLoginUI.this, "网络连接失败，请确认网络连接...",Toast.LENGTH_LONG).show();
					return ;
				}
				if (id.equals("")||password.equals("")||id==null||password==null) {
					Toast.makeText(RegisterOrLoginUI.this, "请输入信息",Toast.LENGTH_LONG).show();
					
				} 
				else {
					new Thread(new Runnable() {
						
						public void run()
						{

							result = new DataHelper().getLoginData(id, password);// 这里返回结果，可以解析
							Message m = handler.obtainMessage();
							handler.sendMessage(m);
						}
					}).start();
				}
				
				handler = new Handler(){
					public void handleMessage(Message msg) {
						
						if(msg.what == 0x111){
							// 提示登录中						
							login.setText("登录中...");
							login.setEnabled(false);
						}
						


						
						if(result.equals("0")){
							Toast.makeText(getApplicationContext(), "登录信息有误!",
									Toast.LENGTH_LONG).show();
							
							login.setText("登录");
							login.setEnabled(true);
							
							storage.putInt("login", 0);
							storage.commitEditor();
							
						}else{
							storage.putInt("login", 1);
							storage.putString("userid",id);
							storage.commitEditor();
							Toast.makeText(getApplicationContext(), "登录成功",
									Toast.LENGTH_LONG).show();
							
							RegisterOrLoginUI.this.finish();
						}
						
						super.handleMessage(msg);
					}
				};

			}
		});

	}
}
