package boitzo;


import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import org.ejml.ops.MatrixComponent;
import org.ejml.simple.SimpleMatrix;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class AhpStructure {
    public ArrayList<String> alternatives= new ArrayList<>();
    private ArrayList<SimpleMatrix> eigenValuesAlternatives= new ArrayList<>();
    private SimpleMatrix eigenResult;
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
    public AhpStructure(String path){
        this.jsonObject=parseJSON(path);
        addAlternatives();
        addGoal();
    }
    public void eigenValueMethod(AhpNode node){

        SimpleMatrix eigenVector=prepareEigenValue(node);
        for (int i = 0; i <node.getChildren().size() ; i++) {

            eigenValueMethod(node.getChildren().get(i),eigenVector.get(i));

        }
        makeEigenResult();

    }

    private void eigenValueMethod(AhpNode node,double weight){
        SimpleMatrix eigenVector=prepareEigenValue(node);
        for (int i = 0; i <eigenVector.numRows() ; i++) {
            eigenVector.set(i,0,eigenVector.get(i)*weight);

        }
        for (int i = 0; i <node.getChildren().size() ; i++) {
            eigenValueMethod(node.getChildren().get(i),eigenVector.get(i));

        }
        if (node.getChildren().size()==0) {
            eigenValuesAlternatives.add(eigenVector);
            eigenVector.print();
        }





    }
    private void makeEigenResult(){
        eigenResult=new SimpleMatrix(alternatives.size(),1);
        for (SimpleMatrix vector: eigenValuesAlternatives) {
            eigenResult=eigenResult.plus(vector);

        }
    }
    public void printEigenResult(){
        System.out.println("Eigen Value method");
        System.out.println(eigenResult);
    }


    private SimpleMatrix prepareEigenValue(AhpNode node){
        SimpleMatrix matrix=new SimpleMatrix(node.getPreferences().size(),node.getPreferences().size());
        for (int i = 0; i <node.getPreferences().size() ; i++) {
            for (int j = 0; j <node.getPreferences().size() ; j++) {
                matrix.setRow(i,j,node.getPreferences().get(i).get(j));
            }
        }
        SimpleMatrix eigenVector=matrix.eig().getEigenVector(matrix.eig().getIndexMax());
        for (int i = 0; i <eigenVector.numRows() ; i++) {
            eigenVector.set(i,0,Math.abs(eigenVector.get(i,0)));
        }
        double sum=eigenVector.elementSum();
        for (int i = 0; i <eigenVector.numRows() ; i++) {
            eigenVector.set(i,0,eigenVector.get(i)/sum);
        }
        return eigenVector;

    }
    public static void main(String [] arg){
        AhpStructure ahpStructure=new AhpStructure("ex3.json");
        ahpStructure.print();
        ahpStructure.eigenValueMethod(ahpStructure.goal);
        ahpStructure.printEigenResult();

    }
}