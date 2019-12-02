/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.helper;

import me.sizableshrimp.adventofcode.Main;
import me.sizableshrimp.adventofcode.templates.Day;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataReader {

    /**
     * The read method is used to locate input data for a specified {@link Day}.
     * <p>
     * This first checks to see if there is a text file with input data for the specified {@link Day} class in
     * the "days" subfolder in resources or run directory. For example, class Day01 would have a corresponding input
     * text file in "days/day01.txt". If an input text file is found, the data from that file is returned in an
     * unmodifiable list.
     * <p>
     * If no input text file is found, this method then <u>tries to connect to the Advent Of Code servers for input
     * data</u>. This step of the method is optional, and requires an environment variable be set to use it.
     * "AOC_SESSION" must be set as an environment variable, which should hold your session cookie for the
     * <a href="http://adventofcode.com">Advent Of Code Website</a>. This cookie can be found using browser inspection.
     * If not set, no connection to the server will be attempted at all, and an empty list will be returned.
     * <p>
     * If a successful connection is made to the AOC server, the input data is stored in a file that is located in the
     * run directory under a "days" subfolder in case of later usage. The data fetched from the server originally is
     * then returned in an unmodifiable list.
     *
     * @param clazz The {@link Day} class of which to read input data.
     * @return An unmodifiable list of strings representing each line of input data.
     */
    public static List<String> read(Class<? extends Day> clazz) {
        Path path = getPath(clazz);
        List<String> lines = getDataFromFile(path);

        if (lines != null)
            return lines;

        if (System.getenv("AOC_SESSION") == null)
            return List.of();

        return getDataFromServer(getNumber(clazz), Main.year, path);
    }

    /**
     * Reads all input data for a given year from the server using the specified "AOC_SESSION" environment variable
     * and saves it to running directory subfolder "days". See {@link #read} for more detail.
     *
     * @param year The Advent Of Code year to read input data for each day.
     */
    public static void writeAllDaysToFile(int year) {
        for (int i = 1; i <= 25; i++) {
            String filename = "day" + Main.pad(i) + ".txt";
            Path path = getBasePath(filename);
            getDataFromServer(i, year, path);
        }
    }

    private static List<String> getDataFromServer(int day, int year, Path path) {
        List<String> lines = new ArrayList<>();

        try {
            URI uri = new URI("https://adventofcode.com/" + year + "/day/" + day + "/input");

            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("User-Agent",
                            "SizableShrimp-AOC-Data-Bot/2.0.1.9 (+http://github.com/SizableShrimp/AdventOfCode2019)")
                    .header("Cookie", "session=" + System.getenv("AOC_SESSION"))
                    .build();
            HttpResponse<Stream<String>> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofLines());

            lines = response.body().collect(Collectors.toList());
            if (path != null)
                write(path, lines);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return List.copyOf(lines);
    }

    private static List<String> getDataFromFile(Path path) {
        try {
            if (path != null && Files.exists(path)) {
                return List.copyOf(Files.readAllLines(path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Path getPath(Class<? extends Day> clazz) {
        String filename = clazz.getSimpleName().toLowerCase(Locale.ROOT) + ".txt";
        URL url = clazz.getResource("/days/" + filename);
        if (url == null) {
            return getBasePath(filename);
        }
        try {
            return Path.of(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return Path.of("");
        }
    }

    private static void write(Path path, List<String> lines) {
        new Thread(() -> {
            Path parent = path.getParent();
            try {
                if (!Files.exists(parent))
                    Files.createDirectory(parent);
                Files.write(path, lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static int getNumber(Class<? extends Day> clazz) {
        return Integer.parseInt(clazz.getSimpleName().substring(3));
    }

    private static Path getBasePath(String filename) {
        try {
            return Path.of(DataReader.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .resolve("days").resolve(filename);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
