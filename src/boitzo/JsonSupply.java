package boitzo;





import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.Scanner;



public class JsonSupply {
    private   int countOfAlternatives;

    private  ArrayList<String> alteratives=new ArrayList<>();

     private  ArrayList<Double> makeMatrix(JSONArray arrayOfChildren){
         Scanner scanner=new Scanner(System.in);
         JSONArray matrixOfPreferences=new JSONArray();
        Double answer;
        int countOfChildren=arrayOfChildren.size();

         for (int j = 0; j <countOfChildren ; j++) {
             ArrayList<Double> row = new ArrayList<>();
             for (int k = 0; k < countOfChildren; k++) {
                 if (j == k) {
                     row.add(1.0);
                     continue;
                 } else {
                     System.out.println("Podaj stosunek " +((JSONObject)arrayOfChildren.get(j)).get("name") + "/" + ((JSONObject)arrayOfChildren.get(k)).get("name"));
                     answer = Validation.getDouble();
                     row.add(answer);
                 }
             }
             matrixOfPreferences.add(row);
         }
         return matrixOfPreferences;
     }
    private  ArrayList<Double> makeMatrix(){
        Scanner scanner=new Scanner(System.in);
        JSONArray matrixOfPreferences=new JSONArray();
        Double answer;
        for (int j = 0; j <countOfAlternatives ; j++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int k = 0; k < countOfAlternatives; k++) {
                if (j == k) {
                    row.add(1.0);
                    continue;
                } else {
                    System.out.println("Podaj stosunek " + alteratives.get(j) + "/" + alteratives.get(k));
                    answer = Validation.getDouble();
                    row.add(answer);
                }
            }
            matrixOfPreferences.add(row);
        }
        return matrixOfPreferences;
    }
    private  JSONObject makeGoal(JSONObject object){
        System.out.println("podaj cel");
        Scanner scanner=new Scanner(System.in);
        JSONObject goal=new JSONObject();
        goal.put("name",scanner.next());

        goal=makeCriteria(goal);
        goal.put("preferences",makeMatrix((JSONArray)(goal.get("children"))));
        object.put("goal",goal);

        return object;
    }

    private  JSONObject makeCriteria(JSONObject mainJson){
        JSONArray jsonArray=new JSONArray();

        int howmany;
        ArrayList<JSONObject> listWithJsons=new ArrayList<>();
        System.out.println(mainJson.get("name")+": podaj ilosc kryteriów");
        Scanner scanner=new Scanner(System.in);

        howmany=Validation.getInteger();
        for (int i = 0; i <howmany ; i++) {
            JSONObject jsonGoal=new JSONObject();

            System.out.println(mainJson.get("name")+": podaj nazwe kryterium");
            jsonGoal.put("name",scanner.next());
            System.out.println(jsonGoal.get("name")+": Czy dodajemy subkryterium? [t/n]");
            Character answer=scanner.next().charAt(0);
            jsonGoal.put("children",new JSONArray());
            if (answer.equals('t')) {
                jsonGoal=makeCriteria(jsonGoal);
                jsonGoal.put("preferences",makeMatrix(((JSONArray)jsonGoal.get("children"))));
            }
            else{
                jsonGoal.put("preferences",makeMatrix());
                jsonGoal.put("children","alternatives");
            }



            jsonArray.add(jsonGoal);

        }
         mainJson.put("children",jsonArray);

        return mainJson;
    }

    public  JSONObject makeMainPartOfJSON() throws IOException {
        JSONObject jsonMain=new JSONObject();
        Scanner scanner=new Scanner(System.in);

        System.out.println("Podaj ilosc mozliwosci");
        int howmany=Validation.getInteger();
        JSONArray jsonArray=new JSONArray();
        countOfAlternatives=howmany;

        for (int i = 0; i <howmany ; i++) {
            System.out.println("podaj mozliwosc;");
            String answer=scanner.next();
            jsonArray.add(answer);
            alteratives.add(answer);
        }

        jsonMain.put("alternatives",jsonArray);

        jsonMain=makeGoal(jsonMain);

        return jsonMain;
    }
    public  static void toFile(JSONObject jsonObject,String path){
        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonObject.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String [] arg) throws IOException, ParseException {

        JSONObject jsonObject=new JsonSupply().makeMainPartOfJSON();

        JsonSupply.toFile(jsonObject,"ex4.json");
        System.out.println(jsonObject.toString());
    }
    }


