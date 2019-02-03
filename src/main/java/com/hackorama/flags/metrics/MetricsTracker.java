package com.hackorama.flags.metrics;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * A facade for the metric collection
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public class MetricsTracker {

    private static MetricRegistry metricRegistry = new MetricRegistry();;
    private static ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();
    private static MetricsTracker instance = new MetricsTracker();
    private static Timer timer;

    public static MetricsTracker getInstance() {
        return MetricsTracker.instance;
    }

    private Timer.Context context;

    private boolean started;

    // Don't let anyone else instantiate this class
    private MetricsTracker() {
    }

    private void initResponseTime() {
        timer = metricRegistry.timer("Request Response Performance");
    }

    public void report(int reportSeconds) {
        if (!started) {
            initResponseTime();
            reporter.start(5, TimeUnit.SECONDS);
            reporter.report();
            started = true;
        }
    }

    public void responsePerfTrackingBegin() {
        context = timer.time();
    }

    public void responsePerfTrackingEnd() {
        context.stop();
    }

    public void updateFlagCount(String country) {
        metricRegistry.meter(country).mark();
    }

}
