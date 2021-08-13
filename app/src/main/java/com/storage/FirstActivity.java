package com.storage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.storage.storage_access_framework.StorageAccessFramework;
import com.storage.storage_access_framework.StorageAccessFrameworkConstants;
import com.storage.utilities.MIMETypeUtils;
import com.storage.utilities.RealPathUtils;

public class FirstActivity extends AppCompatActivity {

    public static final String TAG = FirstActivity.class.getSimpleName();

    private EditText writeEditText, updateEditText;
    private TextView readTextView, currentURIsTextView;
    private Button createButton, readButton, updateButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initializeView();
        initializeEvent();
    }

    private void initializeView() {
        writeEditText           = findViewById(R.id.writeEditText);
        readTextView            = findViewById(R.id.readTextView);
        updateEditText          = findViewById(R.id.updateEditText);
        currentURIsTextView     = findViewById(R.id.currentURIsTextView);
        createButton             = findViewById(R.id.createButton);
        readButton              = findViewById(R.id.readButton);
        updateButton            = findViewById(R.id.updateButton);
        deleteButton            = findViewById(R.id.deleteButton);
    }

    private void initializeEvent() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                StorageAccessFramework.createFile(FirstActivity.this, MIMETypeUtils.guessMimeTypeFromExtension("txt"),"Untitled.txt",true, StorageAccessFrameworkConstants.CREATE_REQUEST_CODE);
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //StorageAccessFramework.openFileUsingActionGetContent(FirstActivity.this, MIMETypeUtils.guessMimeTypeFromExtension("txt"),true, StorageAccessFrameworkConstants.READ_REQUEST_CODE);
                StorageAccessFramework.openFileUsingActionOpenDocument(FirstActivity.this, MIMETypeUtils.guessMimeTypeFromExtension("txt"),true, StorageAccessFrameworkConstants.READ_REQUEST_CODE);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                StorageAccessFramework.openFileForUpdate(FirstActivity.this, MIMETypeUtils.guessMimeTypeFromExtension("txt"),true, StorageAccessFrameworkConstants.UPDATE_REQUEST_CODE);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageAccessFramework.openFileForDelete(FirstActivity.this, MIMETypeUtils.guessMimeTypeFromExtension("txt"),true, StorageAccessFrameworkConstants.DELETE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if(resultCode == Activity.RESULT_OK)
        {
            if (resultData != null)
            {
                Uri uri = resultData.getData();

                if (uri != null)
                {
                    if(StorageAccessFrameworkConstants.CREATE_REQUEST_CODE == requestCode)
                    {
                        StorageAccessFramework.getFileDetailsFromUri(getApplicationContext(),uri);
                        String data = writeEditText.getText().toString();
                        StorageAccessFramework.performWriteOperation(getApplicationContext(),uri,data+" Write at " + System.currentTimeMillis());
                        currentURIsTextView.setText(""+ RealPathUtils.getRealPath(getApplicationContext(), uri));
                    }
                    if(StorageAccessFrameworkConstants.READ_REQUEST_CODE == requestCode)
                    {
                        StorageAccessFramework.getFileDetailsFromUri(getApplicationContext(),uri);
                        StringBuilder stringBuilder = StorageAccessFramework.performReadOperation(getApplicationContext(),uri);
                        readTextView.setText(stringBuilder);
                        currentURIsTextView.setText(""+ RealPathUtils.getRealPath(getApplicationContext(), uri));
                    }
                    if(StorageAccessFrameworkConstants.UPDATE_REQUEST_CODE == requestCode)
                    {
                        StorageAccessFramework.getFileDetailsFromUri(getApplicationContext(),uri);
                        String data = updateEditText.getText().toString();
                        StorageAccessFramework.performUpdateOperation(getApplicationContext(),uri,data+" Update at " + System.currentTimeMillis());
                        currentURIsTextView.setText(""+ RealPathUtils.getRealPath(getApplicationContext(), uri));
                    }
                    if(StorageAccessFrameworkConstants.DELETE_REQUEST_CODE == requestCode)
                    {
                        StorageAccessFramework.getFileDetailsFromUri(getApplicationContext(),uri);
                        StorageAccessFramework.performDeleteOperation(getApplicationContext(),uri);
                        currentURIsTextView.setText(""+ RealPathUtils.getRealPath(getApplicationContext(), uri));
                    }
                }
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(getApplicationContext(), "Activity canceled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Something want wrong", Toast.LENGTH_SHORT).show();
        }
    }
}