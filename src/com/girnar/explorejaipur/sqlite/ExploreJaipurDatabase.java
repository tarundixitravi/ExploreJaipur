package com.girnar.explorejaipur.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.girnar.explorejaipur.constants.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The Class ExploreJaipurDatabase.
 */
public class ExploreJaipurDatabase {

	private String database_name = "ExploreJaipur.db";
	private int database_version = 1;
	private Helper helper;

	private SQLiteDatabase db;

	private String createTableMonumentQuery = "CREATE TABLE Monument(monumentId text primary key,name text,category text,description text,location text,latitude text,longitude text,distance text,distanceBusStand text);";
	private String createTableUtilityQuery = "CREATE TABLE Utility(utilityId text primary key,name text,category text,description text,location text,latitude text,longitude text,distance text,distanceBusStand text);";
	private String createTableJaipurBusQuery = "CREATE TABLE JaipurBus(jaipurBusId text primary key,routeNo text, startStand text,endStand text,intermediate text,color text,radialCircular text);";
	private String createTableExploreJaipurFlag = "CREATE TABLE ExploreJaipurFlag(id text primary key,tableName text, flag text);";

	/**
	 * Instantiates a new explore jaipur database.
	 *
	 * @param context the context
	 */
	public ExploreJaipurDatabase(Context context) {
		helper = new Helper(context, database_name, null, database_version);
	}

	/**
	 * The Class Helper.
	 */
	class Helper extends SQLiteOpenHelper {
		
		/**
		 * Instantiates a new helper.
		 *
		 * @param context the context
		 * @param databaseName the database name
		 * @param factory the factory
		 * @param databaseVersion the database version
		 */
		public Helper(Context context, String databaseName,
				CursorFactory cursorFactory, int databaseVersion) {
			super(context, databaseName, cursorFactory, databaseVersion);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(createTableMonumentQuery);
			db.execSQL(createTableUtilityQuery);
			db.execSQL(createTableJaipurBusQuery);
			db.execSQL(createTableExploreJaipurFlag);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists Monument");
			db.execSQL("drop table if exists Utility");
			db.execSQL("drop table if exists JaipurBus");
			db.execSQL("drop table if exists ExploreJaipurFlag");
			onCreate(db);
		}

	}

	/**
	 * Open.
	 */
	public void open() {
		db = helper.getWritableDatabase();
	}

	/**
	 * Close.
	 */
	public void close() {
		helper.close();
	}
	
	
	public void deleteExploreJaipurFlagdata(){
		open();
		String where = "id !='ABC'";
		db.delete(Constants.EXPLORE_JAIPUR_FLAG, where, null);
		close();
	}
	public void deleteJaipurBusdata(){
		open();
		String where = Constants.JAIPUR_BUS_ID+" !='ABC'";
		db.delete(Constants.JAIPUR_BUS, where, null);
		close();
	}
	public void deleteMonumentdata(){
		open();
		String where =Constants.MONUMENT_ID+" !='ABC'";
		db.delete(Constants.MONUMENT, where, null);
		close();
	}
	public void deleteUtilitydata(){
		open();
		String where = Constants.UTILITY_ID+" !='ABC'";
		db.delete(Constants.UTILITY, where, null);
		close();
	}

	public void insertExploreJaipurFlag(ExploreJaipurFlagSqliteData data) {
		open();
		ContentValues cv = new ContentValues();
		cv.put(Constants.EXPLORE_JAIPUR_FLAG_ID, data.getId());
		cv.put(Constants.EXPLORE_JAIPUR_FLAG_TABLE_NAME, data.getTableName());
		cv.put(Constants.EXPLORE_JAIPUR_FLAG_FLAG, data.getFlag());
		db.insert(Constants.EXPLORE_JAIPUR_FLAG, null, cv);
		close();
	}
	/**
	 * Insert monument.
	 *
	 * @param data the data
	 */
	public void insertMonument(MonumentsSqliteData data) {
		open();
		ContentValues cv = new ContentValues();
		cv.put(Constants.MONUMENT_NAME, data.getName());
		cv.put(Constants.MONUMENT_CATEGORY, data.getCategory());
		cv.put(Constants.MONUMENT_DESCRIPTION, data.getDescription());
		cv.put(Constants.MONUMENT_ID, data.getMonumentId());
		cv.put(Constants.MONUMENT_LATITUDE, data.getLatitude());
		cv.put(Constants.MONUMENT_LONGITUDE, data.getLongitude());
		cv.put(Constants.MONUMENT_LOCATION, data.getLocation());
		cv.put(Constants.MONUMENT_DISTANCE, data.getDistance());
		cv.put(Constants.MONUMENT_DISTANCE_BUS_STAND, data.getDistanceBusStand());
		db.insert(Constants.MONUMENT, null, cv);
		close();
	}

