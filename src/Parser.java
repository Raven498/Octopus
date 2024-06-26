//Syntax Processing, Parse Trees
import java.beans.Expression;
import java.util.*;
public class Parser{
   private ArrayList<String> code;
   private final String[] filter = {":", "(", ")", ","};
   private int cursor = 0;
   private Map<String, String> keys = new HashMap<>();
   public ArrayList<ModelParseTree> MPTs = new ArrayList<>();
   public ArrayList<InstanceParseTree> IPTs = new ArrayList<>();
   public ArrayList<InstanceBehaviorCommand> IBCs = new ArrayList<>();
   public ArrayList<InstanceAttributeCommand> IACs = new ArrayList<>();
   public ArrayList<GeneralBehaviorCommand> GBCs = new ArrayList<>();
   public ArrayList<BehaviorDef> BDs = new ArrayList<>();
   public final String[] gbc_keys = new String[]{"print", "return"};
   public ArrayList<String> command_reg = new ArrayList<>();

   public Parser(ArrayList<String> code){
       keys.put("Model-Key", "new");
       keys.put("Model-B-Key", "-");
       keys.put("Model-B-End-Key", ")");
       keys.put("Model-DefMember-Key", "->");
       keys.put("In-Key", "create");
       keys.put("IC-Key", ">");
       keys.put("Terminate-Key", "END");
       keys.put("IAC-Key", "=");
       keys.put("IBC-Key", "(");
       keys.put("IBC-EscKey", "\\(");
       keys.put("Expression-Key", "<");
       keys.put("Expression-End-Key", ">");
       keys.put("IC-End-Key", ";");
       keys.put("InAccess-Key", ".");
       keys.put("InAccess-EscKey", "\\.");
       keys.put("InRef-Key", "->");
       keys.put("BehaviorDef-Key", "def");
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
   Utility method that looks up an IPT given a name
    */
    public InstanceParseTree searchIPT(String IN){
        for(InstanceParseTree ipt : IPTs){
            if(ipt.INI.equals(IN)){
                return ipt;
            }
        }
        return null;
    }

    public ModelParseTree searchMPT(String MI){
        for(ModelParseTree mpt : MPTs){
            if(mpt.MI.equals(MI)){
                return mpt;
            }
        }
        return null;
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
       Preprocessing - processes the model identifier (MI), 1st Def Member
        */
       modelPT.MI = filter(code.get(cursor));
       cursor += 2;
       modelPT.DefMembers.add(filter(code.get(cursor)));
       cursor += 6;

       //Primary Delta Variable declarations
       String p_delta_A_access = "";
       String p_delta_AI = "";
       String p_delta_A_types = "";
       String p_delta_A_values = "";
       String p_delta_B_access = "";
       String p_delta_B_returns = "";
       String p_delta_BI = "";
       String p_delta_B_params = "";
       String p_delta_B_paramtypes = "";

       //Secondary Delta Variable declarations
       ArrayList<String> s_delta_A_access = new ArrayList<>();
       ArrayList<String> s_delta_AI = new ArrayList<>();
       ArrayList<String> s_delta_A_types = new ArrayList<>();
       ArrayList<String> s_delta_A_values = new ArrayList<>();
       ArrayList<String> s_delta_B_access = new ArrayList<>();
       ArrayList<String> s_delta_B_returns = new ArrayList<>();
       ArrayList<String> s_delta_BI = new ArrayList<>();

       //Tertiary Delta Variable declarations
       ArrayList<ArrayList<String>> t_delta_A_access = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_AI = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_A_types = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_A_values = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_B_access = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_B_returns = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_BI = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_B_paramtypes = new ArrayList<>();
       ArrayList<ArrayList<String>> t_delta_B_params = new ArrayList<>();

       //Quaternary Delta Variable declarations
       ArrayList<ArrayList<ArrayList<String>>> q_delta_B_paramtypes = new ArrayList<>();
       ArrayList<ArrayList<ArrayList<String>>> q_delta_B_params = new ArrayList<>();

       /*
       Turned on when MSP reaches behaviors
       Prevents outer loop from going back to attribute loop
       */
       boolean behavior_mode = false;

       /*
       Syntax Process Loop
       Processes DefMembers, Attributes and Behaviors
        */
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
                   if(code.get(cursor + 1).equals("=")){ //TODO: Codify as separate key
                       cursor += 2;
                       p_delta_A_values = filter(code.get(cursor));
                   } else{
                       p_delta_A_values = "null";
                   }

                   //Attribute Secondary Delta Updates
                   s_delta_A_access.add(p_delta_A_access);
                   s_delta_A_types.add(p_delta_A_types);
                   s_delta_AI.add(p_delta_AI);
                   s_delta_A_values.add(p_delta_A_values);
               }
               cursor += 1;
           }

