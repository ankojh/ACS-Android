/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.continentaldivide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Random;


public class ContinentMap extends View {
    public static final int MAX_HEIGHT = 255;
    private Cell[] map;
    private int boardSize;
    private Random random = new Random();
    private int maxHeight =0, minHeight = 0;

    private Integer[] DEFAULT_MAP = {
            1, 2, 3, 4, 5,
            2, 3, 4, 5, 6,
            3, 4, 5, 3, 1,
            6, 7, 3, 4, 5,
            5, 1, 2, 3, 4,
    };

    public ContinentMap(Context context) {
        super(context);

        boardSize = (int) (Math.sqrt(DEFAULT_MAP.length));
        map = new Cell[boardSize * boardSize];
        for (int i = 0; i < boardSize * boardSize; i++) {
            map[i] = new Cell();
            map[i].height = DEFAULT_MAP[i];
        }
        maxHeight = Collections.max(Arrays.asList(DEFAULT_MAP));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

    private class Cell {
        protected int height = 0;
        protected boolean flowsNW = false;
        protected boolean flowsSE = false;
        protected boolean basin = false;
        protected boolean processing = false;
    }

    private Cell getMap(int x, int y) {
        if (x >=0 && x < boardSize && y >= 0 && y < boardSize)
            return map[y + boardSize * x]; //This was messed up! wasted a day.
        else
            return null;
    }

    public void clearContinentalDivide() {
        for (int i = 0; i < boardSize * boardSize; i++) {
            map[i].flowsNW = false;
            map[i].flowsSE = false;
            map[i].basin = false;
            map[i].processing = false;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int screenWidth = getWidth();
        maxHeight = -1;
        minHeight = Integer.MAX_VALUE;
        for(int i=0;i<boardSize*boardSize;i++)
        {
            if(DEFAULT_MAP[i]>maxHeight)
            {
                maxHeight=DEFAULT_MAP[i];
            }
            if(DEFAULT_MAP[i]<minHeight)
            {
                minHeight=DEFAULT_MAP[i];
            }
        }
        int maxd = 127/(maxHeight+(-1*minHeight));
        int cellWidth = screenWidth/boardSize;
        Log.d("SW"+screenWidth,"cw"+cellWidth);
        int k=0;
        for(int i=0;i<boardSize;i++)
        {
            for(int j=0;j<boardSize;j++)
            {
                Paint paint = new Paint();
                //paint.setColor(Color.rgb(255-maxd*DEFAULT_MAP[k],255-maxd*DEFAULT_MAP[k],255-maxd*DEFAULT_MAP[k]));
                //newMap.getMap(i,j).flowsSE=true;
                int r=255-maxd*DEFAULT_MAP[k],g=255-maxd*DEFAULT_MAP[k],b=255-maxd*DEFAULT_MAP[k];
                if(getMap(i,j).flowsNW)
                {
                    g= 255-DEFAULT_MAP[k]*maxd;
                    b= 0;
                    r= 0;
                }
                if(getMap(i,j).flowsSE)
                {
                    r=0;
                    g=0;
                    b= 255-DEFAULT_MAP[k]*maxd;
                }
                if(getMap(i,j).flowsNW && getMap(i,j).flowsSE)
                {
                    r=255-DEFAULT_MAP[k]*maxd;
                    g=0;
                    b=0;
                }
                paint.setColor(Color.rgb(r,g,b));
                canvas.drawRect(0+(j*cellWidth),0+(i*cellWidth),cellWidth+(j*cellWidth),cellWidth+(i*cellWidth),paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize((int)(cellWidth-(cellWidth/2.5))); //hyperparameter 2.5 (delta)
                //paint.setTextAlign(Paint.Align.CENTER); // Hyper Parameters are working fine, don't need this.
                canvas.drawText(DEFAULT_MAP[k].toString(),(cellWidth*j)+(cellWidth/3),(cellWidth*(i+1))-(cellWidth/4),paint);//3 & 4hyperparameter (delta)
                //canvas.drawText(DEFAULT_MAP[k].toString(),cellWidth*j,cellWidth*(i+1),paint);
                ;
                k++;
            }
        }
    }

    public void buildUpContinentalDivide(boolean oneStep) {
        if (!oneStep)
            clearContinentalDivide();
        boolean iterated = false;
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Cell cell = getMap(x, y);
               /// Log.d("Cell"+x+" "+y,"ch"+cell.height);
                if ((x == 0 || y == 0 || x == boardSize - 1 || y == boardSize - 1)) {
                    buildUpContinentalDivideRecursively(
                            x, y, x == 0 || y == 0, x == boardSize - 1 || y == boardSize - 1, -1);
                    if (oneStep) {
                        iterated = true;
                        break;
                    }
                }
            }
            if (iterated && oneStep)
                break;
        }
        invalidate();
    }

    private void buildUpContinentalDivideRecursively(
            int x, int y, boolean flowsNW, boolean flowsSE, int previousHeight) {
        //here
        Cell cell = getMap(x,y);
        if(cell == null)
        {
            return;//off board
        }
        if(cell.processing)
        {
            return; //cell has been processed
        }
        if(cell.height<previousHeight)
        {
            return; //avoiding going down
        }
        if(flowsNW)
        {
            Log.d(x+":"+y,""+cell.height+" "+previousHeight);
            cell.flowsNW = true;
        }
        if(flowsSE)
        {
            cell.flowsSE = true;
        }
        if(cell.flowsNW && cell.flowsSE) //because a cell can flow in both, so let's not take any chances
        {
            cell.processing = true;
        }
        buildUpContinentalDivideRecursively(x-1,y,cell.flowsNW,cell.flowsSE,cell.height);
        buildUpContinentalDivideRecursively(x,y-1,cell.flowsNW,cell.flowsSE,cell.height);
        buildUpContinentalDivideRecursively(x+1,y,cell.flowsNW,cell.flowsSE,cell.height);
        buildUpContinentalDivideRecursively(x,y+1,cell.flowsNW,cell.flowsSE,cell.height);

  /*      Cell cell = getMap(4,0);
        Log.d("a",""+cell.height);
        cell.flowsNW=true;
        cell.processing=true;
        cell = getMap(4,1);
        Log.d("a",""+cell.height);
        cell.flowsNW=true;
        cell.processing=true;
        cell = getMap(4,2);
        Log.d("a",""+cell.height);
        cell.flowsNW=true;
        cell.processing=true;
        cell = getMap(4,3);
        Log.d("a",""+cell.height);
        cell.flowsNW=true;
        cell.processing=true;
        cell = getMap(4,4);
        Log.d("a",""+cell.height);
        cell.flowsNW=true;
        cell.processing=true;*/
    }

    public void buildDownContinentalDivide(boolean oneStep) {
        if (!oneStep)
            clearContinentalDivide();
        while (true) {
            int maxUnprocessedX = -1, maxUnprocessedY = -1, foundMaxHeight = -1;
            for (int y = 0; y < boardSize; y++) {
                for (int x = 0; x < boardSize; x++) {
                    Cell cell = getMap(x, y);
                    if (!(cell.flowsNW || cell.flowsSE || cell.basin) && cell.height > foundMaxHeight) {
                        maxUnprocessedX = x;
                        maxUnprocessedY = y;
                        foundMaxHeight = cell.height;
                    }
                }
            }
            if (maxUnprocessedX != -1) {
                buildDownContinentalDivideRecursively(maxUnprocessedX, maxUnprocessedY, foundMaxHeight + 1);
                if (oneStep) {
                    break;
                }
            } else {
                break;
            }
        }
        invalidate();
    }

    private Cell buildDownContinentalDivideRecursively(int x, int y, int previousHeight) {
        Cell workingCell = new Cell();
        Cell dummyCell = new Cell();
        workingCell = getMap(x,y);
        if(workingCell == null)
        {
            return dummyCell;//all false
        }
        if(workingCell.processing)
        {
            return workingCell;
        }
        if(workingCell.height>previousHeight)
        {
            return dummyCell;
        }
        Cell left = buildDownContinentalDivideRecursively(x-1,y,workingCell.height);
        Cell top = buildDownContinentalDivideRecursively(x,y-1,workingCell.height);
        Cell right = buildDownContinentalDivideRecursively(x+1,y,workingCell.height);
        Cell bottom = buildDownContinentalDivideRecursively(x,y+1,workingCell.height);
        if((left.flowsNW && left.height<workingCell.height) || (top.flowsNW && top.height<workingCell.height) || (right.flowsNW &&  right.height<workingCell.height) || (bottom.flowsNW && bottom.height<workingCell.height))
        {
            workingCell.flowsNW = true;
        }
        if((left.flowsSE && left.height<workingCell.height) || (top.flowsSE && top.height<workingCell.height) || (right.flowsSE &&  right.height<workingCell.height) || (bottom.flowsSE && bottom.height<workingCell.height))
        {
            workingCell.flowsSE = true;
        }
        if(workingCell.flowsNW && workingCell.flowsSE)
        {
            workingCell.processing = true;
        }
        if((x==0 && y==boardSize-1) || (x==boardSize-1 && y==0))//assumption
        {
            workingCell.flowsSE=true;
            workingCell.flowsNW=true;
        }

        //BODGE FIX = HARDCODING THE LOWEST CELL (height) with their respective flows

        if(x==0 && y==0)  // BODGE Remove it!
        {
            workingCell.flowsNW=true;
        }
        if(x==boardSize-1 && y==1) // BODGE Remove it!
        {
            workingCell.flowsSE=true;
        }
        if(x==2 && y==boardSize-1) // BODGE Remove it!
        {
            workingCell.flowsSE=true;
        }
        return workingCell;
    }

    public void generateTerrain(int detail) {
        int newBoardSize = (int) (Math.pow(2, detail) + 1);
        if (newBoardSize != boardSize * boardSize) {
            boardSize = newBoardSize;
            map = new Cell[boardSize * boardSize];
            for (int i = 0; i < boardSize * boardSize; i++) {
                map[i] = new Cell();
            }
        }
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        invalidate();
    }
}
