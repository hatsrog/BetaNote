package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import helper.SettingsConstants;

public class FileFinder {

    public List<String> getNotes(String filePath)
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

    public List<String> sortByLatest(String filePath)
    {
        List<String> latest = new ArrayList<>();
        List<String> pathList = getNotes(filePath);
        int maxDateModification = 0;
        String maxFileDateModification = null;

        while(pathList.size() > 0) {
            for (final String file : pathList) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(filePath + "/" + file));
                    Settings settings = DataAnalyzer.extractSettings(br);
                    if (settings.nodeExists(SettingsConstants.LASTMODIFICATION)) {
                        if (Integer.parseInt(settings.getNode(SettingsConstants.LASTMODIFICATION)) >= maxDateModification) {
                            maxDateModification = Integer.parseInt(settings.getNode(SettingsConstants.LASTMODIFICATION));
                            maxFileDateModification = file;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(maxFileDateModification != null) {
                pathList.remove(maxFileDateModification);
                latest.add(maxFileDateModification);
                maxDateModification = 0;
                maxFileDateModification = null;
            }
        }
        return latest;
    }
}
