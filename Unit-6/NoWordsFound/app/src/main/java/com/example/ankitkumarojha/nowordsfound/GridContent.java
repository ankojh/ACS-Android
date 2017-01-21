package com.example.ankitkumarojha.nowordsfound;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by ankitkumarojha on 10/16/2016.
 */

public class GridContent {
    public char[][] grid;

    //String[] words = {"ankit","kumar","ojha","likes","chennai","india","russia","america","earth","universe"};

    public static ArrayList<String> insertedWords = new ArrayList<>();
    public static ArrayList<String> leftWords = new ArrayList<>();
    public static HashMap<String, Pair<Integer,Boolean>> storedWords = new HashMap<>();
    //public static HashMap<String, Boolean> storedWordsDirection = new HashMap<>();

    public GridContent(ArrayList<String> words) {
        grid = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = '#';
            }
        }
        leftWords.clear();
        for (String word : words)
            leftWords.add(word);
        Log.d("left words", leftWords.toString());
        insertedWords.clear();
    }

    public ArrayList<Character> findIntersection(String a, String b) {
        int[] hash = new int[26];
        ArrayList<Character> intersectChar = new ArrayList<>();
        for (int i = 0; i < a.length(); i++) {
            hash[(int) a.charAt(i) - 97] = 1;
        }
        for (int i = 0; i < b.length(); i++) {
            if (hash[(int) b.charAt(i) - 97] == 1)
                hash[(int) b.charAt(i) - 97] = 2;
        }
        for (int i = 0; i < 26; i++) {
            if (hash[i] == 2) {
                intersectChar.add((char) (i + 97));
            }
        }
        return intersectChar;
    }

    public boolean checkOccupied(int length, int startIndex, int intersectionIndex, boolean isHorizontal) {
        int ii = startIndex / 10, jj = startIndex % 10;
        if (isHorizontal) {
            for (int j = 0; j < length; j++) {
                if ((ii * 10 + jj + j) < 99 && grid[ii][jj + j] != '#') {
                    if ((ii * 10) + (jj + j) != intersectionIndex)
                        return false;
                }
            }
        } else {
            for (int j = 0; j < length; j++) {
                if (((((ii + j) * 10) + jj) < 99) && (grid[ii + j][jj] != '#')) {
                    if ((ii + j) * 10 + jj != intersectionIndex)
                        return false;
                }
            }
        }
        return true;
    }

    public void putWordInEmptySpace(String word) {
        Random random = new Random();
        boolean flag = false;
        int counter = 0;
        while (!flag && counter < 20) //if new word is not inserted in 20(vaguely taken) tries then chuck it.
        {
            counter++;
            boolean randomDirection = random.nextBoolean();
            int randomStartIndex;

            if (randomDirection) {
                randomStartIndex = random.nextInt(10) * 10 + random.nextInt(10 - word.length());
            } else {
                randomStartIndex = random.nextInt(10 - word.length()) * 10 + random.nextInt(10);
            }
            if (checkOccupied(word.length(), randomStartIndex, -1, randomDirection)) {
                int ii = randomStartIndex / 10, jj = randomStartIndex % 10;
                if (randomDirection)//horizontal
                {
                    for (int i = 0; i < word.length(); i++) {
                        grid[ii][jj + i] = word.charAt(i);
                    }
                    flag = true;
                    insertedWords.add(word);
                    storedWords.put(word, Pair.create(randomStartIndex,randomDirection));
                    //storedWordsDirection.put(word, randomDirection);
                } else //vertical
                {
                    for (int i = 0; i < word.length(); i++) {
                        grid[ii + i][jj] = word.charAt(i);
                    }
                    flag = true;
                    insertedWords.add(word);
                    storedWords.put(word, Pair.create(randomStartIndex,randomDirection));
                    //storedWords.put(word, randomStartIndex);
                    //storedWordsDirection.put(word, randomDirection);
                }
            }
        }
    }

    public void insertValidWords() {
        Random random = new Random();
        int leftWordInitialSize = leftWords.size();
        for (int i = 0; i < leftWordInitialSize; i++) {
            String newWord = leftWords.get(random.nextInt(leftWords.size()));
            if (insertedWords.isEmpty())//empty grid
            {
                int p = random.nextInt(10 - newWord.length()), q = random.nextInt(10 - newWord.length());
                boolean ishorizontalDirection = random.nextBoolean();
                for (int j = 0; j < newWord.length(); j++) {
                    if (ishorizontalDirection) {
                        grid[p][q + j] = newWord.charAt(j);
                    } else {
                        grid[p + j][q] = newWord.charAt(j);
                    }
                }
                insertedWords.add(newWord);
                leftWords.remove(newWord);
                storedWords.put(newWord, Pair.create(((p * 10) + q),ishorizontalDirection));
                //storedWordsDirection.put(newWord, ishorizontalDirection);
            } else {
                boolean insertFlag = false;
                String insertedWord = insertedWords.get(random.nextInt(insertedWords.size()));
                ArrayList<Character> intersectedChars = findIntersection(newWord, insertedWord);
                if (intersectedChars.isEmpty()) {
                    putWordInEmptySpace(newWord);
                    leftWords.remove(newWord);
                    continue;
                }
                char randomChar = intersectedChars.get(random.nextInt(intersectedChars.size()));
                int indexIntersectInsertedWord = insertedWord.indexOf(randomChar), indexNewWord = newWord.indexOf(randomChar);
                int storedWordIndex = storedWords.get(insertedWord).first;
                boolean ishorizontalDirection = storedWords.get(insertedWord).second;
                int newWordGridIndex;
                boolean newWordDirection;
                if (ishorizontalDirection) {
                    int intersectionIndex = storedWordIndex + indexIntersectInsertedWord;
                    newWordGridIndex = intersectionIndex - indexNewWord * 10;
                    newWordDirection = !ishorizontalDirection;
                    if (newWordGridIndex >= 0 && newWordGridIndex + (newWord.length() * 10) < 100) {
                        int l = 0;
                        if (checkOccupied(newWord.length(), newWordGridIndex, intersectionIndex, newWordDirection)) {
                            for (int k = newWordGridIndex; k < newWordGridIndex + (newWord.length() * 10); k += 10)//improve it-just change i not j.
                            {
                                grid[k / 10][k % 10] = newWord.charAt(l++);
                            }
                            insertFlag = true;
                        }
                    } else {
                        continue;
                    }
                } else //vertical
                {
                    int intersectionIndex = storedWordIndex + indexIntersectInsertedWord * 10;
                    newWordGridIndex = intersectionIndex - indexNewWord;
                    newWordDirection = !ishorizontalDirection;
                    if (newWordGridIndex >= 0 && ((newWordGridIndex + (newWord.length())) < (((newWordGridIndex / 10) + 1) * 10))) {

                        if (checkOccupied(newWord.length(), newWordGridIndex, intersectionIndex, newWordDirection)) {
                            int l = 0;
                            for (int k = intersectionIndex - (indexNewWord); k < (intersectionIndex - (indexNewWord)) + newWord.length(); k++) {
                                grid[k / 10][k % 10] = newWord.charAt(l++);
                            }
                            insertFlag = true;
                        }
                    } else {
                        continue;
                    }
                }
                if (insertFlag && newWordGridIndex >= 0) {
                    insertedWords.add(newWord);
                    leftWords.remove(newWord);
                    storedWords.put(newWord, Pair.create(newWordGridIndex,newWordDirection));
                    //storedWordsDirection.put(newWord, newWordDirection);
                    break;
                }
            }
        }
        for (String word : leftWords) {
            putWordInEmptySpace(word);
        }

         camouflageGrid();
    }

    public void camouflageGrid() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (grid[i][j] == '#')
                    grid[i][j] = (char) (random.nextInt(26) + 97);
            }
        }
    }


    public HashMap<String, Pair<Integer,Boolean>> getWordStored() {
        return storedWords;
    }

    //public HashMap<String, Boolean> getWordDirection() {
        //return storedWordsDirection;
    //}
}