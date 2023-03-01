import java.util.ArrayList;
public class Main{
    public static void main(String[] args){
        Reader reader = new Reader();
        ArrayList<String> code = reader.read();
        
        Parser parser = new Parser(code);
        parser.parse();
    }
}
