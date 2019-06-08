package com.example.abdul.pieasdirectory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    public static final int SEARCH_ACTIVITY = 2;
    private final int[] viewsID = {R.id.personNameEditText, R.id.designationEditText, R.id.postEditText, R.id.departmentEditText, R.id.officeLocationEditText, R.id.extensionEditText, R.id.phoneNoEditText, R.id.cellNoEditText, R.id.emailEditText};

    private MainActivity mainActivity;
    private ArrayList<EditText> inputEditTexts = new ArrayList<>();
    private ArrayList<String> searchTags = new ArrayList<>();
    private ArrayList<String> databaseColumns = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(R.string.search_student);

        mainActivity = MainActivity.getContext();
        initViews();
    }

    public void initViews() {
        inputEditTexts.clear();
        for (int aViewsID : viewsID) {
            inputEditTexts.add((EditText) findViewById(aViewsID));
        }
        Collections.addAll(databaseColumns, Person.PERSON_KEYS);
    }

    public void actionPerformed(View view) {
        Button clickedButton = (Button) view;
        if (clickedButton.getText().equals("Search")) {
            setSearchTags();
            mainActivity.setPersonArrayList(DatabaseHandler.selectPersons(this, createQuery()));
            Log.d(TAG, "actionPerformed: studentList.size() -> " + mainActivity.getPersonArrayList().size());
            mainActivity.notifyDataSetChanged();
            finish();
        } else if (clickedButton.getText().equals("Cancel")) {
            finish();
        }
    }

    public String createQuery() {
        Log.d(TAG, "createQuery: starts");
        StringBuilder queryBuilder = new StringBuilder();
        String queryPart;
        boolean firstAND = false;
        for (int i = 0; i < searchTags.size(); i++) {
            if(!searchTags.get(i).equals("")) {
                if (i > 0 && firstAND) {
                    queryBuilder.append(" AND ");
                }
                queryPart = databaseColumns.get(i) + " LIKE UPPER('%" + searchTags.get(i) + "%')";
                queryBuilder.append(queryPart);
                firstAND = true;
            }
        }
        Log.d(TAG, "createQuery: query -> " + queryBuilder.toString());
        Log.d(TAG, "createQuery: ends");
        return queryBuilder.toString();
    }

    public void setSearchTags() {
        Log.d(TAG, "setSearchTags: starts");
        searchTags.clear();
        for (int i = 0; i < inputEditTexts.size(); i++) {
            searchTags.add(inputEditTexts.get(i).getText().toString());
            Log.d(TAG, "setSearchTags: searchTags -> " + i + " -> " + searchTags.get(i));
        }
        Log.d(TAG, "setSearchTags: ends");
    }

}
