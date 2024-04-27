import java.util.ArrayList;

public class BehaviorDef {
    public String M;
    public String B;
    public ArrayList<String> P = new ArrayList<>();
    public ArrayList<String> C = new ArrayList<>();
    public String RT;
    public String RV;

    public String toString(){
        return M + " " + B + " " + P + " " + C + " " + RT + " " + RV;
    }
}
