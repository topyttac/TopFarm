package th.or.nectec.zoning.topfarm.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import th.or.nectec.zoning.topfarm.datatype.Event;
import th.or.nectec.zoning.topfarm.datatype.Plant;
import th.or.nectec.zoning.topfarm.datatype.Processes;
import th.or.nectec.zoning.topfarm.datatype.SubPlant;

/**
 * Created by topyttac on 10/12/2016 AD.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DB_PATH = "/data/data/th.or.nectec.zoning.topfarm/databases/";

    public static String DB_NAME = "Plant.db";

    private SQLiteDatabase myDB;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("tle99 - create", e.getMessage());
            }
        } else {
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("tle99 - create", e.getMessage());
            }
        }

    }

    private boolean checkDataBase() {

        SQLiteDatabase tempDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e("tle99 - check", e.getMessage());
        }

        if (tempDB != null) {
            tempDB.close();
        }

        return tempDB != null ? true : false;
    }


    public void copyDataBase() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("tle99 - copyDatabase", e.getMessage());
        }
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (myDB != null) {
            myDB.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Plant> getAllPlant() {
        List<Plant> listPlant = new ArrayList<>();
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = mDatabase.rawQuery("SELECT * FROM plant", null);

            if (cursor == null) {
                return null;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Plant plant = CursorPlant(cursor);
                    listPlant.add(plant);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }
        return listPlant;
    }

    private Plant CursorPlant(Cursor cursor) {
        Plant plant = new Plant();
        plant.setP_id(cursor.getInt(0));
        plant.setP_name(cursor.getString(1));
        plant.setP_desc(cursor.getString(2));
        plant.setP_picture(cursor.getString(3));
        return plant;
    }

    public List<SubPlant> getAllSubPlant(int plant) {
        List<SubPlant> listSubPlant = new ArrayList<>();
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = mDatabase.rawQuery("SELECT * FROM subPlant WHERE p_id = ?", new String[] {plant + ""});

            if (cursor == null) {
                return null;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    SubPlant subPlant = CursorSubPlant(cursor);
                    listSubPlant.add(subPlant);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }
        return listSubPlant;
    }

    private SubPlant CursorSubPlant(Cursor cursor) {
        SubPlant subPlant = new SubPlant();
        subPlant.setSp_id(cursor.getInt(0));
        subPlant.setSp_name(cursor.getString(1));
        subPlant.setSp_duration(cursor.getInt(2));
        subPlant.setP_id(cursor.getInt(3));
        return subPlant;
    }

    public List<Processes> getAllProcesses(int plant) {
        List<Processes> listProcesses = new ArrayList<>();
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = mDatabase.rawQuery("SELECT * FROM process WHERE p_id = ?", new String[] {plant + ""});
            if (cursor == null) {
                return null;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Processes processes = CursorProcesses(cursor);
                    listProcesses.add(processes);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }
        return listProcesses;
    }

    private Processes CursorProcesses(Cursor cursor) {
        Processes processes = new Processes();
        processes.setPc_id(cursor.getInt(0));
        processes.setPc_name(cursor.getString(1));
        processes.setPc_duration(cursor.getInt(2));
        processes.setP_id(cursor.getInt(3));
        return processes;
    }

    public List<Event> getEvent(String date, int plant) {
        List<Event> listEvent = new ArrayList<>();
        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor;
        try {
            cursor = mDatabase.rawQuery("SELECT * FROM event WHERE '" + date + "' BETWEEN e_start AND e_end AND p_id = ?", new String[] {plant + ""});
            if (cursor == null) {
                return null;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Event event = CursorEvent(cursor);
                    listEvent.add(event);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }
        return listEvent;
    }

    private Event CursorEvent(Cursor cursor) {
        Event event = new Event();
        event.setE_id(cursor.getInt(0));
        event.setE_name(cursor.getString(1));
        event.setE_desc(cursor.getString(2));
        event.setE_start(cursor.getString(3));
        event.setE_end(cursor.getString(4));
        event.setP_id(cursor.getInt(5));
        return event;
    }

}
