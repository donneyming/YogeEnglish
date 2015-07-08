package me.loppis.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import me.loppis.R;
import me.loppis.dao.LocalStorage;

public class MainUI extends Activity {
	
	private View view1, view2,view3,view4,view5,view6;
	private ViewPager viewPager;
	private List<View> viewList;
	private List<String> titleList;
	private LinearLayout thisLayout ;
	public int [] color ;
	public int bgColorIndex = 0 ;
	
	//侧滑对象 ，在initSlidingMenu()中调用
	public SlidingMenu menu ;
	
	//是否退出标志
	private int exitFlag = 0 ; 
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        thisLayout = (LinearLayout) findViewById(R.id.main_layout);
        color = new int[]{
        		R.color.flat_blue_light, R.color.flat_yellow,
        		R.color.flat_green_light,R.color.flat_orange,
        		R.color.theme_pink 
        };
        initView();
        initSlidingMenu();
        
    }
    
    //切换到信息内容Activity ContentUI.java
    private OnClickListener changeIntent = new OnClickListener() {		
		public void onClick(View v) {
			Intent content = new Intent(MainUI.this,ContentUI.class);
			startActivity(content);
		}
	};

	//判断是否点击后退键退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			exitFlag ++ ;
			if(exitFlag==2){
				exitFlag = 0 ;
				this.finish();
				//return false ;
			}else{
				//否则出现友情提示
				
				Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
		        new Handler().postDelayed(new Runnable() {
					public void run() {
						exitFlag = 0 ;
					}
		        }, 3000); //3秒后表示误按 , 还原状态
		        return true ;
			}
		}else if(keyCode == KeyEvent.KEYCODE_MENU){
			//这里是关闭打开侧滑菜单
			menu.toggle();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//初始化主页面滑动效果
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		LayoutInflater lf = getLayoutInflater().from(this);
		view1 = lf.inflate(R.layout.view1, null);
		view2 = lf.inflate(R.layout.view2, null);
		view3 = lf.inflate(R.layout.view3, null);
		view4 = lf.inflate(R.layout.view4, null);
		view5 = lf.inflate(R.layout.view5, null);
		view6 = lf.inflate(R.layout.view6, null);

		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);
		viewList.add(view5);
		viewList.add(view6);

		titleList = new ArrayList<String>();// 每个页面的Title数据				
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int arg0) {
				int index = new Random().nextInt(5);
				thisLayout.setBackgroundColor(MainUI.this.getResources().getColor(color[index]));
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		}) ;

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
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList.get(position));
				return viewList.get(position);
			}

		};

		viewPager.setAdapter(pagerAdapter);
	}

	//初始化侧滑边栏
	private void initSlidingMenu(){
		
		//final SlidingMenu menu = new SlidingMenu(this);  
		menu = new SlidingMenu(this);
		
		// 滑动方向(LEFT,RIGHT,LEFT_RIGHT)  
		menu.setMode(SlidingMenu.LEFT);  
		// 滑动显示SlidingMenu的范围  
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);  
		// 菜单的宽度  
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
		menu.setBehindWidth(dm.widthPixels-80);
		// 把SlidingMenu附加在Activity上  
		// SlidingMenu.SLIDING_WINDOW:菜单拉开后高度是全屏的  
		// SlidingMenu.SLIDING_CONTENT:菜单拉开后高度是不包含Title/ActionBar的内容区域  
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		
		// 加载菜单的布局文件  
		menu.setMenu(R.layout.sliding_menu); 
		
		//加载ListView内容
		ListView lv = (ListView) findViewById(R.id.sliding_layout_listview);
		String[] menuItem = new String[]{
				"基础的世界","进阶的节奏","个人中心","番茄土豆","设置中心"
		};
		List<Map<String, String>> listitems = new ArrayList<Map<String,String>>();
		for(int i=0;i<menuItem.length;i++){
			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put("title", menuItem[i]);
			listitems.add(tmp);
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listitems,
				R.layout.sliding_menu_items, new String[]{"title"}, new int[]{R.id.tv});
		
		lv.setAdapter(simpleAdapter);
		  
		// 监听slidingMenu打开  
		menu.setOnOpenedListener(new OnOpenedListener() {  
		    public void onOpened() { 
		    	
		    }  
		});  
		// 监听slidingMenu关闭  
		menu.setOnClosedListener(new OnClosedListener() {  
		    public void onClosed() {  
		    	
		    }  
		});  
		  
		// 显示SlidingMenu  
		menu.showMenu(true);
		  
		// 关闭SlidingMenu  
		menu.toggle();
			
		
		//点击头像到个人中心
		ImageView header = (ImageView) findViewById(R.id.sliding_menu_header);
		TextView username = (TextView) findViewById(R.id.sliding_menu_username);
		TextView intro = (TextView) findViewById(R.id.sliding_menu_intro);
		
		OnClickListener toPersonnalCenterListener = new OnClickListener() {			
			public void onClick(View v) {
				Intent toPersonalCenter = new Intent(getApplicationContext(),PersonalCenterUI.class);
				startActivity(toPersonalCenter);
			}
		};
		
		//点击后到个人中心
		header.setOnClickListener(toPersonnalCenterListener);
		username.setOnClickListener(toPersonnalCenterListener);
		intro.setOnClickListener(toPersonnalCenterListener);
		
		
		//注册侧滑菜单动作
		//点击后切换到各个不同的Activity
		ListView slidingMennListView = (ListView) findViewById(R.id.sliding_layout_listview);
		slidingMennListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				switch (position) {
				case 0:
					menu.toggle();
					break ;
				case 1:
					Intent toAdvanvedMain = new Intent(getApplicationContext(),AdvancedMainUI.class);
					startActivity(toAdvanvedMain);
					break;
				case 2:
					Intent toPersonalCenter = new Intent(getApplicationContext(),PersonalCenterUI.class);
					startActivity(toPersonalCenter);
					break;
				case 3:
					Intent toTodolist = new Intent(getApplicationContext(),TodoListUI.class);
					startActivity(toTodolist);
					break ;
				case 4:
					Intent toSettingCenter = new Intent(getApplicationContext(),SettingCenterUI.class);
					startActivity(toSettingCenter);
					break;

				default:
					break;
				}
			}
		});
		
		
		//初始化侧滑个人信息
		//初始化头像
		Bitmap bm=null;
		ImageView view1=(ImageView)findViewById(R.id.sliding_menu_header);
		if((bm=haveHead())!=null){
		   view1.setImageBitmap(bm);
		}
		
		//初始化用户名和个人签名
		
		LocalStorage storage = new LocalStorage(MainUI.this);
		if(storage.has("username")){
			String name = storage.getString("username", "Please Edit");
			username.setText(name);
		}
		
		if(storage.has("userintro")){
			String introString = storage.getString("userintro", "Please edit your profile");
			intro.setText(introString);
		}
		
		//切换到登录界面
		TextView login = (TextView) findViewById(R.id.sliding_menu_register_login);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toLogin = new Intent(MainUI.this,RegisterOrLoginUI.class);
				startActivity(toLogin);
			}
		});
		
	}
	
	//判断是否有头像存在
	public Bitmap haveHead(){
	    Bitmap bitmap=BitmapFactory.decodeFile("data/data/me.loppis/files/head.jpg");
	    return bitmap;
	}
	
	
	//这个事件由相应的XML文件里面的标签触发
	public void toContent(View sourceView){
		Intent toContentUI = new Intent(MainUI.this,ContentUI.class);
		String messionName = sourceView.getContentDescription().toString();
		toContentUI.putExtra("messionName", messionName);
		
		switch (sourceView.getId()){
			case R.id.jqm_1_noun:
				toContentUI.putExtra("filename","jqm_1_noun");				
				break;
			case R.id.jqm_2_verb:
				toContentUI.putExtra("filename","jqm_2_verb");
				break;
			case R.id.jqm_3_article:
				toContentUI.putExtra("filename", "jqm_3_article");
				break;
			case R.id.jqm_4_numeral:
				toContentUI.putExtra("filename", "jqm_4_numeral");
				break;
			case R.id.jqm_5_pronoun:
				toContentUI.putExtra("filename", "jqm_5_pronoun");
				break;
			case R.id.jqm_6_adjective:
				toContentUI.putExtra("filename", "jqm_6_adjective");
				break;
			default:
				break;
		}
		
		startActivity(toContentUI);
	}


	@Override
	protected void onStart() {
		super.onStart();
		//点击头像到个人中心
		ImageView header = (ImageView) findViewById(R.id.sliding_menu_header);
		TextView username = (TextView) findViewById(R.id.sliding_menu_username);
		TextView intro = (TextView) findViewById(R.id.sliding_menu_intro);
		
		//初始化侧滑个人信息
		//初始化头像
		Bitmap bm=null;
		ImageView view1=(ImageView)findViewById(R.id.sliding_menu_header);
		if((bm=haveHead())!=null){
		   view1.setImageBitmap(bm);
		}
		
		//初始化用户名和个人签名
		
		LocalStorage storage = new LocalStorage(MainUI.this);
		if(storage.has("username")){
			String name = storage.getString("username", "Please Edit");
			username.setText(name);
		}
		
		if(storage.has("userintro")){
			String introString = storage.getString("userintro", "Please edit your profile");
			intro.setText(introString);
		}
		
		//查询登录状态
		if(storage.has("login")){
			//如果有 看是否成功登录
			TextView textView = (TextView) findViewById(R.id.sliding_menu_register_login);
			textView.setText("已登录");
			int login = storage.getInt("login", 0) ;
			if(login==1){
				textView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Toast.makeText(getApplicationContext(), "已登录", Toast.LENGTH_SHORT).show();
					}
				});
			}else if(login == 0){
				textView.setText("登录");
				textView.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						Intent toLogin = new Intent(MainUI.this,RegisterOrLoginUI.class);
						startActivity(toLogin);						
					}
				});
			}
		}
	
	}
	
}
	
