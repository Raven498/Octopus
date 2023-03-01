import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
//Only reads code
public class Reader{
    public ArrayList<String> read(String filename){
        ArrayList<String> code = new ArrayList<>();
        File file = new File("MOP.txt");
        Scanner scanner = new Scanner(file);
        try{
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                code.add(line);
            }
            scanner.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return code;
    }
}