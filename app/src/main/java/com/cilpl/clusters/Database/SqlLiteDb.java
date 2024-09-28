package com.cilpl.clusters.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class SqlLiteDb extends SQLiteOpenHelper {



    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Cynergytx";
    public static final String PERSON_TABLE_NAME = "bookingdates";





//Qb Conversation table
public static final String QBChatData = "_qbChatData";
    public static final String ctx_date = "_date";
    public static final String ctx_times = "_timelist";
    public static final String ctx_no_count = "_slotscount";
    public static final String ctx_status = "_status";
    public static final String ctx_chairlist = "_chairlist";
    public static final String ctx_chaircount = "_chaircount";
    public static final String ctx_recoredId = "_recordedId";




    public SqlLiteDb(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {





        sqLiteDatabase.execSQL("CREATE TABLE " + PERSON_TABLE_NAME + "(" +
                ctx_date + " TEXT PRIMARY KEY, " +
                ctx_times + " TEXT, " +
                ctx_chairlist + " TEXT, " +
                ctx_chaircount + " INTEGER, " +
                ctx_no_count + " INTEGER, " +
                ctx_recoredId + " INTEGER, " +
                ctx_status + " INTEGER " + ")");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//       if (newVersion > oldVersion) {
//           db.execSQL("CREATE TABLE " + DbsyncTable + "(" +
//                   holarayfuncname + " TEXT PRIMARY KEY, " +
//                   Dbsync_senttime + " DATETIME, " +
//                   Dbsync_recvtime + " DATETIME" + ")");
//
//
//       }

      //  db.execSQL(Cancelitem);
        //db.execSQL(MessageCancelText);

//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContactListTable);
//        onCreate(sqLiteDatabase);
//        if (newVersion > oldVersion) {
//            sqLiteDatabase.execSQL("ALTER TABLE '"+PhotoShootEvent+"' ADD COLUMN '"+PhotoShoot_facebookid+"' TEXT");
//            sqLiteDatabase.execSQL("ALTER TABLE '"+PhotoShootEvent+"' ADD COLUMN '"+PhotoShoot_Instagramid+"' TEXT");
//            sqLiteDatabase.execSQL("ALTER TABLE '"+PhotoShootEvent+"' ADD COLUMN '"+PhotoShoot_othersocialnet+"' TEXT");
//        }
    }

    public void deleteAll() {
        SQLiteDatabase db = SqlLiteDb.this.getWritableDatabase();
        db.execSQL("delete from " + PERSON_TABLE_NAME);

        db.close();
    }
}
