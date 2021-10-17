package com.tistory.starcue.songgainb;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context mContext;
    private SQLiteDatabase sqLiteDatabase;

    private static final int DATABASE_VERSION = 3;

    public static final String DATABASE_NAME = "nha3.sqlite";
    public static final String DALOCATION = "/data/data/com.tistory.starcue.songgainb/databases/";

    private static final String TABLE_NAME = "songTable";

    public static final String COLUMN_INDEX = "indexField";
    public static final String COLUMN_URL = "urlField";
    private static final String COLUMN_NAME = "nameField";
    private static final String COLUMN_TIME = "timeField";
    private static final String COLUMN_IMAGE = "thumbField";
    private static final String COLUMN_POSITION = "position";

    private static final String DATABASE_CREATE_TEAM = "create table if not exists "
            + TABLE_NAME + "(" + COLUMN_INDEX + " INTEGER," + COLUMN_URL + " TEXT," + COLUMN_NAME + "TEXT," + COLUMN_TIME + "TEXT," + COLUMN_IMAGE + "BLOB," + COLUMN_POSITION + "TEXT" + ");";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    public static void setDB(Context context) {
        File folder = new File(DALOCATION);
        if (folder.exists()) {
        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = context.getResources().getAssets();
        File outfile = new File(DALOCATION + DATABASE_NAME);
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open(DATABASE_NAME, AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } else {
            }
        } catch (IOException e) {}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TEAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL(DATABASE_CREATE_TEAM);
//        onCreate(db);
    }

    public void openDatabase(){
//        String dbPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
        String dbPath = DALOCATION + DATABASE_NAME;
        if(sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            return;
        }
        sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public void closeDatabse() {
        if(sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }


    //all song list
    public ArrayList<SongModel> allSongList() {
        SongModel songModel = null;
        ArrayList<SongModel> songModels = new ArrayList<>();
        openDatabase();

        String mainSql = "SELECT * FROM songTable";
        Cursor cursor = sqLiteDatabase.rawQuery(mainSql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            songModel = new SongModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4), cursor.getString(5));
            songModels.add(songModel);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabse();
        return songModels;
    }

    //all random
    public ArrayList<SongModel> allRandomList() {
        SongModel songModel = null;
        ArrayList<SongModel> songModels = new ArrayList<>();
        openDatabase();

        String mainSql = "SELECT * FROM songTable ORDER BY random()";
        Cursor cursor = sqLiteDatabase.rawQuery(mainSql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            songModel = new SongModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4), cursor.getString(5));
            songModels.add(songModel);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabse();
        return songModels;
    }



//    public List<String> getrandom() {
//        String urlModel;
//        List<String> urlModels = null;
//        openDatabase();
//        try {
//            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
////            String mainSql = "SELECT urlField FROM songTable ORDER BY random()";
//            String mainSql = "SELECT urlField FROM songTable";
//            Cursor cursor = sqLiteDatabase.rawQuery(mainSql, null);
//            if (cursor.moveToFirst()) {
//                urlModels = new ArrayList<>();
//                do {
//                    urlModel = cursor.getString(0);
//                    urlModels.add(urlModel);
//                }while (cursor.moveToNext());
//            }
//        } catch (Exception e) {
//            urlModels = null;
//        }
//        return urlModels;
//    }

    //search activity list
    public List<SongModel> search(String keyword) {
        List<SongModel> songModels = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " LIKE ?", new String[] { "%" + keyword + "%"});
            if (cursor.moveToFirst()) {
                songModels = new ArrayList<SongModel>();
                do {
                    SongModel songModel = new SongModel();
                    songModel.set_index(cursor.getInt(0));
                    songModel.set_url(cursor.getString(1));
                    songModel.set_name(cursor.getString(2));
                    songModel.set_time(cursor.getString(3));
                    songModel.set_img(cursor.getBlob(4));
                    songModel.setposition(cursor.getString(5));
                    songModels.add(songModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            songModels = null;
        }
        return songModels;
    }

}
