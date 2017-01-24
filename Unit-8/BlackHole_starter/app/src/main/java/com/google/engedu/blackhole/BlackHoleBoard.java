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

package com.google.engedu.blackhole;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

/* Class that represent the state of the game.
 * Note that the buttons on screen are not updated by this class.
 */
public class BlackHoleBoard {
    // The number of turns each player will take.
    public final static int NUM_TURNS = 10;
    // Size of the game board. Each player needs to take 10 turns and leave one empty tile.
    public final static int BOARD_SIZE = NUM_TURNS * 2 + 1;
    // Relative position of the neighbors of each tile. This is a little tricky because of the
    // triangular shape of the board.
    public final static int[][] NEIGHBORS = {{-1, -1}, {0, -1}, {-1, 0}, {1, 0}, {0, 1}, {1, 1}};
    // When we get to the Monte Carlo method, this will be the number of games to simulate.
    private static final int NUM_GAMES_TO_SIMULATE = 2000;
    // The tiles for this board.
    public BlackHoleTile[] tiles; // for unit test
    // The number of the current player. 0 for user, 1 for computer.
    private int currentPlayer;
    // The value to assign to the next move of each player.
    private int[] nextMove = {1, 1};
    // A single random object that we'll reuse for all our random number needs.
    private static final Random random = new Random();

    // Constructor. Nothing to see here.
    BlackHoleBoard() {
        tiles = new BlackHoleTile[BOARD_SIZE];
        reset();
    }

    // Copy board state from another board. Usually you would use a copy constructor instead but
    // object allocation is expensive on Android so we'll reuse a board instead.
    public void copyBoardState(BlackHoleBoard other) {
        this.tiles = other.tiles.clone();
        this.currentPlayer = other.currentPlayer;
        this.nextMove = other.nextMove.clone();
    }

