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

package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        //Log.d("Add",""+s);
        TrieNode currentNode = this;
        String tempString ="";
        for(int i=0;i<s.length();i++)
        {
            tempString += s.charAt(i);
            if(!currentNode.children.containsKey(tempString))
            {
                TrieNode newNode = new TrieNode();
                currentNode.children.put(tempString,newNode);
            }
            else
            {
                //Keep going deep
            }
            currentNode=currentNode.children.get(tempString);
        }
        currentNode.isWord=true;
    }

    public boolean isWord(String s) {
        TrieNode currentNode = this;
        String tempString ="";
        for(int i=0;i<s.length();i++)
        {
            tempString += s.charAt(i);
            if(!currentNode.children.containsKey(tempString))
                return false;
            currentNode=currentNode.children.get(tempString);
            if (currentNode.isWord && tempString.length() == s.length())
                return true;
        }

        return false;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode currentNode = this;
        String tempString = "";
        Random random = new Random();

        if(s=="")//when computer starts first, spoon-feeding a letter to start with
        {
            int charind = random.nextInt(26);
            s+=(char)(97+charind);
        }
        for(int i=0;i<s.length();i++)
        {
            tempString += s.charAt(i);
            if(!currentNode.children.containsKey(tempString))
                return null;
            currentNode = currentNode.children.get(tempString);
        }
        while(!currentNode.isWord)
        {
            String orginalTS = tempString;
            ArrayList<Integer> index = new ArrayList<>();
            for(int i=0;i<26;i++)
                index.add(i);
            int randind = random.nextInt(index.size());
            int charind = index.get(randind);
            tempString += (char)(97+charind);
            if(currentNode.children.containsKey(tempString))
            {
                currentNode = currentNode.children.get(tempString);
            }
            else {
                tempString = orginalTS;
                index.remove(randind);
            }
        }
        return tempString;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode currentNode = this;
        String tempString = "";
        Random random = new Random();
        if(s=="")//when computer starts first, spoon-feeding a letter to start with
        {
            int charind = random.nextInt(26);
            s+=(char)(97+charind);
        }
        Log.d("PRefix",""+s);
        for(int i=0;i<s.length();i++)
        {
            tempString += s.charAt(i);
            Log.d("currentNodeChildren"+tempString,""+currentNode.children.keySet().toString());
            if(!currentNode.children.containsKey(tempString))
                return null;
            currentNode = currentNode.children.get(tempString);
        }
        ArrayList<String> childrenList = new ArrayList<>(currentNode.children.keySet());
        ArrayList<String> notWordChildren = new ArrayList<>();
        Log.d("ChildrenList",""+childrenList);
        for(int i=0;i<childrenList.size();i++)
        {
            TrieNode tempNode = new TrieNode();
            tempNode=currentNode.children.get(childrenList.get(i));
            if(!tempNode.isWord)
            {
                notWordChildren.add(childrenList.get(i));
            }
        }
        if(notWordChildren.isEmpty())
        {
            Log.d("empty","notword");
            return childrenList.get(random.nextInt(childrenList.size()));
        }
        else
        {
            Log.d("randompick","notword");
            return notWordChildren.get(random.nextInt(notWordChildren.size()));
        }

    }
}
