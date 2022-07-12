package com.application.data.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class PathFinder {
    private static ArrayList<String> KA131Step;
    private static boolean init = false;
    private static ArrayList<String> KA171Step;
    private static ArrayList<String> freeStep;
    private static ArrayList<String> internKA107Step;
    private static ArrayList<String> internKA131Step;
    private static ArrayList<String> internNonEraStep;
    private static ArrayList<String> fame;
    private static ArrayList<String> accordExtraEUStep;

    /**
     * This methode is called to load all path
     */
    private static void load(){
        if (KA131Step != null) return;
        init = true;
        Properties properties = new Properties();
        InputStream is = PathFinder.class.getResourceAsStream("/META-INF/resources/path.properties");
        try{
            is = new FileInputStream("./drive/resources/path.properties");
        }catch (FileNotFoundException ignored) {}
        try {
            properties.load(is);
        }catch (Exception ignored){}
        String[] KA131 = properties.getProperty("KA131").split(",");
        KA131Step = new ArrayList<>(Arrays.asList(KA131));

        String[] KA171 = properties.getProperty("KA171").split(",");
        KA171Step = new ArrayList<>(Arrays.asList(KA171));

        String[] free = properties.getProperty("Free").split(",");
        freeStep = new ArrayList<>(Arrays.asList(free));

        String[] prop = properties.getProperty("fame").split(",");
        fame = new ArrayList<>(Arrays.asList(prop));

        prop = properties.getProperty("internKA107").split(",");
        internKA107Step = new ArrayList<>(Arrays.asList(prop));

        prop = properties.getProperty("internNonEra").split(",");
        internNonEraStep = new ArrayList<>(Arrays.asList(prop));

        prop = properties.getProperty("accordExtraEU").split(",");
        accordExtraEUStep = new ArrayList<>(Arrays.asList(prop));

        prop = properties.getProperty("internKA131").split(",");
        accordExtraEUStep = new ArrayList<>(Arrays.asList(prop));
    }

    /**
     * Return the next step id
     * @param current current index
     * @param exchange type of exchange
     * @return step id as a string
     */
    public static String getNext(String current,String exchange){
        ArrayList<String> progression = getProgression(exchange);
        int currentId =progression.indexOf(current);
        return progression.get(currentId+1);
    }
    public static String getPrevious(String current,String exchange){
        ArrayList<String> progression = getProgression(exchange);
        int currentId =progression.indexOf(current);
        if (currentId==0) return current;
        return progression.get(currentId-1);
    }

    /**
     * Load the progress base on exchange internal name
     * @param exchange internal name
     * @return progression as an array list of step name
     */
    private static ArrayList<String> getProgression(String exchange){
        if (!PathFinder.init) PathFinder.load();
        ArrayList<String> progression;
        switch (exchange){
            case "KA171Intern":
                progression = internKA107Step;
                break;
            case "KA171Student" :
                progression = KA171Step;
                break;
            case "KA131Student":
                progression = KA131Step;
                break;
            case "KA131Intern":
                progression = internKA131Step;
                break;
            case "STAGIARE NON ERASMUS":
                progression = internNonEraStep;
                break;
            case "Admis sur titre":
                progression = freeStep;
                break;
            case "FAME" :
                progression = fame;
                break;
            case "NON-EU Bilateral Exchange" :
            case "EU Bilateral Exchange" :
            case "GE3" :
                progression =accordExtraEUStep;
                break;
            default: // N+i autre double degree (EU/NON EU)
                progression = freeStep;
                break;
        }
        return progression;
    }

    /**
     * Check the position
     * @param studentProgress actual progress
     * @param destination where you want to send it
     * @param exchange name of the exchange type
     * @return true if second greater than first
     */
    public static boolean isNotFurther(String studentProgress, String destination,String exchange){
        ArrayList<String> progression = getProgression(exchange);
        int currentIndex = progression.indexOf(studentProgress);
        int destinationIndex = progression.indexOf(destination);
        return currentIndex<destinationIndex;
    }
    public static int lastIndex(String exchange){
        return getProgression(exchange).size()-1;
    }
    public static int index(String exchange,String step){
        int ans = getProgression(exchange).lastIndexOf(step);
        if (ans==-1) ans = 0;
        return ans;
    }
}
