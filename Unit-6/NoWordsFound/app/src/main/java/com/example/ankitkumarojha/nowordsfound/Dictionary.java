package com.example.ankitkumarojha.nowordsfound;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ankitkumarojha on 10/14/2016.
 */

public class Dictionary {

    public TrieNode rootNode;

    public final int MAXIMUM_WORD_LENGTH = 9;//8
    public final int MINIMUM_NUMBER_OF_WORDS = 6;
    public final int MAXIMUM_NUMBER_OF_WORDS = 10;
    public final int MINIMUM_WORD_LENGTH = 3;//4

    public Dictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        rootNode = new TrieNode();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() < MAXIMUM_WORD_LENGTH && word.length() > MINIMUM_WORD_LENGTH)
                rootNode.add(line.trim());
        }
    }

    public ArrayList<String> getRandomWords()
    {
        Random random = new Random();
        ArrayList<String> randomWords = new ArrayList<>();
        int numberOfRandomWords=random.nextInt(MAXIMUM_NUMBER_OF_WORDS-MINIMUM_NUMBER_OF_WORDS)+MINIMUM_NUMBER_OF_WORDS;//to avoid 0-2
        for(int i=0;i<numberOfRandomWords;i++)
            randomWords.add(rootNode.getRandomWordFromTrie());
        Log.d("Random words",randomWords.toString());
        return randomWords;
    }

    public int checkIsWord(String word)
    {
        return rootNode.checkIsWordFromTrie(word);
    }

}
