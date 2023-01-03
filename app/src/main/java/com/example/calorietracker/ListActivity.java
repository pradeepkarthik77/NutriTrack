package com.example.calorietracker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calorietracker.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private String email;
    private String user_name;
    private String TEMP_FILE = "tempfile.png";
    private Uri image_uri;

    public final String APP_TAG = "CalorieTracker";
    public String photoFileName = "photo.jpg";
    public File photoFile;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "";

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

        BASE_URL = getString(R.string.BASE_URL);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        this.cardview_title = intent.getStringExtra("cardview_title");
        this.chosen_date = intent.getStringExtra("chosen_date");
        this.chosen_time = intent.getStringExtra("chosen_time");
        this.recycler_id = intent.getStringExtra("recycler_id");
        this.context = this;
        this.activity = this;

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Login",0);

        this.email = pref.getString("email","");

        this.user_name = pref.getString("name","");

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

//        this.activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result)
//                    {
//                        //TODO write logic for the result from takeaphoto
//                        int resultcode = result.getResultCode();
//                        Bitmap bitmap;
//
//                        if (resultcode == RESULT_OK)
//                        {
//                            bitmap = (Bitmap) Objects.requireNonNull(result.getData()).getExtras().get("data");
//                            //sampleimg.setImageBitmap(bitmap);
//                            //saveimage(bitmap);
//                            //predictimage(bitmap);
//                            //sendimagetobackend(bitmap);
//
////                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////
////                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
////
////                            byte[] byteArray = byteArrayOutputStream.toByteArray();
////
////                            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
////
////                            byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
////                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
//                            //imageview.setImageBitmap(decodedByte);
//
//                            //predictimage(bitmap);
//
//                            ImageView img = findViewById(R.id.imageview);
//
//                            img.setImageBitmap(bitmap);
//
//                            Toast.makeText(context,bitmap.getByteCount()+"",Toast.LENGTH_LONG).show();
//                            Toast.makeText(context,"Image Taken",Toast.LENGTH_LONG).show();
//                        }
//                        else
//                        {
//                            Toast.makeText(context,"Image not Taken",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//
//        this.pickaphotolauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
//                new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri uri)
//                    {
//                        // Handle the returned Uri
//                        finish();
//                    }
//                });

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
                    try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        photoFile = getPhotoFileUri(photoFileName);

                        Uri fileprovider = FileProvider.getUriForFile(context, "com.codepath.fileprovider", photoFile);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileprovider);

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // Start the image capture intent to take photo
                            startActivityForResult(intent, 1234);
                        }
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
                    }


                }
                else
                {
                    Toast.makeText(context,"Permission For Camera Access is Denied",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            uploadtobackend(bitmap);
        }
    }

    private void uploadtobackend(Bitmap bitmap)
    {
        //Toast.makeText(context,bitmap.getByteCount()+"",Toast.LENGTH_LONG).show();

        String base = BitMapToString(bitmap);

//        HashMap<String,String> map = new HashMap<>();
//
//        map.put("imagebase64",base);
//
//        Call<Void> call = retrofitInterface.executeimage(map);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.code() == 200)
//                {
//                    Toast.makeText(ListActivity.this, "Img send success", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(ListActivity.this, "Image send fail", Toast.LENGTH_SHORT).show();
//            }
//        });
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

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    public String BitMapToString(Bitmap userImage1)
    {

        Bitmap compressedfile  = userImage1;

        try {
            compressedfile = new Compressor(this).compressToBitmap(photoFile);
        }
        catch(Exception e)
        {
            compressedfile = userImage1;
        }

        Toast.makeText(context,userImage1.getByteCount()+" "+compressedfile.getByteCount(),Toast.LENGTH_LONG).show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

}
