package com.liuge.ximalaya.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.liuge.ximalaya.utils.Constants;
import com.liuge.ximalaya.utils.LogUtil;

/**
 * FileName: XimalayaDBHelper
 * Author: LiuGe
 * Date: 2020/8/2 15:57
 * Description: 喜马拉雅APP的数据库帮助工具
 */
public class XimalayaDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "XimalayaDBHelper";

    public XimalayaDBHelper(@Nullable Context context) {
        // name:数据库的名字,factory:游标工厂 version:版本号
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.d(TAG, "onCreate...");
        // 创建订阅数据表
        //字段:
        String subTbSql = "create table " + Constants.SUB_TB_NAME + "(" +
                Constants.SUB_ID + " integer primary key autoincrement," +
                Constants.SUB_COVER_URL + " varchar," +
                Constants.SUB_TITLE + " varchar," +
                Constants.SUB_DESCRIPTION + " varchar," +
                Constants.SUB_PLAY_COUNT + " INTEGER," +
                Constants.SUB_TRACKS_COUNT + " INTEGER," +
                Constants.SUB_AUTHOR_NAME + " varchar," +
                Constants.SUB_ALBUM_ID + " integer" +
                ")";
        db.execSQL(subTbSql);
        // 创建历史记录表
        String historyTbSql = "create table " + Constants.HISTORY_TB_NAME + "(" +
                Constants.HISTORY_ID + " integer primary key autoincrement," +
                Constants.HISTORY_TITLE + " varchar," +
                Constants.HISTORY_COVER + " varchar," +
                Constants.HISTORY_PLAY_COUNT + " INTEGER," +
                Constants.HISTORY_DURATION + " INTEGER," +
                Constants.HISTORY_UPDATE_TIME + " varchar," +
                Constants.HISTORY_TRACK_ID + " integer," +
                Constants.HISTORY_AUTHOR + " varchar" +
                ")";
        db.execSQL(historyTbSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
