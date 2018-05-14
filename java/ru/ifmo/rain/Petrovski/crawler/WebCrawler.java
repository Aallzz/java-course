package ru.ifmo.rain.Petrovski.crawler;

import info.kgeorgiy.java.advanced.crawler.CachingDownloader;
import info.kgeorgiy.java.advanced.crawler.Crawler;
import info.kgeorgiy.java.advanced.crawler.Downloader;
import info.kgeorgiy.java.advanced.crawler.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WebCrawler implements Crawler {

    private final CrawlerInvoke invoke;

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        invoke = new CrawlerInvoke(downloader, downloaders, extractors, perHost);
    }

    public Result download(String url, int depth) {
        List<String> list = new ArrayList<>();
        AppendableLatch latch = new AppendableLatch(1);
        Task t = new Task(url, 1, depth, list, latch, invoke);
        invoke.getDownloading().execute(t::getDownloader);
        try {
            latch.await();
        } catch (InterruptedException e) {
            return null;
        }
        return new Result(list, Collections.emptyMap());
    }

    /**
     * Closes crawler so that it will no longer permit new tasks
     */
    public void close() {
        invoke.close();
    }

    /**
     * Provides interface for using crawler
     * @param args [url, depth, downloaders, extractors, perHost]
     */
    public static void main(String[] args) {
        if (args == null) {
            System.err.println("Invalid args");
            return;
        }
        if (args.length < 5) {
            System.err.println("Not enough parameters");
            return;
        }
        for (int i = 0; i < 5; ++i) {
            if (args[i] == null) {
                System.out.println("Expected not nul value in argument with number" + i);
            }
        }

        int depth = Integer.parseInt(args[1]);
        int downloaders = Integer.parseInt(args[2]);
        int extractors = Integer.parseInt(args[3]);
        int perHost = Integer.parseInt(args[4]);

        try (WebCrawler crawler = new WebCrawler(new CachingDownloader(Paths.get(".")), downloaders, extractors, perHost)) {
            Result res = crawler.download(args[0], depth);
            res.getDownloaded().forEach(System.out::println);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}