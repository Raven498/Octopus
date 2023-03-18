import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
//Only reads code
public class Reader{
    public ArrayList<String> read(String filename) {
        try{
            ArrayList<String> code = new ArrayList<>();
            File file = new File("C:\\All Stuff\\Programming\\Octopus\\src\\MOP.txt");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] lineElements = line.split(" ");
                code.addAll(Arrays.asList(lineElements));
            }
            scanner.close();
            return code;
        } catch(FileNotFoundException e){
            System.out.println("ERROR" + Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}