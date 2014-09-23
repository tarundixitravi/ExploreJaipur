package com.girnar.explorejaipur.constants;

/**
 * The Class Constants.
 */
public class Constants {

	public static final String MONUMENT = "Monument";
	public static final String UTILITY = "Utility";
	public static final String JAIPUR_BUS = "JaipurBus";
	public static final String EXPLORE_JAIPUR_FLAG = "ExploreJaipurFlag";

	public static final String EXPLORE_JAIPUR_FLAG_ID = "id";
	public static final String EXPLORE_JAIPUR_FLAG_TABLE_NAME = "tableName";
	public static final String EXPLORE_JAIPUR_FLAG_FLAG = "flag";

	public static final String MONUMENT_NAME = "name";
	public static final String MONUMENT_CATEGORY = "category";
	public static final String MONUMENT_ID = "monumentId";
	public static final String MONUMENT_LOCATION = "location";
	public static final String MONUMENT_LATITUDE = "latitude";
	public static final String MONUMENT_LONGITUDE = "longitude";
	public static final String MONUMENT_DESCRIPTION = "description";
	public static final String MONUMENT_IMAGE = "image";
	public static final String MONUMENT_DISTANCE = "distance";
	public static final String MONUMENT_DISTANCE_BUS_STAND = "distanceBusStand";
	

	public static final String UTILITY_NAME = "name";
	public static final String UTILITY_CATEGORY = "category";
	public static final String UTILITY_ID = "utilityId";
	public static final String UTILITY_LOCATION = "location";
	public static final String UTILITY_LATITUDE = "latitude";
	public static final String UTILITY_LONGITUDE = "longitude";
	public static final String UTILITY_DESCRIPTION = "description";
	public static final String UTILITY_DISTANCE = "distance";
	public static final String UTILITY_DISTANCE_BUS_STAND = "distanceBusStand";

	public static final String JAIPUR_BUS_ID = "jaipurBusId";
	public static final String JAIPUR_BUS_ROUT_NO = "routeNo";
	public static final String JAIPUR_BUS_START_STAND = "startStand";
	public static final String JAIPUR_BUS_END_STAND = "endStand";
	public static final String JAIPUR_BUS_INTERMIDIATE_STAND = "intermediate";
	public static final String JAIPUR_BUS_RADIAL_CIRCULAR = "radialCircular";
	public static final String JAIPUR_BUS_COLOR = "color";

	public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
	public static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

	public static final String PREFIX_UTILITY_ID = "ut";
	public static final String PREFIX_MONUMENT_ID = "vp";

	public static final Double JAIPUR_JUNCTION_LATITUDE = 26.919755;
	public static final Double JAIPUR_JUNCTION_LONGITUDE = 75.788387;

	public static final String API_URL_JAIPUR_BUS = "http://explorejaipur.girnarsoft.com/index.php/JaipurBus/restlistdata";
	public static final String API_URL_MONUMENT = "http://explorejaipur.girnarsoft.com/index.php/Monument/restlistdata";
	public static final String API_URL_UTILITY = "http://explorejaipur.girnarsoft.com/index.php/Utility/restlistdata";

	public static final String API_URL_FLAG_EXPLORE_JAIPUR = "http://explorejaipur.girnarsoft.com/index.php/UpdateTableStatus/restListData";

}
