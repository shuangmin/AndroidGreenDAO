package com.cn.greendaotest;

import java.util.List;

import com.cn.speedchat.greendao.DBHelper;
import com.cn.speedchat.greendao.SessionEntity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	private DBHelper dBManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dBManager = DBHelper.getInstance(this);
		SessionEntity entity = new SessionEntity();
		entity.setFrom("A");
		entity.setGossip("大家好吗？我来了...");
		entity.setGossipid(10);
		entity.setSessionid("abcdefg");
		entity.setSessiontype(1);
		entity.setTo("B");
		dBManager.saveSession(entity);
		//这样就把entity对象存数据库了，然后我们新建一个SessionEntity再读一下
		List<SessionEntity> listentity = dBManager.loadAllSession();
		for(int i=0;i<listentity.size();i++)
		{
			SessionEntity tmpEntity = listentity.get(i);
			Log.v("tmpEntity.getFrom()",tmpEntity.getFrom());
			Log.v("tmpEntity.getGossip()",tmpEntity.getGossip());
			Log.v("tmpEntity.getGossipid()",tmpEntity.getGossipid()+"");
			Log.v("tmpEntity.getSessionid()",tmpEntity.getSessionid());
			Log.v("tmpEntity.getSessiontype()",tmpEntity.getSessiontype()+"");
			Log.v("tmpEntity.getTo()",tmpEntity.getTo());
			
		}
	}

}
