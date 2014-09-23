package com.girnar.explorejaipur.jsondata;

import java.util.List;

/**
 * The Class JaipurBusData.
 */
public class MonumentData {

	private String totalCount;
	private List<Monuments> monument;
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return the monument
	 */
	public List<Monuments> getMonument() {
		return monument;
	}
	/**
	 * @param monument the monument to set
	 */
	public void setMonument(List<Monuments> monument) {
		this.monument = monument;
	}



	
}
