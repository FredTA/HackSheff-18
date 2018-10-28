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
            FileWriter writer = new FileWriter(pathToFile, true);
            writer.write("\n" + data); //Will break if this is the first entry
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
            scanner.close();
            return convertList(list);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return convertList(list);
        }
    }

    public String[][] convertList(ArrayList<String> inputList){
        String [][] outputList = new String[inputList.size()][3];

        boolean colon1Found = false;
        boolean colon2Found = false;
        String subString = "";

        for (int i=0; i<inputList.size(); i++){
            String inputString = inputList.get(i);
            for (int elem = 0; elem < inputString.length(); elem++) {
                if (inputString.charAt(elem) != ';')  {
                    subString += inputString.charAt(elem);
                }
                else if (!colon1Found) {
                    colon1Found = true;
                    outputList[i][0] = subString;
                    subString = "";
                }
                else if (!colon2Found){
                    colon2Found = true;
                    outputList[i][1] = subString;
                    subString = "";
                }
            }
            colon1Found = colon2Found = false;
            outputList[i][2] = subString;
            subString = "";
        }
 /*
        for (int i = 0; i < outputList.length; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(outputList[i][j] + " ");
            }
            System.out.println("");
        } */
        return outputList;
    }

//    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("hello;hello1;hello2");
//        System.out.println(convertList(list));
//    }
}
