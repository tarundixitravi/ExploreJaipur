package com.girnar.explorejaipur.jsondata;

public class JsonDataJaipurBus {

	 private Boolean success;
	 private String message;
	 private JaipurBusData data ;
	 public Boolean getSuccess() {
	  return success;
	 }
	 public void setSuccess(Boolean success) {
	  this.success = success;
	 }
	 public String getMessage() {
	  return message;
	 }
	 public void setMessage(String message) {
	  this.message = message;
	 }
	/**
	 * @return the data
	 */
	public JaipurBusData getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(JaipurBusData data) {
		this.data = data;
	}
	 
}
