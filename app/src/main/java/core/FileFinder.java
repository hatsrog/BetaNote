package core;

import android.provider.ContactsContract;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<Integer, Map<String, String>> findLatestNotesInContext(String filePath)
    {
        Map<Integer, Map<String, String>> notesInContext = new HashMap<>();
        List<String> notes = findLatestNotes(filePath);
        for(String note : notes)
        {
            //! Read file and extract settings and body text foreach file
            DataAnalyzer.extractBodyText("");
            DataAnalyzer.extractSettings("");
        }
        return notesInContext;
    }
}
