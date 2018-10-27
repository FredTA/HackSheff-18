import java.io.FileWriter;
import java.io.IOException;

public class Data {
    public static void createNewPass(String[] data) throws IOException {
        try{
            String pathToFile = "passwords.txt";
            FileWriter writer = new FileWriter(pathToFile);
            writer.write(data.toString());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
