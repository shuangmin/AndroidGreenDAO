package com.cn.greendaotest;


import com.cn.speedchat.greendao.DaoMaster;
import com.cn.speedchat.greendao.DaoMaster.OpenHelper;
import com.cn.speedchat.greendao.DaoSession;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ControlApp extends Application{

	private static DaoMaster daoMaster;  
	private static DaoSession daoSession;
	public static  SQLiteDatabase db;
	public static final String DB_NAME = "dbname.db";

  /** 
   * 取得DaoMaster 
   *  
   * @param context 
   * @return 
   */  
  public static DaoMaster getDaoMaster(Context context) {  
      if (daoMaster == null) {  
          OpenHelper helper = new DaoMaster.DevOpenHelper(context,DB_NAME, null);  
          daoMaster = new DaoMaster(helper.getWritableDatabase());  
      }  
      return daoMaster;  
  }  
   
  /** 
   * 取得DaoSession 
   *  
   * @param context 
   * @return 
   */  
  public static DaoSession getDaoSession(Context context) {  
      if (daoSession == null) {  
          if (daoMaster == null) {  
              daoMaster = getDaoMaster(context);  
          }  
          daoSession = daoMaster.newSession();  
      }  
      return daoSession;  
  }  
  /** 
   * 得到Datebase 
   *  
   * @param context 
   * @return 
   */  
  public static SQLiteDatabase getSQLDatebase(Context context) {  
      if (daoSession == null) {  
          if (daoMaster == null) {  
              daoMaster = getDaoMaster(context);  
          }  
          db = daoMaster.getDatabase();  
      }  
      return db;  
  }  
  
	@Override
	public void onCreate() {
		
	}

}