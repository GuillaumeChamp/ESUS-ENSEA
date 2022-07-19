package com.application.data.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
@SuppressWarnings("SpellCheckingInspection")
public class PathFinder {
    private static boolean init = false;
    private static ArrayList<String> KA131Step;
    private static ArrayList<String> KA171Step;
    private static ArrayList<String> internKA107Step;
    private static ArrayList<String> internKA131Step;
    private static ArrayList<String> freeStep;
    private static ArrayList<String> internNonEraStep;
    private static ArrayList<String> fame;
    private static ArrayList<String> accordExtraEUStep;
    private static ArrayList<String> accordEUStep,ddnonEUStep,ddnonEraStep,ge3Step,niStep,otherStep;


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
        internKA131Step = new ArrayList<>(Arrays.asList(prop));

        prop = properties.getProperty("accordEU").split(",");
        accordEUStep = new ArrayList<>(Arrays.asList(prop));
        prop = properties.getProperty("DDnonEU").split(",");
        ddnonEUStep = new ArrayList<>(Arrays.asList(prop));
        prop = properties.getProperty("DDnonEra").split(",");
        ddnonEraStep = new ArrayList<>(Arrays.asList(prop));
        prop = properties.getProperty("GE3").split(",");
        ge3Step = new ArrayList<>(Arrays.asList(prop));
        prop = properties.getProperty("N+I").split(",");
        niStep = new ArrayList<>(Arrays.asList(prop));
        prop = properties.getProperty("other").split(",");
        otherStep = new ArrayList<>(Arrays.asList(prop));
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
    /**
     * Return the previous step id or 0 if already at the begining
     * @param current current index
     * @param exchange type of exchange
     * @return step id as a string
     */
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
            case "stagiaire non-ERASMUS":
                progression = internNonEraStep;
                break;
            case "FAME" :
                progression = fame;
                break;
            case "NON-EU Bilateral Exchange" :
                progression =accordExtraEUStep;
                break;
            case "EU Bilateral Exchange" :
                progression =accordEUStep;
                break;
            case "GE3" :
                progression = ge3Step;
                break;
            case "autre" :
                progression = otherStep;
                break;
            case "N+i" :
                progression = niStep;
                break;
            case "Double degree(EU,Non-Erasmus)":
                progression = ddnonEraStep;
                break;
            case "Double degree(Non-EU)" :
                progression = ddnonEUStep;
                break;
            case "Admis sur titre":
            default:
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

    /**
     * Get the index of a particular step in an exchange type
     * @param exchange path to seek
     * @param step step to seek
     * @return position or 0 if not found
     */
    public static int index(String exchange,String step){
        int ans = getProgression(exchange).lastIndexOf(step);
        if (ans==-1) ans = 0;
        return ans;
    }
}
