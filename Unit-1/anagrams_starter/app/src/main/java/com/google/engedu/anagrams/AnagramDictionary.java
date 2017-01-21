package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static android.util.Log.*;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4; //word of length 3 is crashing the app. 4 and above works fine :) .
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    public ArrayList<String> hashKeys = new ArrayList<String>();
    public ArrayList<String> wordList = new ArrayList<String>();
    public HashSet wordSet = new HashSet();
    public HashMap <String, ArrayList<String>> lettersToWord = new HashMap<>();
    public HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<>();
    public int wordLength=DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            // Log.d("AnagramDictionary",word);
            wordList.add(word);
            wordSet.add(word);
            Integer currentWordLength = word.length();
            ArrayList<String> temp = new ArrayList<String>();
            if(sizeToWords.containsKey(currentWordLength))
            {
                temp=sizeToWords.get(currentWordLength);
                temp.add(word);
                sizeToWords.put(currentWordLength,temp);
            }
            else
            {
                temp.add(word);
                sizeToWords.put(currentWordLength,temp);
            }
            String sortedWord = sortLetters(word);
            ArrayList<String> values = new ArrayList<String>();
            if(lettersToWord.containsKey(sortedWord))
            {
                values = lettersToWord.get(sortedWord);
                values.add(word);
                lettersToWord.put(sortedWord,values);
            }
            else
            {
                hashKeys.add(sortedWord);
                values.add(word);
                lettersToWord.put(sortedWord,values);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !word.contains(base))
        {
            return true;
        }
        return false;
    }

    public String sortLetters(String word)
    {
        char[] chararray=word.toCharArray();
        Arrays.sort(chararray);
        String sortedword = new String(chararray);
        return sortedword;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        //ArrayList<String> result = new ArrayList<String>();
        //for(String dictWord : wordList) {
        //    String sortedDictWord = sortLetters(dictWord);
        //    if (sortedDictWord.length() == sortedTargetWord.length() && sortedDictWord == sortedTargetWord)
        //    {
        //        Log.d("result arraylist", dictWord);
        //        result.add(dictWord);
        //    }
        // }
        String sortedTargetWord = sortLetters(targetWord);
        ArrayList<String> result = lettersToWord.get(sortedTargetWord);
        Log.d("getAnagrams","ArrayList Result Returned");
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        for(int i=0;i<26;i++)
        {
            String newWord=word + (char)(97+i);
            String sortedNewWord = sortLetters(newWord);
            if(lettersToWord.containsKey(sortedNewWord))
            {
                temp=lettersToWord.get(sortedNewWord);
                for(String tword:temp)
                {
                    if(!tword.contains(word)) //redundant but required
                        result.add(tword);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> minAnagramWords = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        HashSet wordLengthSet = new HashSet();
        for(String key:sizeToWords.get(wordLength)) {
            if ((lettersToWord.get(sortLetters(key))).size() >= MIN_NUM_ANAGRAMS) {
                temp = lettersToWord.get(sortLetters(key));
                for (String k : temp) {
                    minAnagramWords.add(k);
                }
            }
        }
        String randomString = minAnagramWords.get(random.nextInt(minAnagramWords.size()));
        if(wordLength<=MAX_WORD_LENGTH)
            wordLength++;
        return randomString;
    }
}