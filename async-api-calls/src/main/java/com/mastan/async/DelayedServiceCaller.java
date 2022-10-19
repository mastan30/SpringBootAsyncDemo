package com.mastan.async;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class DelayedServiceCaller {

    @Autowired
    private RestTemplate restTemplate;

    
    @Async("threadAsync")
    public CompletableFuture<JsonNode> callOtherServiceAsync() {
        String localSlowServiceEndpoint = "http://localhost:8086/slow";
        JsonNode responseObj = restTemplate.getForObject(localSlowServiceEndpoint, JsonNode.class);
        return CompletableFuture.completedFuture(responseObj);
    }
    
    public JsonNode callOtherService() {
        String localSlowServiceEndpoint = "http://localhost:8086/slow";
        JsonNode responseObj = restTemplate.getForObject(localSlowServiceEndpoint, JsonNode.class);
        return responseObj;
    }
    
    
}
