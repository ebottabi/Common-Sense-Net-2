package com.commonsensenet.realfarm.actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.commonsensenet.realfarm.HelpEnabledActivityOld;
import com.commonsensenet.realfarm.Homescreen;
import com.commonsensenet.realfarm.R;
import com.commonsensenet.realfarm.control.NumberPicker;
import com.commonsensenet.realfarm.dataaccess.RealFarmProvider;
import com.commonsensenet.realfarm.model.DialogData;
import com.commonsensenet.realfarm.utils.ApplicationTracker;
import com.commonsensenet.realfarm.utils.ApplicationTracker.EventType;
import com.commonsensenet.realfarm.utils.SoundQueue;
import com.commonsensenet.realfarm.view.DialogAdapter;
import com.commonsensenet.realfarm.view.DialogArrayLists;

public class action_selling extends HelpEnabledActivityOld {

	private final action_selling parentReference = this;
	private int sell_no;
	private String units_sell = "0";
	private String crop_sell = "0";
	private int date_sel;
	private String months_harvest = "0";
	private int sell_price;
	private int sell_no_rem = -1;
	private String units_rem_sell = "0";
	private HashMap<String, String> resultsMap;
	private RealFarmProvider mDataProvider;

	public static final String LOG_TAG = "action_selling";

	protected void cancelaudio() {

		playAudio(R.raw.cancel);

		Intent adminintent = new Intent(action_selling.this, Homescreen.class);

		startActivity(adminintent);
		action_selling.this.finish();
	}

	public void onBackPressed() {

		SoundQueue.getInstance().stop();

		Intent adminintent = new Intent(action_selling.this, Homescreen.class);

		startActivity(adminintent);
		action_selling.this.finish();

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState, R.layout.selling_dialog);
		setHelpIcon(findViewById(R.id.helpIndicator));
		
		mDataProvider = RealFarmProvider.getInstance(this);

		System.out.println("selling done");

		playAudio(R.raw.clickingselling);
		
		resultsMap = new HashMap<String, String>();
		resultsMap.put("crop_sell", "0");
		resultsMap.put("months_harvest", "0");
		resultsMap.put("units_sell", "0");
		resultsMap.put("units_rem_sell", "0");
		resultsMap.put("date_sel", "0");
		resultsMap.put("sell_no", "0");
		resultsMap.put("sell_price", "0");
		resultsMap.put("sell_no_rem", "0");

		// bg_day_sow.setImageResource(R.drawable.empty_not);

		final Button item1 = (Button) findViewById(R.id.home_btn_crop_sell);
		final Button item2 = (Button) findViewById(R.id.home_btn_date_sell);
		final Button item3 = (Button) findViewById(R.id.home_btn_month_sell);
		final Button item4 = (Button) findViewById(R.id.home_btn_units_no_sell);
		final Button item5 = (Button) findViewById(R.id.home_btn_units_sell);
		final Button item6 = (Button) findViewById(R.id.home_btn_price_sell);
		final Button item7 = (Button) findViewById(R.id.home_btn_units_no_rem_sell);
		final Button item8 = (Button) findViewById(R.id.home_btn_units_rem_sell);

		final ImageButton home = (ImageButton) findViewById(R.id.aggr_img_home);
		final ImageButton help = (ImageButton) findViewById(R.id.aggr_img_help);

		item1.setOnLongClickListener(this);
		item2.setOnLongClickListener(this);
		item3.setOnLongClickListener(this);
		item4.setOnLongClickListener(this);
		item5.setOnLongClickListener(this);
		item6.setOnLongClickListener(this);
		item7.setOnLongClickListener(this);
		item8.setOnLongClickListener(this);
		help.setOnLongClickListener(this);

		final TableRow crop;
		final TableRow date;
		final TableRow quantity;
		final TableRow priceperquint;
		final TableRow remain;

		crop = (TableRow) findViewById(R.id.crop_sell_tr);
		date = (TableRow) findViewById(R.id.date_sell_tr);

		quantity = (TableRow) findViewById(R.id.quant_sell_tr);
		priceperquint = (TableRow) findViewById(R.id.price_sell_tr);
		remain = (TableRow) findViewById(R.id.rem_quant_sell_tr);

