//Syntax Processing, Parse Trees
import java.util.*;
public class Parser{
   private ArrayList<String> code;
   private final String[] filter = {":", "(", ")", ","};
   private int cursor = 0;
   private Map<String, String> keys = new HashMap<>();
   private ArrayList<ModelParseTree> MPTs = new ArrayList<>();
   private ArrayList<InstanceParseTree> IPTs = new ArrayList<>();

   public Parser(ArrayList<String> code){
       keys.put("Model-Key", "new");
       keys.put("Model-B-Key", "-");
       keys.put("In-Key", "create");
       keys.put("IC-Key", "-->");
       keys.put("Terminate-Key", "END");
       keys.put("Model-B-End-Key", ")");
       this.code = code;
   }

    /*
    Utility method that removes unneeded syntax from a given word
     */
   public String filter(String word){
       for(String filterChar : filter){
           word = word.replace(filterChar, "");
       }
       return word;
   }


   /*
   Model Syntax Process - called anytime the Model Key is triggered
   This will generate and return a new Parse Tree for the given Model
    */
   public ModelParseTree MSP(){
       //Constants for syntax process
       final ModelParseTree modelPT = new ModelParseTree();
       final String[] bad_access = {"int", "boolean", "string", "double", "void"}; //Change this to retrieve data type keywords from Executor

       /*
       Preprocessing - processes the header of the Model definition
       Includes Model Identifier (MI)
        */
       modelPT.MI = filter(code.get(cursor));
       cursor += 2;
       //TODO: Move to Syntax Process Loop
       modelPT.DefMembers.add(filter(code.get(cursor)));
       cursor += 6;

       //Primary Delta Variable declarations
       String p_delta_A_access = "";
       String p_delta_AI = "";
       String p_delta_A_types = "";
       String p_delta_B_access = "";
       String p_delta_B_returns = "";
       String p_delta_BI = "";
       String p_delta_B_paramtypes = "";
       String p_delta_B_params = "";

       //Secondary Delta Variable declarations
       ArrayList<String> s_delta_A_access = new ArrayList<>();
       ArrayList<String> s_delta_AI = new ArrayList<>();
       ArrayList<String> s_delta_A_types = new ArrayList<>();
       ArrayList<String> s_delta_B_access = new ArrayList<>();
       ArrayList<String> s_delta_B_returns = new ArrayList<>();
       ArrayList<String> s_delta_BI = new ArrayList<>();
       ArrayList<String> s_delta_B_paramtypes = new ArrayList<>();
       ArrayList<String> s_delta_B_params = new ArrayList<>();

       //Tertiary Delta Variable declarations
       ArrayList<ArrayList<String>> t_delta_B_paramtypes = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_B_params = new ArrayList<>();

       /*
       Turned on when MSP reaches behaviors
       Prevents outer loop from going back to attribute loop
       */
       boolean behavior_mode = false;

       /*
       Syntax Process Loop
       Processes Attributes and Behaviors
        */
       //TODO: Implement filtering
       while(!code.get(cursor + 1).equals(keys.get("Terminate-Key"))){
           //Attributes Section
           while(!behavior_mode && !(code.get(cursor).equals(keys.get("Model-B-Key")))){
               if(!code.get(cursor).equals("")){ //skips any indentation
                   if(Arrays.asList(bad_access).contains(code.get(cursor))){
                       p_delta_A_access = "private";
                       p_delta_A_types = code.get(cursor);
                       cursor += 1;
                   } else{
                       p_delta_A_access = code.get(cursor);
                       cursor += 1;
                       p_delta_A_types = code.get(cursor);
                       cursor += 1;
                   }
                   p_delta_AI = filter(code.get(cursor));
                   cursor += 1;

                   //Attribute Delta Updates
                   s_delta_A_access.add(p_delta_A_access);
                   s_delta_A_types.add(p_delta_A_types);
                   s_delta_AI.add(p_delta_AI);
               } else{
                  cursor += 1;
               }
           }

           //Turns on behavior mode - prevents outer loop from slipping back to attributes
           behavior_mode = true;
           cursor += 1;

           //Behavior Section
           if(Arrays.asList(bad_access).contains(code.get(cursor))){
               p_delta_B_access = "public";
               p_delta_B_returns = code.get(cursor);
               cursor += 1;
           } else{
               p_delta_B_access = code.get(cursor);
               cursor += 1;
               p_delta_B_returns = code.get(cursor);
               cursor += 1;
           }

           if(code.get(cursor).contains("()")){
               p_delta_B_paramtypes = "void";
               p_delta_B_params = "void";
               s_delta_B_params.add(p_delta_B_params);
               s_delta_B_paramtypes.add(p_delta_B_paramtypes);
           } else{
               String[] delta_elements = code.get(cursor).split("\\(");
               System.out.println(Arrays.toString(delta_elements));
               //Set name delta
               p_delta_BI = delta_elements[0];

               /*
               Split behavior declaration into name + paramtype
               EXAMPLE: setPos(int = setPos, int
               Allows transition into param processing
                */
               code.set(cursor, delta_elements[0]);
               code.add(cursor + 1, delta_elements[1]);
               cursor += 1;
               //Param processing - tertiary deltas are used b/c of this
               while(true){
                    p_delta_B_paramtypes = code.get(cursor);
                    cursor += 1;
                    p_delta_B_params = filter(code.get(cursor));
                    s_delta_B_params.add(p_delta_B_params);
                    s_delta_B_paramtypes.add(p_delta_B_paramtypes);
                    if(code.get(cursor).contains(keys.get("Model-B-End-Key"))) {
                        break;
                    }
                    cursor += 1;
               }

               //Behavior Delta Types - tertiary deltas used for param processing
               s_delta_BI.add(p_delta_BI);
               s_delta_B_access.add(p_delta_B_access);
               s_delta_B_returns.add(p_delta_B_returns);
               t_delta_B_params.add(s_delta_B_params);
               t_delta_B_paramtypes.add(s_delta_B_paramtypes);
           }
        }

       //ModelPT Updates
       modelPT.AI = s_delta_AI;
       modelPT.A_types = s_delta_A_types;
       modelPT.A_access = s_delta_A_access;
       modelPT.BI = s_delta_BI;
       modelPT.B_returns = s_delta_B_returns;
       modelPT.B_params = t_delta_B_params;
       modelPT.B_paramtypes = t_delta_B_paramtypes;
       modelPT.B_access = s_delta_B_access;
       return modelPT;
   }


