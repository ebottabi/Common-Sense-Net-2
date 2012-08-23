package com.commonsensenet.realfarm.actions;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.commonsensenet.realfarm.DataFormActivity;
import com.commonsensenet.realfarm.Global;
import com.commonsensenet.realfarm.R;
import com.commonsensenet.realfarm.dataaccess.RealFarmDatabase;
import com.commonsensenet.realfarm.model.Resource;
import com.commonsensenet.realfarm.utils.ApplicationTracker;
import com.commonsensenet.realfarm.utils.ApplicationTracker.EventType;

public class SprayActionActivity extends DataFormActivity {

	public static final String AMOUNT = "amount";
	public static final String DAY = "day";
	public static final String MONTH = "month";
	public static final String PESTICIDE = "pesticide";
	public static final String PROBLEM = "problem";
	public static final String UNIT = "unit";

	private int mAmount;
	private int mDay;
	private int mMonth;
	private int mPesticide;
	private int mProblem;
	private int mUnit;
	
	private int defaultProblem = -1;
	private int defaultPesticide = -1;
	private int defaultUnit = -1;
	private int defaultMonth = -1;
	private String defaultAmount = "0";
	private String defaultDay = "0";
	
	private List<Resource> problemList;
	private List<Resource> pesticideList;
	private List<Resource> unitList;
	List<Resource> monthList;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState, R.layout.act_spray_action);
		
		problemList = mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_PROBLEM);
		pesticideList = mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_PESTICIDE);
		unitList = mDataProvider.getUnits(RealFarmDatabase.ACTION_TYPE_SPRAY_ID);
		monthList = mDataProvider.getResources(RealFarmDatabase.RESOURCE_TYPE_MONTH);
		
		playAudio(R.raw.clickingspraying);

		// adds the name of the fields to validate.
		mResultsMap.put(PROBLEM, defaultProblem);
		mResultsMap.put(PESTICIDE, defaultPesticide);
		mResultsMap.put(AMOUNT, defaultAmount);
		mResultsMap.put(UNIT, defaultUnit);
		mResultsMap.put(DAY, defaultDay);
		mResultsMap.put(MONTH, defaultMonth);

		View item1 = findViewById(R.id.dlg_lbl_prob_spray);
		View item2 = findViewById(R.id.dlg_lbl_pest_spray);
		View item3 = findViewById(R.id.dlg_lbl_unit_no_spray);
		View item4 = findViewById(R.id.dlg_lbl_units_spray);
		View item5 = findViewById(R.id.dlg_lbl_day_spray);
		View item6 = findViewById(R.id.dlg_lbl_month_spray);

		item1.setOnLongClickListener(this);
		item2.setOnLongClickListener(this);
		item4.setOnLongClickListener(this);
		item5.setOnLongClickListener(this);
		item3.setOnLongClickListener(this);
		item6.setOnLongClickListener(this);

		View problemRow = findViewById(R.id.prob_spray_tr);
		View pesticideRow = findViewById(R.id.pest_spray_tr);
		View amountRow = findViewById(R.id.units_spray_tr);
		View dateRow = findViewById(R.id.day_spray_tr);

		problemRow.setOnLongClickListener(this);
		pesticideRow.setOnLongClickListener(this);
		amountRow.setOnLongClickListener(this);
		dateRow.setOnLongClickListener(this);

		item1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				ApplicationTracker.getInstance().logEvent(EventType.CLICK, getLogTag(),getResources().getResourceEntryName(v.getId()));
				ApplicationTracker.getInstance().flush();

				// TODO AUDIO "Choose the problem for spraying"  This is the audio that is heard when the selector dialog opens
				displayDialog(v, problemList, PROBLEM,"Choose the problem for spraying", R.raw.problems,R.id.dlg_lbl_prob_spray, R.id.prob_spray_tr, 0);
			}
		});

		item2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				ApplicationTracker.getInstance().logEvent(EventType.CLICK, getLogTag(),getResources().getResourceEntryName(v.getId()));
				ApplicationTracker.getInstance().flush();

				// TODO AUDIO "Choose the pesticide"  This is the audio that is heard when the selector dialog opens
				displayDialog(v, pesticideList, PESTICIDE, "Choose the pesticide",R.raw.problems, R.id.dlg_lbl_pest_spray,R.id.pest_spray_tr, 0);
			}
		});

		item3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				ApplicationTracker.getInstance().logEvent(EventType.CLICK, getLogTag(),getResources().getResourceEntryName(v.getId()));
				ApplicationTracker.getInstance().flush();

				// TODO AUDIO: "Choose the quantity" This is the audio that is heard when the selector dialog opens
				// TODO AUDIO:  Text on tap on ok button in Number picker
				// TODO AUDIO:  Text on tap on cancel button in Number picker
				// TODO AUDIO:  Info on long tap on ok button in Number picker
				// TODO AUDIO:  Info on long tap on cancel button in Number picker
				displayDialogNP("Choose the quantity", AMOUNT, R.raw.selecttheunits, 1, 20, 1, 1, 0, R.id.dlg_lbl_unit_no_spray, R.id.units_spray_tr, R.raw.ok, R.raw.cancel, R.raw.quantity_ok, R.raw.quantity_cancel);
			}
		});

		item4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				ApplicationTracker.getInstance().logEvent(EventType.CLICK, getLogTag(),getResources().getResourceEntryName(v.getId()));
				ApplicationTracker.getInstance().flush();

				// TODO AUDIO "Choose the unit"  This is the audio that is heard when the selector dialog opens
				displayDialog(v, unitList, UNIT, "Choose the unit", R.raw.problems,R.id.dlg_lbl_units_spray, R.id.units_spray_tr, 1);
			}
		});

		item5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				ApplicationTracker.getInstance().logEvent(EventType.CLICK, getLogTag(),getResources().getResourceEntryName(v.getId()));
				ApplicationTracker.getInstance().flush();

				// TODO AUDIO: "Choose the day" This is the audio that is heard when the selector dialog opens
				// TODO AUDIO:  Text on tap on ok button in Number picker
				// TODO AUDIO:  Text on tap on cancel button in Number picker
				// TODO AUDIO:  Info on long tap on ok button in Number picker
				// TODO AUDIO:  Info on long tap on cancel button in Number picker
				displayDialogNP("Choose the day", DAY, R.raw.dateinfo, 1, 31,
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 1,
						0, R.id.dlg_lbl_day_spray, R.id.day_spray_tr,
						R.raw.ok, R.raw.cancel, R.raw.day_ok,
						R.raw.day_cancel);
			}
		});

		item6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopAudio();
				
				ApplicationTracker.getInstance().logEvent(EventType.CLICK, getLogTag(),getResources().getResourceEntryName(v.getId()));
				ApplicationTracker.getInstance().flush();

				// TODO AUDIO "Choose the month"  This is the audio that is heard when the selector dialog opens
				displayDialog(v, monthList, MONTH, "Select the month",
						R.raw.choosethemonth, R.id.dlg_lbl_month_spray,
						R.id.day_spray_tr, 0);
			}
		});
	}

	@Override
	public boolean onLongClick(View v) {
		
		ApplicationTracker.getInstance().logEvent(EventType.LONG_CLICK, getLogTag(),getResources().getResourceEntryName(v.getId()));
		ApplicationTracker.getInstance().flush();

		// forces all long click sounds.

		if (v.getId() == R.id.dlg_lbl_prob_spray) {
			// TODO AUDIO: "Select the problem" default if nothing is in the field
			if((Integer) mResultsMap.get(PROBLEM) == defaultProblem) playAudio(R.raw.selecttheproblem, true); 
			else playAudio(problemList.get(((Integer)mResultsMap.get(PROBLEM))).getAudio(), true); 
		} else if (v.getId() == R.id.dlg_lbl_pest_spray) {
			// TODO AUDIO: "Select the pesticide" default if nothing is in the field
			if((Integer) mResultsMap.get(PESTICIDE) == defaultPesticide) playAudio(R.raw.selectthepesticide, true); 
			else playAudio(pesticideList.get(((Integer)mResultsMap.get(PESTICIDE))).getAudio(), true); 
		} else if (v.getId() == R.id.dlg_lbl_units_spray) {
			// TODO AUDIO: "Select the unit" default if nothing is in the field
			if((Integer) mResultsMap.get(UNIT) == defaultUnit) playAudio(R.raw.selecttheunits, true); 
			else playAudio(unitList.get(((Integer)mResultsMap.get(UNIT))).getAudio(), true); 
		} else if (v.getId() == R.id.dlg_lbl_unit_no_spray) {
			// TODO AUDIO: "Select the number" default if nothing is in the field
			if(mResultsMap.get(AMOUNT).equals(defaultAmount)) playAudio(R.raw.selecttheunits, true); 
			// TODO AUDIO: Say the number Integer.valueOf(mResultsMap.get(AMOUNT).toString());
			else playAudio(R.raw.problems, true);   
		} else if (v.getId() == R.id.dlg_lbl_day_spray) {
			// TODO AUDIO: "Select the number" default if nothing is in the field
			if(mResultsMap.get(DAY).equals(defaultDay)) playAudio(R.raw.selectthedate, true); 
			// TODO AUDIO: Say the number Integer.valueOf(mResultsMap.get(DAY).toString());
			else playAudio(R.raw.problems, true);  
		} else if (v.getId() == R.id.dlg_lbl_month_spray) {
			// TODO AUDIO: "Select the unit" default if nothing is in the field
			if((Integer) mResultsMap.get(MONTH) == defaultMonth) playAudio(R.raw.choosethemonth, true); 
			else playAudio(monthList.get(((Integer)mResultsMap.get(MONTH))).getAudio(), true); 
		}
		
		// TODO AUDIO: Check the remaining audio
		else if (v.getId() == R.id.aggr_img_help) {
			playAudio(R.raw.spray_help, true);
		}  else if (v.getId() == R.id.prob_spray_tr) {
			playAudio(R.raw.problems, true);
		} else if (v.getId() == R.id.day_spray_tr) {
			playAudio(R.raw.date, true);
		} else if (v.getId() == R.id.units_spray_tr) {
			playAudio(R.raw.amount, true);
		} else if (v.getId() == R.id.pest_spray_tr) {
			playAudio(R.raw.pesticidename, true);
		} else {
			return super.onLongClick(v);
		}

		// shows the help icon.
		showHelpIcon(v);

		return true;
	}

	@Override
	protected Boolean validateForm() {

		// gets all the values to validate.
		mAmount = Integer.valueOf(mResultsMap.get(AMOUNT).toString());
		mDay = Integer.valueOf(mResultsMap.get(DAY).toString());

		// flag used to determine the validity of the form.
		boolean isValid = true;

		if ((Integer)mResultsMap.get(UNIT) != defaultUnit && mAmount > Integer.parseInt(defaultAmount)) {
			highlightField(R.id.units_spray_tr, false);
		} else {
			ApplicationTracker.getInstance().logEvent(EventType.ERROR, UNIT, AMOUNT);
			ApplicationTracker.getInstance().flush();
			isValid = false;
			highlightField(R.id.units_spray_tr, true);
		}

		if ((Integer)mResultsMap.get(PESTICIDE) != defaultPesticide) {
			highlightField(R.id.pest_spray_tr, false);
		} else {
			ApplicationTracker.getInstance().logEvent(EventType.ERROR, PESTICIDE);
			ApplicationTracker.getInstance().flush();
			isValid = false;
			highlightField(R.id.pest_spray_tr, true);
		}

		if ((Integer)mResultsMap.get(PROBLEM) != defaultProblem) {
			highlightField(R.id.prob_spray_tr, false);
		} else {
			ApplicationTracker.getInstance().logEvent(EventType.ERROR, PROBLEM);
			ApplicationTracker.getInstance().flush();
			isValid = false;
			highlightField(R.id.prob_spray_tr, true);
		}

		if ((Integer) mResultsMap.get(MONTH) != defaultMonth && mDay > Integer.parseInt(defaultDay) && validDate(mDay, monthList.get((Integer) mResultsMap.get(MONTH)).getId())) {
			highlightField(R.id.day_spray_tr, false);
		} else {
			ApplicationTracker.getInstance().logEvent(EventType.ERROR, MONTH, DAY);
			ApplicationTracker.getInstance().flush();
			isValid = false;
			highlightField(R.id.day_spray_tr, true);
		}

		// inserts the action if the form is valid.
		if (isValid) {			
			
			ApplicationTracker.getInstance().logEvent(EventType.CLICK, "data entered");
			ApplicationTracker.getInstance().flush();
			
			mProblem = problemList.get((Integer) mResultsMap.get(PROBLEM)).getId();
			mPesticide =  pesticideList.get((Integer) mResultsMap.get(PESTICIDE)).getId();
			mUnit = unitList.get((Integer) mResultsMap.get(UNIT)).getId();
			mMonth = monthList.get((Integer) mResultsMap.get(MONTH)).getId();
			
			long result = mDataProvider.addSprayAction(Global.userId,
					Global.plotId, mProblem, mPesticide, mAmount, mUnit,
					getDate(mDay, mMonth), 0);

			return result != -1;
		}

		return false;
	}
}