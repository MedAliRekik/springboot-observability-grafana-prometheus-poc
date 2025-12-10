package com.alirekik.observability_poc.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemoryService {

    // Juste pour simuler une fuite mémoire / consommation
    private final List<byte[]> memoryHolder = new ArrayList<>();

    public String allocateMegaBytes(int mb) {
        memoryHolder.add(new byte[mb * 1024 * 1024]);
        return "Allocated " + mb + " MB. Chunks count = " + memoryHolder.size();
    }

    public int getChunksCount() {
        return memoryHolder.size();
    }

}
