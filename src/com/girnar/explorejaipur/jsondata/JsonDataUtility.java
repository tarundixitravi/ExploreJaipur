package com.girnar.explorejaipur.jsondata;

public class JsonDataUtility {

	 private Boolean success;
	 private String message;

	private UtilityData data ;
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
	public UtilityData getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(UtilityData data) {
		this.data = data;
	}

	 
}
