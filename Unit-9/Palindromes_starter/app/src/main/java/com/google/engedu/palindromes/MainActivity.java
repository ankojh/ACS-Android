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

package com.google.engedu.palindromes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<Pair<Integer,Integer>, PalindromeGroup> findings = new HashMap<>();
    public int minNumberOfPalindromes = Integer.MAX_VALUE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean onFindPalindromes(View view) {
        findings.clear();
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textView = (TextView) findViewById(R.id.textView);
        String text = editText.getText().toString();
        text = text.replaceAll(" ", "");
        text = text.replaceAll("'", "");
        char[] textAsChars = text.toCharArray();
        if (isPalindrome(textAsChars, 0, text.length())) {
          textView.setText(text + " is already a palindrome!");
        } else {
            PalindromeGroup palindromes = breakIntoPalindromes(text.toCharArray(), 0, text.length());
            textView.setText(palindromes.toString());
        }
        return true;
    }

    private boolean isPalindrome(char[] text, int start, int end) {
        end--;// discarding excluding one
        while(start<=end)
        {
            if(!(text[start]==text[end]))
            {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }
    //TODO Greedy
   /* private PalindromeGroup breakIntoPalindromes(char[] text, int start, int end) {
        PalindromeGroup bestGroup = null;
        int tempEnd = start + 1;
        if(tempEnd==end)
        {
            bestGroup=new PalindromeGroup(text,start,end);
            Log.d("last one",""+toString(text,start,end));
            return bestGroup;
        }
        while(tempEnd<=end)
        {
            if(!isPalindrome(text,start,tempEnd))
            {
                Log.d("not palidrome",""+toString(text,start,tempEnd));
                bestGroup = new PalindromeGroup(text,start,tempEnd-1);
                Log.d("BestGroup",""+toString(text,start,tempEnd-1));
                break;
            }
            tempEnd++;
        }
        Log.d("Left Group"+(tempEnd-1)+" "+end,""+toString(text,tempEnd-1,end));
        bestGroup.append(breakIntoPalindromes(text,tempEnd-1,end));
        return bestGroup;
    }
*/

    //TODO RECURSIVE
   /* private PalindromeGroup breakIntoPalindromes(char[] text, int start, int end) {
        PalindromeGroup bestGroup = null;
        int minNumOfPalindromes = Integer.MAX_VALUE;
        int tempEnd = start + 1;
        while (tempEnd<=end)
        {
            if(isPalindrome(text,start,tempEnd))
            {
                PalindromeGroup newGroup;
                newGroup = new PalindromeGroup(text,start,tempEnd);
                newGroup.append(breakIntoPalindromes(text,tempEnd,end));
                Log.d("string",""+newGroup.strings.toString());
                if(newGroup.length()<minNumOfPalindromes)
                {
                    bestGroup = newGroup;
                    minNumOfPalindromes = bestGroup.length();
                    Log.d("Size"+bestGroup.length(),""+minNumOfPalindromes);
                }
            }
            tempEnd++;
        }

        return bestGroup;
    }
*/
    //TODO DYNAMIC PROGRAMMING
    private PalindromeGroup breakIntoPalindromes(char[] text, int start, int end) {
        PalindromeGroup bestGroup = null;
        int minNumOfPalindromes = Integer.MAX_VALUE;
        int tempEnd = start + 1;
        while (tempEnd<=end)
        {
            Pair<Integer,Integer> newPair = new Pair<>(start,tempEnd);
            PalindromeGroup newGroup;
            if(isPalindrome(text,start,tempEnd))
            {
                if(findings.containsKey(newPair))
                {
                    newGroup = findings.get(newPair);
                }
                else
                {
                    newGroup = new PalindromeGroup(text,start,tempEnd);
                    newGroup.append(breakIntoPalindromes(text,tempEnd,end));
                    findings.put(newPair,newGroup);
                }
                if(newGroup.length()<minNumOfPalindromes)
                {
                    bestGroup = newGroup;
                    minNumOfPalindromes = bestGroup.length();
                }
            }
            tempEnd++;
        }
        return bestGroup;
    }

    //debug helper function
    public String toString(char[] text, int start, int end)
    {
        String s = "";
        for(int i=start;i<end;i++)
            s+=text[i];
        return s;
    }

}
