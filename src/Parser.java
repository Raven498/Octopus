//Syntax Processing, Parse Trees
import java.util.*;
public class Parser{
    private ArrayList<String> code;
    private final String[] filter = {":", "(", ")", ","};
    private int cursor = 0;
    private Map<String, String> keys = new HashMap<>();

    public Parser(ArrayList<String> code){
        keys.put("Model-Key", "new");
        keys.put("Model-B-Key", "-");
        keys.put("In-Key", "create");
        keys.put("IC-Key", "-->");
        keys.put("Terminate-Key", "END");
        this.code = code;
    }

    public String filter(String word){
        for(String filterChar : filter){
            word = word.replace(filterChar, "");
        }
        return word;
    }

    //WIP
    public ModelParseTree MSP(){
        //Constants for this syntax process
        final ModelParseTree modelPT = new ModelParseTree();
        final String[] bad_access = {"int", "boolean", "string", "double"}; //Change this to retrieve data type keywords from Executor

        //Preprocessing - MI, DefMember
        modelPT.MI = filter(code.get(cursor));
        cursor += 2;
        modelPT.DefMembers.add(filter(code.get(cursor)));
        cursor += 2;

        //Syntax Process Loop - for delta attributes/behaviors
        //TODO: Try a recursive version of this
        //TODO: Simplify syntax process
        boolean behavior_mode = false; //True if the process has reached the behaviors section
        for(; cursor < code.size(); ){
            if(behavior_mode){ //Behaviors

            } else{ //Attributes
                if(Arrays.asList(bad_access).contains(code.get(cursor))){
                    modelPT.A_access.add("private");
                    modelPT.A_types.add(code.get(cursor));
                    modelPT.AI.add(filter(code.get(cursor) + 1));
                    cursor += 2;
                }
                else{
                    modelPT.A_access.add(code.get(cursor));
                    modelPT.A_types.add(code.get(cursor) + 1);
                    modelPT.AI.add(filter(code.get(cursor) + 2));
                    cursor += 3;
                }
            }

            if(code.get(cursor).equals(keys.get("Model-B-Key"))){
                behavior_mode = true;
            }
        }
        return modelPT;
    }

    public InstanceParseTree ISP(){
        final InstanceParseTree instPT = new InstanceParseTree();
        final ArrayList<ArrayList<String>> ipt_lists = new ArrayList<>(
                Arrays.asList(
                        instPT.AI,
                        instPT.A_types,
                        instPT.A_access,
                        instPT.BI,
                        instPT.B_returns,
                        instPT.B_params,
                        instPT.B_paramtypes,
                        instPT.B_access
                )
        );

        return instPT;
    }

    public void parse(){
        ArrayList<ModelParseTree> MPTs = new ArrayList<>();
        ArrayList<InstanceParseTree> IPTs = new ArrayList<>();
        for(cursor = 0; cursor < code.size(); cursor++){
            if(code.get(cursor).equals(keys.get("Model-Key"))){
                cursor += 1;
                ModelParseTree mpt = MSP();
                MPTs.add(mpt);
            }
            else if(code.get(cursor).equals(keys.get("In-Key"))){
                cursor += 1;
                ISP();
                ModelParseTree mpt = MSP();
                MPTs.add(mpt);
            }
        }
    }
}
