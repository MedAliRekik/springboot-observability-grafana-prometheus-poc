package com.alirekik.observability_poc.controller;

import com.alirekik.observability_poc.monitoring.ApiMetrics;
import com.alirekik.observability_poc.service.MemoryService;
import com.alirekik.observability_poc.service.SlowService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final SlowService slowService;
    private final MemoryService memoryService;
    private final ApiMetrics apiMetrics;

    public DemoController(SlowService slowService,
                          MemoryService memoryService,
                          ApiMetrics apiMetrics) {
        this.slowService = slowService;
        this.memoryService = memoryService;
        this.apiMetrics = apiMetrics;
    }

    @GetMapping("/hello")
    public String hello() {
        apiMetrics.incrementHello();
        return "Hello Observability 👋";
    }

    @GetMapping("/slow")
    public String slow() throws Exception {
        return apiMetrics.recordSlowCallable(() -> slowService.doSlowWork());
    }

    @PostMapping("/memory")
    public String allocate(@RequestParam(defaultValue = "10") int mb) {
        return memoryService.allocateMegaBytes(mb);
    }

    @GetMapping("/memory/chunks")
    public String chunks() {
        return "Chunks count = " + memoryService.getChunksCount();
    }










}
