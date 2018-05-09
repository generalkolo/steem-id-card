package com.semanientreprise.steemidcard;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class idCardActivity extends AppCompatActivity {

    @BindView(R.id.user_profile_img)
    ImageView userProfileImg;
    @BindView(R.id.id_number)
    TextView idNumber;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.steemitusername)
    TextView steemitusername;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.rank)
    TextView rank;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.qr_code)
    ImageView qrCode;
    @BindView(R.id.idCardLayout)
    RelativeLayout idCardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        setSentDetails();
    }

    private void setSentDetails() {
        Intent intent = getIntent();
        
        String idNumber_string = intent.getStringExtra("idNumber");
        String username_string = intent.getStringExtra("name");
        String steemitusername_string = intent.getStringExtra("steemitusername");
        String location_string = intent.getStringExtra("location");
        String rank_string = intent.getStringExtra("rank");
        String date_string = intent.getStringExtra("dateJoined");

        Picasso.with(this).load(intent.getStringExtra("profileImage")).into(userProfileImg);        
        idNumber.setText(idNumber_string);
        username.setText(username_string);
        steemitusername.setText(steemitusername_string);
        location.setText(location_string);
        rank.setText(rank_string);
        date.setText(date_string);
        
        String QRCODE = "Name: "+username_string+" ID Number: "+idNumber_string+" Location: "+location_string+"" +
                "Rank: "+rank_string+" Date Joined: "+date_string +"Steemit Username: "+steemitusername_string;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(QRCODE, BarcodeFormat.QR_CODE,600,600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
            
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveImage();
                break;
        }
        return true;
    }

    private void saveImage() {
        Layout_to_Image layout_to_image = new Layout_to_Image(idCardActivity.this,idCardLayout);
        Bitmap bitmap = layout_to_image.convert_layout();
        
        if (isWriteStoragePermissionGranted()){
            saveImgeToPhone(bitmap);
        }
    }

    private boolean isWriteStoragePermissionGranted() {
        boolean returnValue = false;

        if (Build.VERSION.SDK_INT >= 23){
            if (isExternalStorageWritavble()){
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    returnValue = true;
                }
                else {
                    ActivityCompat.requestPermissions(this,new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                    returnValue = false;
                }
            }
        }
        else
            returnValue = true;

        return returnValue;
    }

    private boolean isExternalStorageWritavble() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return true;
        return false;
    }

    private void saveImgeToPhone(Bitmap bitmap) {
        String location = Environment.getExternalStorageDirectory().toString();
        
        File myPath = new File(location,"steemidcard.png");

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}