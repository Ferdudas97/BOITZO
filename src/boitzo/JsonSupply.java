package boitzo;





import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



public class JsonSupply {
     private  static int countOfAlternatives;

     private static ArrayList<String> alteratives=new ArrayList<>();
     private static ArrayList<Double> makeMatrix(){
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
                     answer = scanner.nextDouble();
                     row.add(answer);
                 }
             }
             matrixOfPreferences.add(row);
         }
         return matrixOfPreferences;
     }
    private static JSONObject makeGoal(JSONObject object){
        System.out.println("podaj cel");
        Scanner scanner=new Scanner(System.in);
        JSONObject goal=new JSONObject();
        goal.put("name",scanner.next());
        goal.put("preferences",makeMatrix());
        goal=makeCriteria(goal);
        object.put("goal",goal);

        return object;
    }

    private static JSONObject makeCriteria(JSONObject mainJson){
        JSONArray jsonArray=new JSONArray();

        int howmany;
        ArrayList<JSONObject> listWithJsons=new ArrayList<>();
        System.out.println("podaj ilosc kryteriów");
        Scanner scanner=new Scanner(System.in);

        howmany=scanner.nextInt();
        for (int i = 0; i <howmany ; i++) {
            JSONObject jsonGoal=new JSONObject();

            System.out.println("podaj nazwe kryterium");
            jsonGoal.put("name",scanner.next());


            jsonGoal.put("preferences",makeMatrix());
            System.out.println("Czy schodzimy w głąb? [t/n]");
            Character answer=scanner.next().charAt(0);
            jsonGoal.put("children",new JSONArray());

            if (answer.equals('t')) jsonGoal=makeCriteria(jsonGoal);
            jsonArray.add(jsonGoal);

        }

        mainJson.put("children",jsonArray);
        return mainJson;
    }

    public static JSONObject makeMainPartOfJSON() throws IOException {
        JSONObject jsonMain=new JSONObject();
        Scanner scanner=new Scanner(System.in);

        System.out.println("Podaj ilosc mozliwosci");
        int howmany=scanner.nextInt();
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
    public static void main(String [] arg) throws IOException {

        JSONObject jsonObject=makeMainPartOfJSON();
        try (FileWriter file = new FileWriter("myJSO.json")) {
            file.write(jsonObject.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + jsonObject);
        }

        System.out.println(jsonObject.toString());
    }
    }


