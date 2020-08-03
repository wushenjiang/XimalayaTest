package com.liuge.ximalaya.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liuge.ximalaya.base.BaseApplication;
import com.liuge.ximalaya.utils.Constants;
import com.liuge.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: HistoryDao
 * Author: LiuGe
 * Date: 2020/8/3 16:36
 * Description: 历史的dao
 */
public class HistoryDao implements IHistoryDao {

    private static final String TAG = "HistoryDao";
    private final XimalayaDBHelper mDBHelper;
    private IHistoryDaoCallback mCallback = null;
    private Object mLock = new Object();

    public HistoryDao() {

        mDBHelper = new XimalayaDBHelper(BaseApplication.getAppContext());
    }

    @Override
    public void setCallback(IHistoryDaoCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void addHistory(Track track) {
        synchronized (mLock) {

            SQLiteDatabase db = null;
            boolean isSuccess = false;
            try {
                db = mDBHelper.getWritableDatabase();
                // 先去删除
                db.delete(Constants.HISTORY_TB_NAME, Constants.HISTORY_TRACK_ID + "=?", new String[]{track.getDataId() + ""});
                // 删除以后再添加
                db.beginTransaction();
                ContentValues values = new ContentValues();
                // 封装数据
                values.put(Constants.HISTORY_TITLE, track.getTrackTitle());
                values.put(Constants.HISTORY_PLAY_COUNT, track.getPlayCount());
                values.put(Constants.HISTORY_COVER, track.getCoverUrlLarge());
                values.put(Constants.HISTORY_DURATION, track.getDuration());
                values.put(Constants.HISTORY_UPDATE_TIME, track.getUpdatedAt());
                values.put(Constants.HISTORY_TRACK_ID, track.getDataId());
                values.put(Constants.HISTORY_AUTHOR, track.getAnnouncer().getNickname());
                db.insert(Constants.HISTORY_TB_NAME, null, values);
                db.setTransactionSuccessful();
                isSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                isSuccess = false;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallback != null) {
                    mCallback.onHistoryAdd(isSuccess);
                }
            }
        }

    }

    @Override
    public void delHistory(Track track) {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            boolean isDeleteSuccess = false;
            try {
                db = mDBHelper.getWritableDatabase();
                db.beginTransaction();
                //删除数据
                int delete = db.delete(Constants.HISTORY_TB_NAME, Constants.HISTORY_TRACK_ID + "=?", new String[]{track.getDataId() + ""});
                LogUtil.d(TAG, "delete -->" + delete);
                db.setTransactionSuccessful();
                isDeleteSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                isDeleteSuccess = false;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallback != null) {
                    mCallback.onHistoryDel(isDeleteSuccess);
                }
            }
        }
    }

    @Override
    public void clearHistory() {
        synchronized (mLock) {
            SQLiteDatabase db = null;
            boolean isDeleteSuccess = false;
            try {
                db = mDBHelper.getWritableDatabase();
                db.beginTransaction();
                //删除数据
                int delete = db.delete(Constants.HISTORY_TB_NAME, null, null);
                LogUtil.d(TAG, "delete -->" + delete);
                db.setTransactionSuccessful();
                isDeleteSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                isDeleteSuccess = false;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                if (mCallback != null) {
                    mCallback.onHistoryClean(isDeleteSuccess);
                }
            }
        }
    }

    @Override
    public void listHistories() {
        synchronized (mLock) {
            // 从数据表中查询所有的历史记录
            SQLiteDatabase db = null;
            List<Track> histories = new ArrayList<>();
            try {
                db = mDBHelper.getReadableDatabase();
                db.beginTransaction();
                Cursor cursor = db.query(Constants.HISTORY_TB_NAME, null, null, null, null, null, "_id desc");
                while (cursor.moveToNext()) {
                    Track track = new Track();
                    int trackId = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_TRACK_ID));
                    track.setDataId(trackId);
                    String title = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_TITLE));
                    track.setTrackTitle(title);
                    int playCount = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_PLAY_COUNT));
                    track.setPlayCount(playCount);
                    int duration = cursor.getInt(cursor.getColumnIndex(Constants.HISTORY_DURATION));
                    track.setDuration(duration);
                    long updateTime = cursor.getLong(cursor.getColumnIndex(Constants.HISTORY_UPDATE_TIME));
                    track.setUpdatedAt(updateTime);
                    String cover = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_COVER));
                    track.setCoverUrlLarge(cover);
                    track.setCoverUrlMiddle(cover);
                    track.setCoverUrlSmall(cover);
                    String author = cursor.getString(cursor.getColumnIndex(Constants.HISTORY_AUTHOR));
                    Announcer announcer = new Announcer();
                    announcer.setNickname(author);
                    track.setAnnouncer(announcer);
                    histories.add(track);
                }
                db.setTransactionSuccessful();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
                // 通知UI更新
                if (mCallback != null) {
                    mCallback.onHistoriesLoaded(histories);
                }
            }
        }
    }
}
