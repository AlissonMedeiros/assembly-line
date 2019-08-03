package com.medeiros.assemblyline.parser;

import com.medeiros.assemblyline.model.Task;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskParser {

    public static final String MAINTENANCE = "maintenance";
    public static final long MAINTENANCE_MINUTES = 5L;
    public static final String MINUTES = "min";
    public static final String UNNAMED = "unnamed";
    private static Pattern timePattern = Pattern.compile("([-0-9]+)(?=min)");
    private static Pattern namePattern = Pattern.compile("([A-Za-z- ]+)");

    public static Task parserLine(String line) {
        if (isMaintenance(line)) {
            return Task.builder()
                    .minutes(MAINTENANCE_MINUTES)
                    .name(line)
                    .build();
        }
        return Task.builder()
                .minutes(getValue(line))
                .name(getName(line))
                .build();
    }

    private static Long getValue(String line) {
        Matcher timeMatcher = timePattern.matcher(line);
        if (timeMatcher.find()) {
            String timeValue = timeMatcher.group();
            return Long.valueOf(timeValue);
        }
        return 0L;
    }

    private static String getName(String line) {
        Matcher nameMatcher = namePattern.matcher(line);
        if (hasName(nameMatcher)) {
            return nameMatcher.group().trim();
        }
        return UNNAMED;
    }

    private static boolean hasName(Matcher nameMatcher) {
        return nameMatcher.find() && !nameMatcher.group().trim().equals(MINUTES);
    }

    private static boolean isMaintenance(String line) {
        return line.contains(MAINTENANCE);
    }
}
