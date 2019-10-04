package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {

    public List<String> findLatestNotes(String filePath)
    {
        List<String> mapList = new ArrayList<>();
        File fileFinder = new File(filePath);
        File[] files = fileFinder.listFiles();
        if(files != null) {
            for (File file : files) {
                mapList.add(file.getName());
            }
        }
        return mapList;
    }
}
