package com.example.alex.worldpay3;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.ToIntFunction;

public class MainActivity extends AppCompatActivity {


    String blahbla;
    private int counter;
    class JSONDownload extends AsyncTask<String, Void, Void>
    {

        String call(String param){
            try {
                URL url = new URL(param);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line = null;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                return builder.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected Void doInBackground(String... params)
        {
            boolean notGenerated = true;
            try
            {
                    //textView.setText("test");
                    Thread.sleep(10);
                    if (counter==1) {
                        String response = call("http://10.0.0.5:5000/statusFaceDetection");
                        //.setText("maybe");
                        int verify = Integer.parseInt(response);
                        //Toast.makeText(MainActivity.this,verify,10);
                        textView.setText(response);
                        if(verify >= 0)
                        {
                            counter = 2;
                        }
                    }
                    else if (counter == 2 && notGenerated){
                        String response = call("http://http://10.0.0.5:5000/generateSequence");
                        notGenerated = false;
                        textView.setText(response);
                        counter = 3;
                    }else if(counter == 3){
                        Thread.sleep(5000);
                        String response = call("http://http://10.0.0.5:5000/completedSequence");
                        if (response.equalsIgnoreCase("ok"))
                            counter = 4;
                        else {
                            textView.setText("You fucked up");
                            counter = 1;
                        }
                    }else if(counter ==4){
                        String response = call("http://http://10.0.0.5:5000/buyService");
                        counter = 1;
                        textView.setText("You bought service and status was "+response);
                    }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {

        }
    }

    private TextView textView;
    //private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        //textView2 = findViewById(R.id.textView2);

        counter = 1;

        if(counter==1)
        {
            JSONDownload fdect = new JSONDownload();
            fdect.execute("http://10.0.0.5:5000/statusFaceDetection");
        }
        if(counter==2)
        {
            //textView2.setText("Detection Made");
            JSONDownload desc = new JSONDownload();
            desc.execute("http://10.0.0.5:5000/generateSequence");
        }
        if(counter==3)
        {
            //textView2.setText("Generated Sequence");
        }


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                promptSpeechInput();
            }
        });

        Button button1 = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });

        Button button2 = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });

        Button button3 = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });


        Button button4 = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });


        //queue.add(stringRequest);
    }
    public void promptSpeechInput()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        String languagePref = "en_US";//or, whatever iso code...
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languagePref);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languagePref);
        i.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, languagePref);
        //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something!");

        try{
            startActivityForResult(i,100);
        }
        catch(ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Sorry! This device doesn't support speech recognition", Toast.LENGTH_LONG).show();
        }
    }
    public void onActivityResult(int request_code, int result_code, Intent i){
        super.onActivityResult(request_code,result_code,i);
        switch(request_code)
        {
            case 100: if(result_code==RESULT_OK&&i!=null)
            {
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                textView.setText(result.get(0));
            }
                break;
        }
    }
}