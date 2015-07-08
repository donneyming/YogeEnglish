package me.loppis.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.loppis.R;
import me.loppis.dao.DBTools;

public class TodoListUI extends Activity {
	private static final int Card = 0x1234 ;
	private static final int EDITSIGN = 0x1235 ;
	DBTools db ;
	public View currentView ;
	
	//是TodoList的盒子
	public LinearLayout container ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todolist);
		
		//添加自定义任务
		ImageButton add = (ImageButton) findViewById(R.id.todolist_add);
		add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toCard = new Intent(TodoListUI.this,TodoListCardUI.class);
				toCard.putExtra("method","add");
				startActivityForResult(toCard,TodoListUI.this.Card);
				//startActivity(toCard);
			}
		});
		
		container = (LinearLayout) findViewById(R.id.todolist_container);
		
		db = new DBTools(TodoListUI.this);
		Cursor cursor = db.selectAll(DBTools.TODO);
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			String name = cursor.getString(1);
			String time = cursor.getString(2);
			String status = cursor.getString(3);
			makeTodoView(id, name, time, status);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		

			if(resultCode == Card){
				
				
				String type = data.getCharSequenceExtra("type").toString();
				if(type.equals("add")){
					String name = data.getCharSequenceExtra("messionName").toString();
					String time = data.getCharSequenceExtra("time").toString();
					String id = data.getCharSequenceExtra("id").toString();
					makeTodoView(id, name, time, "进行中");
					//Toast.makeText(getApplicationContext(),"111",Toast.LENGTH_SHORT).show();
				}else if(type.equals("edit")){
					String newName = data.getCharSequenceExtra("messionName").toString();
					TextView nameV = (TextView) currentView.findViewById(R.id.todolist_card_title);
					nameV.setText(newName);
				}
			}
			
	}
	
	public void toEdit(final View view){
		currentView = view ;
		
		TextView id = (TextView) view.findViewById(R.id.todolist_card_data);
		final String idString = id.getContentDescription().toString();
		
		TextView name = (TextView) view.findViewById(R.id.todolist_card_title);
		final String messionName = name.getText().toString();
		
		final TextView time = (TextView) view.findViewById(R.id.todolist_card_time);
		final String timeStatus = time.getText().toString() ; 
		
		final Builder alert = new AlertDialog.Builder(TodoListUI.this);
		alert.setIcon(R.mipmap.new_logo);
		alert.setTitle("优咯英语");
		//alert.setMessage("请对当前任务进行操作!");
		
/*		alert.setButton(DialogInterface.BUTTON_POSITIVE, "完成",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				db.updateStatus(idString);
				String newTimeStatus = timeStatus.trim().substring(0,11) + " 已完成";
				time.setText(" " + newTimeStatus);
			}
		});
		alert.setButton(DialogInterface.BUTTON_NEUTRAL,"编辑",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent toCard = new Intent(TodoListUI.this,TodoListCardUI.class);
				toCard.putExtra("id", idString);
				toCard.putExtra("messionName", messionName);
				toCard.putExtra("method","edit");
				alert.dismiss();
				TodoListUI.this.startActivity(toCard);
			}
		});
		alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alert.dismiss();
			}
		});
*/
		String[] items = new String[]{"完成","编辑","删除"};
		alert.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						db.updateStatus(idString);
						String newTimeStatus = timeStatus.trim().substring(0,11) + " 已完成";
						time.setText(" " + newTimeStatus);
						break;
					case 2:
						db.delete(idString);
						view.setVisibility(View.GONE);
						break;
					case 1:
						Intent toCard = new Intent(TodoListUI.this,TodoListCardUI.class);
						toCard.putExtra("id", idString);
						toCard.putExtra("messionName", messionName);
						toCard.putExtra("method","edit");
						TodoListUI.this.startActivityForResult(toCard,Card);
						break;
				}
			}
		});
		
		alert.create().show();
	}
	
	private void makeTodoView(String id , String name , String time ,String status){
		LayoutInflater factorys = LayoutInflater.from(TodoListUI.this);
		View view = factorys.inflate(R.layout.todolist_card, null);
		
		TextView title = (TextView) view.findViewById(R.id.todolist_card_title);
		TextView timeView = (TextView) view.findViewById(R.id.todolist_card_time);
		TextView idView = (TextView) view.findViewById(R.id.todolist_card_data);
		title.setText(" " + name);
		timeView.setText(" " + time + " " + status);
		idView.setContentDescription(id);
		container.addView(view,0);
	}
}
