package com.bannuranurag.android.augv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PrintJSON extends AppCompatActivity {
    TextView jsonText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_json);
        jsonText=findViewById(R.id.print_json);



        String json;
        Bundle extras = getIntent().getExtras();
        json= extras.getString("JSON_OUTPUT");
        jsonText.setText(json);

    }



}
