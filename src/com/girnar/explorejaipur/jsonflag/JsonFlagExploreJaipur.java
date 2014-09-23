package com.girnar.explorejaipur.jsonflag;


public class JsonFlagExploreJaipur {

		 private Boolean success;
		 private String message;
		 private ExploreJaipurFlagStatus data ;
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
		public ExploreJaipurFlagStatus getData() {
			return data;
		}
		/**
		 * @param data the data to set
		 */
		public void setData(ExploreJaipurFlagStatus data) {
			this.data = data;
		}

}
