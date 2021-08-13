package com.storage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.storage.storage_access_framework.StorageAccessFramework;
import com.storage.storage_access_framework.StorageAccessFrameworkConstants;
import com.storage.utilities.RealPathUtils;

public class SelectFileActivity extends AppCompatActivity {

    public static final String TAG = SelectFileActivity.class.getSimpleName();

    private Button selectFileButton;
    private ImageView imageView;

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        initializeView();
        initializeEvent();
    }

    private void initializeView() {
        selectFileButton  = findViewById(R.id.selectFileButton);
        imageView         = findViewById(R.id.imageView);
    }

    private void initializeEvent() {
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                StorageAccessFramework.selectFolder(SelectFileActivity.this, StorageAccessFrameworkConstants.SELECT_FOLDER_REQUEST_CODE);
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
                    String realPath = RealPathUtils.getRealPath(SelectFileActivity.this, uri);
                    System.out.println("========================="+realPath);
                }
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(SelectFileActivity.this, "Activity canceled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(SelectFileActivity.this, "Something want wrong", Toast.LENGTH_SHORT).show();
        }
    }
}