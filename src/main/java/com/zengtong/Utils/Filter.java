/*
package com.zengtong.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


public class Filter {

    class Trie{
        private Boolean isSensitive;

        private HashMap<Character,Trie> son;

        public void addSon(Character[] characters){

            if (son.isEmpty()) son.put(' ', new Trie() );

            for (int i = 0 ; i < characters.length - 1; ++i){

                Character character = characters[i];

                if ( !son.containsKey(character)){

                    son.put(character,)

                }

            }
        }
    }

    private static String ReadFileToString(String filepath) throws IOException {

        File file = new File(filepath);

        FileInputStream f = new FileInputStream(file);

        InputStreamReader in = new InputStreamReader(f,"UTF-8");

        StringBuffer sb = new StringBuffer();

        while (in.ready()){
            sb.append((char)in.read());
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException {


    }
}
*/
