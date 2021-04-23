package com.jserrano;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {
    static Runtime runtime = null;
    static Process process = null;

    public static void main(String[] args) throws Exception {
        final String ORIENTATION = "landscape";
        final String QUERY = "Dark%20Background%20Ultrawide";
        final String ACCESS_KEY = Files.readString(Paths.get("/Users/juan/dev/UnsplashWallpaper/.env")).split("=")[1];

        int alternator = 1;
        HttpResponse response;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;

        while (true){
            request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.unsplash.com/photos/random?query=" + QUERY + "&client_id=" + ACCESS_KEY + "&orientation=" + ORIENTATION))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String rawURL = response.body().toString().split("\\{\"raw\":\"")[1].split("\\\\")[0];

            if (alternator == 1) {
                try(InputStream in = new URL(rawURL).openStream()){
                    Files.copy(in, Paths.get("/Users/juan/Pictures/Background1.jpg"), StandardCopyOption.REPLACE_EXISTING);
                }
                File file = new File("/Users/juan/Pictures/Background1.jpg");
                setWallpaper(file);
                file = null;
                alternator = 2;
                System.out.println("Changed to B1");
            } else {
                try(InputStream in = new URL(rawURL).openStream()){
                    Files.copy(in, Paths.get("/Users/juan/Pictures/Background2.jpg"), StandardCopyOption.REPLACE_EXISTING);
                }
                File file = new File("/Users/juan/Pictures/Background2.jpg");
                setWallpaper(file);
                file = null;
                alternator = 1;
                System.out.println("Changed to B2");
            }

            Thread.sleep(10 * 60000);
        }

    }
    public static void setWallpaper(File file) throws Exception {
        String[] as = {
                "osascript",
                "-e", "tell application \"Finder\"",
                "-e", "set desktop picture to POSIX file \"" + file.getAbsolutePath() + "\"",
                "-e", "end tell"
        };
        runtime = Runtime.getRuntime();
        process = runtime.exec(as);
        runtime = null;
        process = null;
    }
}
