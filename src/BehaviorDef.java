import java.util.ArrayList;

public class BehaviorDef {
    public String name;
    public ArrayList<String> P = new ArrayList<>();
    public ArrayList<String> C = new ArrayList<>();

    public String toString(){
        return name + " " + P + " " + C;
    }
}
