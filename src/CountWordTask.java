import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

public class CountWordTask implements Callable<Map<String, Integer>> {

    private File file;

    public CountWordTask(File file) {
        this.file = file;
    }

    @Override
    public Map<String, Integer> call() throws Exception {
       return StatisticUtils.getWordsStatisticFromFile(file);
    }
}
