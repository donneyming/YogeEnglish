package me.loppis.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import me.loppis.R;
import me.loppis.dao.DBTools;

public class TodoListCardUI extends Activity {
	private static final int Card = 0x1234 ;
	private static final int EDITSIGN = 0x1235 ;
	public DBTools db ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todolist_add_card);
		
		final EditText editText = (EditText) findViewById(R.id.todolist_add_card_text);		
		final Intent sourceIntent = getIntent();
		
		db = new DBTools(this);

		
		//对组件做不同处理
		final String method = sourceIntent.getCharSequenceExtra("method").toString();
		if(method.equals("add")){
			Button deleteButton = (Button)findViewById(R.id.todolist_card_delete);
			deleteButton.setVisibility(View.INVISIBLE);
		}else if(method.equals("edit")){
			String messionName = sourceIntent.getCharSequenceExtra("messionName").toString().trim();
			editText.setText(messionName);
			Button deleteButton = (Button)findViewById(R.id.todolist_card_delete);
			deleteButton.setVisibility(View.INVISIBLE);
		}
		
		Button add = (Button) findViewById(R.id.todolist_card_submit);
		add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				if(method.equals("add")){
					//获得任务信息并且返回
					String name = editText.getText().toString().trim();
					if(name.equals("")){
						Toast.makeText(getApplicationContext(), "请输入任务名称!", Toast.LENGTH_SHORT).show();
						return ;
					}
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
					String time = formatter.format(new GregorianCalendar().getTime());
					String id = String.valueOf(System.currentTimeMillis());		
					
					//添加进数据库
					db.add(id, name.trim(), time, DBTools.TODO);
					
					//用于更新View层
					sourceIntent.putExtra("messionName",name);
					sourceIntent.putExtra("id", id);
					sourceIntent.putExtra("time",time);
					sourceIntent.putExtra("type", "add");
					setResult(Card, sourceIntent);	
					TodoListCardUI.this.finish();				
					
				}else if(method.equals("edit")){
					String name = editText.getText().toString().trim();
					if(name.equals("")){
						Toast.makeText(getApplicationContext(), "请输入任务名称!", Toast.LENGTH_SHORT).show();
						return ;
					}
					String id = sourceIntent.getCharSequenceExtra("id").toString();
					db.updateName(id, name.trim());
					sourceIntent.putExtra("messionName", name.trim());
					sourceIntent.putExtra("type", "edit");
					setResult(Card, sourceIntent);
					TodoListCardUI.this.finish();
				}
			}
		});
		
//		Button delete = (Button) findViewById(R.id.todolist_card_delete);
//		delete.setOnClickListener(new OnClickListener() {C
//			
//			public void onClick(View v) {
//				String id = sourceIntent.getCharSequenceExtra("id").toString();
//				db.delete(id);
//				TodoListCardUI.this.finish();
//			}
//		});
		
		
	}
}
