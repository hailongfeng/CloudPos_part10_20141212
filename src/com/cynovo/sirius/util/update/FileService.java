package com.cynovo.sirius.util.update;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 业务bean
 *
 */
public class FileService {
    private UpdateDBHelper openHelper;

    public FileService(Context context) {
        openHelper = new UpdateDBHelper(context);
    }
    /**
     * 获取每条线程已经下载的文件长度
     * @param path
     * @return
     */
    public Map<Integer, Integer> getData(String path, String version){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=? " +
        		"and version=?", new String[]{path, version});
        Map<Integer, Integer> data = new HashMap<Integer, Integer>();
        while(cursor.moveToNext()){
            data.put(cursor.getInt(0), cursor.getInt(1));
        }
        cursor.close();
        db.close();
        return data;
    }
    /**
     * 保存每条线程已经下载的文件长度
     * @param path
     * @param map
     */
    public void save(String path,  Map<Integer, Integer> map, String version){//int threadid, int position
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            for(Map.Entry<Integer, Integer> entry : map.entrySet()){
                db.execSQL("insert into filedownlog(downpath, threadid, downlength, version) values(?,?,?,?)",
                        new Object[]{path, entry.getKey(), entry.getValue(), version});
            }
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
        db.close();
    }
    /**
     * 实时更新每条线程已经下载的文件长度
     * @param path
     * @param map
     */
    public void update(String path, int threadId, int pos, String version){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("update filedownlog set downlength=? where downpath=? and threadid=? and version=?",
                new Object[]{pos, path, threadId, version});
        db.close();
    }
    /**
     * 当文件下载完成后，删除对应的下载记录
     * @param path
     */
    public void delete(String path, String version){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("delete from filedownlog where downpath=? and version=?", new Object[]{path, version});
        db.close();
    }
    
}