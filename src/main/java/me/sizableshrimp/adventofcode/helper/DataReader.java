/*
 * AdventOfCode2019
 * Copyright (C) 2022 SizableShrimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
     * If not set, an {@link IllegalArgumentException} is thrown.
     * <p>
     * If a successful connection is made to the AOC servers, the input data is stored in a file that is located in the
     * working directory inside an "aoc_input" folder in case of later usage. The data fetched from the server
     * originally is then returned in an unmodifiable list.
     *
     * @param day The integer day of which to read input data.
     * @return An unmodifiable list of strings representing each line of input data.
     */
    public static List<String> read(int day) {
        Path path = getPath(day);
        List<String> lines = getDataFromFile(path);

        if (lines != null)
            return lines;

        if (System.getenv("AOC_SESSION") == null)
            throw new IllegalArgumentException("No AOC session cookie found!");

        return getDataFromServer(day, Main.YEAR, path);
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

    private static Path getPath(int day) {
        String filename = "day" + Main.pad(day) + ".txt";
        URL url = Main.class.getResource("/days/" + filename);
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

    public static void write(Path path, List<String> lines) {
        Path parent = path.getParent();
        try {
            if (!Files.exists(parent))
                Files.createDirectory(parent);
            //remove empty last line of input files
            Files.writeString(path, String.join(System.lineSeparator(), lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getBasePath(String filename) {
        return Path.of("aoc_input", filename);
    }
}
