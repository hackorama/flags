package com.hackorama.flags.metrics;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

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
    private boolean started;

    public static MetricsTracker getInstance() {
        return MetricsTracker.instance;
    }

    private MetricsTracker() {
        // No instances
    }

    public void updateFlagCount(String country) {
        metricRegistry.meter(country).mark();
    }

    public void report(int reportSeconds) {
        if (!started) {
            reporter.start(5, TimeUnit.SECONDS);
            reporter.report();
            started = true;
        }
    }

}
