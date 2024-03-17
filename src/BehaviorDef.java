import java.util.ArrayList;

public class BehaviorDef {
    public String B;
    public ArrayList<String> P = new ArrayList<>();
    public ArrayList<GeneralBehaviorCommand> C = new ArrayList<>();
    public String RT;
    public String RV;

    public String toString(){
        return B + " " + P + " " + C + " " + RT + " " + RV;
    }
}
