import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Data {

    String pathToFile = "passwords.txt";

    public void createNewPass(String data) throws IOException {
        try{
//            String pathToFile = "passwords.txt";
            FileWriter writer = new FileWriter(pathToFile);
            writer.write(data);
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public ArrayList<String> readData(){
        Scanner scanner = new Scanner(pathToFile);
        ArrayList<String> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            list.add(scanner.next());
        }
        return list;
    }
}
