package com.example.wenyun.voicetester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.lang.Object;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);


        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("requestCode", requestCode+"");
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    if(result.get(0).contains("call") || result.get(0).contains("phone")){
                        call("1234567890");
                    }
                    if(result.get(0).contains("email") || result.get(0).contains("mail")){
                        email("abcdefg@company.com");
                    }
                    if(result.get(0).contains("photo") || result.get(0).contains("picture")){
                        camera();
                    }
                }
                break;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void call(String no) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+no));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        startActivity(intent);

    }

    private void email(String email){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "Test");
        i.putExtra(Intent.EXTRA_TEXT   , "I am an email sent automatically");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void camera(){
        String folder = "/storage/emulated/0/test/";
        File newdir = new File(folder);
        newdir.mkdir();

        File newfile = new File(folder+"1.jpg");
        try {
            newfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri temp_uri = Uri.fromFile(newfile);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, temp_uri);
        startActivityForResult(i, 0);
    }


}
