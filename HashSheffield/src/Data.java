import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Data {

    File pathToFile = new File("passwords.txt");

    public void createNewPass(String data) throws IOException {
        try{
            FileWriter writer = new FileWriter(pathToFile);
            writer.write(data);
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public ArrayList<String> readData() throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(pathToFile);
            while (scanner.hasNextLine()) {
                list.add(scanner.next());
            }
            return list;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return list;
        }
    }
}
