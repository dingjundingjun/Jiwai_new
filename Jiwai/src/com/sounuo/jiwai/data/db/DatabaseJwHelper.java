package com.sounuo.jiwai.data.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 叽歪数据库
 * @author gq
 *
 */
public class DatabaseJwHelper extends SQLiteOpenHelper{
	private static DatabaseJwHelper mInstance = null;

	public synchronized static DatabaseJwHelper getInstance(
			Context context) {
		if (mInstance == null) {
			mInstance = new DatabaseJwHelper(context);
		}
		return mInstance;
	};


	public DatabaseJwHelper(Context context, String name,CursorFactory factory, int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
	}

	public DatabaseJwHelper(Context context, String name) {
		this(context, name, 1);
	}

	public DatabaseJwHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DatabaseJwHelper(Context context) {
		this(context, PersonalInfoColums.DB_NAME,PersonalInfoColums.DB_VERSION);
	}
	
	private static final String DB_PERSONINFO_CREATE = "CREATE TABLE "
			+ PersonalInfoColums.TABLE_NAME + " (" 
			+ PersonalInfoColums.ACCOUNT_ID+" INTEGER PRIMARY KEY,"
			+ PersonalInfoColums.NICK_NAME+ " TEXT," 
			+ PersonalInfoColums.PASSWORD + " TEXT,"
			+ PersonalInfoColums.PHOTO_PATH+ " TEXT,"
			+ PersonalInfoColums.SEX + " TEXT," 
			+ PersonalInfoColums.GRADE + " TEXT " + ");"; 
	
	
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( DB_PERSONINFO_CREATE);
    }
    
	/** 升级数据库 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PersonalInfoColums.TABLE_NAME);
		onCreate(db);
	}
	
	// 更新个人信息数据库
	public void personInfoTableRecreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + PersonalInfoColums.TABLE_NAME);
		db.execSQL(DB_PERSONINFO_CREATE);
	}

	public boolean tabIsExist(String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
