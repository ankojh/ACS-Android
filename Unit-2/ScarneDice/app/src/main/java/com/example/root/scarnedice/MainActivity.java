package com.example.root.scarnedice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int MAX_SCORE = 100;
    public static final int MAX_DICE_VALUE = 6;
    public int userOverallScore = 0;
    public int userTurnScore = 0;
    public int compOverallScore = 0;
    public int compTurnScore = 0;
    public int currentDiceValue = 0;
    public boolean userTurn = true; //1 is user & 0 is computer // user starts the game always
    public boolean holdButtonPressed = false; //0 is no & 1 is yes

    public void updateTextView()
    {
        TextView textView = (TextView) findViewById(R.id.scoreView);
        TextView textView1 = (TextView) findViewById(R.id.tempScore);
        String textViewString="Your Overall Score:"+userOverallScore+" | Computer Overall Score:"+compOverallScore+".";
        String textViewString1="Your Score:"+userTurnScore+" | Computer Score:"+compTurnScore+".";
        textView.setText(textViewString);
        textView1.setText(textViewString1);
    }

    public void updateAction(String a)
    {
        TextView textActionView = (TextView) findViewById(R.id.action);
        String actionText=a;
        if (userTurn) {
            actionText = "User " + a;
        } else {
            actionText = "Computer " + a;
        }
        textActionView.setText(actionText);

    }

    public void updateUserTurnText()
    {
        TextView holdText = (TextView)findViewById(R.id.holdText);
        Log.d("updateUser","called");
        if(userTurn)
            holdText.setText("Next: User's Turn");
        else
            holdText.setText("Next: Computer's Turn");
    }

    public void computerTurn()
    {

        //Log.d("Computer turn","Function called");
        Button rollButton = (Button)findViewById(R.id.rollButton);
        final TextView winTextView = (TextView) findViewById(R.id.winText);
        final Button holdButton = (Button)findViewById(R.id.holdButton);

        Random random = new Random();
        int p = random.nextInt(1);

        //if(p==0)
        //{
        //    rollButton.performClick();
        //}
        //else
        // {
        //    holdButton.performClick();
        // }

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateAction("Scarne's Dice");
                Log.d("Computer Turn","Dice Rolled");
                Random random = new Random();
                currentDiceValue = random.nextInt(MAX_DICE_VALUE-1)+1;
                Log.d("Dice Value",""+currentDiceValue);
                ImageView diceImage = (ImageView)findViewById(R.id.imageView);
                if(currentDiceValue == 1)
                {
                    diceImage.setImageResource(R.drawable.dice1);
                }
                if(currentDiceValue == 2)
                {
                    diceImage.setImageResource(R.drawable.dice2);
                }
                if(currentDiceValue == 3)
                {
                    diceImage.setImageResource(R.drawable.dice3);
                }
                if(currentDiceValue == 4)
                {
                    diceImage.setImageResource(R.drawable.dice4);
                }
                if(currentDiceValue == 5)
                {
                    diceImage.setImageResource(R.drawable.dice5);
                }
                if(currentDiceValue == 6)
                {
                    diceImage.setImageResource(R.drawable.dice6);
                }
                if(userTurn)
                {
                    userTurnScore+=currentDiceValue;
                }
                else
                {
                    compTurnScore+=currentDiceValue;
                }
                if(currentDiceValue == 1)
                {
                    updateAction("rolled one");
                    userTurn=!userTurn;
                    userTurnScore=0;
                    compTurnScore=0;
                    updateTextView();
                    Log.d("1 facing up","Turn values Reset");

                }
                updateTextView();
                updateUserTurnText();
            }

        });
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView();
                userOverallScore += userTurnScore;
                compOverallScore += compTurnScore;
                updateTextView();
                userTurnScore = 0;
                compTurnScore = 0;
                updateAction("holds");
                userTurn = !userTurn;
                //if userturn 0 then call computer turn here.
                updateTextView();
                if(userOverallScore >= 100 || compOverallScore >= 100)
                {
                    winTextView.setVisibility(View.VISIBLE);
                    if(userOverallScore >= 100)
                        winTextView.setText("USER WINS");
                    else
                        winTextView.setText("COMPUTER WINS");

                    userOverallScore = 0;
                    userTurnScore = 0;
                    compOverallScore = 0;
                    compTurnScore = 0;
                    currentDiceValue = 0;
                    //userTurn=true;
                    updateAction("Scarne\'s Dice");
                    updateUserTurnText();
                }
                updateUserTurnText();
                Log.d("TextView", "Updated on click of roll button");
                updateUserTurnText();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rollButton = (Button)findViewById(R.id.rollButton);
        final Button holdButton = (Button)findViewById(R.id.holdButton);
        final Button resetButton = (Button)findViewById(R.id.resetButton);
        final TextView winTextView = (TextView) findViewById(R.id.winText);

        winTextView.setVisibility(View.INVISIBLE);

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateAction("Scarne's Dice");
                Random random = new Random();
                currentDiceValue = random.nextInt(MAX_DICE_VALUE-1)+1;
                Log.d("Dice Value",""+currentDiceValue);
                ImageView diceImage = (ImageView)findViewById(R.id.imageView);
                if(currentDiceValue == 1)
                {
                    diceImage.setImageResource(R.drawable.dice1);
                }
                if(currentDiceValue == 2)
                {
                    diceImage.setImageResource(R.drawable.dice2);
                }
                if(currentDiceValue == 3)
                {
                    diceImage.setImageResource(R.drawable.dice3);
                }
                if(currentDiceValue == 4)
                {
                    diceImage.setImageResource(R.drawable.dice4);
                }
                if(currentDiceValue == 5)
                {
                    diceImage.setImageResource(R.drawable.dice5);
                }
                if(currentDiceValue == 6)
                {
                    diceImage.setImageResource(R.drawable.dice6);
                }
                if(userTurn)
                {
                    userTurnScore+=currentDiceValue;
                }
                else
                {
                    compTurnScore+=currentDiceValue;
                }
                if(currentDiceValue == 1)
                {
                    updateAction("rolled one");
                    userTurn=!userTurn;
                    userTurnScore=0;
                    compTurnScore=0;
                    updateTextView();
                    Log.d("1 facing up","Turn values Reset");

                }
                updateTextView();
                updateUserTurnText();
            }

        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userOverallScore = 0;
                userTurnScore = 0;
                compOverallScore = 0;
                compTurnScore = 0;
                currentDiceValue = 0;
                userTurn = true;
                updateUserTurnText();
                updateTextView();
                holdButtonPressed = false;
                winTextView.setVisibility(View.INVISIBLE);
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextView();
                userOverallScore += userTurnScore;
                compOverallScore += compTurnScore;
                updateTextView();
                userTurnScore = 0;
                compTurnScore = 0;
                updateAction("holds");
                userTurn = !userTurn;
                //if(!userTurn)
                //computerTurn();
                if(!userTurn)
                {
                    computerTurn();
                    Log.d("Called ComputerTurn:"," from userturn");
                }
                //if userturn 0 then call computer turn here.
                updateTextView();
                if(userOverallScore >= 100 || compOverallScore >= 100)
                {
                    winTextView.setVisibility(View.VISIBLE);
                    if(userOverallScore >= 100)
                        winTextView.setText("USER WINS");
                    else
                        winTextView.setText("COMPUTER WINS");

                    userOverallScore = 0;
                    userTurnScore = 0;
                    compOverallScore = 0;
                    compTurnScore = 0;
                    currentDiceValue = 0;
                    //userTurn=true;
                    updateAction("Scarne\'s Dice");
                    updateUserTurnText();
                }
                updateUserTurnText();
                Log.d("TextView", "Updated on click of roll button");
                updateUserTurnText();
            }
        });
    }
}