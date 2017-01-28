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

import android.text.TextUtils;

import java.util.ArrayList;


public class PalindromeGroup {
    protected ArrayList<String> strings = new ArrayList<>();

    public PalindromeGroup(char[] text, int start, int end) {
        strings.add(new String(text, start, end-start));
    }

    public void append(PalindromeGroup other) {
        if (other != null) {
            strings.addAll(other.strings);
        }
    }

    public int length() {
        return strings.size();
    }

    @Override
    public String toString() {
        return TextUtils.join(" ", strings);
    }
}
