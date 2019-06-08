package com.example.abdul.pieasdirectory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class DatabaseHandler {

    private static final String TAG = "DatabaseHandler";
    private static SQLiteDatabase sqLiteDatabase;
    private static String DATABASE_NAME = "PersonData";
    private static String TABLE_NAME = "personTable";

    public static void initialize(Context context) {
        Log.d(TAG, "initialize: starts");
        try {
            sqLiteDatabase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY, personName VARCHAR, designation VARCHAR, post VARCHAR, department VARCHAR, officeLocation VARCHAR, extension VARCHAR, phoneNo VARCHAR, cellNo VARCHAR, email VARCHAR)");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        Log.d(TAG, "initialize: ends");
    }

    public static void deleteTable(Context context) {
        Log.d(TAG, "deleteTable: starts");
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME);
        Log.d(TAG, "deleteTable: ends");
    }

    public static void insertPerson(Context context, Person std) {
        Log.d(TAG, "insertPerson: starts");
        MainActivity mainActivity = MainActivity.getContext();
        Log.d(TAG, "insertPerson: mainActivity.getPersonArrayList().size() -> " + mainActivity.getPersonArrayList().size());
        for (Person prevPerson : mainActivity.getPersonArrayList()) {
            Log.d(TAG, "insertPerson: prevPerson -> " + prevPerson);
            if (prevPerson.equals(std)) {
                Log.d(TAG, "insertPerson: Person Already Exists");
                return;
            }
        }
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_NAME + " (personName, designation, post, department, officeLocation, extension, phoneNo, cellNo, email) VALUES (" + Person.parsePersonToString(std) + ")");
        Log.d(TAG, "insertPerson: Person Added");
        Log.d(TAG, "insertPerson: ends");
    }

    public static void deletePerson(Context context, int index) {
        Person person = MainActivity.getContext().getPersonArrayList().get(index);
        sqLiteDatabase.execSQL("DELETE FROM personTable WHERE officeLocation = '" + person.getPersonData("officeLocation") + "'");
        Log.d(TAG, "deletePerson: Person Deleted");
    }

    public static void updatePerson(Context context, Person person, String key, String newValue) {
        sqLiteDatabase.execSQL("UPDATE " + TABLE_NAME + " SET " + key + " = '" + newValue + "' WHERE officeLocation = '" + person.getPersonData("regNo") + "'");
        Log.d(TAG, "updatePerson: Person Updated");
    }

    public static ArrayList<Person> selectPersons(Context context, String query) {
        Log.d(TAG, "selectPersons: starts");
        ArrayList<Person> matchedPeople = new ArrayList<>();
        String[] values = new String[Person.PERSON_KEYS.length];
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + query, null);
        Log.d(TAG, "selectPersons: cursor.length -> " + cursor.getCount());
        String[] cols = cursor.getColumnNames();
        for (String col : cols) {
            Log.d(TAG, "selectPersons: col -> " + col);
        }
        try {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    for (int i = 1; i < cursor.getColumnNames().length; i++) {
                        values[i - 1] = cursor.getString(i);
                    }
                    matchedPeople.add(new Person(values));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(context, "No Person Data in Database", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        cursor.close();
        Log.d(TAG, "selectPersons: ends");
        return matchedPeople;
    }

    public static void loadPersonsToDatabase(Context context) {
        Log.d(TAG, "loadPersonsToDatabase: starts");
        ArrayList<Person> persons = new ArrayList<>();
        InputStream inputStream = context.getResources().openRawResource(R.raw.person_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String line;
        try {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                Log.d(TAG, "loadPersonsToDatabase: line -> " + line);
                String[] values = line.split(",");
                persons.add(new Person(values));
            }
        } catch (Exception e) {
            Log.d(TAG, "loadPersonsToDatabase: " + e.getMessage());
            e.printStackTrace();
        }

        for (Person person : persons) {
            insertPerson(context, person);
        }
        Log.d(TAG, "loadPersonsToDatabase: ends");
    }

}