   public InstanceParseTree ISP(){
       final InstanceParseTree instPT = new InstanceParseTree();

       //Preprocessing
       String[] header_el = code.get(cursor).split("(");
       instPT.INM = header_el[0];
       instPT.INI = filter(header_el[1]);

       String p_delta_in_members = "";
       String p_delta_def_members = "";
       
       ArrayList<String> s_delta_in_members = new ArrayList<>();
       ArrayList<String> s_delta_def_members = new ArrayList<>();

       //No primary deltas for DefMem Lists - not coming from the code
       ArrayList<ArrayList<String>> s_delta_AI = new ArrayList<>();
       ArrayList<ArrayList<String>> s_delta_A_types = new ArrayList<>();
       ArrayList<ArrayList<String>> s_delta_A_access = new ArrayList<>();
       ArrayList<ArrayList<String>> s_delta_BI = new ArrayList<>();
       ArrayList<ArrayList<String>> s_delta_B_access = new ArrayList<>();
       ArrayList<ArrayList<String>> s_delta_B_returns = new ArrayList<>();
       ArrayList<ArrayList<ArrayList<String>>> s_delta_B_params = new ArrayList<>();
       ArrayList<ArrayList<ArrayList<String>>> s_delta_B_paramtypes = new ArrayList<>();

       /*
        Syntax Process Loop
       */
       while(!code.get(cursor + 1).equals(keys.get("Terminate-Key"))){
           cursor += 1;
           //Skips indentation
           while(code.get(cursor).equals("")){
               cursor += 1;
           }
           cursor += 1;
           p_delta_in_members = filter(code.get(cursor));
           cursor += 1;
           p_delta_def_members = filter(code.get(cursor));

           s_delta_in_members.add(p_delta_in_members);
           s_delta_def_members.add(p_delta_def_members);

           //Look up all lists for the current Def Member
           for(MPT mpt : MPTs){
               for(String defmem : mpt.DefMembers){
                   if(p_delta_def_members.equals(defmem)){
                       int defmem_index = mpt.DefMembers.indexOf(defmem);
                       s_delta_AI.add(mpt.AI[index]);
                       s_delta_A_types.add(mpt.A_types[index]);
                       s_delta_A_access.add(mpt.A_access[index]);
                       s_delta_BI.add(mpt.BI[index]);
                       s_delta_B_returns.add(mpt.B_returns[index]);
                       s_delta_B_access.add(mpt.B_access[index]);
                       s_delta_B_params.add(mpt.B_params[index]);
                       s_delta_B_paramtypes.add(mpt.B_paramtypes[index]);
                   }
               }
           }
       }

       //InstancePT Updates
       instPT.In_Members = s_delta_in_members;
       instPT.DefMembers = s_delta_def_members;
       instPT.AI = s_delta_AI;
       instPT.A_types = s_delta_A_types;
       instPT.A_access = s_delta_A_access;
       instPT.BI = s_delta_BI;
       instPT.B_returns = s_delta_B_returns;
       instPT.B_access = s_delta_B_access;
       instPT.B_params = s_delta_B_params;
       instPT.B_paramtypes = s_delta_B_paramtypes;
       return instPT;
   }

   public void parse(){
       for(cursor = 0; cursor < code.size(); cursor++){
           if(code.get(cursor).equals(keys.get("Model-Key"))){
               cursor += 1;
               ModelParseTree mpt = MSP();
               MPTs.add(mpt);
               System.out.println(mpt.toString());
           }
           else if(code.get(cursor).equals(keys.get("In-Key"))){
               cursor += 2;
               ISP();
               ModelParseTree mpt = MSP();
               MPTs.add(mpt);
           }
       }
   }
}



