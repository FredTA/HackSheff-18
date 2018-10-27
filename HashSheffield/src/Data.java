import java.io.FileWriter;
import java.io.IOException;

public class Data {
    public static void createNewPass(String hashedPassword, String nameOfService, String date) throws IOException {
        try{
            String pathToFile = "passwords.txt";
            FileWriter writer = new FileWriter(pathToFile);
            String[] data;
            data = new String[3];
            data[0] = hashedPassword;
            data[1] = nameOfService;
            data[2] = date;
            writer.write(data.toString());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        createNewPass("aasdbvaskhdbaksd", "askfjhdbasjhkbd", "sfkjasdbkj");
    }
}
