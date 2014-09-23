package com.girnar.explorejaipur.jsonflag;

import java.util.List;

/**
 * The Class JaipurBusData.
 */
public class ExploreJaipurFlagStatus {

	private String totalCount;
	private List<ExploreJaipurFlag> updateTableStatus;
	
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return the updateTableStatus
	 */
	public List<ExploreJaipurFlag> getUpdateTableStatus() {
		return updateTableStatus;
	}
	/**
	 * @param updateTableStatus the updateTableStatus to set
	 */
	public void setUpdateTableStatus(List<ExploreJaipurFlag> updateTableStatus) {
		this.updateTableStatus = updateTableStatus;
	}

	
}
