package ca.sfu.dba56.cmpt276;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

/*
* This class, allows to view image in a larger scale
* and make another picture (that overwrites previous or take an image from gallery)
*/

public class ViewImage extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int WES_PERMISSION_CODE = 103;
    private ConfigurationsManager manager = ConfigurationsManager.getInstance();
    private int indexOfGamePlay = -1;
    private ImageView image;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        setTitle("Manage Image");

        image = findViewById(R.id.image_view_for_activity);

//        ActionBar bar = getSupportActionBar();
//        assert bar != null;
//        bar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        int indexOfConfig = manager.getIndexOfCurrentConfiguration();
        if(b != null){
            //activity is open from AddNewGame
            indexOfGamePlay = b.getInt("Selected GamePlay position");
        }
        else{
            //activity is open from ViewConfiguration
            setUpCameraBtn();

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //checks if the class was selected in the AddNewGame activity
                if (indexOfGamePlay != -1) {
                    //goes to celebration page
                    Intent intent = new Intent(this, AchievementCelebration.class);
                    intent.putExtra("selected game", indexOfGamePlay);
                    startActivity(intent);
                }
                // checks if the class was selected in the ViewConfig activity
                else {
                    this.finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Camera Permission is required to use camera", Toast.LENGTH_SHORT).show();
                    break;
                }
            case WES_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Gallery Permission is required to use camera", Toast.LENGTH_SHORT).show();
                    break;
                }
            default:
                break;

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openCamera() {
        Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Gallery Permission Granted", Toast.LENGTH_SHORT).show();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the ViewImage class");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            image.setImageURI(image_uri);
            manager.getItemAtIndex(manager.getIndexOfCurrentConfiguration()).setUriForConfigImage(image_uri);
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WES_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void setUpCameraBtn() {
        Button cameraBtn = findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(view -> {
            //check for the camera permission
            askCameraPermission();
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewImage.class);
    }

//class ends here
}