import java.util.ArrayList;
public class InstanceBehaviorCommand{
    public String IN;
    public String IM;
    public String B;
    public ArrayList<Integer> PN = new ArrayList<>();
    public ArrayList<String> V = new ArrayList<>();
    public String R;
    public ArrayList<ExpressionParseTree> E = new ArrayList<>();
    public ArrayList<InstanceBehaviorCommand> C = new ArrayList<>();

    public String toString(){
        return IN + " " + IM + " " + B + " " + PN + " " + V + " " + E + " " + C;
    }
}