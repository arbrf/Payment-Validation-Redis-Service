package com.cpt.payments.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpt.payments.constants.Endpoint;
import com.cpt.payments.pojo.PaymentRequest;
import com.cpt.payments.pojo.PaymentResponse;
import com.cpt.payments.service.PaymentService;
import com.cpt.payments.service.impl.RedisService;
import com.cpt.payments.util.LogMessage;

@RestController
@RequestMapping(Endpoint.VALIDATION_MAPPING)
public class PaymentsController {

	private static final Logger LOGGER = LogManager.getLogger(PaymentsController.class);
	@Autowired
	 private  RedisService redisService;
	@Autowired
	private PaymentService paymentService;
	


	@PostMapping(value = Endpoint.INITIATE_PAYMENT)
	public ResponseEntity<PaymentResponse> sale(@RequestBody PaymentRequest paymentRequest) {
		LogMessage.setLogMessagePrefix("/INITIATE_PAYMENT");
		LogMessage.log(LOGGER, " initiate payment request yes" + paymentRequest);

		return new ResponseEntity<>(paymentService.validateAndInitiatePayment(paymentRequest), HttpStatus.CREATED);
	}
	
	@PostMapping("/add/{key}")
	public void addValueToList(@PathVariable String key, @RequestParam String value) {
		System.out.println("REdis API called");
        redisService.addValueToList(key, value);
    }
	
	@GetMapping("/list/{key}")  
	public List<String> getValuesFromList(@PathVariable String key) {
        return redisService.getAllValuesFromList(key);
    }
}
