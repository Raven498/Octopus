//Syntax Processing, Parse Trees
import java.util.ArrayList;
import java.util.Map;
public class Parser{
    private ArrayList<String> code;
    private final String[] filter = {":", "(", ")", ","};
    private final InstanceParseTree instPT = new InstanceParseTree();
    private final ModelParseTree modelPT = new ModelParseTree();
    private final ArrayList<String>[] mpt_lists = [modelPT.DefMembers,
                                                modelPT.AI, 
                                                modelPT.A_types, 
                                                modelPT.A_access, 
                                                modelPT.BI, 
                                                modelPT.B_returns,
                                                modelPT.B_params,
                                                modelPT.B_paramtypes,
                                                modelPT.B_access];
    private final ArrayList<String>[] ipt_lists = [instPT.In_Members
                                                instPT.AI, 
                                                instPT.A_types, 
                                                instPT.A_access, 
                                                instPT.BI, 
                                                instPT.B_returns,
                                                instPT.B_params,
                                                instPT.B_paramtypes,
                                                instPT.B_access];
    private Map<String, String> keys = Map.of(
        "Model-Key", "new",
        "Model-B-Key", "-",
        "In-Key", "create",
        "IC-Key", "-->",
        "Terminate-Key", "END"
    )
    public Parser(ArrayList<String> code){
        this.code = code;
    }

    public String filter(String word){
        for(String filterChar : filter){
            word.replace(filterChar, "");
        }
        return word;
    }

    public void MSP(int index){
        modelPT.MI = filter(code[index]);
        index += 2;
        modelPT.DefMembers.add(filter(code.get(index)));
        index += 2;
        
        int tree_index = 1;
        for(int i = index; i < code.size(); i++){
            mpt_lists[tree_index].add(code.get(i).replace(",", ""));
        }

        //TODO: Return parse trees to parse()
    }

    public void ISP(int index){
        
    }

    public void parse(){
        for(int i = 0; i < code.size(); i++){
            if(word.equals(keys.get("Model-Key"))){
                MSP(i + 1);
                //TODO: Return parse trees to parse()
            }
            else if(word.equals(keys.get("In-Key"))){
                ISP(i + 1);
            }
        }
    }
}