    // Reset this board to its default state.
    public void reset() {
        currentPlayer = 0;
        nextMove[0] = 1;
        nextMove[1] = 1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            tiles[i] = null;
        }
    }

    // Translates column and row coordinates to a location in the array that we use to store the
    // board.
    protected int coordsToIndex(int col, int row) {
        return col + row * (row + 1) / 2;
    }

    // This is the inverse of the method above.
    protected Coordinates indexToCoords(int i) {
        Coordinates result;
        // TODO: Compute the column and row number for the ith location in the array.
        // The row number is the triangular root of i as explained in wikipedia:
        // https://en.wikipedia.org/wiki/Triangular_number#Triangular_roots_and_tests_for_triangular_numbers
        // The column number is i - (the number of tiles in all the previous rows).
        // This is tricky to compute correctly so use the unit test in BlackHoleBoardTest to get it
        // right.
        double temp = (Math.sqrt(8*i+1)-1)/2;
        int y=(int)temp;
        int x=(int)Math.ceil((temp-y)*(y));
        result = new Coordinates(x,y);
        return result;
    }

    // Getter for the number of the player's next move.
    public int getCurrentPlayerValue() {
        return nextMove[currentPlayer];
    }

    // Getter for the number of the current player.
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    // Check whether the current game is over (only one blank tile).
    public boolean gameOver() {
        int empty = -1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (tiles[i] == null) {
                if (empty == -1) {
                    empty = i;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    // Pick a random valid move on the board. Returns the array index of the position to play.
    public int pickRandomMove() {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (tiles[i] == null) {
                possibleMoves.add(i);
            }
        }
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    // Pick a good move for the computer to make. Returns the array index of the position to play.
    public int pickMove() {
        // TODO: Implement this method have the computer make a move.
        // At first, we'll just invoke pickRandomMove (above) but later, you'll need to replace
        // it with an algorithm that uses the Monte Carlo method to pick a good move.
        HashMap<Integer,ArrayList<Integer>> possibleMoves=new HashMap<>();
        ArrayList<Integer> nullTiles = new ArrayList<>();
        for(int k=0;k<NUM_GAMES_TO_SIMULATE;k++) {
            BlackHoleBoard cBoard = new BlackHoleBoard();
            cBoard.copyBoardState(this);
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (cBoard.tiles[i] == null) {
                    nullTiles.add(i);
                }
            }
            //Log.d("nullTiles", "" + nullTiles.toString());
            int fmove=-1;
            int originalNullTilesSize = nullTiles.size();
            for (int i = 0; i < originalNullTilesSize - 1; i++) {
                int randIndex = random.nextInt(nullTiles.size());
                int randTile = nullTiles.get(randIndex);
                if(i==0)
                {
                    fmove=randTile;
                }
              //  Log.d("RandTile", "" + randTile);
                nullTiles.remove(randIndex);
                //Log.d("nullTiles", "" + nullTiles.toString());
                cBoard.tiles[randIndex] = new BlackHoleTile(cBoard.currentPlayer, cBoard.nextMove[cBoard.currentPlayer]);
                cBoard.nextMove[cBoard.currentPlayer]++;
                cBoard.currentPlayer = cBoard.currentPlayer == 0 ? 1 : 0;
            }
            //Log.d("GetScore", "" + cBoard.getScore());
            ArrayList<Integer> tList;
            if(!possibleMoves.containsKey(fmove))
            {
                tList = new ArrayList<>();
            }
            else
            {
               tList=possibleMoves.get(fmove);
            }
            tList.add(cBoard.getScore());
            possibleMoves.put(fmove,tList);
        }
        //Log.d("PM",""+possibleMoves.keySet());
        int oavg=Integer.MAX_VALUE,oindex=Integer.MAX_VALUE;
        for(int i=0;i<BOARD_SIZE;i++)
        {
            int tsum=-1,tavg=-1;
            if(possibleMoves.containsKey(i))
            {
                ArrayList<Integer> tList = possibleMoves.get(i);
                for(int j=0;j<tList.size();j++)
                {
                    tsum+=tList.get(j);
                }
                tavg=tsum/tList.size();
            }
            else
            {
                continue;
            }
            if(tavg<oavg)
            {
                oavg=tavg;
                oindex=i;
            }
        }

        return oindex;
    }

    // Makes the next move on the board at position i. Automatically updates the current player.
    public void setValue(int i) {
        tiles[i] = new BlackHoleTile(currentPlayer, nextMove[currentPlayer]);
        nextMove[currentPlayer]++;
        currentPlayer++;
        currentPlayer %= 2;
    }

    /* If the game is over, computes the score for the current board by adding up the values of
     * all the tiles that surround the empty tile.
     * Otherwise, returns 0.
     */
    public int getScore() {
        int score = 0;
        // TODO: Implement this method to compute the final score for a given board.
        // Find the empty tile left on the board then add/substract the values of all the
        // surrounding tiles depending on who the tile belongs to.
        //getNeighbors()
        ArrayList<BlackHoleTile> blackHoleTiles = new ArrayList<>();
        int uScore = 0, cScore = 0;
        for(int i=0;i<BOARD_SIZE;i++)
        {
            if(tiles[i]==null)
            {
                blackHoleTiles=getNeighbors(indexToCoords(i));
                break;
            }
        }
        for(int i=0;i<blackHoleTiles.size();i++)
        {
            if(blackHoleTiles.get(i).player==0)
            {
                uScore+=blackHoleTiles.get(i).value;
            }
            else
            {
                cScore+=blackHoleTiles.get(i).value;
            }
        }
        score = cScore - uScore;
        //Log.d("uScore"+uScore,"cScore"+cScore);
        return score;
    }

    // Helper for getScore that finds all the tiles around the given coordinates.
    private ArrayList<BlackHoleTile> getNeighbors(Coordinates coords) {
        ArrayList<BlackHoleTile> result = new ArrayList<>();
        for(int[] pair : NEIGHBORS) {
            BlackHoleTile n = safeGetTile(coords.x + pair[0], coords.y + pair[1]);
            if (n != null) {
                result.add(n);
            }
        }
        return result;
    }

    // Helper for getNeighbors that gets a tile at the given column and row but protects against
    // array over/underflow.
    private BlackHoleTile safeGetTile(int col, int row) {
        if (row < 0 || col < 0 || col > row) {
            return null;
        }
        int index = coordsToIndex(col, row);
        if (index >= BOARD_SIZE) {
            return null;
        }
        return tiles[index];
    }
}
