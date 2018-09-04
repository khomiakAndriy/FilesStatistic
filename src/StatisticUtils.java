import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        Map<String, Integer> result = new ConcurrentHashMap<>();
        try (Scanner input = new Scanner(file, "windows-1251");) {
            input.useDelimiter(" +");
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] words = line.split("[\\s\\p{Punct}]+");
                for (String word : words) {
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

    public static List<String> getListOfWordsFromFile(File file) {
        List<String> result = new ArrayList<>();
        try (Scanner input = new Scanner(file, "windows-1251");) {
            input.useDelimiter(" +");
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] words = line.split("[\\s\\p{Punct}]+");
                for (String word : words) {
                    if (word.length() != 0) {
                        result.add(word);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