		crop.setOnLongClickListener(this);
		date.setOnLongClickListener(this);
		quantity.setOnLongClickListener(this);
		priceperquint.setOnLongClickListener(this);
		remain.setOnLongClickListener(this);

		item1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopaudio();
				
				ArrayList<DialogData> m_entries = mDataProvider.getCropsThisSeason();
				displayDialog(v, m_entries, "crop_sell", "Select the crop", R.raw.problems, R.id.dlg_lbl_crop_sell, R.id.crop_sell_tr);

			}
		});

		item2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopaudio();
				Log.d("in variety sowing dialog", "in dialog");
				
				displayDialogNP("Choose the day", "date_sel", R.raw.dateinfo, 1, 31, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 1, 0, R.id.dlg_lbl_date_sell, R.id.date_sell_tr, R.raw.dateinfo, R.raw.dateinfo, R.raw.dateinfo, R.raw.dateinfo);
			}
		});

		item3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopaudio();
				Log.d("in variety sowing dialog", "in dialog");
				
				ArrayList<DialogData> m_entries = DialogArrayLists.getMonthArray(v);
				displayDialog(v, m_entries, "months_harvest", "Select the month", R.raw.bagof50kg, R.id.dlg_lbl_month_sell, R.id.date_sell_tr);

			}

		});


		item4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopaudio();
				Log.d("in variety sowing dialog", "in dialog");
				
				displayDialogNP("Choose the number of bags", "sell_no", R.raw.dateinfo, 0, 200, 0, 1, 0, R.id.dlg_lbl_unit_no_sell, R.id.quant_sell_tr, R.raw.dateinfo, R.raw.dateinfo, R.raw.dateinfo, R.raw.dateinfo);
			}
		});

		item5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopaudio();
				Log.d("in units sow dialog", "in dialog");
				
				final ArrayList<DialogData> m_entries = DialogArrayLists.getItUnitsArray(v, 20, 51, 1);
				displayDialog(v, m_entries, "units_sell", "Select the unit", R.raw.problems, R.id.dlg_lbl_unit_sell, R.id.quant_sell_tr);

			}
		});

		item6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopaudio();

				displayDialogNP("Enter the price", "sell_price", R.raw.dateinfo, 0, 9999, 3200, 50, 0, R.id.dlg_lbl_price_sell, R.id.price_sell_tr, R.raw.dateinfo, R.raw.dateinfo, R.raw.dateinfo, R.raw.dateinfo);
			}
		});

		final TextView no_text3 = (TextView) findViewById(R.id.dlg_lbl_unit_no_rem_sell);

		item7.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopaudio();
				Log.d("in variety sowing dialog", "in dialog");
				
				displayDialogNP("Choose the number of bags", "sell_no_rem", R.raw.dateinfo, 0, 200, 0, 1, 0, R.id.dlg_lbl_unit_no_rem_sell, R.id.rem_quant_sell_tr, R.raw.dateinfo, R.raw.dateinfo, R.raw.dateinfo, R.raw.dateinfo);

			}
		});

		item8.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopaudio();
				Log.d("in units sow dialog", "in dialog");
				
				final ArrayList<DialogData> m_entries = DialogArrayLists.getItUnitsArray(v, 20, 51, 1);
				displayDialog(v, m_entries, "units_rem_sell", "Select the unit", R.raw.problems, R.id.dlg_lbl_unit_rem_sell, R.id.quant_sell_tr);

			}
		});

		Button btnNext = (Button) findViewById(R.id.sell_ok);
		Button cancel = (Button) findViewById(R.id.sell_cancel);

		btnNext.setOnLongClickListener(this); // Integration
		cancel.setOnLongClickListener(this);

		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancelaudio();
			}

		});

		btnNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				crop_sell = resultsMap.get("crop_sell");
				months_harvest = resultsMap.get("months_harvest");
				units_sell = resultsMap.get("units_sell");
				units_rem_sell = resultsMap.get("units_rem_sell");
				date_sel = Integer.parseInt(resultsMap.get("date_sel"));
				sell_no = Integer.parseInt(resultsMap.get("sell_no"));
				sell_price = Integer.parseInt(resultsMap.get("sell_price"));
				sell_no_rem = Integer.parseInt(resultsMap.get("sell_no_rem"));
				
				int flag1, flag2, flag3, flag4, flag5;

				if (crop_sell.toString().equalsIgnoreCase("0")) {

					flag1 = 1;

					TableRow tr_feedback = (TableRow) findViewById(R.id.crop_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img_not);

				} else {

					flag1 = 0;

					TableRow tr_feedback = (TableRow) findViewById(R.id.crop_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img);
				}

				if (months_harvest.toString().equalsIgnoreCase("0")
						|| date_sel == 0) {

					flag2 = 1;

					TableRow tr_feedback = (TableRow) findViewById(R.id.date_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img_not);

				} else {

					flag2 = 0;

					TableRow tr_feedback = (TableRow) findViewById(R.id.date_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img);
				}

				if (units_sell.toString().equalsIgnoreCase("0") || sell_no == 0) {

					flag3 = 1;

					TableRow tr_feedback = (TableRow) findViewById(R.id.quant_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img_not);

				} else {

					flag3 = 0;

					TableRow tr_feedback = (TableRow) findViewById(R.id.quant_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img);
				}

				if (sell_price == 0) {
					flag4 = 1;

					TableRow tr_feedback = (TableRow) findViewById(R.id.price_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img_not);

				} else {
					flag4 = 0;

					TableRow tr_feedback = (TableRow) findViewById(R.id.price_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img);
				}

				if (units_rem_sell.toString().equalsIgnoreCase("0")
						|| sell_no_rem == -1) {

					flag5 = 1;

					TableRow tr_feedback = (TableRow) findViewById(R.id.rem_quant_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img_not);

				} else {

					flag5 = 0;

					TableRow tr_feedback = (TableRow) findViewById(R.id.rem_quant_sell_tr);

					tr_feedback.setBackgroundResource(R.drawable.def_img);
				}

				if (flag1 == 0 && flag2 == 0 && flag3 == 0 && flag4 == 0
						&& flag5 == 0) {

					Intent adminintent = new Intent(action_selling.this,
							Homescreen.class);

					startActivity(adminintent);
					action_selling.this.finish();
					okaudio();

				} else {
					initmissingval();
				}

			}
		});

		home.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent adminintent = new Intent(action_selling.this,
						Homescreen.class);

				startActivity(adminintent);
				action_selling.this.finish();

			}
		});

	}

	@Override
	public boolean onLongClick(View v) {
		if (v.getId() == R.id.home_btn_month_sell) {

			playAudioalways(R.raw.choosethemonth);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.quant_sell_tr) {

			playAudioalways(R.raw.quantity);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.crop_sell_tr) {

			playAudioalways(R.raw.crop);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.date_sell_tr) {

			playAudioalways(R.raw.date);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.price_sell_tr) {

			playAudioalways(R.raw.priceperquintal);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.rem_quant_sell_tr) {

			playAudioalways(R.raw.remaining);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.home_btn_crop_sell) {

			playAudioalways(R.raw.crop);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.home_btn_date_sell) {

			playAudioalways(R.raw.date);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.home_btn_month_sell) {

			playAudioalways(R.raw.choosethemonth);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.home_btn_units_no_sell) {

			playAudioalways(R.raw.noofbags);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.home_btn_units_sell) {
			playAudioalways(R.raw.keygis);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.home_btn_price_sell) {

			playAudioalways(R.raw.value);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.home_btn_units_no_rem_sell) {

			playAudioalways(R.raw.noofbags);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.home_btn_units_rem_sell) {

			playAudioalways(R.raw.keygis);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.aggr_img_help) {

			playAudioalways(R.raw.help);
			ShowHelpIcon(v);
		}

		if (v.getId() == R.id.sell_ok) {

			playAudioalways(R.raw.ok);
			ShowHelpIcon(v);

		}
		if (v.getId() == R.id.sell_cancel) {

			playAudioalways(R.raw.cancel);
			ShowHelpIcon(v);

		}

		return true;
	}
	
	private void displayDialog(View v, final ArrayList<DialogData> m_entries, final String mapEntry, final String title, int entryAudio, final int varText, final int trFeedback){ 
		final Dialog dialog = new Dialog(v.getContext());
		dialog.setContentView(R.layout.mc_dialog);
		dialog.setTitle(title);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		DialogAdapter m_adapter = new DialogAdapter(v.getContext(), R.layout.mc_dialog_row, m_entries);
		ListView mList = (ListView)dialog.findViewById(R.id.liste);
		mList.setAdapter(m_adapter);

		dialog.show();
		playAudio(entryAudio); // TODO: onOpen

		mList.setOnItemClickListener(new OnItemClickListener(){ // TODO: adapt the audio in the db
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Does whatever is specific to the application
				Log.d("var "+position+" picked ", "in dialog");
				TextView var_text = (TextView) findViewById(varText);
				DialogData choice = m_entries.get(position);
				var_text.setText(choice.getName());
				resultsMap.put(mapEntry, choice.getValue());  
				TableRow tr_feedback = (TableRow) findViewById(trFeedback);
				tr_feedback.setBackgroundResource(android.R.drawable.list_selector_background);

				// tracks the application usage.
				ApplicationTracker.getInstance().logEvent(
						EventType.CLICK, LOG_TAG, title,
						choice.getValue());
				
				Toast.makeText(parentReference, resultsMap.get(mapEntry), Toast.LENGTH_SHORT).show();
						
				// onClose
				dialog.cancel();
				int iden = choice.getAudioRes();
				//view.getContext().getResources().getIdentifier("com.commonsensenet.realfarm:raw/" + choice.getAudio(), null, null);
				playAudio(iden);
			}});

		mList.setOnItemLongClickListener(new OnItemLongClickListener(){

			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { // TODO: adapt the audio in the db
				int iden = m_entries.get(position).getAudioRes();
				//view.getContext().getResources().getIdentifier("com.commonsensenet.realfarm:raw/" + m_entries.get(position).getAudio(), null, null);
				playAudioalways(iden);
				return true;
			}});
	}
	
	private void displayDialogNP(String title, final String mapEntry, int openAudio, double min, double max, double init, double inc, int nbDigits, int textField, int tableRow, final int okAudio, final int cancelAudio, final int infoOkAudio, final int infoCancelAudio){ 

		final Dialog dialog = new Dialog(parentReference);
		dialog.setTitle(title);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		playAudio(openAudio); // opening audio
		
		if(!resultsMap.get(mapEntry).equals("0") && !resultsMap.get(mapEntry).equals("-1")) init = Double.valueOf(resultsMap.get(mapEntry));

		NumberPicker np = new NumberPicker(parentReference, min, max, init, inc, nbDigits);
		dialog.setContentView(np);
		
		final TextView tw_sow = (TextView) findViewById(textField);
		final TableRow tr_feedback = (TableRow) findViewById(tableRow);

		final TextView tw = (TextView)dialog.findViewById(R.id.tw);
		ImageButton ok = (ImageButton)dialog.findViewById(R.id.ok);
		ImageButton cancel = (ImageButton)dialog.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener(){ 
			public void onClick(View view) {
				String result = tw.getText().toString(); 
				resultsMap.put(mapEntry, result); 
				tw_sow.setText(result);
				tr_feedback.setBackgroundResource(android.R.drawable.list_selector_background);
				Toast.makeText(parentReference , result, Toast.LENGTH_LONG).show();
				dialog.cancel();
				playAudio(okAudio); // ok audio
		}});
        cancel.setOnClickListener(new View.OnClickListener(){ 
			public void onClick(View view) {
				dialog.cancel();
				playAudio(cancelAudio); // cancel audio
				ApplicationTracker.getInstance().logEvent(EventType.CLICK, LOG_TAG, "amount", "cancel");
		}});
        ok.setOnLongClickListener(new View.OnLongClickListener(){ 
			public boolean onLongClick(View view) {
				playAudio(infoOkAudio); // info audio
				return true;
		}});
        cancel.setOnLongClickListener(new View.OnLongClickListener(){ 
			public boolean onLongClick(View view) {
				playAudio(infoCancelAudio); // info audio
				return true;
		}});
        tw.setOnLongClickListener(new View.OnLongClickListener(){ 
			public boolean onLongClick(View view) {
				String num = tw.getText().toString();
				playAudio(R.raw.dateinfo); // info audio
				return false;
		}});
        				
		dialog.show();
	}
}