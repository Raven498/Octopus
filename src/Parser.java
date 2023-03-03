//Syntax Processing, Parse Trees
import java.util.ArrayList;
import java.util.Map;
public class Parser{
    private ArrayList<String> code;
    private final String[] filter = {":", "(", ")", ","};
    private int cursor = 0;
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

    public ModelParseTree MSP(){
        final ModelParseTree modelPT = new ModelParseTree();
        final ArrayList<String>[] mpt_lists = [modelPT.DefMembers,
                                                modelPT.AI, 
                                                modelPT.A_types, 
                                                modelPT.A_access, 
                                                modelPT.BI, 
                                                modelPT.B_returns,
                                                modelPT.B_params,
                                                modelPT.B_paramtypes,
                                                modelPT.B_access];
        modelPT.MI = filter(code[index]);
        cursor += 2;
        modelPT.DefMembers.add(filter(code.get(index)));
        cursor += 2;
        
        int tree_index = 0;
        for(cursor; cursor < code.size(); cursor++){
            if(code.get(cursor).)
            mpt_lists[tree_index].add(code.get(cursor).replace(",", ""));
            cursor++;
            tree_index++;
        }
    }

    public InstanceParseTree ISP(){
        final InstanceParseTree instPT = new InstanceParseTree();
        final ArrayList<String>[] ipt_lists = [instPT.In_Members
                                                instPT.AI, 
                                                instPT.A_types, 
                                                instPT.A_access, 
                                                instPT.BI, 
                                                instPT.B_returns,
                                                instPT.B_params,
                                                instPT.B_paramtypes,
                                                instPT.B_access];


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
            else if(word.equals(keys.get("In-Key"))){
                cursor += 1;
                ISP();
                ModelParseTree mpt = MSP();
                MPTs.add(mpt);
            }
        }
    }
}