           //Turns on behavior mode - prevents outer loop from slipping back to attributes
           behavior_mode = true;
           cursor += 1;

           ArrayList<String> s_delta_B_paramtypes = new ArrayList<>();
           ArrayList<String> s_delta_B_params = new ArrayList<>();

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
               p_delta_BI = filter(code.get(cursor));
               p_delta_B_paramtypes = "void";
               p_delta_B_params = "void";
               s_delta_B_params.add(p_delta_B_params);
               s_delta_B_paramtypes.add(p_delta_B_paramtypes);
           } else{
               String[] delta_elements = code.get(cursor).split("\\(");
               //System.out.println(Arrays.toString(delta_elements));
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

               //Param processing
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
           }

           //Behavior Secondary Delta Updates
           s_delta_BI.add(p_delta_BI);
           s_delta_B_access.add(p_delta_B_access);
           s_delta_B_returns.add(p_delta_B_returns);

           //Behavior Tertiary Delta Updates (for params)
           t_delta_B_params.add(s_delta_B_params);
           t_delta_B_paramtypes.add(s_delta_B_paramtypes);

           if(code.get(cursor + 1).equals(keys.get("Model-DefMember-Key")) || code.get(cursor + 1).equals(keys.get("Terminate-Key"))){
               //Tertiary Updates (behaviors)
               t_delta_A_access.add(s_delta_A_access);
               t_delta_AI.add(s_delta_AI);
               t_delta_A_types.add(s_delta_A_types);
               t_delta_A_values.add(s_delta_A_values);
               t_delta_B_access.add(s_delta_B_access);
               t_delta_B_returns.add(s_delta_B_returns);
               t_delta_BI.add(s_delta_BI);

               //Quaternary Delta Updates (for params)
               q_delta_B_params.add(t_delta_B_params);
               q_delta_B_paramtypes.add(t_delta_B_paramtypes);

               if(code.get(cursor + 1).equals(keys.get("Model-DefMember-Key"))){
                   //No deltas for DefMembers for sake of simplicity
                   cursor += 2;
                   modelPT.DefMembers.add(filter(code.get(cursor)));
                   cursor += 6;
                   behavior_mode = false;

                   //Reset secondary deltas  + param tertiary deltas
                   s_delta_A_access = new ArrayList<>();
                   s_delta_AI = new ArrayList<>();
                   s_delta_A_types = new ArrayList<>();
                   s_delta_A_values = new ArrayList<>();
                   s_delta_B_access = new ArrayList<>();
                   s_delta_B_returns = new ArrayList<>();
                   s_delta_BI = new ArrayList<>();
                   t_delta_B_params = new ArrayList<>();
                   t_delta_B_paramtypes = new ArrayList<>();
               }
           }
       }

       //ModelPT Updates
       modelPT.AI = t_delta_AI;
       modelPT.A_types = t_delta_A_types;
       modelPT.A_access = t_delta_A_access;
       modelPT.A_values = t_delta_A_values;
       modelPT.BI = t_delta_BI;
       modelPT.B_returns = t_delta_B_returns;
       modelPT.B_params = q_delta_B_params;
       modelPT.B_paramtypes = q_delta_B_paramtypes;
       modelPT.B_access = t_delta_B_access;
       return modelPT;
}


   public InstanceParseTree ISP(){
       final InstanceParseTree instPT = new InstanceParseTree();

       //Preprocessing
       String[] header_el = code.get(cursor).split("\\(");
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
       ArrayList<ArrayList<String>> s_delta_A_values = new ArrayList<>();
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
           p_delta_def_members = filter(code.get(cursor));
           cursor += 1;
           p_delta_in_members = filter(code.get(cursor));

           s_delta_in_members.add(p_delta_in_members);
           s_delta_def_members.add(p_delta_def_members);

           //Look up all lists for the current Def Member
           for(ModelParseTree mpt : MPTs){
               for(String defmem : mpt.DefMembers){
                   if(p_delta_def_members.equals(defmem)){
                       int defmem_index = mpt.DefMembers.indexOf(defmem);
                       s_delta_AI.add(mpt.AI.get(defmem_index));
                       s_delta_A_types.add(mpt.A_types.get(defmem_index));
                       s_delta_A_access.add(mpt.A_access.get(defmem_index));
                       s_delta_A_values.add(mpt.A_values.get(defmem_index));
                       s_delta_BI.add(mpt.BI.get(defmem_index));
                       s_delta_B_returns.add(mpt.B_returns.get(defmem_index));
                       s_delta_B_access.add(mpt.B_access.get(defmem_index));
                       s_delta_B_params.add(mpt.B_params.get(defmem_index));
                       s_delta_B_paramtypes.add(mpt.B_paramtypes.get(defmem_index));
                   }
               }
           }

           cursor += 1;
           while(code.get(cursor).equals("")){
               cursor += 1;
           }
           cursor += 1;
           for(ModelParseTree mpt : MPTs){
               for(String defmem : mpt.DefMembers){
                   if(p_delta_def_members.equals(defmem)){
                       int defmem_index = mpt.DefMembers.indexOf(defmem);
                       int ai_index = mpt.AI.get(defmem_index).indexOf(code.get(cursor));
                       
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
       instPT.A_values = s_delta_A_values;
       instPT.BI = s_delta_BI;
       instPT.B_returns = s_delta_B_returns;
       instPT.B_access = s_delta_B_access;
       instPT.B_params = s_delta_B_params;
       instPT.B_paramtypes = s_delta_B_paramtypes;

       IPTs.add(instPT);
       return instPT;
   }

   public InstanceBehaviorCommand IBC(){
       //IBC CSP Primary Deltas (except for IN, IM, B)
       int primary_delta_PN = 0;
       String primary_delta_V = "";
       InstanceBehaviorCommand primary_delta_C;
       ExpressionParseTree primary_delta_E;
       InstanceBehaviorCommand IBC = new InstanceBehaviorCommand();

       //IBC CSP Secondary Deltas
       ArrayList<Integer> secondary_delta_PN = new ArrayList<>();
       ArrayList<String> secondary_delta_V = new ArrayList<>();
       ArrayList<InstanceBehaviorCommand> secondary_delta_C = new ArrayList<>();
       ArrayList<ExpressionParseTree> secondary_delta_E = new ArrayList<>();

       //Header Processing
       IBC.IN = code.get(cursor);

       //Command Processing
       cursor += 2;
       String[] primary_ghost = code.get(cursor).split(keys.get("InAccess-EscKey"));
       IBC.IM = primary_ghost[0];
       String[] secondary_ghost = primary_ghost[1].split(keys.get("IBC-EscKey"));
       code.set(cursor, secondary_ghost[0]);
       code.add(cursor + 1, secondary_ghost[1]);
       IBC.B = code.get(cursor);

       //Parameter Processing
       cursor += 1;
       boolean endFound = false;
       boolean valueMode = false;
       while(!endFound){
           primary_delta_PN += 1;
           String currentText = filter(code.get(cursor));
           if(currentText.contains(keys.get("IC-End-Key"))){
               if(filter(currentText).equals(keys.get("IC-End-Key"))){
                   break;
               }
               currentText = currentText.replace(keys.get("IC-End-Key"), "");
               endFound = true;
               valueMode = true;
           }
           if(currentText.contains(keys.get("Expression-Key"))){
               primary_delta_E = E(currentText);
               secondary_delta_E.add(primary_delta_E);
           }
           else if(cursor <= code.size() - 3 && code.get(cursor + 2).contains(keys.get("IBC-Key"))){ //NEEDS TESTING
               primary_delta_C = IBC();
               secondary_delta_C.add(primary_delta_C);
               if(code.get(cursor).contains(keys.get("IC-End-Key"))) {
                   secondary_delta_PN.add(primary_delta_PN);
                   continue;
               }
           }
           else{
               primary_delta_V = currentText;
               secondary_delta_V.add(primary_delta_V);
           }

           if(valueMode){
               secondary_delta_PN.add(primary_delta_PN);
               continue;
           }

           cursor += 1;
           secondary_delta_PN.add(primary_delta_PN);
       }

       IBC.PN = secondary_delta_PN;
       IBC.E = secondary_delta_E;
       IBC.V = secondary_delta_V;
       IBC.C = secondary_delta_C;

       IBCs.add(IBC);
       command_reg.add(IBC.B + "-IBC");
       return IBC;
   }

   public InstanceAttributeCommand IAC(){
       return null;
   }

   public ExpressionParseTree E(String word){
        ExpressionParseTree EPT = new ExpressionParseTree();
        String[] major_ghost = word.split("");
        for(int i = 0; i < major_ghost.length; i++){ //TODO: Process embedded IBCs and Expressions
            EPT.mode.add("mode" + i);
            ArrayList<String> mode_elem = new ArrayList<>();
            while(!major_ghost[i].equals(keys.get("Expression-End-Key"))){
                mode_elem.add(major_ghost[i]);
            }
            EPT.ELEM.add(mode_elem);
        }

        return EPT;
   }

   public GeneralBehaviorCommand GBC(String key){
       GeneralBehaviorCommand GBC = new GeneralBehaviorCommand();
       GBC.B = key;

       //Parameter Processing
       if(key.equals("print")){ //declaration split similar to the one used in MSP
           String[] gbc_el = code.get(cursor).split("\\(");
           String gbc_name = gbc_el[0];
           String gbc_param = gbc_el[1];
           code.set(cursor, gbc_name);
           code.add(cursor + 1, gbc_param);
       }

       cursor += 1;

       //Command Case
       if(searchIPT(filter(code.get(cursor))) != null){
           GBC.C.add(IBC());
       }

       //Value Case - for literals
       //WIP

       GBCs.add(GBC);
       //System.out.println(GBC);
       return GBC;
   }

   //Command Syntax Process Controller
   public String CSPC(){ //TODO: Turn word parameters into an array - allows us to isolate any expression operands
       cursor += 1;
       String ID = "";
       for(String key : gbc_keys){
           if(code.get(cursor).contains(key)){
              GeneralBehaviorCommand GBC = GBC(key);
              ID = GBC.B + "-GBC";
              command_reg.add(ID);
              //System.out.println(ID);
              return ID;
           }
       }

       cursor += 1;
       if(code.get(cursor).contains(keys.get("InRef-Key"))){
           cursor -= 1;
           InstanceBehaviorCommand IBC = IBC();
           ID = IBC.B + "-IBC";
           command_reg.add(ID);
           //System.out.println(ID);
           return ID;
       }

       cursor += 1;
       if(code.get(cursor).contains(keys.get("IAC-Key"))){
           //System.out.println("IAC");
           //IACs.add(IAC());
       }
       return "";
   }

   public BehaviorDef BD(){
       ArrayList<String> paramNames = new ArrayList<>();
       BehaviorDef BD = new BehaviorDef();
       BD.RT = code.get(cursor);
       cursor += 1;

       String[] primary_ghost = code.get(cursor).split("\\.");
       BD.M = primary_ghost[0];

       String[] secondary_ghost = primary_ghost[1].split("\\(");
       String dec_name = secondary_ghost[0];
       String dec_param = secondary_ghost[1];
       code.set(cursor, BD.M + "." + dec_name);
       code.add(cursor + 1, dec_param);
       BD.B = dec_name;

       //Parameter Processing
       while(true){
           cursor += 1;
           if(Objects.equals(code.get(cursor), "int")){
               continue;
           }
           if(code.get(cursor).contains(keys.get("Model-B-End-Key"))){
               break;
           }
           paramNames.add(filter(code.get(cursor)));
           //System.out.println(filter(code.get(cursor)));
       }

       cursor += 5;
       //System.out.println(code.get(cursor));
       while(!code.get(cursor).equals(keys.get("Terminate-Key"))){
           if(code.get(cursor).equals(keys.get("IC-Key"))){
               String ID = CSPC(); //need to add command to BD
               BD.C.add(ID);
               command_reg.remove(ID);
               continue;
           }
           cursor += 1;
       }
       cursor += 1;
       
       BD.P = paramNames;
       return BD;
   }

   public void parse(){
       for(cursor = 0; cursor < code.size(); cursor++){
           if(code.get(cursor).equals(keys.get("Model-Key"))){
               cursor += 1;
               ModelParseTree mpt = MSP();
               MPTs.add(mpt);
               System.out.println("MODEL: " + mpt.toString());
           }
           else if(code.get(cursor).equals(keys.get("In-Key"))){
               cursor += 2;
               InstanceParseTree ipt = ISP();
               IPTs.add(ipt);
               System.out.println("INSTANCE " + ipt.toString());
           }
           else if(code.get(cursor).equals(keys.get("IC-Key"))){
               CSPC();
           }
           else if(code.get(cursor).equals(keys.get("BehaviorDef-Key"))){
               cursor += 1;
               BehaviorDef BD = BD();
               BDs.add(BD);
               System.out.println("BD: " + BD.toString());
           }

       }
       for(InstanceBehaviorCommand IBC : IBCs){
           System.out.println("IBC " + IBC.toString());
       }
       for(GeneralBehaviorCommand GBC : GBCs){
           System.out.println("GBC " + GBC.toString());
       }
   }

   public ArrayList<String> getCommandReg(){
       return command_reg;
   }
}



