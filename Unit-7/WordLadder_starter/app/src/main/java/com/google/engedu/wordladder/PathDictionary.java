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

package com.google.engedu.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;

public class PathDictionary {
    private static final int MAX_WORD_LENGTH = 4;
    private static final int MIN_WORD_LENGTH =3; //no point of playing with single characters or a couple of 'em.
    private static HashSet<String> words = new HashSet<>();
    public HashMap<String,GraphNode> word2Node = new HashMap<>();
    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict");
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH || word.length()<MIN_WORD_LENGTH) {
                continue;
            }
            GraphNode newNode = new GraphNode(word);
            word2Node.put(word,newNode);
            for(String s:words)
            {
                if(difference(word,s))
                {
                    //Log.d(""+word,""+s);
                    newNode.neighbours.add(s);
                    word2Node.get(s).neighbours.add(word);
                }
            }
            words.add(word);
            //Log.d("word "+word,""+word2Node.get(word).neighbours);

        }
        Log.d("Added","all words");
    }

    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    private ArrayList<String> neighbours(String word) {
        return word2Node.get(word).neighbours;
    }

    public boolean difference(String word1,String word2) //helper
    {
        if(word1.length()!=word2.length())
            return false;
        int c=0;
        for(int i=0;i<word1.length();i++)
        {
            if(word1.charAt(i)!=word2.charAt(i))
            {
                c++;
            }
        }
        if(c==1)
            return true;
        else
            return false;
    }

    public String[] findPath(String start, String end) {
        Log.d("called","fp");
        ArrayDeque<ArrayList<String>> deque = new ArrayDeque<>();
        HashSet<String> visited = new HashSet<>();
        ArrayList<String> pathList = new ArrayList<>();
        pathList.add(start);
        deque.addLast(pathList);
        while(!deque.isEmpty())
        {
            ArrayList<String> pathFinder = deque.pollFirst();
            if(pathFinder.size()>8) //avoiding going very deep
            {
                continue;
            }
            Log.d("Bug","Hello");
            Log.d("PF",""+pathFinder.toString());
            if(pathFinder == null)
            {
                Log.d("Continue","null");
                //continue;
            }
            String tempEnd = pathFinder.get(pathFinder.size()-1);
            ArrayList<String> neighbours = word2Node.get(tempEnd).neighbours;
            Log.d("Neighbour"," "+neighbours.size());
            Log.d("NS",""+neighbours.toString());
            for(int j=0;j<neighbours.size();j++)
            {

                String newWord = neighbours.get(j);
                ArrayList<String> tempPath = new ArrayList<>();
                for(int k=0;k<pathFinder.size();k++)
                {
                    tempPath.add(pathFinder.get(k));
                }
                Log.d("newWord",""+newWord);
                if(newWord.equals(end))
                {
                    tempPath.add(end);
                    Log.d("Path",""+tempPath.toString());
                    return tempPath.toArray(new String[tempPath.size()]);
                }
                if(visited.contains(newWord))
                {
                    continue;
                }
                tempPath.add(newWord);
                deque.addLast(tempPath);
                Log.d("newPath",""+tempPath.toString());
                //Log.d("pathFindeer",""+pathFinder.toString());
            }
        //Log.d("PF",""+pathFinder.toString());
            }
        return null;
    }
}
