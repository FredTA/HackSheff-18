import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
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
            boolean flag1 = false;
            boolean flag2 = false;
            StringBuilder string1 = new StringBuilder();
            StringBuilder string2 = new StringBuilder();
            for (int j = 0; j < inputList.get(i).length(); j++) {
                if (!flag1){
                    if (Objects.equals(inputList.get(i).substring(j, j + 1), ";")){
                        flag1 = true;
                        outputList[i][0] = string1.toString();
                    }
                    else {
                        string1.append(inputList.get(i).substring(j, j + 1));
                    }
                }
                else if (!flag2){
                    if (Objects.equals(inputList.get(i).substring(j, j + 1), ";")){
                        outputList[i][1] = string2.toString();
                        outputList[i][2] = inputList.get(i).substring(j+1);
                        break;
                    }
                    else {
                        string2.append(inputList.get(i).substring(j, j + 1));
                    }
                }
            }
        }
        return outputList;
    }

//    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("hello;hello1;hello2");
//        System.out.println(convertList(list));
//    }
}
