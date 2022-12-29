package com.example.calorietracker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

import com.example.calorietracker.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private String recycler_id="0";
    private int imagesize = 32;
//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        this.recyclerView.getAdapter().notifyDataSetChanged();
//    }

    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("recycler_id", this.recycler_id);
        setResult(0, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listactivity);

        Intent intent = getIntent();

        this.cardview_title = intent.getStringExtra("cardview_title");
        this.chosen_date = intent.getStringExtra("chosen_date");
        this.chosen_time = intent.getStringExtra("chosen_time");
        this.recycler_id = intent.getStringExtra("recycler_id");
        this.context = this;
        this.activity = this;

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        ImageButton back_btn = findViewById(R.id.list_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("recycler_id", recycler_id);
                setResult(0, intent);
                finish();
            }
        });

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
                            predictimage(bitmap);
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

       if(this.cardview_title.equals("Water") || this.cardview_title.equals("Juices"))
       {
            camera_btn.setVisibility(View.GONE);
       }

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

//    private void saveimage(Bitmap bitmap) {
//        Uri images;
//        ContentResolver contentResolver = getContentResolver();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
//        } else {
//            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
//        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");
//        Uri uri = contentResolver.insert(images, contentValues);
//        try {
//            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            Objects.requireNonNull(outputStream);
//
//            //
//
//        } catch (Exception e) {
//            //
//            e.printStackTrace();
//
//        }
//
//    }

    private void predictimage(Bitmap bitmap)
    {
        Bitmap oldbitmap = bitmap;
        int dimension = Math.min(bitmap.getWidth(),bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,dimension,dimension); //Since our model requires a square image
        bitmap = Bitmap.createScaledBitmap(bitmap,imagesize,imagesize,false);

        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imagesize * imagesize * 3); //4 bite to 1 byte * length * breath * 3rgb in colors

            int[] intValues = new int[imagesize * imagesize];

            bitmap.getPixels(intValues,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());

            int i,j;

            int pixel = 0;

            for(i=0;i<imagesize;i++)
            {
                for(j=0;j<imagesize;j++)
                {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1)); //his model does the preprocessing in the model itself otherwise divide by 255
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeature0.getFloatArray();

            //code to find the highest probalble element among the confidence array ( find max element).

            int maxPos = 0;
            float maxConfidence = 0;

            for(i=0;i<confidence.length;i++)
            {
                if(confidence[i] > maxConfidence)
                {
                    maxConfidence = confidence[i];
                    maxPos = i;
                }
            }

            String[] classes = new String[]{"Apple","Orange","Banana"};

            Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_SHORT).show();

            createmodeldialog(classes,maxPos,confidence,oldbitmap);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    private void createmodeldialog(String[] classes, int maxPos, float[] confidence, Bitmap oldbitmap)
    {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.model_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);

        ImageView imageView = dialog.findViewById(R.id.taken_image);
        TextView prediction = dialog.findViewById(R.id.prediction_name);
        TextView apples = dialog.findViewById(R.id.apples);
        TextView oranges = dialog.findViewById(R.id.oranges);
        TextView bananas = dialog.findViewById(R.id.bananas);

        imageView.setImageBitmap(oldbitmap);

        prediction.setText(classes[maxPos]);

        apples.setText("Apple: "+confidence[0]);

        oranges.setText("Orange: "+confidence[1]);

        bananas.setText("Banana: "+confidence[2]);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }
}
