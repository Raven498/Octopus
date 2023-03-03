import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
//Only reads code
public class Reader{
    public ArrayList<String> read(String filename) throws FileNotFoundException {
        ArrayList<String> code = new ArrayList<>();
        File file = new File("MOP.txt");
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] lineElements = line.split(" ");
            code.addAll(Arrays.asList(lineElements));
        }
        scanner.close();
        return code;
    }
}