package com.google.engedu.worldladder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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

        //Extension Part
        View.OnFocusChangeListener listener;

        listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int vId= v.getId();
                EditText editText = (EditText)findViewById(vId);
                Log.d("edittext",""+editText.getText()+" "+words[vId]);
                if(editText.getText().toString().equals(words[vId]))
                {
                    Log.d("green","ghantaGreen");
                    editText.setTextColor(getResources().getColor(R.color.greenColor));
                }
                else
                {
                    Log.d("red","hartime Red");
                    editText.setTextColor(getResources().getColor(R.color.redColor));
                }
            }
        };


        for(int i=0;i<words.length-2;i++)
        {
            EditText editText = new EditText(this);
            editText.setId(i+1);
            Log.d("EditText"+i,""+words[i+1]);
            editText.setLayoutParams(params);
            editText.setOnFocusChangeListener(listener);
           // editText.addTextChangedListener(editTextWatcher);
            layout.addView(editText);
        }
        TextView endTextView = new TextView(this);
        endTextView.setText(words[words.length-1]);
        //endTextView.setId(99+2);;//Actually I think I dont need it.
        layout.addView(endTextView);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        Button solverButton = new Button(this);
        solverButton.setText("Solve");
        solverButton.setLayoutParams(buttonParams);
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
