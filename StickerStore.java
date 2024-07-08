package com.example.test7;


import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class StickerStore extends AppCompatActivity {

    private Button getStickerButton;
    private TextView statusTextView;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int STICKER_COST = 10; // Cost of a sticker
    private int userFunds = 100; // Initial user funds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_store);

        getStickerButton = findViewById(R.id.getStickerButton);
        statusTextView = findViewById(R.id.statusTextView);

        updateFundsDisplay();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }

        getStickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSufficientFunds()) {
                    getRandomSticker();
                } else {
                    Toast.makeText(StickerStore.this, "Insufficient funds!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean hasSufficientFunds() {
        return userFunds >= STICKER_COST;
    }

    private void updateFundsDisplay() {
        statusTextView.setText("Your funds: $" + userFunds);
    }

    private void getRandomSticker() {
        TypedArray stickers = getResources().obtainTypedArray(R.array.sticker_array);
        int randomIndex = new Random().nextInt(stickers.length());
        int resourceId = stickers.getResourceId(randomIndex, -1);
        stickers.recycle();

        if (resourceId != -1) {
            Drawable drawable = getResources().getDrawable(resourceId);
            saveImageToGallery((BitmapDrawable) drawable);
            userFunds -= STICKER_COST;
            updateFundsDisplay();
        } else {
            Toast.makeText(this, "Error getting sticker", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToGallery(BitmapDrawable drawable) {
        Bitmap bitmap = drawable.getBitmap();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "sticker_" + System.currentTimeMillis() + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Stickers");

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            Toast.makeText(this, "Sticker saved successfully", Toast.LENGTH_SHORT).show();

            // Share the sticker
            shareSticker(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving sticker", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareSticker(Uri imageUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Sticker"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}