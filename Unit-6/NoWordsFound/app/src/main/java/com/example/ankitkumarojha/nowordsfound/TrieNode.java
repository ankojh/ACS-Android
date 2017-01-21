package com.example.ankitkumarojha.nowordsfound;

import android.util.Log;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by ankitkumarojha on 10/14/2016.
 */

public class TrieNode {
    public HashMap<String,TrieNode> children;
    public boolean isEndOfWord;

    TrieNode()
    {
        children = new HashMap<>();
        isEndOfWord = false;
    }

    public void add(String word)
    {
        final String Trie_Node_Add ="TrieNode-add";
        //Log.d(Trie_Node_Add,"Method called");
        //Log.d(Trie_Node_Add,"method called");
        TrieNode currentNode = this;
        String tempString = "";
        for(int i=0;i<word.length();i++)
        {
            tempString += word.charAt(i);
            if(!currentNode.children.containsKey(tempString))
            {
                TrieNode newNode = new TrieNode();
                currentNode.children.put(tempString,newNode);
                //Log.d(Trie_Node_Add,"new node created-"+tempString);
            }
            else
            {
                //Log.d(Trie_Node_Add,"node exist-"+tempString);
            }
            currentNode=currentNode.children.get(tempString);
        }
        currentNode.isEndOfWord=true;
        //Log.d(Trie_Node_Add,"isEndOfWord set True");
    }


    public String getRandomWordFromTrie()
    {
        final String GET_RANDOM_WORD_FROM_TRIE="Random Word";
        final int NUMBER_OF_CHARACTERS = 26;
        //Log.d(GET_RANDOM_WORD_FROM_TRIE,"Method Called");
        Random random = new Random();
        TrieNode currentNode = this;
        //Log.d("root",""+currentNode.children.keySet());
        String tempString ="";
        String tempString1="";
        String alphabet="abcdefghijklmnopqrstuvwxyz";
        //Log.d("CNisEndword",""+currentNode.isEndOfWord);
        //Log.d("CN WORD",""+currentNode.children.keySet());
        while(!currentNode.isEndOfWord)
        {
            String tempAlphabet=alphabet;
            //Log.d("tempAlphabet",tempAlphabet);
            int indexSize=NUMBER_OF_CHARACTERS;
            int randomIndex=-1;
            //Log.d("Temp String",tempString);
            while(!currentNode.children.containsKey(tempString)) {
                tempString=tempString1;
                //Log.d("Temp String", "Does not contains");
                randomIndex = random.nextInt(indexSize);
                tempString += tempAlphabet.charAt(randomIndex);
                //Log.d("TempString", tempString);
                tempAlphabet = tempAlphabet.substring(0, randomIndex) + tempAlphabet.substring(randomIndex + 1);
                indexSize--;
            }
            //Log.d(GET_RANDOM_WORD_FROM_TRIE,"IN loop TempString="+tempString);
            tempString1=tempString;
            currentNode=currentNode.children.get(tempString);
        }
        //Log.d(GET_RANDOM_WORD_FROM_TRIE,"Method Ends");
        return tempString;
    }

    public int checkIsWordFromTrie(String word)
    {
        final String CHECK_IS_WORD_FROM_TRIE="iswordfromtrie";
        //Log.d(CHECK_IS_WORD_FROM_TRIE,"method called");
        TrieNode currentNode = this;
        String tempString = "";
        for(int i=0;i<word.length();i++)
        {
            tempString += word.charAt(i);
            if(!currentNode.children.containsKey(tempString))
            {
                //Log.d(CHECK_IS_WORD_FROM_TRIE,"Returning False");
                return -1;
            }
            //Log.d(CHECK_IS_WORD_FROM_TRIE,"Continuing Loop");
            currentNode=currentNode.children.get(tempString);
            if(currentNode.isEndOfWord)
                return i;
        }
        //Log.d(CHECK_IS_WORD_FROM_TRIE,"Returning boolean"+currentNode.isEndOfWord);
        return -1;
    }



}
