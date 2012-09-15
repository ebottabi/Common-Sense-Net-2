package com.commonsensenet.realfarm;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.commonsensenet.realfarm.actions.HarvestActionActivity;
import com.commonsensenet.realfarm.actions.ReportActionActivity;
import com.commonsensenet.realfarm.dataaccess.RealFarmProvider;
import com.commonsensenet.realfarm.model.Plot;
import com.commonsensenet.realfarm.view.PlotItemAdapter;

/**
 * Activity that enables the selection of one plot.
 * 
 * @author Oscar Bola�os <@oscarbolanos>
 * @author Nguyen Lisa
 */
public class PlotListActivity extends HelpEnabledActivity implements
		OnItemClickListener, OnItemLongClickListener {

	/** Access to the underlying database of the application. */
	private RealFarmProvider mDataProvider;
	/** ListAdapter used to handle the plots. */
	private PlotItemAdapter mPlotItemAdapter;
	/** ListView where the plots are rendered. */
	private ListView mPlotsListView;
	/** Id of the add action in the ActionBar. */
	private MenuItem mAddItemId;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// enables to add a new plot
		mAddItemId = menu.add("Add New Plot").setIcon(R.drawable.ic_new_plot)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// goes to the add plot page.
		if (item.equals(mAddItemId)) {
			startActivity(new Intent(this, AddPlotActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// sets the layout
		setContentView(R.layout.act_choose_plot);

		// gets the data provider
		mDataProvider = RealFarmProvider.getInstance(this);

		// gets the users from the database.
		List<Plot> plots;
		// harvest and report require the plot to have been sown
		if (Global.selectedAction == HarvestActionActivity.class
				|| Global.selectedAction == ReportActionActivity.class) {
			plots = mDataProvider.getPlotsByUserIdAndEnabledFlagAndHasCrops(
					Global.userId, 1);
		} else {
			plots = mDataProvider.getPlotsByUserIdAndEnabledFlag(Global.userId,
					1);
		}

		mPlotItemAdapter = new PlotItemAdapter(this, plots, mDataProvider);

		// gets the list from the UI.
		mPlotsListView = (ListView) findViewById(R.id.choose_plot_listview_list);
		// enables the focus on the items.
		mPlotsListView.setItemsCanFocus(true);
		// sets the custom adapter.
		mPlotsListView.setAdapter(mPlotItemAdapter);
		// adds the item listeners.
		mPlotsListView.setOnItemClickListener(this);
		mPlotsListView.setOnItemLongClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// gets the selected view using the position
		Plot selectedPlot = mPlotItemAdapter.getItem(position);

		// sets the active plotId
		Global.plotId = selectedPlot.getId();

		// TODO: orbolanos: should open the plot details
		// Soil Moisture should be set from there.
		// loads the target activityRemoved
		if (Global.selectedAction != null) {
			view.getContext().startActivity(
					new Intent(view.getContext(), Global.selectedAction));
			// ensures that back will not reach it.
			PlotListActivity.this.finish();
		}
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO: add help sound based on selected plot
		// Plot selectedPlot = mPlotItemAdapter.getItem(position);
		playAudio(R.raw.may);

		return false;
	}
}
