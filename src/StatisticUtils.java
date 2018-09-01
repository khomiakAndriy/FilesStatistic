import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StatisticUtils {

    public static List<File> listFilesFromFolder(List <File> result, File folder) {
        if (folder == null){
            return result;
        } else  if (folder.isFile()){
            result.add(folder);
            return result;
        } else if (folder.isDirectory()){
            for (File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    listFilesFromFolder(result, fileEntry);
                } else {
                    result.add(fileEntry);
                }
            }
        }
        return result;
    }

    public static Map<String, Integer> getWordsStatisticFromFile(File file) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        try (Scanner input = new Scanner(file, "windows-1251");) {
            input.useDelimiter(" +");
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] words = line.split("[\\s\\p{Punct}]+");
                for (String word : words
                        ) {
                    if (word.length() != 0) {
                        if (!result.containsKey(word)) {
                            result.put(word, 1);
                        } else {
                            result.put(word, result.get(word) + 1);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
