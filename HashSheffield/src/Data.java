import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Data {

    private File pathToFile = new File("passwords.txt");

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
    public String[][] readData() throws FileNotFoundException {
        ArrayList<String> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(pathToFile);
            while (scanner.hasNextLine()) {
                list.add(scanner.next());
            }
            return convertList(list);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return convertList(list);
        }
    }

    public String[][] convertList(ArrayList<String> inputList){
        String [][] outputList = new String[inputList.size()][3];
        for (int i=0; i<inputList.size(); i++){
            int locationOfColon = inputList.get(i).indexOf(";");
            outputList[i][0] = inputList.get(i).substring(0, locationOfColon - 1);
            int locationOfColon2 = inputList.get(i).substring(locationOfColon).indexOf(";");
            outputList[i][1] = inputList.get(i).substring(locationOfColon + 1, locationOfColon2 - 1);
            outputList[i][2] = inputList.get(i).substring(locationOfColon2 + 1);
        }
        return outputList;
    }

//    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("hello;hello1;hello2");
//        System.out.println(convertList(list));
//    }
}
