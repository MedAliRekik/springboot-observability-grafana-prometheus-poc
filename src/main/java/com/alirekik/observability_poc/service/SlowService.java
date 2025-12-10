package com.alirekik.observability_poc.service;


import org.springframework.stereotype.Service;

@Service
public class SlowService {

    public String doSlowWork(){
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Slow work completed";
    }
}
