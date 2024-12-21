package com.spacECE.spaceceedu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBController  extends SQLiteOpenHelper {

    String sql;
    SQLiteDatabase sqLiteDatabase;
    final String TAG = "DBController";

    public DBController(Context context) {
        super(context,"Activity.db",null,1);
        sqLiteDatabase = this.getWritableDatabase();
        Log.d(TAG, "DBController: Connection Eastablished");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sql = "CREATE TABLE IF NOT EXISTS ActivityData"+
                "(status TEXT,"+
                "id TEXT,"+
                "name TEXT,"+
                "level TEXT,"+
                "dev_domain TEXT,"+
                "objectives TEXT,"+
                "key_dev TEXT,"+
                "material TEXT,"+
                "assessment TEXT,"+
                "process TEXT,"+
                "instructions TEXT,"+
                "complete_status TEXT,"+
                "result TEXT)";

        db.execSQL(sql);
        Log.d(TAG, "onCreate: Table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sql = "DROP TABLE IF EXISTS ActivityData";
        db.execSQL(sql);
        onCreate(db);
    }

    public List<ActivityData> getAll(){

        List<ActivityData> activityDataList = new ArrayList<>();
        ActivityData activityData;
        String status,no,name,level,dev_domain,objectives,key_dev,material,assessment,process,instructions,complete_status,result;
        Data data;


        sql = "SELECT * FROM ActivityData";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);


        while (cursor.moveToNext()){

            status = cursor.getString(0);
            no = cursor.getString(1);
            name = cursor.getString(2);
            level = cursor.getString(3);
            dev_domain = cursor.getString(4);
            objectives = cursor.getString(5);
            key_dev = cursor.getString(6);
            material = cursor.getString(7);
            assessment = cursor.getString(8);
            process = cursor.getString(9);
            instructions = cursor.getString(10);
            complete_status = cursor.getString(11);
            result = cursor.getString(12);

            data = new Data(no,name,level,dev_domain,objectives,key_dev,material,assessment,process,instructions);
            List<Data> dataList = new ArrayList<>();
            dataList.add(data);
            Log.d(TAG, "getAll: "+dataList.size());

            activityData = new ActivityData(status,dataList,result);
            activityDataList.add(activityData);


        }

        return activityDataList;
    }

    public ActivityData getLastActivity(){
        List<ActivityData> activityDataList = getAll();
        ActivityData lastActivity = activityDataList.get(activityDataList.size()-1);
        return lastActivity;
    }
    public int insertRecord(ActivityData activityData){

        Log.d(TAG, "insertRecord: "+activityData.getData().get(0).getActivityInstructions());

        ContentValues contentValues = new ContentValues();
        long updateCount;

        contentValues.put("status",activityData.getStatus());
        contentValues.put("result",activityData.getResult());

        contentValues.put("id",activityData.getData().get(0).getActivityNo());
        contentValues.put("name",activityData.getData().get(0).getActivityName());
        contentValues.put("level",activityData.getData().get(0).getActivityLevel());
        contentValues.put("dev_domain",activityData.getData().get(0).getActivityDevDomain());
        contentValues.put("objectives",activityData.getData().get(0).getActivityObjectives());
        contentValues.put("key_dev",activityData.getData().get(0).getActivityKeyDev());
        contentValues.put("material",activityData.getData().get(0).getActivityMaterial());
        contentValues.put("assessment",activityData.getData().get(0).getActivityAssessment());
        contentValues.put("process",activityData.getData().get(0).getActivityProcess());
        contentValues.put("instructions",activityData.getData().get(0).getActivityInstructions());
        contentValues.put("complete_status","Pending");


        try{
            updateCount = sqLiteDatabase.insertOrThrow("ActivityData",null,contentValues);
        }catch (SQLiteConstraintException e){
            Log.d(TAG, "insertRecord: Exception"+e);
            return -2;
        }

        return (int) updateCount;
    }

    public int isNewUser(){

         sql = "SELECT * FROM ActivityData";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        int count = cursor.getCount();
        Log.d(TAG, "isNewUser: "+count);

        return count;
    }

}
