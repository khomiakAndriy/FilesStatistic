import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class FileStatistic {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        File folder = new File("F:/tmp/");
        List<File> files = new ArrayList<>();
        StatisticUtils.listFilesFromFolder(files, folder);

        Map<String, AtomicInteger> statistic = new ConcurrentHashMap<>();

        files.parallelStream().forEach(file -> {
            List<String> words = StatisticUtils.getListOfWordsFromFile(file);
            words.parallelStream().forEach(e -> {
                synchronized (statistic) {
                    if (!statistic.containsKey(e)) {
                        statistic.put(e, new AtomicInteger(1));
                    } else {
                        statistic.put(e, new AtomicInteger(statistic.get(e).incrementAndGet()));
                    }
                }
            });
        });

        System.out.println("Size - " +statistic.size());
        System.out.println("All words count " + statistic.values().stream().mapToInt(i -> i.get()).sum());
        System.out.println("Files statistic:");
        System.out.println("Word - count");
        statistic.forEach((key, value) -> System.out.println(key + " - " + value));
   }
}
