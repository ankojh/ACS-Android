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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random random = new Random();
        if(prefix == "")//if computer starts return any random word
        {
            return words.get(random.nextInt(words.size()));
        }
        for(int i=0;i<words.size();i++)
        {
            if(words.get(i).startsWith(prefix))
            {
                return words.get(i);
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        Random random = new Random();
        ArrayList<String> oddWordSet = new ArrayList<>();
        ArrayList<String> evenWordSet = new ArrayList<>();
        int rangeBegin=-1,rangeEnd=-1;//all words starting with prefix
        int begin = 0, end = words.size()-1,mid;
        while (begin<=end)
        {
            mid=(begin+end)/2;
            if(words.get(mid).startsWith(prefix))
            {
                for(int i=mid-1;i>=0;i--)
                {
                    if(!words.get(i).startsWith(prefix))
                    {
                        rangeBegin=i+1;
                        break;
                    }
                }
                for(int i=mid+1;i<words.size();i++)
                {
                    if(!words.get(i).startsWith(prefix))
                    {
                        rangeEnd=i-1;
                        break;
                    }
                }
            }
        }
        if(rangeBegin == -1 || rangeEnd == -1)
            return selected;//null;
        for(int i=rangeBegin;i<rangeEnd;i++)
        {
            if(words.get(i).length()%2==0)
                evenWordSet.add(words.get(i));
            else
                oddWordSet.add(words.get(i));
        }


        return selected;
    }
}
