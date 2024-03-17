import java.util.ArrayList;

public class GeneralBehaviorCommand {
    public String B;
    public ArrayList<String> V = new ArrayList<>();
    public ArrayList<InstanceBehaviorCommand> C = new ArrayList<>();
    public String R;

    public String toString(){
        return B + " " + C + " " + R;
    }
}
