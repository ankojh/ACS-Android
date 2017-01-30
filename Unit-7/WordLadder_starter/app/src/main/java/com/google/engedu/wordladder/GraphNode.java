package com.google.engedu.wordladder;

import java.util.ArrayList;

/**
 * Created by ankit on 30/1/17.
 */

public class GraphNode {
    String word;
    ArrayList<String> neighbours;
    GraphNode(String s)
    {
        word=s;
        neighbours=new ArrayList<>();
    }
}
