package com.commonsensenet.realfarm.actions;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.commonsensenet.realfarm.DataFormActivity;
import com.commonsensenet.realfarm.Global;
import com.commonsensenet.realfarm.R;
import com.commonsensenet.realfarm.dataaccess.RealFarmDatabase;
import com.commonsensenet.realfarm.model.Resource;

public class SowActionActivity extends DataFormActivity {

	public static final String AMOUNT = "amount";
	public static final String DAY = "day";
	public static final String INTERCROP = "intercrop";
	public static final String MONTH = "month";
	public static final String TREATMENT = "treatment";
	public static final String VARIETY = "variety";

	private int mAmount;
	private int mDay;
	private int mIntercrop;
	private int mMonth;
	private int mSeedType;
	private int mTreatment;
	
	private List<Resource> varietiesList;
	private List<Resource> treatmentList;
	private List<Resource> intercropList;
	private List<Resource> monthList;
	
	private int defaultVariety =  -1;
	private String defaultAmount =  "0";
	private String defaultDay =  "0";
	private int defaultMonth =  -1;
	private int defaultTreatment =  -1;
	private int defaultIntercrop =  -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState, R.layout.act_sow_action);
		
		varietiesList = mDataProvider.getVarieties();
		treatmentList = mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_TREATMENT);
		intercropList = mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_INTERCROP);
		monthList = mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_MONTH);

		// adds the fields to validate to the map.
		mResultsMap.put(VARIETY, defaultVariety);
		mResultsMap.put(AMOUNT, defaultAmount);
		mResultsMap.put(DAY, defaultDay);
		mResultsMap.put(MONTH, defaultMonth);
		mResultsMap.put(TREATMENT, defaultTreatment);
		mResultsMap.put(INTERCROP, defaultIntercrop);

		playAudio(R.raw.thankyouclickingactionsowing);

		View item1 = findViewById(R.id.dlg_var_text_sow);
		View item2 = findViewById(R.id.dlg_lbl_unit_no_sow);
		View item3 = findViewById(R.id.dlg_lbl_day_sow);
		View item4 = findViewById(R.id.dlg_lbl_month_sow);
		View item5 = findViewById(R.id.dlg_lbl_treat_sow);
		View item6 = findViewById(R.id.dlg_lbl_intercrop_sow);

		item1.setOnLongClickListener(this);
		item2.setOnLongClickListener(this);
		item3.setOnLongClickListener(this);
		item4.setOnLongClickListener(this);
		item5.setOnLongClickListener(this);
		item6.setOnLongClickListener(this);

		View varietyRow = findViewById(R.id.seed_type_sow_tr);
		View amountRow = findViewById(R.id.units_sow_tr);
		View dateRow = findViewById(R.id.day_sow_tr);
		View treatmentRow = findViewById(R.id.treatment_sow_tr);
		View intercropRow = findViewById(R.id.intercrop_sow_tr);

		varietyRow.setOnLongClickListener(this);
		amountRow.setOnLongClickListener(this);
		dateRow.setOnLongClickListener(this);
		treatmentRow.setOnLongClickListener(this);
		intercropRow.setOnLongClickListener(this);

		item1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();

				// TODO AUDIO: "Select the variety" This is the audio that is heard when the selector dialog opens
				displayDialog(v, varietiesList, VARIETY, "Select the variety", R.raw.select_the_variety, R.id.dlg_var_text_sow, R.id.seed_type_sow_tr, 0);
			}
		});

		item3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();

				// TODO AUDIO: "Choose the day" This is the audio that is heard when the selector dialog opens
				// TODO AUDIO:  Text on tap on ok button in Number picker
				// TODO AUDIO:  Text on tap on cancel button in Number picker
				// TODO AUDIO:  Info on long tap on ok button in Number picker
				// TODO AUDIO:  Info on long tap on cancel button in Number picker
				displayDialogNP("Choose the day", DAY, R.raw.dateinfo, 1, 31,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 1,0, R.id.dlg_lbl_day_sow, R.id.day_sow_tr,R.raw.ok, R.raw.cancel, R.raw.day_ok,R.raw.day_cancel);
			}
		});

		item5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				// TODO AUDIO: "Select if the seeds were treated" This is the audio that is heard when the selector dialog opens
				displayDialog(v, treatmentList, TREATMENT,"Select if the seeds were treated", R.raw.treatmenttoseeds1,R.id.dlg_lbl_treat_sow, R.id.treatment_sow_tr, 0);

			}
		});

		item2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				// TODO AUDIO: "Choose the number of serus" This is the audio that is heard when the selector dialog opens
				// TODO AUDIO:  Text on tap on ok button in Number picker
				// TODO AUDIO:  Text on tap on cancel button in Number picker
				// TODO AUDIO:  Info on long tap on ok button in Number picker
				// TODO AUDIO:  Info on long tap on cancel button in Number picker
				displayDialogNP("Choose the number of serus", AMOUNT, R.raw.choose_serus, 1, 999, 1, 1, 0, R.id.dlg_lbl_unit_no_sow, R.id.units_sow_tr, R.raw.ok, R.raw.cancel, R.raw.serus_ok, R.raw.serus_cancel);
			}
		});

		item6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				// TODO AUDIO: "Main crop or intercrop?" This is the audio that is heard when the selector dialog opens
				displayDialog(v, intercropList, INTERCROP, "Main crop or intercrop?", R.raw.maincrop_intercrop, R.id.dlg_lbl_intercrop_sow,	R.id.intercrop_sow_tr, 0);
			}
		});

		item4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();

				// TODO AUDIO: "Select the month" This is the audio that is heard when the selector dialog opens
				displayDialog(v, monthList, MONTH, "Select the month",R.raw.choosethemonth, R.id.dlg_lbl_month_sow,R.id.day_sow_tr, 0);
			}
		});
	}

	@Override
	public boolean onLongClick(View v) {

		// long click sounds are always forced since they represent
		// the helping system.
		// checks which view was clicked.
		if (v.getId() == R.id.dlg_var_text_sow) {
			// TODO AUDIO: "Select the variety" default if nothing is in the field
			if((Integer) mResultsMap.get(VARIETY) == defaultVariety) playAudio(R.raw.varietyofseedssowd, true); 
			else playAudio(varietiesList.get(((Integer)mResultsMap.get(VARIETY))).getAudio(), true); 
		} else if (v.getId() == R.id.dlg_lbl_unit_no_sow) {
			// TODO AUDIO: "Select the number of serus" default if nothing is in the field
			if(mResultsMap.get(AMOUNT).equals(defaultAmount)) playAudio(R.raw.selecttheunits, true); 
			// TODO AUDIO: Say the number Integer.valueOf(mResultsMap.get(AMOUNT).toString());
			else playAudio(R.raw.problems, true);  
		} else if (v.getId() == R.id.dlg_lbl_month_sow) {
			// TODO AUDIO: "Choose the month" default if nothing is in the field
			if((Integer) mResultsMap.get(MONTH) == defaultMonth) playAudio(R.raw.choosethemonth, true); 
			else playAudio(monthList.get(((Integer)mResultsMap.get(MONTH))).getAudio(), true); 
		} else if (v.getId() == R.id.dlg_lbl_treat_sow) {
			// TODO AUDIO: "Treated or not treated?" default if nothing is in the field
			if((Integer) mResultsMap.get(TREATMENT) == defaultTreatment) playAudio(R.raw.treatmenttoseeds1, true); 
			else playAudio(monthList.get(((Integer)mResultsMap.get(TREATMENT))).getAudio(), true); 
		} else if (v.getId() == R.id.dlg_lbl_intercrop_sow) {
			// TODO AUDIO: "Main crop or intercrop?" default if nothing is in the field
			if((Integer) mResultsMap.get(INTERCROP) == defaultIntercrop) playAudio(R.raw.croptype, true); 
			else playAudio(intercropList.get(((Integer)mResultsMap.get(INTERCROP))).getAudio(), true); 
		}  else if (v.getId() == R.id.dlg_lbl_day_sow) {
			// TODO AUDIO: "Select the day" default if nothing is in the field
			if(mResultsMap.get(DAY).equals(defaultDay)) playAudio(R.raw.selectthedate, true); 
			// TODO AUDIO: Say the number Integer.valueOf(mResultsMap.get(DAY).toString());
			else playAudio(R.raw.problems, true);   
		} 
		
		// TODO AUDIO: Check the remaining audio
		else if (v.getId() == R.id.aggr_img_help) {
			playAudio(R.raw.data_sow_help, true);
		} else if (v.getId() == R.id.seed_type_sow_tr) {
			playAudio(R.raw.variety, true);
		} else if (v.getId() == R.id.units_sow_tr) {
			playAudio(R.raw.amount, true);
		} else if (v.getId() == R.id.treatment_sow_tr) {
			playAudio(R.raw.treatmentdone, true);
		} else if (v.getId() == R.id.intercrop_sow_tr) {
			playAudio(R.raw.intercrop, true);
		} else if (v.getId() == R.id.day_sow_tr) {
			playAudio(R.raw.date, true);
		} else {
			// checks if the parent has the sound.
			return super.onLongClick(v);
		}

		// shows the help icon.
		showHelpIcon(v);

		return true;
	}

	@Override
	protected Boolean validateForm() {

		// gets the current values from the map.
		mAmount = Integer.valueOf(mResultsMap.get(AMOUNT).toString());
		// month corresponds to the id in the resource table.
		mDay = Integer.valueOf(mResultsMap.get(DAY).toString());

		// flag that indicates if the form is valid.
		boolean isValid = true;

		if ((Integer)mResultsMap.get(VARIETY) != defaultVariety) {
			highlightField(R.id.seed_type_sow_tr, false);
		} else {
			isValid = false;
			highlightField(R.id.seed_type_sow_tr, true);
		}

		if (mAmount > Integer.parseInt(defaultAmount)) {
			highlightField(R.id.units_sow_tr, false);
		} else {
			isValid = false;
			highlightField(R.id.units_sow_tr, true);
		}

		if ((Integer) mResultsMap.get(MONTH) != defaultMonth && mDay > Integer.parseInt(defaultDay) && validDate(mDay, monthList.get((Integer) mResultsMap.get(MONTH)).getId())) {
			highlightField(R.id.day_sow_tr, false);
		} else {
			isValid = false;
			highlightField(R.id.day_sow_tr, true);
		}

		if ((Integer) mResultsMap.get(TREATMENT) != defaultTreatment) {
			highlightField(R.id.treatment_sow_tr, false);
		} else {
			isValid = false;
			highlightField(R.id.treatment_sow_tr, true);
		}

		if ((Integer) mResultsMap.get(INTERCROP) != defaultIntercrop) {
			highlightField(R.id.intercrop_sow_tr, false);
		} else {
			isValid = false;
			highlightField(R.id.intercrop_sow_tr, true);
		}

		// if all the fields are valid the data is inserted into the database.
		if (isValid) {
			mSeedType = varietiesList.get((Integer)mResultsMap.get(VARIETY)).getId();
			mMonth = monthList.get((Integer) mResultsMap.get(MONTH)).getId();
			mTreatment = treatmentList.get((Integer) mResultsMap.get(TREATMENT)).getId();
			mIntercrop = intercropList.get((Integer) mResultsMap.get(INTERCROP)).getId();

			// inserts the new plot into the table.
			long result = mDataProvider.addSowAction(Global.userId,
					Global.plotId, mAmount, mSeedType, mTreatment, mIntercrop,
					getDate(mDay, mMonth), 0);

			// returns true if no error was produced.
			return result != -1;
		}
		return false;
	}
}