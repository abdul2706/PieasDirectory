package com.example.abdul.pieasdirectory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowPersonActivity extends AppCompatActivity {

    private static final String TAG = "ShowPersonActivity";
    public static final int SHOW_PERSON_ACTIVITY = 1;
    private static final int CALL_PHONE_PERMISSION = 1;
    private static final int SMS_SEND_PERMISSION = 2;
    private final int[] viewsID = {R.id.personNameEditText, R.id.designationEditText, R.id.postEditText, R.id.departmentEditText, R.id.officeLocationEditText, R.id.extensionEditText, R.id.phoneNoEditText, R.id.cellNoEditText, R.id.emailEditText};

    private ArrayList<TextView> textViews = new ArrayList<>();
    private Person person;
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showperson);

        mainActivity = MainActivity.getContext();
        initViews();

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);
        if (index != -1) {
            person = mainActivity.getPerson(index);
            setTitle(person.getPersonData("personName"));
            setViewsData();
        } else {
            Toast.makeText(ShowPersonActivity.this, "Invalid Person", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        textViews.clear();
        for (int aViewsID : viewsID) {
            textViews.add((TextView) findViewById(aViewsID));
        }
    }

    public void setViewsData() {
        for (int i = 0; i < viewsID.length; i++) {
            textViews.get(i).setText(person.getPersonData(Person.PERSON_KEYS[i]));
        }
    }

    public void actionPerformed(View view) {
        switch (view.getId()) {
            case R.id.callBtn:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + person.getPersonData("phoneNo")));
                    startActivity(callIntent);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION);
                }
                break;
            case R.id.msgBtn:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_SENDTO);
                    callIntent.setData(Uri.fromParts("sms", person.getPersonData("phoneNo"), null));
                    startActivity(callIntent);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSION);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Call Permission Granted", Toast.LENGTH_SHORT).show();
        } else if (requestCode == SMS_SEND_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Message Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

}
