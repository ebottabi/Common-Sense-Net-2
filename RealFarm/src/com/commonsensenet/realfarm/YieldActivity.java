package com.commonsensenet.realfarm;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsensenet.realfarm.dataaccess.RealFarmDatabase;
import com.commonsensenet.realfarm.model.Resource;
import com.commonsensenet.realfarm.utils.ActionDataFactory;
import com.commonsensenet.realfarm.utils.ApplicationTracker;
import com.commonsensenet.realfarm.utils.ApplicationTracker.EventType;

public class YieldActivity extends TopSelectorActivity implements
		OnClickListener {

	/**
	 * Yield tool. Acts as a search engine.
	 * 
	 * @author Nguyen Lisa
	 */

	/*
	 * TODO: updateValues(), audio, initialisation (see db structure) and
	 * dialogs (see db structure)
	 */

	protected Resource locationSelectorData;
	protected Resource yearSelectorData;
	protected Resource soilTypeSelectorData;
	protected Resource varietySelectorData;
	protected Resource dateSelectorData;
	protected Resource irrigationSelectorData;
	protected Resource fertilizeSelectorData;
	protected Resource diseaseSelectorData;
	protected Resource pestSelectorData;
	protected Resource spraySelectorData;

	private final Context context = this;
	protected int mActionTypeId = RealFarmDatabase.ACTION_TYPE_SELL_ID;
	private TextView maxLabel;
	private TextView avgLabel;
	private TextView minLabel;
	private TextView dateLabel;
	private TextView varietyLabel;
	private TextView soilTypeLabel;
	private TextView irrigationLabel;
	private TextView fertilizerLabel;
	private TextView pestLabel;
	private TextView diseaseLabel;
	private TextView sprayLabel;
	private TextView number;
	private ImageView soilTypeSelector;
	private ImageView varietySelector;
	private ImageView sowingDateSelector;
	private ImageView irrigationSelector;
	private ImageView fertilizeSelector;
	private ImageView pestSelector;
	private ImageView diseaseSelector;
	private ImageView spraySelector;

	private static final int SOIL_TYPE = 10;
	private static final int VARIETY = 11;
	private static final int SOWING_DATE = 12;
	private static final int IRRIGATION = 13;
	private static final int FERTILIZER = 14;
	private static final int PEST = 15;
	private static final int DISEASE = 16;
	private static final int SPRAY = 17;

	public static final String LOG_TAG = "YieldDetailsActivity";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.yielddetails, context);
		setList();

		soilTypeSelector = (ImageView) findViewById(R.id.selector_soil_type);
		varietySelector = (ImageView) findViewById(R.id.selector_variety);
		sowingDateSelector = (ImageView) findViewById(R.id.selector_sowing_date);
		irrigationSelector = (ImageView) findViewById(R.id.selector_irrigation);
		fertilizeSelector = (ImageView) findViewById(R.id.selector_fertilizer);
		pestSelector = (ImageView) findViewById(R.id.selector_pest);
		diseaseSelector = (ImageView) findViewById(R.id.selector_disease);
		spraySelector = (ImageView) findViewById(R.id.selector_spray);

		number = (TextView) findViewById(R.id.number);
		maxLabel = (TextView) findViewById(R.id.max_label);
		avgLabel = (TextView) findViewById(R.id.avg_label);
		minLabel = (TextView) findViewById(R.id.min_label);
		dateLabel = (TextView) findViewById(R.id.selector_sowing_date_label);
		varietyLabel = (TextView) findViewById(R.id.selector_variety_label);
		soilTypeLabel = (TextView) findViewById(R.id.selector_soil_type_label);
		irrigationLabel = (TextView) findViewById(R.id.selector_irrigation_label);
		fertilizerLabel = (TextView) findViewById(R.id.selector_fertilizer_label);
		pestLabel = (TextView) findViewById(R.id.selector_pest_label);
		diseaseLabel = (TextView) findViewById(R.id.selector_disease_label);
		sprayLabel = (TextView) findViewById(R.id.selector_spray_label);

		final ImageView soilTypeSelector = (ImageView) findViewById(R.id.selector_soil_type);
		final ImageView varietySelector = (ImageView) findViewById(R.id.selector_variety);
		final ImageView sowingDateSelector = (ImageView) findViewById(R.id.selector_sowing_date);
		final ImageView irrigationSelector = (ImageView) findViewById(R.id.selector_irrigation);
		final ImageView fertilizeSelector = (ImageView) findViewById(R.id.selector_fertilizer);
		final ImageView pestSelector = (ImageView) findViewById(R.id.selector_pest);
		final ImageView diseaseSelector = (ImageView) findViewById(R.id.selector_disease);
		final ImageView spraySelector = (ImageView) findViewById(R.id.selector_spray);

		final LinearLayout maxRow = (LinearLayout) findViewById(R.id.max_row);
		final LinearLayout avgRow = (LinearLayout) findViewById(R.id.avg_row);
		final LinearLayout minRow = (LinearLayout) findViewById(R.id.min_row);

		final ImageButton home = (ImageButton) findViewById(R.id.aggr_img_home);
		final ImageButton help = (ImageButton) findViewById(R.id.aggr_img_help);
		final View crop = findViewById(R.id.aggr_crop);
		final View locationSelector = findViewById(R.id.selector_location);
		final View yearSelector = findViewById(R.id.selector_year);
		Button back = (Button) findViewById(R.id.button_back);

		maxRow.setOnLongClickListener(this);
		avgRow.setOnLongClickListener(this);
		minRow.setOnLongClickListener(this);
		number.setOnLongClickListener(this);
		soilTypeSelector.setOnLongClickListener(this);
		sowingDateSelector.setOnLongClickListener(this);
		varietySelector.setOnLongClickListener(this);
		irrigationSelector.setOnLongClickListener(this);
		fertilizeSelector.setOnLongClickListener(this);
		pestSelector.setOnLongClickListener(this);
		diseaseSelector.setOnLongClickListener(this);
		spraySelector.setOnLongClickListener(this);
		locationSelector.setOnLongClickListener(this);
		yearSelector.setOnLongClickListener(this);
		home.setOnLongClickListener(this);
		back.setOnLongClickListener(this);
		help.setOnLongClickListener(this);
		crop.setOnLongClickListener(this);

		home.setOnClickListener(this);
		help.setOnClickListener(this);
		back.setOnClickListener(this);
		crop.setOnClickListener(this);
		locationSelector.setOnClickListener(this);
		yearSelector.setOnClickListener(this);
		soilTypeSelector.setOnClickListener(this);
		sowingDateSelector.setOnClickListener(this);
		varietySelector.setOnClickListener(this);
		irrigationSelector.setOnClickListener(this);
		fertilizeSelector.setOnClickListener(this);
		pestSelector.setOnClickListener(this);
		diseaseSelector.setOnClickListener(this);
		spraySelector.setOnClickListener(this);

	}

	protected void cancelAudio() {

		Intent adminintent = new Intent(YieldActivity.this, Homescreen.class);
		startActivity(adminintent);
		YieldActivity.this.finish();
	}

	public void setList() {

		topSelectorData = ActionDataFactory.getTopSelectorData(mActionTypeId,
				mDataProvider, Global.userId);
		locationSelectorData = mDataProvider.getResources(
				RealFarmDatabase.RESOURCE_TYPE_LOCATION).get(0); // default: CK
																	// Pura
		yearSelectorData = mDataProvider.getResources(
				RealFarmDatabase.RESOURCE_TYPE_YEAR).get(0); // default: most
																// recent
		/**
		 * TODO: uncomment this and set the right defaults according to the
		 * database structure soilTypeSelectorData =
		 * mDataProvider.getResources(RealFarmDatabase
		 * .RESOURCE_TYPE_YEAR).get(0); // TODO: set right default
		 * varietySelectorData =
		 * mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_YEAR
		 * ).get(0); // TODO: set right default irrigationSelectorData =
		 * mDataProvider
		 * .getResources(RealFarmDatabase.RESOURCE_TYPE_YEAR).get(0); // TODO:
		 * set right default dateSelectorData =
		 * mDataProvider.getResources(RealFarmDatabase
		 * .RESOURCE_TYPE_YEAR).get(0); // TODO: set right default
		 * fertilizeSelectorData =
		 * mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_YEAR
		 * ).get(0); // TODO: set right default diseaseSelectorData =
		 * mDataProvider
		 * .getResources(RealFarmDatabase.RESOURCE_TYPE_YEAR).get(0); // TODO:
		 * set right default pestSelectorData =
		 * mDataProvider.getResources(RealFarmDatabase
		 * .RESOURCE_TYPE_YEAR).get(0); // TODO: set right default
		 * spraySelectorData =
		 * mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_YEAR
		 * ).get(0); // TODO: set right default
		 */

		if (topSelectorData != null)
			super.setTopSelector(mActionTypeId);
		if (locationSelectorData != null)
			setLocationSelector();
		if (yearSelectorData != null)
			setYearSelector();
		/**
		 * TODO: uncomment this when the Resources are correctly set just above
		 * if(dateSelectorData != null) updateSelector(dateLabel,
		 * sowingDateSelector, dateSelectorData); if(soilTypeSelectorData !=
		 * null) updateSelector(soilTypeLabel, soilTypeSelector,
		 * soilTypeSelectorData); if(varietySelectorData != null)
		 * updateSelector(varietyLabel, varietySelector, varietySelectorData);
		 * if(irrigationSelectorData != null) updateSelector(irrigationLabel,
		 * irrigationSelector, irrigationSelectorData); if(fertilizeSelectorData
		 * != null) updateSelector(fertilizerLabel, fertilizeSelector,
		 * fertilizeSelectorData); if(diseaseSelectorData != null)
		 * updateSelector(diseaseLabel, diseaseSelector, diseaseSelectorData);
		 * if(pestSelectorData != null) updateSelector(pestLabel, pestSelector,
		 * pestSelectorData); if(spraySelectorData != null)
		 * updateSelector(sprayLabel, spraySelector, spraySelectorData);
		 */
	}

	public void setList(int type, Resource choice) { // change the query
		switch (type) {
		case 2:
			topSelectorData = choice;
			super.setTopSelector(mActionTypeId);
			// TODO: reinitialize the variety selector below according to the
			// new crop?
			break;
		case 3:
			locationSelectorData = choice;
			setLocationSelector();
			break;
		case 4:
			yearSelectorData = choice;
			setYearSelector();
			break;
		case SOIL_TYPE:
			soilTypeSelectorData = choice;
			updateSelector(soilTypeLabel, soilTypeSelector, choice);
			break;
		case VARIETY:
			varietySelectorData = choice;
			updateSelector(varietyLabel, varietySelector, choice);
			break;
		case SOWING_DATE:
			dateSelectorData = choice;
			updateSelector(dateLabel, sowingDateSelector, choice);
			break;
		case IRRIGATION:
			irrigationSelectorData = choice;
			updateSelector(irrigationLabel, irrigationSelector, choice);
			break;
		case FERTILIZER:
			fertilizeSelectorData = choice;
			updateSelector(fertilizerLabel, fertilizeSelector, choice);
			break;
		case PEST:
			pestSelectorData = choice;
			updateSelector(pestLabel, pestSelector, choice);
			break;
		case DISEASE:
			diseaseSelectorData = choice;
			updateSelector(diseaseLabel, diseaseSelector, choice);
			break;
		case SPRAY:
			spraySelectorData = choice;
			updateSelector(sprayLabel, spraySelector, choice);
			break;

		default:
			break;

		}

		updateValues();
	}

	private void updateSelector(TextView label, ImageView selector,
			Resource data) {
		label.setText(data.getShortName());
		// this image should come from the data. It is set in the auety to
		// compose the selector
		selector.setImageResource(data.getImage1());
	}

	private void updateValues() {
		// TODO: Query the database and get the max, min and average> Use the
		// Resources selectorData
		double min = 0;
		double max = 0;
		double avg = 0;
		maxLabel.setText(min + "");
		minLabel.setText(max + "");
		avgLabel.setText(avg + "");

	}

	private void setLocationSelector() {
		final TextView selectorText = (TextView) findViewById(R.id.location_selector_label);
		selectorText.setText(locationSelectorData.getShortName());
	}

	private void setYearSelector() {
		final TextView selectorText = (TextView) findViewById(R.id.year_selector_label);
		selectorText.setText(yearSelectorData.getShortName());
	}

	public void onClick(View v) { // displayDialog return in setList(int type,
									// Resource choice)
		// tracks the application usage.
		ApplicationTracker.getInstance().logEvent(EventType.CLICK,
				Global.userId, getLogTag(),
				getResources().getResourceEntryName(v.getId()));
		ApplicationTracker.getInstance().flush();

		if (v.getId() == R.id.aggr_img_home) {
			startActivity(new Intent(YieldActivity.this, Homescreen.class));
		} else if (v.getId() == R.id.aggr_img_help) {
			playAudio(R.raw.help);
		} else if (v.getId() == R.id.button_back) {
			cancelAudio();
		} else if (v.getId() == R.id.selector_location) {
			List<Resource> data = mDataProvider
					.getResources(RealFarmDatabase.RESOURCE_TYPE_LOCATION);
			displayDialog(v, data, "Select the place", R.raw.problems, null, 3);
		} else if (v.getId() == R.id.aggr_crop) {
			final ImageView img_1 = (ImageView) findViewById(R.id.aggr_crop_img);
			List<Resource> data = ActionDataFactory.getTopSelectorList(
					mActionTypeId, mDataProvider);
			displayDialog(v, data, "Select the variety", R.raw.problems, img_1,
					2);
		} else if (v.getId() == R.id.selector_year) {
			List<Resource> data = mDataProvider
					.getResources(RealFarmDatabase.RESOURCE_TYPE_YEAR);
			displayDialog(v, data, "Select the year", R.raw.problems, null, 4);
		} else { // selectors
			if (v.getId() == R.id.selector_soil_type) {
				List<Resource> data = mDataProvider.getSoilTypes();
				displayDialog(v, data, "Select the soil type", R.raw.problems,
						null, SOIL_TYPE);
			} else if (v.getId() == R.id.selector_variety) {
				List<Resource> data = mDataProvider
						.getVarietiesByCrop(topSelectorData.getId());
				displayDialog(v, data, "Select the variety", R.raw.problems,
						null, VARIETY);
			} else if (v.getId() == R.id.selector_sowing_date) {
				List<Resource> data = mDataProvider
						.getResources(RealFarmDatabase.RESOURCE_TYPE_SOWING_WINDOW);
				displayDialog(v, data, "Select the sowing window",
						R.raw.problems, null, SOWING_DATE);
			} else if (v.getId() == R.id.selector_irrigation) {
				// TODO: Yes/No/all selector
				// Decide if it is done in the database or in the query

			} else if (v.getId() == R.id.selector_fertilizer) {
				// TODO: Yes/No/all selector
				// Decide if it is done in the database or in the query

			} else if (v.getId() == R.id.selector_pest) {
				// TODO: Yes/No/all selector
				// Decide if it is done in the database or in the query

			} else if (v.getId() == R.id.selector_disease) {
				// TODO: Yes/No/all selector
				// Decide if it is done in the database or in the query

			} else if (v.getId() == R.id.selector_spray) {
				// TODO: Yes/No/all selector
				// Decide if it is done in the database or in the query

			}
		}
	}

	// TODO AUDIO: check the right audio.
	// TODO AUDIO: See the farmbook description for which sentences to compose.
	// All the needed data is in the Resources or in the TextView labels
	public boolean onLongClick(View v) {
		playAudio(R.raw.problems);

		ApplicationTracker.getInstance().logEvent(EventType.LONG_CLICK,
				Global.userId, getLogTag(),
				getResources().getResourceEntryName(v.getId()));
		ApplicationTracker.getInstance().flush();

		if (v.getId() == R.id.aggr_img_home) {

		} else if (v.getId() == R.id.aggr_img_help) {

		} else if (v.getId() == R.id.button_back) {

		} else if (v.getId() == R.id.selector_location) {

		} else if (v.getId() == R.id.aggr_crop) {

		} else if (v.getId() == R.id.selector_year) {

		} else if (v.getId() == R.id.selector_soil_type) {

		} else if (v.getId() == R.id.selector_variety) {

		} else if (v.getId() == R.id.selector_sowing_date) {

		} else if (v.getId() == R.id.selector_irrigation) {

		} else if (v.getId() == R.id.selector_fertilizer) {

		} else if (v.getId() == R.id.selector_pest) {

		} else if (v.getId() == R.id.selector_disease) {

		} else if (v.getId() == R.id.selector_spray) {

		} else if (v.getId() == R.id.number) {

		} else if (v.getId() == R.id.max_row) {

		} else if (v.getId() == R.id.avg_row) {

		} else if (v.getId() == R.id.min_row) {

		}

		return true;
	}
}
