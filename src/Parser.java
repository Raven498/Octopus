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
       //Constants for syntax process
       final ModelParseTree modelPT = new ModelParseTree();
       final String[] bad_access = {"int", "boolean", "string", "double"}; //Change this to retrieve data type keywords from Executor


       //Preprocessing - MI, DefMember --> process the header
       modelPT.MI = filter(code.get(cursor));
       cursor += 2;
       modelPT.DefMembers.add(filter(code.get(cursor)));
       cursor += 6;


       //Primary Delta Variable declarations
       String p_delta_A_access;
       String p_delta_AI;
       String p_delta_A_types;
       String p_delta_B_access;
       String p_delta_B_returns;
       String p_delta_BI;
       String p_delta_B_paramtypes;
       String p_delta_B_params;

       //Secondary Delta Variable declarations - WIP
       String[] s_delta_A_access;
       String[] s_delta_AI;
       String[] s_delta_A_types;

       /*
       Turned on when MSP reaches behaviors
       Prevents outer loop from going back to attribute loop
       */
       boolean behavior_mode = false;


       //Syntax Process Loop
       //TODO: Implement filtering
       while(!code.get(cursor).equals(keys.get("Terminate-Key")){
           //Attributes Section
           while(!code.get(cursor).equals(keys.get("Model-B-Key") && !behavior_mode){
               if(bad_access.contains(code.get(cursor))){
                   delta_A_access = "private";
                   delta_A_types = code.get(cursor);
                   cursor += 1;
               } else{
                   delta_A_access = code.get(cursor);
                   cursor += 1;
                   delta_A_types = code.get(cursor);
                   cursor += 1;
               }
              
               //Allows MSP to skip indentation after attributes (when it gets there)
               if(!code.get(cursor).equals(" ")){
                   delta_AI = filter(code.get(cursor));
                   cursor += 1;
               }
           }


           //Turns on behavior mode - prevents outer loop from slipping back to attributes
           behavior_mode = true;
           cursor += 1;

           //Behavior Section
           if(bad_access.contains(code.get(cursor))){
               delta_B_access = "public";
               delta_B_returns = code.get(cursor);
               cursor += 1;
           } else{
               delta_B_access = code.get(cursor);
               cursor += 1;
               delta_B_returns = code.get(cursor);
               cursor += 1;
           }


           String delta_BI = code.get(cursor).split("(")[0];
           String delta_paramtypes
           if(code.get(cursor).equals("()")){
               delta_B_paramtypes = "void";
           } else{

           }
              
        }

        return modelPT;
   }


   public InstanceParseTree ISP(){
       final InstanceParseTree instPT = new InstanceParseTree();

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



