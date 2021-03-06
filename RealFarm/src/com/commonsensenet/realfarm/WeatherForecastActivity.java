package com.commonsensenet.realfarm;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.commonsensenet.realfarm.dataaccess.RealFarmProvider.OnWeatherForecastDataChangeListener;
import com.commonsensenet.realfarm.model.WeatherForecast;
import com.commonsensenet.realfarm.model.WeatherType;
import com.commonsensenet.realfarm.utils.ApplicationTracker;
import com.commonsensenet.realfarm.utils.ApplicationTracker.EventType;
import com.commonsensenet.realfarm.view.WeatherForecastItemAdapter;

public class WeatherForecastActivity extends HelpEnabledActivity implements
		OnWeatherForecastDataChangeListener, OnItemLongClickListener,
		OnItemClickListener {

	/** Celsius indicator. */
	public static final String CELSIUS = "�";
	/** ListAdapter used to handle the weather forecasts. */
	private WeatherForecastItemAdapter mWeatherForecastItemAdapter;
	/** ListView where the weather forecasts are rendered. */
	private ListView mWeatherForecastListView;
	/** WeatherForecasts obtained from the Database. */
	private List<WeatherForecast> mWeatherForecasts;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.act_weather_forecast);

		// sets the data change listener in case the weather information is
		// updated.
		mDataProvider.setWeatherForecastDataChangeListener(this);

		// gets the forecast from the database for today.
		mWeatherForecasts = mDataProvider.getWeatherForecasts(new Date());

		if (mWeatherForecasts == null || mWeatherForecasts.size() == 0)
			playAudio(R.raw.no_wf);

		// creates the adapter used to manage the data.
		mWeatherForecastItemAdapter = new WeatherForecastItemAdapter(this,
				mWeatherForecasts, mDataProvider);

		// gets the list from the UI.
		mWeatherForecastListView = (ListView) findViewById(R.id.weather_forecast_list);
		// enables the focus on the items.
		mWeatherForecastListView.setItemsCanFocus(true);
		// sets the custom adapter.
		mWeatherForecastListView.setAdapter(mWeatherForecastItemAdapter);
		// adds the long click to enable the help feature
		mWeatherForecastListView.setOnItemClickListener(this);
		mWeatherForecastListView.setOnItemLongClickListener(this);
	}

	public void onDataChanged(String date, int temperature, int weatherTypeId) {

	}

	// TODO: this is not implemented. If a new weather is added it should be
	// displayed in the ui
	public void onDataChanged(String date, int temperature, String type) {
		// item should be added to the adapter.
	}

	protected void onDestroy() {
		// removes the listener
		mDataProvider.setWeatherForecastDataChangeListener(null);
		mDataProvider = null;
		super.onDestroy();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ApplicationTracker.getInstance().logEvent(EventType.CLICK,
				Global.userId, getLogTag(),
				mWeatherForecasts.get(position).getDate());
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		ApplicationTracker.getInstance().logEvent(EventType.LONG_CLICK,
				Global.userId, getLogTag(),
				mWeatherForecasts.get(position).getDate());

		// stops any sound that could be playing.
		stopAudio();

		// gets the selected forecast
		WeatherForecast wf = mWeatherForecastItemAdapter.getItem(position);
		WeatherType wt = mDataProvider
				.getWeatherTypeById(wf.getWeatherTypeId());

		String date1 = wf.getDate();
		String[] separated1 = date1.split("-");
		playInteger(Integer.valueOf(separated1[2]));
		playInteger(Integer.valueOf(separated1[1]));
		playInteger(Integer.valueOf(separated1[0]));
		addToSoundQueue(R.raw.forecastss);
		addToSoundQueue(wt.getAudio());
		addToSoundQueue(R.raw.and);
		addToSoundQueue(R.raw.max_temp);
		playInteger(wf.getTemperature());
		addToSoundQueue(R.raw.degree_centigrade);

		playSound();

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.equals(mHelpItem)) {

			// tracks the application usage
			ApplicationTracker.getInstance().logEvent(EventType.CLICK,
					Global.userId, getLogTag(), "help");
			playAudio(R.raw.wf_help, true);

			return true;
		} else { // asks the parent.
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onPause() {
		super.onPause();

		// removes the data change listener.
		mDataProvider.setWeatherForecastDataChangeListener(null);
	}

	protected void onResume() {
		super.onResume();

		// sets the data change listener in case the weather information is
		// updated.
		mDataProvider.setWeatherForecastDataChangeListener(this);
	}
}
