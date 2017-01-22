package at.lemme.fluent.orm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by thomas on 16.11.16.
 */

class Generic<T> {

    final Class<T> clazz;

    public Generic(Class<T> obj) {
        this.clazz = obj;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Generic<T> getSelf() {
        return this;
    }

    public List<T> getList() {
        return new ArrayList<>();
    }

}

public class Test {

    public static void main(String[] args) throws Exception {

        List<Integer> list = new ArrayList();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }

        list = new ArrayList(0);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            list.add(i);
        }
        System.out.println(System.currentTimeMillis() - start);

//        List<String> x = from(String.class).getSelf().getList();
//        String y = from(String.class).getSelf().getList().get(0);

    }

    public static <T> Generic<T> from(Class<T> entityClass) {
        return new Generic<T>(entityClass);
    }

    private static void createSql() throws IOException, URISyntaxException {
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
