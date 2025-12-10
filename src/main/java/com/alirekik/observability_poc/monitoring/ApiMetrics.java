package com.alirekik.observability_poc.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class ApiMetrics {

    private final Counter helloCounter;
    private final Timer slowTimer;

    public ApiMetrics(MeterRegistry registry) {
        this.helloCounter = Counter.builder("demo_hello_requests_total")
                .description("Nombre d'appels à /api/hello")
                .register(registry);

        this.slowTimer = Timer.builder("demo_slow_request_duration")
                .description("Durée des appels à /api/slow")
                .register(registry);
    }

    public void incrementHello() {
        helloCounter.increment();
    }

    public <T> T recordSlow(Runnable runnable) {
        slowTimer.record(runnable);
        return null;
    }

    public <T> T recordSlowCallable(java.util.concurrent.Callable<T> callable) throws Exception {
        return slowTimer.recordCallable(callable);
    }



}
