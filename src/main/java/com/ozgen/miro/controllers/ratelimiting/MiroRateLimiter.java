package com.ozgen.miro.controllers.ratelimiting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A rate limiter with property file watcher functionality.
 *
 */
public class MiroRateLimiter implements RateLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiroRateLimiter.class);
    private static final int SECONDS_TO_WAIT_FOR_SHUTDOWN = 10;
    private final ScheduledExecutorService limitScheduler = Executors.newScheduledThreadPool(1);
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            resetCounters();
        }
    };
    private final String rateLimiterId;
    private int requestCountInPeriod;
    private long resetTimeInMilliSeconds;
    // Configuration instance variables
    private int periodInSeconds;
    private int maxRequestsInPeriod;
    // scheduler for file watcher
    private ScheduledFuture<?> future;

    /**
     * @param rateLimiterId       Unique id of the rate limiter
     * @param periodInSeconds     Period in seconds in which number of request will be limited
     * @param maxRequestsInPeriod Maximum number of requests in a given period
     */
    public MiroRateLimiter(String rateLimiterId, int periodInSeconds, int maxRequestsInPeriod) {
        this.rateLimiterId = rateLimiterId;
        this.periodInSeconds = periodInSeconds;
        this.maxRequestsInPeriod = maxRequestsInPeriod;
        String configLocation = System.getProperty("spring.config.location");
        if (configLocation != null) {
            File file = new File(configLocation);
            if (loadProperties(file)) {
                createWatcher(file.getParent());
            }
        }
        configure(periodInSeconds, maxRequestsInPeriod);
    }

    private boolean loadProperties(File f) {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(f));
            String propPeriodInSeconds = (String) props.get("rateLimiter." + rateLimiterId + ".periodInSeconds");
            String propmaxRequestsInPeriod = (String) props
                    .get("rateLimiter." + rateLimiterId + ".maxRequestsInPeriod");
            this.periodInSeconds = Integer.parseInt(propPeriodInSeconds);
            this.maxRequestsInPeriod = Integer.parseInt(propmaxRequestsInPeriod);
            return true;
        } catch (NumberFormatException | IOException e) {
            LOGGER.error("Error occured reading property file : " + f.getAbsolutePath());
        }
        return false;
    }

    private boolean createWatcher(String strPath) {
        try {
            final Path watchedDirectory = FileSystems.getDefault().getPath(strPath);
            final WatchService watchService = FileSystems.getDefault().newWatchService();
            watchedDirectory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            Thread watcherThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            final WatchKey wk = watchService.take();
                            for (WatchEvent<?> event : wk.pollEvents()) {
                                // we only register "ENTRY_MODIFY" so the context is always a Path.
                                final Path changedFile = (Path) event.context();
                                if (changedFile.endsWith("application.properties")) {
                                    Path absolutePath = watchedDirectory.resolve(changedFile);
                                    if (loadProperties(absolutePath.toAbsolutePath().toFile())) {
                                        configure(periodInSeconds, maxRequestsInPeriod);
                                    }
                                }
                            }
                            wk.reset();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            watcherThread.start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private synchronized void configure(final int periodInSeconds, final int maxRequestsInPeriod) {
        this.periodInSeconds = periodInSeconds;
        this.maxRequestsInPeriod = maxRequestsInPeriod;
        if (future != null) {
            future.cancel(true);
        }
        resetCounters();
        future = limitScheduler.scheduleAtFixedRate(task, periodInSeconds, periodInSeconds, TimeUnit.SECONDS);
    }

    private synchronized void resetCounters() {
        requestCountInPeriod = 0;
        resetTimeInMilliSeconds = System.currentTimeMillis();
    }

    @Override
    public synchronized RateLimit acquire() throws RateLimitExceededException {
        if (requestCountInPeriod == maxRequestsInPeriod) {
            throw new RateLimitExceededException();
        }
        requestCountInPeriod++;
        return new RateLimit.Builder().withLimiterId(rateLimiterId).withMaxRequestsInPeriod(maxRequestsInPeriod)
                .withPeriodInSeconds(periodInSeconds).withRemainingRequests(maxRequestsInPeriod - requestCountInPeriod)
                .withNextResetInMilliSeconds(
                        resetTimeInMilliSeconds + periodInSeconds * 1000 - System.currentTimeMillis())
                .build();
    }

    @Override
    public synchronized void shutdown() {
        limitScheduler.shutdown();
        try {
            limitScheduler.awaitTermination(SECONDS_TO_WAIT_FOR_SHUTDOWN, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
