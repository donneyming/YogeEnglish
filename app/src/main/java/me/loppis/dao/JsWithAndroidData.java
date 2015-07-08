package me.loppis.dao;

public class JsWithAndroidData {
	public String todoData = "" ;
	public int untodo ;
	public int todo ;
	public JsWithAndroidData() {}

	@android.webkit.JavascriptInterface
	public void setData(String dataString){
		todoData = dataString ;
	}

	@android.webkit.JavascriptInterface
	public String getData(){
		return todoData ;
	}

	@android.webkit.JavascriptInterface
	public int getUntodo(){
		return untodo ;
	}

	@android.webkit.JavascriptInterface
	public int getTodo(){
		return todo ;
	}
}
