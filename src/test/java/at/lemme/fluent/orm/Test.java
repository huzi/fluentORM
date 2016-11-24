package at.lemme.fluent.orm;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by thomas on 16.11.16.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(ClassLoader.getSystemClassLoader().getResource("persons.csv").toURI()));

        String sql = lines.stream().skip(1)
                .map(s -> {
                    String[] parts = s.split(";");
                    LocalDate date = null;
                    while (date == null) {
                        try {
                            date = LocalDate.of(Integer.parseInt(parts[5]), Integer.parseInt(parts[4]), new Random().nextInt(30) - 1);
                        } catch (Exception e) {
                        }
                    }
                    return "('" + parts[0] + "', '" + parts[1] + "', '" + parts[2] + "', '" + date.format(DateTimeFormatter.ISO_DATE) + "')";
                }).collect(Collectors.joining(",\n "));
        System.out.println(sql);
    }
}
