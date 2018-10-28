import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

public class User {
    public ArrayList<String []> searchForPairs () throws FileNotFoundException {
        Data dataHandler = new Data();
        String[][] dataArray = dataHandler.readData();
        ArrayList<String> passwords = new ArrayList<>();
        for (int i = 0; i < dataArray.length; i++) {
            passwords.add(dataArray[i][2]);
        }
        ArrayList<Integer []> matchedPasswordIndex = new ArrayList<>();
        ArrayList<String []> badVar = new ArrayList<>();
        for (int i = 0; i < passwords.size(); i++) {
            ArrayList<Integer> currentIndices = new ArrayList<>();
            for (int j = 0; j < passwords.size(); j++) {
                if (Objects.equals(passwords.get(i), passwords.get(j))) {
                    currentIndices.add(j);
                    System.out.println(currentIndices);
                }
            }
            if (currentIndices.size()>1){
                for (int k = 0; k < currentIndices.size(); k++) {
                    boolean flag = false;
                    for (int j = 0; j < matchedPasswordIndex.size(); j++) {
                        if (matchedPasswordIndex.contains(currentIndices.toArray(new Integer[currentIndices.size()]))){
                            flag = true;
                        }
                    }
                    if (!flag){
                        matchedPasswordIndex.add(currentIndices.toArray(new Integer[currentIndices.size()]));
                    }
                }
            }
        }
        for (int i = 0; i < matchedPasswordIndex.size(); i++) {
            String[] thisIsAnArray = new String[matchedPasswordIndex.size()];
            for (int j = 0; j < matchedPasswordIndex.get(i).length; j++) {
                System.out.println(matchedPasswordIndex);
//                System.out.println(matchedPasswordIndex.get(i)[j]);
//                System.out.println(dataArray[matchedPasswordIndex.get(i)[j]][0]);
                if (dataArray[matchedPasswordIndex.get(i)[j]][0] != null && matchedPasswordIndex.get(i)[j] != null){
                    thisIsAnArray[j] = dataArray[matchedPasswordIndex.get(i)[j]][0];
                }
            }
            badVar.add(thisIsAnArray);
        }
        return badVar;
    }
}
