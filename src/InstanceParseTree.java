import java.util.ArrayList;
public class InstanceParseTree{
    public String INM;
    public String INI;
    public ArrayList<String> In_Members = new ArrayList<>();
    public ArrayList<String> Def_Members = new ArrayList<>();
    public ArrayList<ArrayList<String>> AI = new ArrayList<>();
    public ArrayList<ArrayList<String>> A_types = new ArrayList<>();
    public ArrayList<ArrayList<String>> A_access = new ArrayList<>();
    public ArrayList<ArrayList<String>> BI = new ArrayList<>();
    public ArrayList<ArrayList<String>> B_returns = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> B_params = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> B_paramtypes = new ArrayList<>();
    public ArrayList<ArrayList<String>> B_access = new ArrayList<>();
}