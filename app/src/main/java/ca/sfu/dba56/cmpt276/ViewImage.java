package ca.sfu.dba56.cmpt276;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import ca.sfu.dba56.cmpt276.model.ConfigurationsManager;

/*
* This class, allows to view image in a larger scale
* and make another picture (that overwrites previous or take an image from gallery)
*/

public class ViewImage extends AppCompatActivity {

    private ConfigurationsManager manager = ConfigurationsManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        setTitle("Manage Image");
        Bundle b = getIntent().getExtras();
        //no default for indexOfConfig because it is given in extra intent for all cases of activity call
        int indexOfConfig = b.getInt(getString(R.string.selected_config_position));
        int indexOfGamePlay = b.getInt("Selected GamePlay position", -1);
        //activity is open from ViewConfiguration
        if(indexOfGamePlay == -1){
            SetUpCameraBtn();
        }
        //activity is open from AddNewGame
        else {
            //TODO
        }
    }

    private void SetUpCameraBtn() {
        Button cameraBtn = findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(view -> {
            //check for the camera permission
            askCameraPermission();
        });
    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 101);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openCamera() {
        Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewImage.class);
    }
}