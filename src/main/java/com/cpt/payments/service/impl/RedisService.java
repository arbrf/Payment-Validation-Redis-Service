package com.cpt.payments.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cpt.payments.dao.impl.ValidatorRulesDao;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ListOperations<String, String> listOperations;
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ValidatorRulesDao validatorRulesDao;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
    }


    public void addValueToList(String key, String value) {
        listOperations.rightPush(key, value);
    }


    public List<String> getAllValuesFromList(String key) {
        return listOperations.range(key, 0, -1);
    }
    

    public List<String> fetchValuesFromRedis(String key) {
    	 String url = "http://localhost:8081/payments/list/"+key;
        List<String> values = restTemplate.getForObject(url, List.class);
       // if (values == null || values.isEmpty()) 
        if(true)
        {
            // Fetch the values from the DAO
            List<String> defaultValues = validatorRulesDao.getAllRuleNames();
            System.out.println("Validators From DB"+defaultValues);
            // Insert each value into Redis using the RedisService
            for (String value : defaultValues) {
               addValueToList(key, value);
            }
            // Return the newly inserted values
            return defaultValues;
        }
        return values;
    }
    
   
}

