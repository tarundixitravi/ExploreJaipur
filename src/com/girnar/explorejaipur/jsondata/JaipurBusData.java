package com.girnar.explorejaipur.jsondata;

import java.util.List;

/**
 * The Class JaipurBusData.
 */
public class JaipurBusData {

	private String totalCount;
	private List<JaipurBus> jaipurBus;
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return the jaipurBus
	 */
	public List<JaipurBus> getJaipurBus() {
		return jaipurBus;
	}
	/**
	 * @param jaipurBus the jaipurBus to set
	 */
	public void setJaipurBus(List<JaipurBus> jaipurBus) {
		this.jaipurBus = jaipurBus;
	}
	

	
}
