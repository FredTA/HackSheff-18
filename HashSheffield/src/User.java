import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class User {
    public ArrayList<String []> searchForPairs () throws FileNotFoundException {
        Data datahandler = new Data();
        String[][] dataArray = datahandler.readData();
        ArrayList<String> passwords = new ArrayList<>(Arrays.asList(dataArray[3]));
        ArrayList<Integer []> matchedPasswordIndex = new ArrayList<>();
        ArrayList<String []> badVar = new ArrayList<>();
        for (int i = 0; i < passwords.size(); i++) {
            ArrayList<Integer> currentIndices = new ArrayList<>();
            for (int j = i; j < passwords.size(); j++) {
                if (Objects.equals(passwords.get(i), passwords.get(j))){
                    currentIndices.add(j);
                }
                if (currentIndices.size()>0){
                    currentIndices.add(i);
                    for (int k = 0; k < currentIndices.size(); k++) {
                        passwords.remove(k);
                        matchedPasswordIndex.add(currentIndices.toArray(new Integer[currentIndices.size()]));
                    }
                }
            }
        }
        for (Integer[] aMatchedPasswordIndex : matchedPasswordIndex) {
            String[] thisIsAnArray = new String[aMatchedPasswordIndex.length];
            for (int j = 0; j < aMatchedPasswordIndex.length; j++) {
                thisIsAnArray[j] = dataArray[0][aMatchedPasswordIndex[j]];
            }
            badVar.add(thisIsAnArray);
        }
        return badVar;
    }
}
