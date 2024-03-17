import java.io.FileNotFoundException;
import java.util.ArrayList;
public class Main{
    public static void main(String[] args) throws FileNotFoundException {
        Reader reader = new Reader();
        ArrayList<String> code = reader.read("MOP.txt");

        System.out.println(code);


        Parser parser = new Parser(code);
        parser.parse();
        System.out.println(parser.getCommandReg());


        /*
        Executor executor = new Executor(parser.IBCs, parser.BDs, parser.MPTs, parser.getCommandReg());
        executor.execute();
*/
    }
}
