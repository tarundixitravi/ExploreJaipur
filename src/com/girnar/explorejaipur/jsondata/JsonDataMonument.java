package com.girnar.explorejaipur.jsondata;

public class JsonDataMonument {

	 private Boolean success;
	 private String message;

	private MonumentData data ;
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
	 public MonumentData getData() {
		return data;
	}
	public void setData(MonumentData data) {
		this.data = data;
	}
	 
}
