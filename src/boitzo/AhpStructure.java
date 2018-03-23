package boitzo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AhpStructure {
    public ArrayList<String> alternatives= new ArrayList<>();
    public AhpNode goal=new AhpNode();
    private JSONObject jsonObject;

    public static JSONObject parseJSON(String path) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(path));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject;
    }
    private void addAlternatives(){
        for (Object alt: (JSONArray) jsonObject.get("alternatives")) {
            alternatives.add((String)alt);
        }



    }
    private void addGoal(){
        JSONObject child=(JSONObject)jsonObject.get("goal");
        goal.setName((String)child.get("name"));
        goal.setPreferences((ArrayList<ArrayList<Double>>)child.get("preferences"));
        for (Object object:(JSONArray)child.get("children")) {
            goal.getChildren().add(addChildren(new AhpNode(), (JSONObject)object));
        }

    }
    private AhpNode addChildren(AhpNode node,JSONObject child){

            node.setName((String)child.get("name"));
           //System.out.println(node.getName());

            node.setPreferences((ArrayList<ArrayList<Double>>)child.get("preferences"));
        if (!child.get("children").equals("alternatives")) {
            for (Object object : (JSONArray) child.get("children")) {

                if (!((JSONObject) object).toString().equals("alternatives")) {

                    node.getChildren().add(addChildren(new AhpNode(), (JSONObject) object));
                }
            }
        }


        return node;
    }
    public void print(){
        System.out.println("Alternatives: "+alternatives.toString());
        print(goal);
        System.out.println("Goal= "+goal.getName());

    }
    private void print(AhpNode node){
        StringBuilder stringBuilder=new StringBuilder(node.getName()).append(" ==> ");

        if (node.getChildren().size()>0) {
            for (AhpNode object : node.getChildren()) {

                stringBuilder.append(object.getName()).append(", ");
                print(object);

            }

        }
        else stringBuilder.append("alternatives");
        System.out.println(stringBuilder);



    }
    public AhpStructure(){
        this.jsonObject=parseJSON("ex3.json");
        addAlternatives();
        addGoal();
    }
    public static void main(String [] arg){
        AhpStructure ahpStructure=new AhpStructure();
        ahpStructure.print();

    }
}