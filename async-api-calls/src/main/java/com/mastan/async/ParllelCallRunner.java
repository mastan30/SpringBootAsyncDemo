package com.mastan.async;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ParllelCallRunner implements CommandLineRunner {

    @Autowired
    DelayedServiceCaller delayedServiceCaller;

    @Override
    public void run(String... args) throws Exception {
        Instant start = Instant.now();
        List<CompletableFuture<JsonNode>> allFutures = new ArrayList<>();
        List<JsonNode> allFuturesSlow = new ArrayList<JsonNode>();

        for (int i = 0; i < 10; i++) {
            allFuturesSlow.add(delayedServiceCaller.callOtherService());
        }
        
        for (int i = 0; i < 10; i++) {
            System.out.println("response without Async: " + allFuturesSlow.get(i).toString());
        }

        System.out.println("Total time without Async: " + Duration.between(start, Instant.now()).getSeconds());
        
        start = Instant.now();
        
        for (int i = 0; i < 10; i++) {
            allFutures.add(delayedServiceCaller.callOtherServiceAsync());
        }

        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]));

        for (int i = 0; i < 10; i++) {
            System.out.println("response with Async: " + allFutures.get(i).get().toString());
        }

        System.out.println("Total time with Async: " + Duration.between(start, Instant.now()).getSeconds());
    }
}
