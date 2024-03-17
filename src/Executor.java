import java.util.ArrayList;
import java.util.Objects;

public class Executor {
    public ArrayList<InstanceBehaviorCommand> IBCs;
    public ArrayList<BehaviorDef> BDs;
    public ArrayList<ModelParseTree> MPTs;
    public ArrayList<String> command_reg;

    public Executor(ArrayList<InstanceBehaviorCommand> IBCs, ArrayList<BehaviorDef> BDs, ArrayList<ModelParseTree> MPTs, ArrayList<String> command_reg){
        this.IBCs = IBCs;
        this.BDs = BDs;
        this.MPTs = MPTs;
        this.command_reg = command_reg;
    }

    public void IBC(){
        //Look up BD

    }

    public void GBC(){

    }

    public void execute(){
        for(String command : command_reg){
            if(command.contains("IBC")){

            } else if(command.contains("GBC")){

            }
        }
    }
}
