package com.commonsensenet.realfarm.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

import com.commonsensenet.realfarm.overlay.Polygon;

public class RealFarmProvider {
	/** Real farm database access. */
	private RealFarmDatabase mDb;

	public RealFarmProvider(RealFarmDatabase database) {

		// database that will be used to handle data.
		mDb = database;

		// used to force creation.
		mDb.open();
		mDb.close();
	}

	public List<Polygon> getPlots(int userId) {
		List<Polygon> tmpList = new ArrayList<Polygon>();

		// opens the db.
		mDb.open();

		Cursor c0 = mDb.getEntries(RealFarmDatabase.TABLE_NAME_PLOT,
				new String[] { RealFarmDatabase.COLUMN_NAME_PLOT_ID },
				RealFarmDatabase.COLUMN_NAME_PLOT_USERID + "=" + userId, null,
				null, null, null);

		// if there are users in the table
		if ((!c0.isClosed()) && (c0.getCount() > 0)) {
			int i = 0;

			// goes to the first entry
			c0.moveToFirst();

			do { // for each plot, draw them
				int id = c0.getInt(0);

				// Read points from database for each user
				Cursor c02 = mDb.getEntries(RealFarmDatabase.TABLE_NAME_POINT,
						new String[] { RealFarmDatabase.COLUMN_NAME_POINT_X,
								RealFarmDatabase.COLUMN_NAME_POINT_Y },
						RealFarmDatabase.COLUMN_NAME_POINT_PLOTID + "=" + id,
						null, null, null, null);

				// if there are points to plotx
				if (c02.getCount() > 0) {
					int j = 0;
					c02.moveToFirst();

					int[] polyX = new int[c02.getCount()];
					int[] polyY = new int[c02.getCount()];

					do { // for each point in the plot, draw it

						int x1 = c02.getInt(0);
						int y1 = c02.getInt(1);

						polyX[j] = x1;
						polyY[j] = y1;

						j = j + 1;
					} while (c02.moveToNext());

					// adds the polygon to the list.
					tmpList.add(new Polygon(polyX, polyY, polyX.length, id));
				}
				i = i + 1;
			} while (c0.moveToNext());
		}

		return tmpList;
	}

	public List<Polygon> getPlots() {
		List<Polygon> tmpList = new ArrayList<Polygon>();

		mDb.open();

		// Create objects to display in plotOverlay for all users
		Cursor c0 = mDb.getAllEntries(RealFarmDatabase.TABLE_NAME_PLOT,
				new String[] { RealFarmDatabase.COLUMN_NAME_PLOT_ID,
						RealFarmDatabase.COLUMN_NAME_PLOT_USERID });

		// if there are users in the table
		if ((!c0.isClosed()) && (c0.getCount() > 0)) {
			int i = 0;
			c0.moveToFirst();

			do { // for each plot, draw them
				int id = c0.getInt(0);

				// Read points from database for each user
				Cursor c02 = mDb.getEntries(RealFarmDatabase.TABLE_NAME_POINT,
						new String[] { RealFarmDatabase.COLUMN_NAME_POINT_X,
								RealFarmDatabase.COLUMN_NAME_POINT_Y },
						RealFarmDatabase.COLUMN_NAME_POINT_PLOTID + "=" + id,
						null, null, null, null);

				// if there are points to plot
				if (c02.getCount() > 0) {
					int j = 0;
					c02.moveToFirst();

					int[] polyX = new int[c02.getCount()];
					int[] polyY = new int[c02.getCount()];

					do { // creates each polygon object using the given points.

						int x1 = c02.getInt(0);
						int y1 = c02.getInt(1);

						polyX[j] = x1;
						polyY[j] = y1;

						j = j + 1;
					} while (c02.moveToNext());

					// adds the polygon to the list.
					tmpList.add(new Polygon(polyX, polyY, polyX.length, id));
				}
				i = i + 1;
			} while (c0.moveToNext());
		}
		mDb.close();

		return tmpList;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Map<Integer, String> getActions() {

		// opens the database.
		mDb.open();

		// query all actions
		Cursor c = mDb.getEntries(RealFarmDatabase.TABLE_NAME_ACTIONNAME,
				new String[] { RealFarmDatabase.COLUMN_NAME_ACTIONNAME_ID,
						RealFarmDatabase.COLUMN_NAME_ACTIONNAME_NAME }, null,
				null, null, null, null);
		c.moveToFirst();

		Map<Integer, String> tmpMap = new HashMap<Integer, String>();

		int actionId;
		String actionName;

		if (c.getCount() > 0) {
			do {

				actionId = c.getInt(0);
				actionName = c.getString(1);
				tmpMap.put(actionId, actionName);
			} while (c.moveToNext());
		}

		mDb.close();

		return tmpMap;
	}
}