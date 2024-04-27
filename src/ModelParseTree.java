import java.util.ArrayList;
public class ModelParseTree{
    public String MI;
    public ArrayList<String> DefMembers = new ArrayList<>();
    public ArrayList<ArrayList<String>> AI = new ArrayList<>();
    public ArrayList<ArrayList<String>> A_types = new ArrayList<>();
    public ArrayList<ArrayList<String>> A_access = new ArrayList<>();
    public ArrayList<ArrayList<String>> A_values = new ArrayList<>();
    public ArrayList<ArrayList<String>> BI = new ArrayList<>();
    public ArrayList<ArrayList<String>> B_returns = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> B_params = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> B_paramtypes = new ArrayList<>();
    public ArrayList<ArrayList<String>> B_access = new ArrayList<>();

    public String toString(){
        return MI + " " + DefMembers + " " + AI + " " + A_types + " " + A_access + " " + A_values + " "+ BI + " " + B_returns + " " + B_params + " " + B_paramtypes + " " + B_access;
    }
}