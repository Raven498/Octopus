import java.io.FileNotFoundException;
import java.util.ArrayList;
public class Main{
    public static void main(String[] args) throws FileNotFoundException {
        Reader reader = new Reader();
        ArrayList<String> code = reader.read("MOP.txt");
        
        Parser parser = new Parser(code);
        parser.parse();
    }
}