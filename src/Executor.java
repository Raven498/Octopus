import java.util.ArrayList;
import java.util.Objects;

public class Executor {
    public ArrayList<InstanceBehaviorCommand> IBCs;
    public ArrayList<BehaviorDef> BDs;
    public ArrayList<ModelParseTree> MPTs;

    public Executor(ArrayList<InstanceBehaviorCommand> IBCs, ArrayList<BehaviorDef> BDs, ArrayList<ModelParseTree> MPTs){
        this.IBCs = IBCs;
        this.BDs = BDs;
        this.MPTs = MPTs;
    }

    public void execute(){
        ArrayList<String> paramNames = new ArrayList<>();
        for(int i = 0; i < BDs.size(); i++){
            for(int j = 0; j < IBCs.size(); j++){
                if(Objects.equals(IBCs.get(j).B, BDs.get(i).name)){
                    for(String C : BDs.get(i).C){
                        if(C.contains("print")){
                            String param = C.split("\\(")[1].replace(")", "");
                            if(BDs.get(i).P.contains(param)){
                                int param_index = BDs.get(i).P.indexOf(param);
                                System.out.println(IBCs.get(j).V.get(param_index));
                            }
                        }
                    }
                }
            }
        }
    }
}
