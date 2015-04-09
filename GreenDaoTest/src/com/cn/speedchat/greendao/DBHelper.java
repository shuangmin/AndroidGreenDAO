package com.cn.speedchat.greendao;

import java.util.ArrayList;
import java.util.List;

import com.cn.greendaotest.ControlApp;
import com.cn.speedchat.greendao.MqttChatEntityDao.Properties;

import de.greenrobot.dao.query.QueryBuilder;
import android.content.Context;
import android.util.Log;

public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();  
    private static DBHelper instance;  
    private static Context appContext;
    private DaoSession mDaoSession;  
    private MqttChatEntityDao chatDao;
    private SessionEntityDao sessionDao;  
    private DBHelper() {
    } 
    
    public static  DBHelper getInstance(Context context) {  
        if (instance == null) {
        		   instance = new DBHelper();  
                   if (appContext == null){  
                       appContext = context.getApplicationContext();  
                   }  
                   instance.mDaoSession = ControlApp.getDaoSession(context);
                   instance.chatDao = instance.mDaoSession.getMqttChatEntityDao();
                   instance.sessionDao = instance.mDaoSession.getSessionEntityDao();
               } 
        return instance;  
    }
    
    public  void dropSessionTable()
    {
    	SessionEntityDao.dropTable(mDaoSession.getDatabase(), true);
    }
    public void dropChatTable()
    {
    	MqttChatEntityDao.dropTable(mDaoSession.getDatabase(), true);
    }
    
    public void dropAllTable()
    {
    	MqttChatEntityDao.dropTable(mDaoSession.getDatabase(), true);
    	SessionEntityDao.dropTable(mDaoSession.getDatabase(), true);
    	ReplayEntityDao.dropTable(mDaoSession.getDatabase(), true);
    }
    
    public void createAllTable()
    {
    	MqttChatEntityDao.createTable(mDaoSession.getDatabase(), true);
    	SessionEntityDao.createTable(mDaoSession.getDatabase(), true);
    	ReplayEntityDao.createTable(mDaoSession.getDatabase(), true);
    }
    /** 
     * insert or update note 
     * @param note 
     * @return insert or update note id 
     */  
    public long saveSession(SessionEntity session){  
        return sessionDao.insertOrReplace(session);  
    }  
    
    
    public List<SessionEntity> loadAllSession() {
    	List<SessionEntity> sessions = new ArrayList<SessionEntity>();
    	List<SessionEntity> tmpSessions = sessionDao.loadAll();
    	int len = tmpSessions.size();
    	for (int i = len-1; i >=0; i--) {
				sessions.add(tmpSessions.get(i));
		}
    	return sessions;
    }  
    
    public void DeleteSession(SessionEntity entity) {
    	sessionDao.delete(entity);
    }  
    
    public void DeleteNoteBySession(SessionEntity entity) {
    	QueryBuilder<MqttChatEntity> mqBuilder = chatDao.queryBuilder();
    	mqBuilder.where(Properties.Sessionid.eq(entity.getSessionid()));
    	List<MqttChatEntity> chatEntityList = mqBuilder.build().list();
    	chatDao.deleteInTx(chatEntityList);
    }  
    
    public MqttChatEntity loadNote(long id) {
        return chatDao.load(id);  
    }  
      
    public List<MqttChatEntity> loadAllNote(){  
        return chatDao.loadAll();  
    }  
      
    /** 
     * query list with where clause 
     * ex: begin_date_time >= ? AND end_date_time <= ? 
     * @param where where clause, include 'where' word 
     * @param params query parameters 
     * @return 
     */  
      
    public List<MqttChatEntity> queryNote(String where, String... params){
    	ArrayList<MqttChatEntity> ad = new ArrayList<MqttChatEntity>();
    	return chatDao.queryRaw(where, params); 
    }
    
    public List<MqttChatEntity> loadFirstPage(String sessionid){
    	QueryBuilder<MqttChatEntity> mqBuilder = chatDao.queryBuilder();
    	mqBuilder.where(Properties.Sessionid.eq(sessionid))
    	.orderDesc(Properties.Id)
    	.limit(20);
    	List<MqttChatEntity> tmp = new ArrayList<MqttChatEntity>();
    	List<MqttChatEntity> ret = new ArrayList<MqttChatEntity>();
    	tmp = mqBuilder.list();
    	for(int i=tmp.size()-1;i>=0;i--)
    	{
    		ret.add(tmp.get(i));
    	}
    	return ret;
    }
    
    public List<MqttChatEntity> loadLastMsgBySessionid(String sessionid){
    	QueryBuilder<MqttChatEntity> mqBuilder = chatDao.queryBuilder();
    	mqBuilder.where(Properties.Sessionid.eq(sessionid))
    	.orderDesc(Properties.Id)
    	.limit(1);
    	return mqBuilder.list();
    }

    public List<MqttChatEntity> loadMoreMsgById(String sessionid, Long id){
    	QueryBuilder<MqttChatEntity> mqBuilder = chatDao.queryBuilder();
    	mqBuilder.where(Properties.Id.lt(id))
    	.where(Properties.Sessionid.eq(sessionid))
    	.orderDesc(Properties.Id)
    	.limit(20);
    	return mqBuilder.list();
    }
      
      
    /** 
     * insert or update note 
     * @param note 
     * @return insert or update note id 
     */  
    public long saveNote(MqttChatEntity chat){  
        return chatDao.insertOrReplace(chat);  
    }  
      
    /** 
     * 当进入ChatActivity的时候，将改会话的所有消息设置为已读 
     * @param note 
     * @return insert or update note id 
     */  
    public void updateReaded(String sessionid){
    	QueryBuilder<MqttChatEntity> queryBuilder = chatDao.queryBuilder();
    	queryBuilder.where(Properties.Sessionid.eq(sessionid))
    	.where(Properties.Isread.eq(false));
    	List<MqttChatEntity> tmps =  new ArrayList<MqttChatEntity>();
    	tmps = queryBuilder.list();
    	for(int i=0;i<tmps.size();i++)
    	{
    		tmps.get(i).setIsread(true);
    	}
    	chatDao.updateInTx(tmps);
    }  
    
    /** 
     * 通过sessionid得到未读的数量
     * @param note 
     * @return insert or update note id 
     */  
    public long getUnreadCountBySessionid(String sessionid){
    	QueryBuilder<MqttChatEntity> queryBuilder = chatDao.queryBuilder();
    	queryBuilder.where(Properties.Sessionid.eq(sessionid))
    	.where(Properties.Isread.eq(false));
    	return queryBuilder.list().size();
    }  
    
    /** 
     * 得到所有未读消息
     * @param note 
     * @return insert or update note id 
     */  
    public long getUnreadCount(){
    	QueryBuilder<MqttChatEntity> queryBuilder = chatDao.queryBuilder();
    	queryBuilder.where(Properties.Isread.eq(false));
    	return queryBuilder.list().size();
    }  
    
    /** 
     * insert or update noteList use transaction 
     * @param list 
     */  
    public void saveNoteLists(final List<MqttChatEntity> list){  
            if(list == null || list.isEmpty()){  
                 return;  
            }  
            chatDao.getSession().runInTx(new Runnable() {  
            @Override  
            public void run() {  
                for(int i=0; i<list.size(); i++){  
                    MqttChatEntity note = list.get(i);  
                    chatDao.insertOrReplace(note);  
                }  
            }  
        });  
          
    }  
      
    /** 
     * delete all note 
     */  
    public void deleteAllNote(){  
        chatDao.deleteAll();  
    }  
      
    /** 
     * delete note by id 
     * @param id 
     */  
    public void deleteNote(long id){  
        chatDao.deleteByKey(id);  
        Log.i(TAG, "delete");  
    }  
      
    public void deleteNote(MqttChatEntity note){  
        chatDao.delete(note);  
    }  
}
