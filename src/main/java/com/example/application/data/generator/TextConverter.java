package com.example.application.data.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TextConverter {
    /**
     * Convert a file into a string
     * @param fileName name of file
     * @return one string with all the content of the file
     */
    public static String ConvertFile(String fileName){
        StringBuilder string = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((HeaderReader.findFile(fileName)), StandardCharsets.UTF_8));
            String line = reader.readLine();
            do {
                string.append(line).append("\n");
                line=reader.readLine();
            } while(line!=null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return string.toString();
    }
    public static String ConvertFile(File file){
        StringBuilder string = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line = reader.readLine();
            do {
                string.append(line).append("\n");
                line=reader.readLine();
            } while(line!=null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return string.toString();
    }
}
