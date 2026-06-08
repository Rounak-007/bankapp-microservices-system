package com.demo.bankapp.v1.account_service.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties
public class ConfigProperties {

	private String message;
	
	private String customerIdExceptionMsg;
	private String accountIdExceptionMsg;
			
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCustomerIdExceptionMsg() {
		return customerIdExceptionMsg;
	}

	public void setCustomerIdExceptionMsg(String customerIdExceptionMsg) {
		this.customerIdExceptionMsg = customerIdExceptionMsg;
	}

	public String getAccountIdExceptionMsg() {
		return accountIdExceptionMsg;
	}

	public void setAccountIdExceptionMsg(String accountIdExceptionMsg) {
		this.accountIdExceptionMsg = accountIdExceptionMsg;
	}

	@Override
	public String toString() {
		return "ConfigProperties [message=" + message + ", customerIdExceptionMsg=" + customerIdExceptionMsg
				+ ", accountIdExceptionMsg=" + accountIdExceptionMsg + "]";
	}
}
