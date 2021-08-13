package com.storage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.storage.storage_access_framework.StorageAccessFramework;
import com.storage.utilities.MIMETypeUtils;
import com.storage.utilities.RealPathUtils;

public class NotAgainGetPermissionActivity extends AppCompatActivity {

    public static final String TAG = SecondActivity.class.getSimpleName();

    private TextView currentURIsTextView;
    private Button createSubDirectoryButton, createFileButton;

    private DocumentFile rootDirectory, subDirectory, file;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_again_get_permission);
        initializeView();
        initializeEvent();
    }

    private void initializeView() {
        currentURIsTextView                         = findViewById(R.id.currentURIsTextView);
        createSubDirectoryButton                    = findViewById(R.id.createSubDirectoryButton);
        createFileButton                            = findViewById(R.id.createFileButton);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializeEvent() {
        rootDirectory = StorageAccessFramework.takeRootDirectoryWithPermission(getApplicationContext(),NotAgainGetPermissionActivity.this);
        /* NOTE : If it is return null then folder is deleted again ask permission for folder creation*/
        currentURIsTextView.setText(""+ RealPathUtils.getRealPath(NotAgainGetPermissionActivity.this, StorageAccessFramework.getUri(rootDirectory)));

        createSubDirectoryButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                subDirectory = StorageAccessFramework.createDirectory("newDirectory", rootDirectory);
                currentURIsTextView.setText(""+ RealPathUtils.getRealPath(NotAgainGetPermissionActivity.this, StorageAccessFramework.getUri(subDirectory)));
            }
        });

        createFileButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                file = StorageAccessFramework.createFile(MIMETypeUtils.guessMimeTypeFromExtension("txt"), "newFile", subDirectory);
                currentURIsTextView.setText(""+ RealPathUtils.getRealPath(NotAgainGetPermissionActivity.this, StorageAccessFramework.getUri(file)));
            }
        });
    }
}