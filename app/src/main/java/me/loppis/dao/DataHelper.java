package me.loppis.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DataHelper
{
    String result="";
    /**
     * 设置tudo信息
     * @return
     */
    public String setTodo(String id,String content)
    { 
        try
        {
        	String target="http://172.16.103.13:8080/Android/setTodo.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpRequest = new HttpPost(target);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id));
            params.add(new BasicNameValuePair("content", content));
            HttpResponse httpResponse;
        	httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
            httpResponse=httpClient.execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
                result+=EntityUtils.toString(httpResponse.getEntity());
            }
            else {
                result="0";
            }
            return result;
        } catch (Exception e)
        {
            return "0";
        }
    }
    
    /**
     * 设置finish信息
     */
    public String setFinish(String id,String content)
    {
        try
        {
        	String target="http://172.16.103.13:8080/Android/setFinish.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpRequest = new HttpPost(target);
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id));
            params.add(new BasicNameValuePair("content", content));
            HttpResponse httpResponse;
        	httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
            httpResponse=httpClient.execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
                result+=EntityUtils.toString(httpResponse.getEntity());
            }
            else {
                result="0";
            }
            return result;
        } catch (Exception e)
        {
            return "0";
        }
    }
    /**
     * 获取登录表中的数据
     */
    public String getLoginData(String id,String password)
    {
    	String target="http://172.16.103.13:8080/Android/loginData.php?id="+id+"&pass="+password;
    	//String target="http://172.16.103.13:8080/Android/loginData.php?id=yoge1&pass=yoge2";
        URL url;
        try
        {
            url=new URL(target);
            HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
            InputStreamReader isr=new InputStreamReader(urlConn.getInputStream());
            BufferedReader buffer=new BufferedReader(isr);
            String inputline;
            while((inputline=buffer.readLine())!=null)
            {
                result=inputline;
            }
            isr.close();
            urlConn.disconnect();
            return result;
        } catch (Exception e)
        {
            return "0";
        }
    }
    /**
     * 获取finish的信息
     */
    public String getFinish(String id)
    {	
    	String target="http://172.16.103.13:8080/Android/finishData.php?content="+id;
        URL url;
        try
        {
            url=new URL(target);
            HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
            InputStreamReader isr=new InputStreamReader(urlConn.getInputStream());
            BufferedReader buffer=new BufferedReader(isr);
            String inputline;
            while((inputline=buffer.readLine())!=null)
            {
                result=inputline;
            }
            isr.close();
            urlConn.disconnect();
            return result;
        } catch (Exception e)
        {
            return "0";
        }
    }
    /**
     * 获取todo的信息
     */
    public String getTodo(String id)
    {
    	String target="http://172.16.103.13:8080/Android/todoData.php?content="+id;
        URL url;
        try
        {
            url=new URL(target);
            HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
            InputStreamReader isr=new InputStreamReader(urlConn.getInputStream());
            BufferedReader buffer=new BufferedReader(isr);
            String inputline;
            while((inputline=buffer.readLine())!=null)
            {
                result=inputline;
            }
            isr.close();
            urlConn.disconnect();
            return result;
        } catch (Exception e)
        {
            return "0";
        }
    }
    /**
     * 判断网络是否能连接
     */
	public boolean isConnect(Context context)
	{
		try{
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
			if(cm!=null)
			{
				NetworkInfo info=cm.getActiveNetworkInfo();
				if(info!=null&&info.isConnected())
				{
					if(info.getState()==NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}catch(Exception e)
		{
			return false;
		}
		return false;
	}
}



