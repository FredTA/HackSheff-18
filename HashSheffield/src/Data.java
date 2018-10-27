import java.io.FileWriter;
import java.io.IOException;

public class Data {
    public static void createNewPass(String data) throws IOException {
        try{
            String pathToFile = "passwords.txt";
            FileWriter writer = new FileWriter(pathToFile);
            //System.out.println(data.toString());
            writer.write(data);
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
