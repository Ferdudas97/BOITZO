package boitzo;

import java.util.ArrayList;

public class AhpNode {
    private String name;
    private ArrayList<ArrayList<Double>> preferences;
    private ArrayList<AhpNode> children=new ArrayList<>();

    public String getName() {
        return name;
    }

    public ArrayList<ArrayList<Double>> getPreferences() {
        return preferences;
    }

    public void setPreferences(ArrayList<ArrayList<Double>> preferences) {
        this.preferences = preferences;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AhpNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<AhpNode> children) {
        this.children = children;
    }
}
