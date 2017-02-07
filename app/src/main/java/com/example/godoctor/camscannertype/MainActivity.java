package com.example.godoctor.camscannertype;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CROP_IMAGE=2;
    ImageView mImageView;
    Bitmap imageBitmap,operation,bmp;
//    private Uri picUri;
    ImageView image1,image2,image3;
    ProgressBar progressBar;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView= (ImageView) findViewById(R.id.image);
        image1= (ImageView) findViewById(R.id.image1);
        image2= (ImageView) findViewById(R.id.image2);
        image3= (ImageView) findViewById(R.id.image3);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         doBrightness();
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              bright();
                Log.e("Clicked","Clicked");
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             gama(v);
            }
        });
        dispatchTakePictureIntent();
    }

        private void dispatchTakePictureIntent() {
            Intent cropIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
             cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (cropIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cropIntent, REQUEST_IMAGE_CAPTURE);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            performCrop();
        }else{
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            bmp=imageBitmap;
            mImageView.setImageBitmap(imageBitmap);
        }
    }


    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(Uri.fromFile(file), "image/*");
    //            cropIntent.putExtra("data",imageBitmap);
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 2);
//            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
//            cropIntent.putExtra("outputX", 256);
//            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_IMAGE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void gray(View view) {
        operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());
        double red = 0.33;
        double green = 0.59;
        double blue = 0.11;

        for (int i = 0; i < bmp.getWidth(); i+=1) {
            for (int j = 0; j < bmp.getHeight(); j+=1) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                r = (int) red * r;
                g = (int) green * g;
                b = (int) blue * b;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        mImageView.setImageBitmap(operation);
    }

    public void bright(){
        operation= Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),bmp.getConfig());
        progressBar.setVisibility(View.VISIBLE);
//        mImageView.setImageResource(R.mipmap.ic_launcher);
        for(int i=0; i<bmp.getWidth(); i+=10){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = 10  +  r;
                g = 10  + g;
                b = 10  + b;
                alpha = 10 + alpha;
                operation.setPixel(i, j, Color.argb(alpha, r, g, b));
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
        mImageView.setImageBitmap(operation);
    }

    public void dark(View view){
        operation= Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i+=2){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  r - 10;
                g =  g - 10;
                b =  b - 10;
                alpha = alpha -10;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        mImageView.setImageBitmap(operation);
    }

    public void gama(View view) {
        operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i+=2){
            for(int j=0; j<bmp.getHeight(); j+=2){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  r + 10;
                g =  0;
                b =  0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        mImageView.setImageBitmap(operation);
    }

    public void green(View view){
        operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());

        for(int i=0; i <bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  0;
                g =  g+150;
                b =  0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        mImageView.setImageBitmap(operation);
    }

    public void blue(View view){
        operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  0;
                g =  0;
                b =  b+150;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(/*Color.alpha(p), r, g, b*/80, 255, 0, 0));
            }
        }
        mImageView.setImageBitmap(operation);
    }
    void doBrightness() {
        int value=100;
      Bitmap  src=imageBitmap;
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += value;
                if (R > 255)
                {
                    R = 255;
                } else if (R < 0)
                {
                    R = 0;
                }

                G += value;
                if (G > 255)
                {
                    G = 255;
                } else if (G < 0)
                {
                    G = 0;
                }

                B += value;
                if (B > 255)
                {
                    B = 255;
                } else if (B < 0)
                {
                    B = 0;
                }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        mImageView.setImageBitmap(bmOut);    }
}