	/**
	 * Insert utility.
	 *
	 * @param data the data
	 */
	public void insertUtility(UtilitySqliteData data) {
		open();
		ContentValues cv = new ContentValues();
		cv.put(Constants.UTILITY_NAME, data.getName());
		cv.put(Constants.UTILITY_CATEGORY, data.getCategory());
		cv.put(Constants.UTILITY_DESCRIPTION, data.getDescription());
		cv.put(Constants.UTILITY_ID, data.getUtilityId());
		cv.put(Constants.UTILITY_LATITUDE, data.getLatitude());
		cv.put(Constants.UTILITY_LONGITUDE, data.getLongitude());
		cv.put(Constants.UTILITY_LOCATION, data.getLocation());
		cv.put(Constants.UTILITY_DISTANCE, data.getDistance());
		cv.put(Constants.UTILITY_DISTANCE_BUS_STAND, data.getDistanceBusStand());
		db.insert(Constants.UTILITY, null, cv);
		close();
	}

	/**
	 * Insert jaipur bus.
	 *
	 * @param data the data
	 */
	public void insertJaipurBus(JaipurBusSqliteData data) {
		open();
		ContentValues cv = new ContentValues();
		cv.put(Constants.JAIPUR_BUS_ID, data.getJaipurBusId());
		cv.put(Constants.JAIPUR_BUS_ROUT_NO, data.getRouteNo());
		cv.put(Constants.JAIPUR_BUS_START_STAND, data.getStartStand());
		cv.put(Constants.JAIPUR_BUS_END_STAND, data.getEndStand());
		cv.put(Constants.JAIPUR_BUS_INTERMIDIATE_STAND, data.getIntermediateStands());
		cv.put(Constants.JAIPUR_BUS_COLOR, data.getColor());
		cv.put(Constants.JAIPUR_BUS_RADIAL_CIRCULAR, data.getRadialCircular());
		db.insert(Constants.JAIPUR_BUS, null, cv);
		close();
	}
	
	
	
	
	public List<ExploreJaipurFlagSqliteData> retriveExploreJaipurFlag() {
		List<ExploreJaipurFlagSqliteData> list = new ArrayList<ExploreJaipurFlagSqliteData>();
		open();
		Cursor cursor = db.rawQuery("select * from ExploreJaipurFlag", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String id = cursor.getString(0);
				String tableName = cursor.getString(1);
				String flag = cursor.getString(2);
				
				ExploreJaipurFlagSqliteData ejfsd = new ExploreJaipurFlagSqliteData();

				ejfsd.setId(id);
				ejfsd.setTableName(tableName);
				ejfsd.setFlag(flag);
				list.add(ejfsd);
			}
		}
		close();
		return list;
	}


	/**
	 * Retrive monument.
	 *
	 * @return the list
	 */
	public List<MonumentsSqliteData> retriveMonument() {
		List<MonumentsSqliteData> list = new ArrayList<MonumentsSqliteData>();
		open();
		Cursor cursor = db.rawQuery("select * from Monument"+" ORDER BY name", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String monumentId = cursor.getString(0);
				String name = cursor.getString(1);
				String category = cursor.getString(2);
				String description = cursor.getString(3);
				String location = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String distance = cursor.getString(7);
				String distanceBusStand = cursor.getString(8);
				

				MonumentsSqliteData msd = new MonumentsSqliteData();

				msd.setMonumentId(monumentId);
				msd.setName(name);
				msd.setCategory(category);
				msd.setDescription(description);
				msd.setLocation(location);
				msd.setLatitude(latitude);
				msd.setLongitude(longitude);
				msd.setDistance(distance);
				msd.setDistanceBusStand(distanceBusStand);

				list.add(msd);
			}
		}
		close();
		return list;
	}

	/**
	 * Retrive utility.
	 *
	 * @return the list
	 */
	public List<UtilitySqliteData> retriveUtility() {
		List<UtilitySqliteData> list = new ArrayList<UtilitySqliteData>();
		open();
		Cursor cursor = db.rawQuery("select * from Utility"+" ORDER BY name", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String utilityId = cursor.getString(0);
				String name = cursor.getString(1);
				String category = cursor.getString(2);
				String description = cursor.getString(3);
				String location = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String distance = cursor.getString(7);
				String distanceBusStand = cursor.getString(8);

				UtilitySqliteData usd = new UtilitySqliteData();

				usd.setUtilityId(utilityId);
				usd.setName(name);
				usd.setCategory(category);
				usd.setDescription(description);
				usd.setLocation(location);
				usd.setLatitude(latitude);
				usd.setLongitude(longitude);
				usd.setDistance(distance);
				usd.setDistanceBusStand(distanceBusStand);
				list.add(usd);
			}
		}
		close();
		return list;
	}

	/**
	 * Retrive jaipur bus.
	 *
	 * @return the list
	 */
	public List<JaipurBusSqliteData> retriveJaipurBus() {
		List<JaipurBusSqliteData> list = new ArrayList<JaipurBusSqliteData>();
		open();
		Cursor cursor = db.rawQuery("select * from JaipurBus"+" ORDER BY routeNo", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String jaipurBusId = cursor.getString(0);
				String routeNo = cursor.getString(1);
				String startStand = cursor.getString(2);
				String endStand = cursor.getString(3);
				String intermediateStand = cursor.getString(4);
				String color = cursor.getString(5);
				String radialCircular = cursor.getString(6);

				JaipurBusSqliteData jbsd = new JaipurBusSqliteData();

				jbsd.setJaipurBusId(jaipurBusId);
				jbsd.setRouteNo(routeNo);
				jbsd.setStartStand(startStand);
				jbsd.setEndStand(endStand);
				jbsd.setIntermediateStands(intermediateStand);
				jbsd.setColor(color);
				jbsd.setRadialCircular(radialCircular);

				list.add(jbsd);
			}
		}
		close();
		return list;
	}

	/**
	 * Retrive selected monument.
	 *
	 * @param ob the ob
	 * @return the list
	 */
	public List<MonumentsSqliteData> retriveSelectedMonument(
			MonumentsSqliteData ob) {
		List<MonumentsSqliteData> list = new ArrayList<MonumentsSqliteData>();
		open();
		String searchId = ob.getName();
		Cursor cursor = db.rawQuery("select * from Monument where name="+"'"
				+ searchId+"'"+" ORDER BY name", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String monumentId = cursor.getString(0);
				String name = cursor.getString(1);
				String category = cursor.getString(2);
				String description = cursor.getString(3);
				String location = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String distance = cursor.getString(7);
				String distanceBusStand = cursor.getString(8);

				MonumentsSqliteData msd = new MonumentsSqliteData();

				msd.setMonumentId(monumentId);
				msd.setName(name);
				msd.setCategory(category);
				msd.setDescription(description);
				msd.setLocation(location);
				msd.setLatitude(latitude);
				msd.setLongitude(longitude);
				msd.setDistance(distance);
				msd.setDistanceBusStand(distanceBusStand);				

				list.add(msd);
			}
		}
		close();
		return list;
	}

	/**
	 * Retrive selected utility.
	 *
	 * @param ob the ob
	 * @return the list
	 */
	public List<UtilitySqliteData> retriveSelectedUtility(UtilitySqliteData ob) {
		List<UtilitySqliteData> list = new ArrayList<UtilitySqliteData>();
		open();
		String searchId = ob.getName();
		Cursor cursor = db.rawQuery("select * from Utility where name="+"'"
				+ searchId+"'"+" ORDER BY name", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String utilityId = cursor.getString(0);
				String name = cursor.getString(1);
				String category = cursor.getString(2);
				String description = cursor.getString(3);
				String location = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String distance = cursor.getString(7);
				String distanceBusStand = cursor.getString(8);

				UtilitySqliteData usd = new UtilitySqliteData();

				usd.setUtilityId(utilityId);
				usd.setName(name);
				usd.setCategory(category);
				usd.setDescription(description);
				usd.setLocation(location);
				usd.setLatitude(latitude);
				usd.setLongitude(longitude);
				usd.setDistance(distance);
				usd.setDistanceBusStand(distanceBusStand);		
				list.add(usd);
			}
		}
		close();
		return list;
	}

	/**
	 * Retrive selected jaipur bus.
	 *
	 * @param ob the ob
	 * @return the list
	 */
	public List<JaipurBusSqliteData> retriveSelectedJaipurBus(
			JaipurBusSqliteData ob) {
		List<JaipurBusSqliteData> list = new ArrayList<JaipurBusSqliteData>();
		open();
		String searchId = ob.getJaipurBusId();
		Cursor cursor = db.rawQuery(
				"select * from JaipurBus where jaipurBusId="+"'" + searchId+"'"+" ORDER BY routeNo", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String jaipurBusId = cursor.getString(0);
				String routeNo = cursor.getString(1);
				String startStand = cursor.getString(2);
				String endStand = cursor.getString(3);
				String intermediateStand = cursor.getString(4);
				String color = cursor.getString(5);
				String radialCircular = cursor.getString(6);

				JaipurBusSqliteData jbsd = new JaipurBusSqliteData();

				jbsd.setJaipurBusId(jaipurBusId);
				jbsd.setRouteNo(routeNo);
				jbsd.setStartStand(startStand);
				jbsd.setEndStand(endStand);
				jbsd.setIntermediateStands(intermediateStand);
				jbsd.setColor(color);
				jbsd.setRadialCircular(radialCircular);

				list.add(jbsd);
			}
		}
		close();
		return list;
	}
	
	/**
	 * Retrive selected category monument.
	 *
	 * @param ob the ob
	 * @return the list
	 */
	public List<MonumentsSqliteData> retriveSelectedCategoryMonument(
			MonumentsSqliteData ob) {
		List<MonumentsSqliteData> list = new ArrayList<MonumentsSqliteData>();
		open();
		String searchCategory = ob.getCategory();
		Cursor cursor = db.rawQuery("select * from Monument where category="+"'"
				+ searchCategory+"'"+" ORDER BY name", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String monumentId = cursor.getString(0);
				String name = cursor.getString(1);
				String category = cursor.getString(2);
				String description = cursor.getString(3);
				String location = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String distance = cursor.getString(7);
				String distanceBusStand = cursor.getString(8);

				MonumentsSqliteData msd = new MonumentsSqliteData();

				msd.setMonumentId(monumentId);
				msd.setName(name);
				msd.setCategory(category);
				msd.setDescription(description);
				msd.setLocation(location);
				msd.setLatitude(latitude);
				msd.setLongitude(longitude);
				msd.setDistance(distance);
				msd.setDistanceBusStand(distanceBusStand);

				list.add(msd);
			}
		}
		close();
		return list;
	}

	/**
	 * Retrive selected category utility.
	 *
	 * @param ob the ob
	 * @return the list
	 */
	public List<UtilitySqliteData> retriveSelectedCategoryUtility(UtilitySqliteData ob) {
		List<UtilitySqliteData> list = new ArrayList<UtilitySqliteData>();
		open();
		String searchCategory = ob.getCategory();
		Cursor cursor = db.rawQuery("select * from Utility where category="+"'"
				+ searchCategory+"'"+" ORDER BY name", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {

				String utilityId = cursor.getString(0);
				String name = cursor.getString(1);
				String category = cursor.getString(2);
				String description = cursor.getString(3);
				String location = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String distance = cursor.getString(7);
				String distanceBusStand = cursor.getString(8);

				UtilitySqliteData usd = new UtilitySqliteData();

				usd.setUtilityId(utilityId);
				usd.setName(name);
				usd.setCategory(category);
				usd.setDescription(description);
				usd.setLocation(location);
				usd.setLatitude(latitude);
				usd.setLongitude(longitude);
				usd.setDistance(distance);
				usd.setDistanceBusStand(distanceBusStand);
				list.add(usd);
			}
		}
		close();
		return list;
	}

}
