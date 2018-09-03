import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class FileStatistic {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        File folder = new File("F:/tmp/");
        List<File> files = new ArrayList<>();
        StatisticUtils.listFilesFromFolder(files, folder);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<Map<String, Integer>>> furures = addFilesToExecutor(files, executor);

        Map<String, Integer> finalStatistic = getFinalStatistic(furures);

        System.out.println("Files statistic:");
        System.out.println("Word - count");

        finalStatistic.forEach((key, value) -> System.out.println(key + " - " + value));

        executor.shutdown();
    }

    private static List<Future<Map<String, Integer>>> addFilesToExecutor(List<File> files, ExecutorService executor) {
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();
        for (File file: files){
            Future<Map<String, Integer>> future = executor.submit(new CountWordTask(file));
            futures.add(future);
        }
        return futures;
    }

    private static  Map<String, Integer> getFinalStatistic(List<Future<Map<String, Integer>>> futures) throws InterruptedException, ExecutionException {
        Map<String, Integer> finalStatistic = new HashMap<>();

        for (Future<Map<String, Integer>> future : futures){
            Map<String, Integer> stringIntegerMap = future.get();
            for (Map.Entry<String, Integer> entry : stringIntegerMap.entrySet()){
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (!finalStatistic.containsKey(key)){
                    finalStatistic.put(key, value);
                } else {
                    finalStatistic.put(key, finalStatistic.get(key)+value);
                }
            }
        }
        return finalStatistic;
    }
}
