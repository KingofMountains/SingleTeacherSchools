package com.sts.singleteacherschool.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.sts.singleteacherschool.Data.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class GetSanchVillageAcharyaTask extends AsyncTask<String, String, Integer> {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public GetSanchVillageAcharyaTask(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... params) {

        System.out.println("response ------- " + params[0]);

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        
//        db.execSQL("delete from village");
//        db.execSQL("delete from sanch");
//        db.execSQL("delete from acharya");

        try {
            JSONObject resp = new JSONObject(params[0]);

            db.beginTransaction();

            if (resp.has("village")) {

                JSONArray village = resp.getJSONArray("village");

                for (int i = 0; i < village.length(); i++) {

                    JSONObject obj = village.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.clear();

                    values.put("id", obj.getString("id"));
                    values.put("village_name", obj.getString("village_name"));
                    values.put("sanch_id", obj.getString("sanch_id"));
                    values.put("live_id", obj.getString("live_id"));

//                    db.insert("village", null, values);
                    db.insertWithOnConflict("village", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                }

            }

            if (resp.has("sanch")) {

                JSONArray sanch = resp.getJSONArray("sanch");

                for (int i = 0; i < sanch.length(); i++) {

                    JSONObject obj = sanch.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.clear();

                    values.put("id", obj.getString("id"));
                    values.put("sanch_name", obj.getString("sanch_name"));
                    values.put("live_id", obj.getString("live_id"));

//                    db.insert("sanch", null, values);
                    db.insertWithOnConflict("sanch", null, values, SQLiteDatabase.CONFLICT_REPLACE);

                }

            }

            if (resp.has("acharya")) {

                JSONArray acharya = resp.getJSONArray("acharya");

                for (int i = 0; i < acharya.length(); i++) {

                    JSONObject obj = acharya.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.clear();

                    values.put("id", obj.getString("id"));
                    values.put("acharya_name", obj.getString("acharya_name"));
                    values.put("sanch_id", obj.getString("sanch_id"));
                    values.put("village_name", obj.getString("village_name"));
                    values.put("village_id", obj.getString("village_id"));
                    values.put("live_id", obj.getString("live_id"));

//                    db.insert("acharya", null, values);
                    db.insertWithOnConflict("acharya", null, values, SQLiteDatabase.CONFLICT_REPLACE);

                }
            }

            db.setTransactionSuccessful();

        } catch (JSONException e) {
            e.printStackTrace();
        }  finally {
            db.endTransaction();
            db.close();
            dbHelper.close();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

    }
}