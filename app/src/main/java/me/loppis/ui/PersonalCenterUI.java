package me.loppis.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.loppis.R;
import me.loppis.dao.DBTools;
import me.loppis.dao.JsWithAndroidData;
import me.loppis.dao.LocalStorage;

public class PersonalCenterUI extends Activity {
	
	DBTools db ;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center);
		
		db = new DBTools(this);
		
		//返回主界面
		ImageView back = (ImageView) findViewById(R.id.personal_center_back);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				PersonalCenterUI.this.finish();
			}
		});
		
		//初始化侧滑个人信息
		//初始化头像
		Bitmap bm=null;
		ImageView view1=(ImageView)findViewById(R.id.personal_center_header);
		if((bm=haveHead())!=null)
		{
		   view1.setImageBitmap(bm);
		}
		
		//初始化昵称和签名
		TextView nameTextView = (TextView)findViewById(R.id.personal_center_name);
		TextView introTextView = (TextView)findViewById(R.id.personal_center_intro);
		LocalStorage storage = new LocalStorage(PersonalCenterUI.this);
		if(storage.has("username")){
			String name = storage.getString("username", "Please Edit");
			nameTextView.setText(name);
		}
		
		if(storage.has("userintro")){
			String introString = storage.getString("userintro", "Please Edit");
			introTextView.setText(introString);
		}
		
		initView();
		
	}
	
	public Bitmap haveHead()
	{
	    Bitmap bitmap=BitmapFactory.decodeFile("data/data/me.loppis/files/head.jpg");
	    return bitmap;
	}
	
	
	//初始化滑动页面
	private void initView() {
		ViewPager viewPager = (ViewPager) findViewById(R.id.personal_center_viewpager);

		LayoutInflater lf = getLayoutInflater().from(this);
		final View finish = lf.inflate(R.layout.personal_center_finish, null);
		final View todo = lf.inflate(R.layout.personal_center_todo, null);
		final View chart = lf.inflate(R.layout.personal_center_chart, null);
		
		
		
		//数据显示逻辑
		//finish部分 ：
		LinearLayout finish_container = (LinearLayout) finish.findViewById(R.id.personal_center_finish_container);	
		Cursor cursor = db.selectAll(DBTools.FINISH);
		while(cursor.moveToNext()){
			String name = cursor.getString(1);
			String time = cursor.getString(2);
			View tmpView = makeView(name, time);
			finish_container.addView(tmpView, 0);
		}
		
		//TodoList部分：
		LinearLayout todo_container = (LinearLayout) todo.findViewById(R.id.personal_center_todo_container);
		Cursor cursor2 = db.selectTodoFinished();
		while(cursor2.moveToNext()){
			String name = cursor2.getString(1);
			String time = cursor2.getString(2);
			String status = cursor2.getString(3);
			View tmpView = makeView(name, time ,status);
			todo_container.addView(tmpView, 0);
		}
		
		JsWithAndroidData data = new JsWithAndroidData() ;
		int tododata = 0 ;
		int untododata = 0 ;
		Cursor cursor3 = db.selectAll(DBTools.TODO);
		while(cursor3.moveToNext()){
			String code = cursor3.getString(4);
			if(code.equals("1")){
				tododata ++ ;
			}else if(code.equals("0")){
				untododata++ ;
			}
		}
		
		data.todo = tododata ;
		data.untodo = untododata ;
		
		WebView webNavi = (WebView) chart.findViewById(R.id.personal_center_webview);
		webNavi.setWebChromeClient(new WebChromeClient());
		webNavi.setWebViewClient(new WebViewClient());
		
		//获取WebView设置对象
		WebSettings setting = webNavi.getSettings() ;
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(false);
		setting.setBuiltInZoomControls(false);
		webNavi.addJavascriptInterface(data, "TODO");
		webNavi.loadUrl("file:///android_asset/chart.html");
		
		
		

		final ArrayList<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(finish);
		viewList.add(todo);
		viewList.add(chart);

		final ArrayList<String> titleList = new ArrayList<String>();// 每个页面的Title数据
		titleList.add("View1");
		titleList.add("View2");
		titleList.add("View3");
		
		//顶部导航三角形
		
		final int[] top = new int[]{
				R.id.personal_center_icon0,
				R.id.personal_center_icon1,
				R.id.personal_center_icon2
		};
		
		final View[] currView = new View[]{finish , todo ,chart};
		
		
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int pos) {
				
				//上方导航逻辑
				for(int i=0;i<3;i++){
					ImageView tmp = (ImageView) findViewById(top[i]);
					tmp.setVisibility(View.INVISIBLE);
				}
				
				ImageView tmp = (ImageView) findViewById(top[pos]);
				tmp.setVisibility(View.VISIBLE);
				
				//数据显示逻辑
				
/*				if(pos == 0){
					
					LinearLayout container = (LinearLayout) finish.findViewById(R.id.personal_center_todo_container);
					
					Cursor cursor = db.selectAll(DBTools.FINISH);
					while(cursor.moveToNext()){
						String name = cursor.getString(1);
						String time = cursor.getString(2);
						View tmpView = makeView(name, time);
						container.addView(tmpView, 0);
					}
					
				}else if(pos == 1){
					
				}else if(pos == 2){
					
				}
*/
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});

		PagerAdapter pagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;
			}

			@Override
			public int getCount() {

				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(viewList.get(position));

			}

			@Override
			public int getItemPosition(Object object) {
				
				return super.getItemPosition(object);
			}

			@Override
			public CharSequence getPageTitle(int position) {

				return titleList.get(position);
				// 直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。
				// 当然你可以使用。
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList.get(position));

				return viewList.get(position);
			}

		};
		
		viewPager.setAdapter(pagerAdapter);
		
	}
	
	private View makeView(String name , String time){
		LayoutInflater factorys = LayoutInflater.from(PersonalCenterUI.this);
		View view = factorys.inflate(R.layout.personal_center_card, null);
		
		TextView title = (TextView) view.findViewById(R.id.personal_center_card_title);
		TextView timeView = (TextView) view.findViewById(R.id.personal_center_card_timeshow);
		//TextView idView = (TextView) view.findViewById(R.id.todolist_card_data);
		title.setText(" " + name);
		timeView.setText(" " + time + " ");
		return view ;
	}
	
	private View makeView(String name , String time ,String status){
		LayoutInflater factorys = LayoutInflater.from(PersonalCenterUI.this);
		View view = factorys.inflate(R.layout.personal_center_card, null);
		
		TextView title = (TextView) view.findViewById(R.id.personal_center_card_title);
		TextView timeView = (TextView) view.findViewById(R.id.personal_center_card_timeshow);
		//TextView idView = (TextView) view.findViewById(R.id.todolist_card_data);
		title.setText(" " + name);
		timeView.setText(" " + time + " " + status );
		return view ;
	}
}
