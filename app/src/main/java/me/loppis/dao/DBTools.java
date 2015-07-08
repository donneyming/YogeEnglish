package me.loppis.dao;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBTools
{
    public static final int TODO=1; 
    public static final int FINISH=2;
    private SqliteHandle sqh;
    private SQLiteDatabase db;
    public DBTools(Context context)
    {
        sqh=new SqliteHandle(context);
        db=sqh.getWritableDatabase();
    }
    /**
     * 向数据库中写入数据
     *
     */
    public void add(String id,String name,String time,int kind)
    {
        switch (kind)
        {
        case TODO:
            db.execSQL("insert into todo (id,name,time,status,statuscode) values (?,?,?,?,?)",new Object[]{id,name,time,"进行中","0"});
            break;
        case FINISH:
            db.execSQL("insert into finish (id,name,time) values (?,?,?)",new Object[]{id,name,time});
            break;
        default:
            break;
        }
       
    }
    /**
     * 更新数据库信息
     * @param id
     * @param name
     */
    public void updateName(String id,String name)
    {
        db.execSQL("update todo set name=? where id=?",new Object[]{name,id});
    }
    /**
     * 更新数据库信息
     *
     */
    public void updateStatus(String id)
    {
        db.execSQL("update todo set status=? , statuscode = ? where id=?",new Object[]{"已完成","1",id});
    }
    /**
     * 查找数据
     * @param data
     * @return result
     */
    private String find(String data)
    {
        Cursor cursor=db.rawQuery("select id,name,time,status from yoge where xxx=?",new String []{"xxx"} );
        if(cursor.moveToNext())
        {
            String result=cursor.getString(0);
            return result;
        }
        return null;
    }
    /**
     * 根据属性来删除信息
     * @param id
     */
    public void delete(String id)
    {
        db.execSQL("delete from todo where id = ?",new Object[]{id});
    }
    /**
     * 查找多条数据，用list来返回
     * @return
     */
    public List<Object> findData()
    {
        List<Object> list=new ArrayList<Object>();
        Cursor cursor = db.rawQuery("select * from yoge",new String[]{});
        while(cursor.moveToNext())
        {
            list.add(cursor.getString(0));
        }
        return list;
    }
    /**
     * 获取数据库数据条数
     * @return
     */
    public long getCount()
    {
        Cursor cursor = db.rawQuery("select count(xxx) from yoge", null);
        long result=cursor.getLong(0);
        return result;
    }
    
    /**
     * 查找数据
     *
     */
    public Cursor select(String id,int kind)
    {
        Cursor cursor=null;
        switch (kind)
        {
        case TODO:
            cursor=db.rawQuery("select id,name,time,status from todo where id = ?",new String[]{id} );
            cursor.moveToNext();
            break;
        case FINISH:
            cursor=db.rawQuery("select id,name,time from finish where id= ?",new String[]{id} );
            cursor.moveToNext();
            break;
        default:
            break;
        }
        return cursor;
    }
    
    public Cursor selectAll(int kind)
    {
        Cursor cursor=null;
        switch (kind)
        {
        case TODO:
            cursor=db.rawQuery("select id,name,time,status,statuscode from todo",new String[]{});
            break;
        case FINISH:
            cursor=db.rawQuery("select id,name,time from finish",new String[]{});
            break;
        default:
            break;
        }
        return cursor;
    }
    
    public Cursor selectTodoFinished(){  //选择todo中statuscode为1的，即已完成的
    	Cursor cursor=null;
    	cursor=db.rawQuery("select id,name,time,status from todo where statuscode = ?",new String[]{"1"});
    	return cursor ;
    }
    
    public void deleteAll(int kind){
    	switch (kind)
        {
	        case TODO:
	        	db.delete("todo", null, null);
	            break;
	        case FINISH:
	        	db.delete("finish", null, null);
	            break;
	        default:
	            break;
	    }
    }

}
