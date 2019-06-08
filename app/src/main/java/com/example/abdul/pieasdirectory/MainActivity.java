package com.example.abdul.pieasdirectory;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static MainActivity context;

    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private ArrayList<Person> personArrayList = new ArrayList<>();
    private int totalPersonsInDataBase = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        DatabaseHandler.initialize(this);
        DatabaseHandler.deleteTable(this);

        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new CustomAdapter(this, personArrayList);
        recyclerView.setAdapter(this.customAdapter);

        personArrayList = DatabaseHandler.selectPersons(this, "id = id");
        if(personArrayList.size() == 0) {
            DatabaseHandler.loadPersonsToDatabase(this);
            personArrayList = DatabaseHandler.selectPersons(this, "id = id");
        }
        Log.d(TAG, "onResume: personArrayList.size() -> " + personArrayList.size());
        totalPersonsInDataBase = personArrayList.size();
        notifyDataSetChanged();

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: starts");
        notifyDataSetChanged();
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent;
        switch (item.getItemId()) {
            case R.id.showAll:
                Log.i(TAG, "onOptionsItemSelected : " + "case showAll");
                personArrayList = DatabaseHandler.selectPersons(this, "id = id");
                Log.d(TAG, "onResume: personArrayList.size() -> " + personArrayList.size());
                notifyDataSetChanged();
                break;
            case R.id.search:
                Log.i(TAG, "onOptionsItemSelected : " + "case search");
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivityForResult(intent, SearchActivity.SEARCH_ACTIVITY);
                break;
            case R.id.about:
                Log.i(TAG, "onOptionsItemSelected : " + "case about");
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.pieas_logo)
                        .setTitle("About")
                        .setMessage("This app is for PIEAS University.\nIt is created by Hassan Sattar &\nAbdul Rehman from BSCIS(17-21).")
                        .setPositiveButton("OK", null)
                        .setNegativeButton("", null)
                        .show();
                break;
            default:
                Log.i(TAG, "onOptionsItemSelected : " + "Returning False (default)");
                return false;
        }
        Log.i(TAG, "onOptionsItemSelected : " + "Returning True");
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult : " + "requestCode -> " + requestCode);
        Log.i(TAG, "onActivityResult : " + "resultCode -> " + resultCode);
        switch (requestCode) {
            case ShowPersonActivity.SHOW_PERSON_ACTIVITY:
                break;
            case SearchActivity.SEARCH_ACTIVITY:
                Toast.makeText(this, "total -> " + totalPersonsInDataBase + "\nshowing -> " + personArrayList.size(), Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    public static MainActivity getContext() {
        return context;
    }

    public ArrayList<Person> getPersonArrayList() {
        return this.personArrayList;
    }

    public void setPersonArrayList(ArrayList<Person> people) {
        this.personArrayList = people;
    }

    public Person getPerson(int index) {
        return this.personArrayList.get(index);
    }

    public void notifyDataSetChanged() {
        this.customAdapter.loadPersonsData(this.personArrayList);
    }

}
