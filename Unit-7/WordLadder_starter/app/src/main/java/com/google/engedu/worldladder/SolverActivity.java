package com.google.engedu.worldladder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SolverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver);

        Intent intent = getIntent();
        final String[] words = intent.getStringArrayExtra("path");

        LinearLayout layout =(LinearLayout)findViewById(R.id.activity_solver);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView startTextView = new TextView(this);
        startTextView.setText(words[0]);
        //startTextView.setText(99+1);//depth less than 99
        layout.addView(startTextView);
        Log.d("StartText",""+words[0]);

        for(int i=0;i<words.length-2;i++)
        {
            EditText editText = new EditText(this);
            editText.setId(i+1);
            Log.d("EditText"+i,""+words[i+1]);
            editText.setLayoutParams(params);
            layout.addView(editText);
        }
        TextView endTextView = new TextView(this);
        endTextView.setText(words[words.length-1]);
        //endTextView.setId(99+2);;//Actually I think I dont need it.
        layout.addView(endTextView);

        Button solverButton = new Button(this);
        solverButton.setText("Solve");
        solverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<words.length-2;i++)
                {
                    EditText editText = (EditText)findViewById(i+1);
                    editText.setText(words[i+1]);
                }
            }
        });
        layout.addView(solverButton);

    }
}
