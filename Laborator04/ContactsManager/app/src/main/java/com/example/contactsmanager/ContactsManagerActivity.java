package com.example.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    boolean isVisible = false;

    EditText nameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    EditText addressEditText;
    EditText jobEditText;
    EditText companyEditText;
    EditText websiteText;
    EditText imText;
    Button toggleAdditionalFields;
    Button saveButton;
    Button cancelButton;
    LinearLayout extraFieldsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacts_manager);

        //  Set edit text views
        nameEditText = findViewById(R.id.name);

        phoneEditText = findViewById(R.id.phoneNumber);
        phoneEditText.setEnabled(false);

        emailEditText = findViewById(R.id.email);
        addressEditText = findViewById(R.id.address);
        jobEditText = findViewById(R.id.jobTitle);
        companyEditText = findViewById(R.id.company);
        websiteText = findViewById(R.id.website);
        imText = findViewById(R.id.im);

        //  Set toggled layout
        extraFieldsLayout = findViewById(R.id.extraFieldsLayout);

        //  Set toggle button
        toggleAdditionalFields = findViewById(R.id.toggleFields);
        toggleAdditionalFields.setOnClickListener(v -> {
            isVisible = !isVisible;

            if (isVisible) {
                extraFieldsLayout.setVisibility(View.VISIBLE);
                toggleAdditionalFields.setText(R.string.hide_additional_fields);
            } else {
                extraFieldsLayout.setVisibility(View.GONE);
                toggleAdditionalFields.setText(R.string.show_additional_fields);
            }
        });

        //  Set cancel button
        cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        });

        //  Set save button
        saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(v -> {
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String jobTitle = jobEditText.getText().toString();
            String company = companyEditText.getText().toString();
            String website = websiteText.getText().toString();
            String im = imText.getText().toString();

            if (!name.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            }
            if (!phone.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
            }
            if (!email.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            }
            if (!address.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
            }
            if (!jobTitle.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
            }
            if (!company.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
            }

            ArrayList<ContentValues> contactData = new ArrayList<>();

            if (!website.isEmpty()) {
                ContentValues websiteRow = new ContentValues();
                websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                contactData.add(websiteRow);
            }
            if (!im.isEmpty()) {
                ContentValues imRow = new ContentValues();
                imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                contactData.add(imRow);
            }

            intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
            startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
        });

        //  Check intent and add phone number from PhoneDialler
        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("com.example.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}