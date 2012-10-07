package com.commonsensenet.realfarm;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.commonsensenet.realfarm.dataaccess.RealFarmDatabase;
import com.commonsensenet.realfarm.model.Resource;
import com.commonsensenet.realfarm.model.aggregate.AggregateItem;
import com.commonsensenet.realfarm.utils.ActionDataFactory;
import com.commonsensenet.realfarm.utils.ApplicationTracker;
import com.commonsensenet.realfarm.utils.ApplicationTracker.EventType;

public class MarketPriceActivity extends AggregateMarketActivity implements
		OnLongClickListener {

	/** Maximum market price. */
	private int mMax = 0;
	/** Minimum market price. */
	private int mMin = 0;

	protected void makeAudioAggregateMarketItem(AggregateItem item,
			boolean header) {

		int variety = mTopSelectorData.getAudio();
		int days = mDaysSelectorData.getAudio();
		int number = item.getNews();
		long min = item.getSelector3();
		long max = item.getSelector2();
		int kg = mDataProvider.getResourceImageById(item.getSelector1(),
				RealFarmDatabase.TABLE_NAME_UNIT,
				RealFarmDatabase.COLUMN_NAME_UNIT_AUDIO);

		addToSoundQueue(R.raw.last);
		addToSoundQueue(days);
		addToSoundQueue(R.raw.days_paid_price_touch_here);
		playSound();

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.act_marketprice);

		// indicates that should obtain market prices.
		mCurrentAction = TopSelectorActivity.LIST_WITH_TOP_SELECTOR_TYPE_MARKET;

		// obtains the market price values.
		mMin = mDataProvider
				.getLimitPrice(RealFarmDatabase.COLUMN_NAME_MARKETPRICE_MIN);
		mMax = mDataProvider
				.getLimitPrice(RealFarmDatabase.COLUMN_NAME_MARKETPRICE_MAX);

		TextView tw = (TextView) findViewById(R.id.max_price);
		tw.setText(String.valueOf(mMax));
		tw = (TextView) findViewById(R.id.min_price);
		tw.setText(String.valueOf(mMin));

		// default seed/crop type id
		mTopSelectorData = ActionDataFactory.getTopSelectorData(mActionTypeId,
				mDataProvider, Global.userId);
		// default 7 days.
		mDaysSelectorData = mDataProvider.getResources(
				RealFarmDatabase.RESOURCE_TYPE_DAYS_SPAN).get(0);

		// shows the list of available prices.
		setList();

		// final View crop = findViewById(R.id.aggr_crop);
		final View daySelectorRow = findViewById(R.id.days_selector_row);
		final View marketInfo = findViewById(R.id.market_info);
		final View daysSelector = findViewById(R.id.selector_days);
		Button back = (Button) findViewById(R.id.button_back);

		daySelectorRow.setOnLongClickListener(this);
		daysSelector.setOnLongClickListener(this);
		marketInfo.setOnLongClickListener(this);
		back.setOnLongClickListener(this);
		// crop.setOnLongClickListener(this);

		marketInfo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// tracks the application usage.
				ApplicationTracker.getInstance().logEvent(EventType.CLICK,
						Global.userId, getLogTag(),
						getResources().getResourceEntryName(v.getId()));
			}
		});

		daySelectorRow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// tracks the application usage.
				ApplicationTracker.getInstance().logEvent(EventType.CLICK,
						Global.userId, getLogTag(),
						getResources().getResourceEntryName(v.getId()));
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});

		// crop.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		//
		// ApplicationTracker.getInstance().logEvent(EventType.CLICK,
		// Global.userId, getLogTag(),
		// getResources().getResourceEntryName(v.getId()));
		//
		// final ImageView img_1 = (ImageView) findViewById(R.id.aggr_crop_img);
		// List<Resource> data = ActionDataFactory.getTopSelectorList(
		// mActionTypeId, mDataProvider);
		// displayDialog(v, data, "Select the variety",
		// R.raw.select_the_variety, img_1, 2);
		// }
		// });

		daysSelector.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				ApplicationTracker.getInstance().logEvent(EventType.CLICK,
						Global.userId, getLogTag(),
						getResources().getResourceEntryName(v.getId()));

				List<Resource> data = mDataProvider
						.getResources(RealFarmDatabase.RESOURCE_TYPE_DAYS_SPAN);
				displayDialog(v, data, "Select the time span", R.raw.problems,
						null, 1);
			}
		});
	}

	public boolean onLongClick(View v) {

		ApplicationTracker.getInstance().logEvent(EventType.LONG_CLICK,
				Global.userId, getLogTag(),
				getResources().getResourceEntryName(v.getId()));

		if (v.getId() == R.id.aggr_crop) {
			playAudio(mTopSelectorData.getAudio(), true);
		} else if (v.getId() == R.id.market_info) {
			addToSoundQueue(R.raw.chal_max_price);
			playInteger(mMax);
			addToSoundQueue(R.raw.rupees_every_quintal);
			addToSoundQueue(R.raw.chal_min_price);
			playInteger(mMin);
			addToSoundQueue(R.raw.rupees_every_quintal);
			playSound();
		} else if (v.getId() == R.id.button_back) {
			playAudio(R.raw.back_button, true);
		} else if (v.getId() == R.id.selector_days) {
			playAudio(mDaysSelectorData.getAudio(), true);
		} else if (v.getId() == R.id.days_selector_row) {
			playAudio(R.raw.select_time_span, true);
		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.equals(mHelpItem)) {

			// tracks the application usage
			ApplicationTracker.getInstance().logEvent(EventType.CLICK,
					Global.userId, getLogTag(), item.getTitle());
			playAudio(R.raw.mp_help, true);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
