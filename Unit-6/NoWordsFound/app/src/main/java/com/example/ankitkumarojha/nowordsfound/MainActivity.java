package com.example.ankitkumarojha.nowordsfound;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Queue;

import static java.lang.Math.abs;

// TODO
// 1. Result text back to normal after user goes for another word
// 2. On Reset - reset all the textviews.
// 3. naiveComputerTurn on Finish
// 4. adeptComputerTurn on Finish
// 5. gridSize Issue
// 6. UI improvement

public class MainActivity extends AppCompatActivity {

    public final int gridSize = 10;
    public final String DICTIONARY_LOAD = "Dictionary-LOAD";
    public Dictionary dictionary;
    public boolean isGridContentSet=false;
    public String selectedString="";
    public int score=0,penalty=0,numberOfCharactersSelected=0,frontId=-1,rearId=-1,screenWidth;
    public ArrayList<Integer> foundWordGridId=new ArrayList<>();
    public ArrayList<Integer> resultWordGridId=new ArrayList<>();
    public boolean correctWord=false;
    public ColorStateList resultViewDefaultTextColor;
    HashMap<Integer,Character> gridMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PopulateWords populateWords = new PopulateWords();
        TextView resultTextView = (TextView)findViewById(R.id.resultTextView);
        resultViewDefaultTextColor=resultTextView.getTextColors();
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new Dictionary(inputStream);
        } catch (IOException e) {
            Log.e(DICTIONARY_LOAD, "Cannot Load The Dictionary");
        }
        onStart();

    }

    @Override
    protected void onStart() {

        Button checkWordButton = (Button) findViewById(R.id.checkWordButton);
        final Button finishButton = (Button) findViewById(R.id.finishButton);
        Button resetButton = (Button) findViewById(R.id.resetButton);
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);
        TextView resultTextView = (TextView)findViewById(R.id.resultTextView);
        resultTextView.setText(":)");
        resultTextView.setTextColor(resultViewDefaultTextColor);
        foundWordGridId.clear();
        resultWordGridId.clear();
        final GridContent gridContent = new GridContent(dictionary.getRandomWords());
        gridContent.insertValidWords();
        score=0;
        penalty=0;
        updateScoreTextView();
        numberOfCharactersSelected=0;
        selectedString="";
        frontId=-1;
        rearId=-1;
        correctWord=false;
        storeFoundWordGridIds();
        isGridContentSet=false;
        fillTable(table,gridContent);

        checkWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);
                TextView resultTextView = (TextView) findViewById(R.id.resultTextView);
                Log.d("CHECK BUTTON",""+dictionary.checkIsWord(selectedString.toString()));
                if(selectedString!="")
                    if(dictionary.checkIsWord(selectedString.toString())==selectedString.length()-1)
                    {
                        updateScore(true);
                        correctWord=true;
                    }
                    else
                        updateScore(false);
                else
                {
                    resultTextView.setTextColor(resultViewDefaultTextColor);
                    resultTextView.setText("select word");
                }
                Log.d("OnSTart","STWGI");
                storeFoundWordGridIds();
                correctWord=false;
                frontId=-1;
                rearId=-1;
                selectedString="";
                numberOfCharactersSelected=0;
                fillTable(table,gridContent);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView resultTextView=(TextView)findViewById(R.id.resultTextView);
                TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);
                resultTextView.setText(""+(score-penalty));
                resultTextView.setTextColor(resultViewDefaultTextColor);
                //naiveComputerTurn(gridContent);
                        //or
                adeptComputerTurn(gridContent);

                fillTable(table,gridContent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        super.onStart();
    }

    public void storeFoundWordGridIds()
    {
        if(correctWord)
        {
            boolean isHorizontalD=false;
            isHorizontalD=(rearId-frontId<10)?true:false;
            Log.d("STGWI","HORIZONTALD "+isHorizontalD);
            Log.d("front and rear",""+frontId+" "+rearId);
            if(isHorizontalD)
            {
                while(!(frontId>rearId))
                {
                    foundWordGridId.add(frontId);
                    frontId++;
                }
            }
            else
            {
                while(!(frontId>rearId))
                {
                    foundWordGridId.add(frontId);
                    frontId +=10;
                }
            }
        }
        Log.d("STOREFOUND",foundWordGridId.toString());
    }

    public void fillTable(TableLayout table,GridContent gridContent) {
        Log.d("filltable","methodcalled");
        TextView resultTextView = (TextView)findViewById(R.id.resultTextView);
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        Log.d("Screen size",""+screenWidth);
        table.removeAllViews();
        //table.setBackgroundColor(Color.LTGRAY);
        int sizeOfEachCell = screenWidth/11; //10 grids + 0.5x2 spacing on both sides
        gridMap.clear();
        for (int i = 0; i < 10; i++)
        {
            TableRow row = new TableRow(MainActivity.this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < gridSize; j++)
            {
                final TextView gridTextView = new TextView(this);
                final int gridViewId = (i * gridSize + j);

                gridTextView.setId(gridViewId);
                gridTextView.setHeight(sizeOfEachCell);
                gridTextView.setWidth(sizeOfEachCell);
                gridTextView.setText(""+gridContent.grid[i][j]);
                //gridTextView.setText("#");
                gridMap.put(gridViewId,gridContent.grid[i][j]);
                //gridTextView.setText(""+populateWords.gameGrid[i][j]);
                gridTextView.setTag("n_selected");
                gridTextView.setTextSize(20);//it takes float size not pixel. :(
                gridTextView.setGravity(1);
                gridTextView.setPaddingRelative(sizeOfEachCell/4,sizeOfEachCell/16,sizeOfEachCell/4,sizeOfEachCell/16);
                if(foundWordGridId.contains(gridViewId) && resultWordGridId.contains(gridViewId))
                {
                    gridTextView.setBackgroundResource(R.drawable.gridcorrectsolutionstyle);
                }
                else if(foundWordGridId.contains(gridViewId))
                {
                    gridTextView.setBackgroundResource(R.drawable.gridcorrectstyle);
                }
                else if(resultWordGridId.contains(gridViewId))
                {

                    gridTextView.setBackgroundResource(R.drawable.gridsolutionstyle);
                }
                else
                {
                    gridTextView.setBackgroundResource(R.drawable.gridcellstyle);
                }
                row.addView(gridTextView);
                //Log.d("GridView","row added");
                gridTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        TextView resultTextView = (TextView)findViewById(R.id.resultTextView);
                        resultTextView.setText(":)");
                        resultTextView.setTextColor(resultViewDefaultTextColor);
                        //Log.d("GridView","ONCLICKLISTENER");
                        if(gridTextView.getTag().equals("selected"))
                        {
                            int flag=-1;
                            if(gridViewId==frontId)
                            {
                                flag=0;
                            }
                            if(gridViewId==rearId)
                            {
                                flag=1;
                            }
                            if(flag==0) //remove front selected
                            {
                                gridTextView.setTag("n_selected");
                                gridTextView.setBackgroundResource(R.drawable.gridcellstyle);
                                selectedString=selectedString.substring(1);
                                numberOfCharactersSelected--;
                                if(rearId-frontId<gridSize)
                                {
                                    frontId++;
                                }
                                else
                                {
                                    frontId+=gridSize;
                                }
                                Log.d("Front ID shifted",""+frontId);
                                if(numberOfCharactersSelected==0)
                                {
                                    rearId=-1;
                                    frontId=-1;
                                }
                                if(numberOfCharactersSelected==1)
                                {
                                    //front and rear id must be same
                                }

                                Log.d("selectedString",""+selectedString);
                                Log.d("numberCharSelected",""+numberOfCharactersSelected);
                            }
                            if(flag==1) //remove last selected
                            {
                                gridTextView.setTag("n_selected");
                                if(foundWordGridId.contains(gridViewId))
                                {
                                    gridTextView.setBackgroundResource(R.drawable.gridselectedstyle);
                                }
                                else
                                {
                                    gridTextView.setBackgroundResource(R.drawable.gridcellstyle);
                                }
                                selectedString=selectedString.substring(0,selectedString.length()-1);
                                numberOfCharactersSelected--;
                                if(rearId-frontId<gridSize)
                                {
                                    rearId--;
                                }
                                else
                                {
                                    rearId-=gridSize;
                                }
                                Log.d("Rear ID shifted",""+rearId);
                                if(numberOfCharactersSelected==0)
                                {
                                    rearId=-1;
                                    frontId=-1;
                                }
                                if(numberOfCharactersSelected==1)
                                {
                                    //front and rear id must be same
                                }
                                Log.d("selectedString",""+selectedString);
                                Log.d("numberCharSelected",""+numberOfCharactersSelected);
                            }
                        }
                        else
                        {
                            //Log.d("Lol","pajji");
                            int flag =-1;
                            if(numberOfCharactersSelected==0)
                            {
                                Log.d("numberCharacterSelected",""+numberOfCharactersSelected);
                                flag=0;
                                frontId=gridViewId;
                                rearId=gridViewId;

                            }
                            if(numberOfCharactersSelected>0) {
                                flag=checkAtExtremeOfLine(gridViewId);
                            }
                            if(flag==0)//insert at beg
                            {
                                gridTextView.setBackgroundResource(R.drawable.gridselectedstyle);
                                char selectedCharacter = gridMap.get(gridViewId);
                                selectedString=selectedCharacter+selectedString;
                                numberOfCharactersSelected++;
                                frontId=gridViewId;
                                gridTextView.setTag("selected");
                                Log.d("characterSelected",""+gridViewId);
                                Log.d("String",selectedString);
                            }
                            if(flag==1)//insert at end
                            {
                                gridTextView.setBackgroundResource(R.drawable.gridselectedstyle);
                                char selectedCharacter = gridMap.get(gridViewId);
                                selectedString=selectedString+selectedCharacter;
                                numberOfCharactersSelected++;
                                rearId=gridViewId;
                                gridTextView.setTag("selected");
                                Log.d("characterSelected",""+gridViewId);
                                Log.d("String",selectedString);
                            }
                        }

                    }
                });

            }
            table.addView(row);
        }
    }

    public int checkAtExtremeOfLine(int gridViewTextId)
    {
        //Log.d("CAEOL numcharSelected",""+numberOfCharactersSelected);
        if(numberOfCharactersSelected==1) //for one grid selected 4 possibilities
        {
            if(gridViewTextId-1==frontId)
            {
                //Log.d("gridRelative","selected right cell");
                return 1;
            }
            if(gridViewTextId+1==frontId)
            {
                //Log.d("gridRelative","selected left cell");
                return 0;
            }
            if(gridViewTextId-10==frontId)
            {
                //Log.d("gridRelative","selected below cell");
                return 1;
            }
            if(gridViewTextId+10==frontId)
            {
                //Log.d("gridRelative","selected above cell");
                return 0;
            }
        }
        else //for more than one grid selected only 2 possibilities Left or right and above and below
        {
           if(rearId-frontId<10)
           {
               //Log.d("charSelected","Horizontal");
               if(gridViewTextId==rearId+1)
               {
                   //Log.d("Insert Character","At Last of String");
                   return 1;
               }
               if(gridViewTextId==frontId-1)
               {
                   //Log.d("Insert Character","In front of String");
                   return 0;
               }

           }
           else
           {
               //Log.d("charSelected","Vertical");
               if(gridViewTextId==rearId+10)
               {
                   //Log.d("Insert Character","At Last of String");
                   return 1;
               }
               if(gridViewTextId==frontId-10)
               {
                   //Log.d("Insert Character","In front of String");
                   return 0;
               }

          }
        }
        return -1;
    }

    public void updateScore(boolean correct)
    {
        final String UPDATE_SCORE="Update Score Method";
        TextView resultTextView = (TextView)findViewById(R.id.resultTextView);
        if(correct)
        {
            //Log.d(UPDATE_SCORE,"Correct Word Selected");
            resultTextView.setText("Correct!");
            resultTextView.setTextColor(Color.GREEN);
            score += 10;
        }
        else
        {
           // Log.d(UPDATE_SCORE,"Wrong Word Selected");
            resultTextView.setText("Opps No!");
            resultTextView.setTextColor(Color.RED);
            penalty +=5;
        }
        updateScoreTextView();

        //Log.d(UPDATE_SCORE,"Score Updated");
    }

    public void updateScoreTextView()
    {
        final String UPDATE_SCORE_TEXT_VIEW = "Update Score TextView";
        //Log.d(UPDATE_SCORE_TEXT_VIEW,"Method Called");
        TextView scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        TextView penaltyTextView = (TextView) findViewById(R.id.penaltyTextView);
        scoreTextView.setText("Score = "+score);
        penaltyTextView.setText("Penalty = -"+penalty);
        //Log.d(UPDATE_SCORE_TEXT_VIEW,"Score TextView Updated");
    }

    public void naiveComputerTurn(GridContent gridContent)
    {
        HashMap<String,Pair<Integer,Boolean>> storedWords = gridContent.getWordStored();
        //HashMap<String,Boolean> storedWordsDirection = gridContent.getWordDirection();
        ArrayList<String> words = gridContent.insertedWords;
        for(String word:words)
        {
            if(storedWords.containsKey(word))
            {
                boolean isHorizontal=storedWords.get(word).second;
                if(isHorizontal)
                {
                    int startIndex=storedWords.get(word).first;
                    for(int i=0;i<word.length();i++)
                    {
                        resultWordGridId.add(startIndex+i);
                    }
                }
                else
                {
                    int startIndex=storedWords.get(word).first;
                    for(int i=0;i<word.length();i++)
                    {
                        resultWordGridId.add(startIndex+(i*10));
                    }
                }
            }
        }
    }

    public void adeptComputerTurn(GridContent gridContent)
    {
        //for horizontalwords
        for(int i=0;i<10;i++)
        {
            String tempString = "";
            for(int j=0;j<10;j++)//picking one whole row
            {
                tempString+=gridContent.grid[i][j];
            }
            for(int j=0;j<7;j++)//a word not start after 6th index as minimum word size is 4
            {
                int checkWordResult = dictionary.checkIsWord(tempString);
                if(checkWordResult!=-1)//check if tempString has a word
                {
                    Log.d("CheckWordResult",""+checkWordResult);
                    for(int k=0;k<=checkWordResult;k++)
                    {
                        Log.d("Adding ids "+tempString,""+((i*10)+(j+k)));
                        resultWordGridId.add((i*10)+(j+k));
                    }
                    tempString=tempString.substring(checkWordResult+1);
                    j+=checkWordResult;
                }
                else
                {
                    tempString=tempString.substring(1);
                }
            }
        }

        //for verticalwords
        for(int i=0;i<10;i++)
        {
            String tempString = "";
            for(int j=0;j<10;j++)//picking a whole column
            {
                tempString+=gridContent.grid[j][i];
            }
            for(int j=0;j<7;j++)//a word not start after 6th index as minimum word size is 4
            {
                int checkWordResult = dictionary.checkIsWord(tempString);
                if(checkWordResult!=-1)//check if tempString has a word
                {
                    Log.d("CheckWordResult"," "+checkWordResult);
                    for(int k=0;k<=checkWordResult;k++)
                    {
                        Log.d("Adding ids "+tempString,""+(((j+k)*10)+i));
                        resultWordGridId.add(((j+k)*10)+i);
                    }
                    j+=checkWordResult;
                    tempString=tempString.substring(checkWordResult+1);
                    Log.d("J skipped",""+j);
                }
                else
                {
                    tempString=tempString.substring(1);
                }
            }
        }
    }
}