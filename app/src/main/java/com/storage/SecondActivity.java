package com.storage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.storage.storage_access_framework.StorageAccessFramework;
import com.storage.storage_access_framework.StorageAccessFrameworkConstants;
import com.storage.utilities.MIMETypeUtils;
import com.storage.utilities.RealPathUtils;

public class SecondActivity extends AppCompatActivity {

    public static final String TAG = SecondActivity.class.getSimpleName();

    private TextView currentURIsTextView;
    private Button getPermissionAndCreateRootDirectoryButton, createSubDirectoryButton, createFileButton;

    private DocumentFile rootDirectory, subDirectory, file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initializeView();
        initializeEvent();
    }

    private void initializeView() {
        getPermissionAndCreateRootDirectoryButton   = findViewById(R.id.getPermissionAndCreateRootDirectoryButton);
        currentURIsTextView                         = findViewById(R.id.currentURIsTextView);
        createSubDirectoryButton                    = findViewById(R.id.createSubDirectoryButton);
        createFileButton                            = findViewById(R.id.createFileButton);
    }

    private void initializeEvent() {
        getPermissionAndCreateRootDirectoryButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                rootDirectory = StorageAccessFramework.takeRootDirectoryWithPermission(getApplicationContext(),SecondActivity.this);
                //StorageAccessFramework.releaseRootDirectoryWithPermission(getApplicationContext(),SecondActivity.this);
            }
        });

        createSubDirectoryButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                subDirectory = StorageAccessFramework.createDirectory("newDirectory", rootDirectory);
                currentURIsTextView.setText(""+ RealPathUtils.getRealPath(SecondActivity.this, StorageAccessFramework.getUri(subDirectory)));
            }
        });

        createFileButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                file = StorageAccessFramework.createFile(MIMETypeUtils.guessMimeTypeFromExtension("txt"), "newFile", subDirectory);
                currentURIsTextView.setText(""+ RealPathUtils.getRealPath(SecondActivity.this, StorageAccessFramework.getUri(file)));
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
                    if(StorageAccessFrameworkConstants.SELECT_FOLDER_REQUEST_CODE == requestCode)
                    {
                        /* Save the obtained directory permissions */
                        final int takeFlags = resultData.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        getContentResolver().takePersistableUriPermission(uri, takeFlags);

                        /* Save uri */
                        SharedPreferences sharedPreferences = getSharedPreferences(StorageAccessFramework.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(StorageAccessFramework.ALLOW_DIRECTORY, uri.toString());
                        sharedPreferencesEditor.apply();

                        rootDirectory = StorageAccessFramework.getRootDirectory(getApplicationContext(),uri);
                        currentURIsTextView.setText(""+ RealPathUtils.getRealPath(getApplicationContext(), StorageAccessFramework.getUri(rootDirectory)));
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