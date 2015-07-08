package me.loppis.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import me.loppis.R;
import me.loppis.dao.DBTools;
import me.loppis.dao.DataHelper;
import me.loppis.dao.LocalStorage;

public class SettingCenterUI extends Activity {

	LocalStorage storage ;
	Handler handler;
	String result="";
	String json ="" ;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_center);

		ImageView back = (ImageView) findViewById(R.id.setting_center_back);
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SettingCenterUI.this.finish();
			}
		});
		
		Button updataBtn = (Button) findViewById(R.id.setting_center_update);
		Button logoutBtn = (Button) findViewById(R.id.setting_center_logout);
		//Button update_finish = (Button) findViewById(R.id.setting_center_update_finish);
		
		updataBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateMyData(v);
			}
		});
		
		
		logoutBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				logoutMyAccount(v);
			}
		});
		
		
		
		storage = new LocalStorage(this);
		if(storage.has("login")){
			int login = storage.getInt("login", 0);
			if(login == 0){

				updataBtn.setVisibility(View.GONE);
				logoutBtn.setVisibility(View.GONE);
				
			}
		}else{
			updataBtn.setVisibility(View.GONE);
			logoutBtn.setVisibility(View.GONE);
		}
		

		
		
		
	}

	public void toSettingPage(View sourceView) {
		Intent intent = new Intent(SettingCenterUI.this,
				SettingChangePageUI.class);
		switch (sourceView.getId()) {
		case R.id.setting_center_name:
			intent.putExtra("content", 1);
			startActivity(intent);
			break;
		case R.id.setting_center_sign:
			intent.putExtra("content", 2);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	public void selectHeader(View view) {
		Intent fileIntent = new Intent(SettingCenterUI.this , SelectHead.class);
		startActivity(fileIntent);
	}

	public void toClean(View view) {
		DBTools db = new DBTools(this);
		switch (view.getId()) {
		case R.id.setting_center_delete_finish:
			db.deleteAll(DBTools.FINISH);
			Toast.makeText(SettingCenterUI.this, "success", Toast.LENGTH_LONG).show();
			break;

		case R.id.setting_center_delete_todo:
			db.deleteAll(DBTools.TODO);
			Toast.makeText(SettingCenterUI.this, "success", Toast.LENGTH_LONG).show();
			break;
		}
	}

	public void toAboutPage(View view) {
		Intent intent = new Intent(SettingCenterUI.this, AboutPageUI.class);
		startActivity(intent);
	}
	
	public void updateMyData(View view){
		
		final String id = storage.getString("userid", "");
		final DBTools db = new DBTools(this);
		final DataHelper http = new DataHelper();
		
		if(http.isConnect(SettingCenterUI.this)){
			new Thread(new Runnable() {
				public void run() {
					
					StringBuilder buffer = new StringBuilder(200);
					Cursor cursor = db.selectAll(DBTools.TODO);
					buffer.append("{");
					while(cursor.moveToNext()){
						buffer.append("\"" + cursor.getString(0) + "\":");
						buffer.append("[");
						buffer.append("\"" + cursor.getString(1) + "\",");
						buffer.append("\"" + cursor.getString(2) + "\",");
						buffer.append("\"" + cursor.getString(3) + "\",") ;
						buffer.append("\"" + cursor.getString(4) + "\"") ;
						buffer.append("]");
						if(!cursor.isLast()){
							buffer.append(",");
						}
					}
					buffer.append("}");
					json = buffer.toString();
					result = http.setTodo(id,json);
					Message msg=handler.obtainMessage();
					handler.sendMessage(msg);
				}
			}).start();
			handler = new Handler(){
				public void handleMessage(Message msg) {
					Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
					super.handleMessage(msg);
				}
			};
		}else{
			Toast.makeText(getApplicationContext(), "AAA", Toast.LENGTH_SHORT).show();
		}
		

	}

	public void logoutMyAccount(View view){
		storage.putInt("login", 0);
		//storage.putString("username", "Please Edit");
		//storage.putString("userintro", "Please Edit Here !");
		storage.putString("userid", "");
		storage.commitEditor();
		Button updataBtn = (Button) findViewById(R.id.setting_center_update);
		Button logoutBtn = (Button) findViewById(R.id.setting_center_logout);
		//updataBtn.setEnabled(false);
		//logoutBtn.setEnabled(false);
		//updataBtn.setClickable(false);
		//updataBtn.setClickable(false);
		updataBtn.setVisibility(View.GONE);
		logoutBtn.setVisibility(View.GONE);
		SettingCenterUI.this.finish();
	}


}
