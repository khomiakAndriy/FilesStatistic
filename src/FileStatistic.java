import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public class FileStatistic {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        File folder = new File("F:/tmp/");
        List<File> files = new ArrayList<>();
        StatisticUtils.listFilesFromFolder(files, folder);
//        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Map.Entry<String, Integer>> listOfEntriesFromParralelStreams = files.parallelStream().map(file -> StatisticUtils.getWordsStatisticFromFile(file)).flatMap(map -> map.entrySet().stream()).collect(Collectors.toList());
        Map<String, Integer> finalStatistic = getFinalStatisticUsingParallelResult(listOfEntriesFromParralelStreams);

        System.out.println("Quantity of unique word " + finalStatistic.size());
        System.out.println("All words count " + finalStatistic.values().stream().mapToInt(i -> i).sum());
        System.out.println("Files statistic:");
        System.out.println("Word - count");

        finalStatistic.forEach((key, value) -> System.out.println(key + " - " + value));

//        executor.shutdown();
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

    private static  Map<String, Integer> getFinalStatisticUsingParallelResult(List<Map.Entry<String, Integer>> mapEntries) throws InterruptedException, ExecutionException {
        Map<String, Integer> finalStatistic = new HashMap<>();

        for (Map.Entry<String, Integer> entry : mapEntries){
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (!finalStatistic.containsKey(key)) {
                finalStatistic.put(key, value);
            } else {
                finalStatistic.put(key, finalStatistic.get(key) + value);
            }
        }
        return finalStatistic;
    }
}
