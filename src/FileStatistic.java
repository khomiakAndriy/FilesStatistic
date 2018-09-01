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

        List<Map<String, Integer>> futureResults = convertFutureToMap(furures);

        Map<String, Integer> finalStatistic = getFinalResultFromFutures(futureResults);

        System.out.println("Files statistic:");
        System.out.println("Word - count");

        finalStatistic.forEach((key, value) -> System.out.println(key + " - " + value));

        executor.shutdown();
    }

    private static List<Future<Map<String, Integer>>> addFilesToExecutor(List<File> files, ExecutorService executor) {
        List<Future<Map<String, Integer>>> furures = new ArrayList<>();
        for (File file: files){
            Future<Map<String, Integer>> future = executor.submit(new CountWordTask(file));
            furures.add(future);
        }
        return furures;
    }

    private static List<Map<String, Integer>> convertFutureToMap(List<Future<Map<String, Integer>>> furures) throws InterruptedException, ExecutionException {
        List<Map<String, Integer>> futureResults = new ArrayList<>();
        for (Future<Map<String, Integer>> future : furures){

            Map<String, Integer> stringIntegerMap = future.get();
            futureResults.add(stringIntegerMap);
        }
        return futureResults;
    }

    private static Map<String, Integer> getFinalResultFromFutures(List<Map<String, Integer>> futureResults) {
        Map<String, Integer> finalStatistic = new HashMap<>();

        for (Map<String, Integer> var : futureResults){
            for (Map.Entry<String, Integer> entry : var.entrySet()){
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
