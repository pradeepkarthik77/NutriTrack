package com.example.calorietracker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity
{
    private String cardview_title;

    private LoadTheDatabase loadTheDatabase;
    private ExcelClass excelClass;
    private Context context;
    private RecyclerView recyclerView;
    private NewRecyclerAdapter newRecyclerAdapter;
    private String[] favorites_list;
    private int cardview_count;
    private List<List<String>> item_values;
    private String[] not_favorite_list;
    private GridLayoutManager gridLayoutManager;
    private String chosen_date;
    private String chosen_time;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private Activity activity;
    private ActivityResultLauncher<String> pickaphotolauncher;
    private Uri imageUri;

//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        this.recyclerView.getAdapter().notifyDataSetChanged();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listactivity);

        Intent intent = getIntent();

        this.cardview_title = intent.getStringExtra("cardview_title");
        this.chosen_date = intent.getStringExtra("chosen_date");
        this.chosen_time = intent.getStringExtra("chosen_time");
        this.context = this;
        this.activity = this;

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        TextView list_title = (TextView) findViewById(R.id.list_title);

        list_title.setText(this.cardview_title);

        this.loadTheDatabase = new LoadTheDatabase(this.context);

        this.excelClass = new ExcelClass(this.context);

        this.excelClass.create_excel();

        this.favorites_list = this.excelClass.get_favorites(this.cardview_title);

        this.loadTheDatabase.setValues();

        this.cardview_count = this.loadTheDatabase.get_count(this.cardview_title);

        this.not_favorite_list = this.loadTheDatabase.get_unFavorites(this.cardview_title,this.favorites_list);

        this.item_values = this.loadTheDatabase.get_smaller_card_values(this.favorites_list,this.not_favorite_list);

        this.gridLayoutManager = new GridLayoutManager(this.context,2);

        this.recyclerView = findViewById(R.id.listActivity_recycler);

       this.newRecyclerAdapter = new NewRecyclerAdapter(this.context,this.cardview_title,this.cardview_count,this.loadTheDatabase,this.item_values,this.excelClass,this.favorites_list,this.chosen_date,this.chosen_time);

       this.recyclerView.setAdapter(this.newRecyclerAdapter);

       this.recyclerView.setLayoutManager(gridLayoutManager);

       //Toast.makeText(context,String.join(",",this.favorites_list),Toast.LENGTH_LONG).show();

       //ImageView sampleimg = findViewById(R.id.sampleimg);

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        //TODO write logic for the result from takeaphoto
                        int resultcode = result.getResultCode();
                        Bitmap bitmap;

                        if (resultcode == RESULT_OK)
                        {
                            bitmap = (Bitmap) Objects.requireNonNull(result.getData()).getExtras().get("data");
                            //sampleimg.setImageBitmap(bitmap);
                            //saveimage(bitmap);
                            Toast.makeText(context,"Image Taken",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(context,"Image not Taken",Toast.LENGTH_LONG).show();
                        }
                    }
                });

        this.pickaphotolauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri)
                    {
                        // Handle the returned Uri
                        finish();
                    }
                });

       ImageView camera_btn = findViewById(R.id.add_photo);

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions((Activity)context))
                {
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    activityResultLauncher.launch(takePicture);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(cameraIntent);
                }
                else
                {
                    Toast.makeText(context,"Permission For Camera Access is Denied",Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void saveimage(Bitmap bitmap) {
        Uri images;
        ContentResolver contentResolver = getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");
        Uri uri = contentResolver.insert(images, contentValues);
        try {
            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);

            //

        } catch (Exception e) {
            //
            e.printStackTrace();

        }

    }

}
