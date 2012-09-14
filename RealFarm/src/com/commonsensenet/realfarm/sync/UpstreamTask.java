package com.commonsensenet.realfarm.sync;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.buzzbox.mob.android.scheduler.Task;
import com.buzzbox.mob.android.scheduler.TaskResult;
import com.commonsensenet.realfarm.dataaccess.RealFarmProvider;
import com.commonsensenet.realfarm.model.Action;
import com.commonsensenet.realfarm.model.Model;
import com.commonsensenet.realfarm.model.Plot;
import com.commonsensenet.realfarm.model.User;
import com.commonsensenet.realfarm.utils.ApplicationTracker;
import com.commonsensenet.realfarm.utils.ApplicationTracker.EventType;

/**
 * Recurring Task that implements your business logic. The BuzzBox SDK Scheduler
 * will take care of running the doWork method according to the scheduling.
 * 
 */
public class UpstreamTask implements Task {

	/** Identifies when an SMS has been delivered. */
	private static final String DELIVERED = "SMS_DELIVERED";
	/** Identifies when an SMS has been sent. */
	private static final String SENT = "SMS_SENT";
	/** Phone number of the server. */
	public static final String SERVER_PHONE_NUMBER = "9742016861";

	/** List of Actions obtained from the Database. */
	private List<Action> mActionList;
	/** Access to the underlying database. */
	private RealFarmProvider mDataProvider;
	/** Receiver used to detect if an SMS was delivered correctly. */
	private BroadcastReceiver mDeliveredBroadcastReceiver;
	/** List of messages to send to the server. */
	private List<Model> mMessageList;
	/** List of Plots obtained from the Database. */
	private List<Plot> mPlotList;
	/** Receiver used to detect if an SMS was sent correctly. */
	private BroadcastReceiver mSendBroadcastReceiver;
	/** List of Users obtained from the Database. */
	private List<User> mUserList;

	public void registerReceivers(Context context) {
		if (mSendBroadcastReceiver == null) {
			mSendBroadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {

					// gets the extras from the Bundle.
					Bundle extras = arg1.getExtras();

					long id = -1;
					int type = -1;

					// obtains the values from the bundle.
					if (extras != null) {
						id = extras.getLong("id");
						type = extras.getInt("type");
					}

					String resultMessage;

					switch (getResultCode()) {
					case Activity.RESULT_OK:
						resultMessage = "SMS sent";
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						resultMessage = "Generic Failure";
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						resultMessage = "No Service";
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						resultMessage = "Null PDU";
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						resultMessage = "Radio Off";
						break;
					default:
						resultMessage = "Error";
						break;
					}

					// tracks the send notification.
					ApplicationTracker.getInstance().logSyncEvent(
							EventType.SYNC, "SEND-" + resultMessage,
							"type:" + type + ", id:" + id);
					Toast.makeText(arg0, resultMessage, Toast.LENGTH_SHORT)
							.show();

				}
			};

			// registers the send receiver.
			context.registerReceiver(mSendBroadcastReceiver, new IntentFilter(
					SENT));
		}

		if (mDeliveredBroadcastReceiver == null) {
			mDeliveredBroadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context arg0, Intent arg1) {

					// gets the extras from the Bundle.
					Bundle extras = arg1.getExtras();

					long id = -1;
					int type = -1;

					// obtains the values from the bundle.
					if (extras != null) {
						id = extras.getLong("id");
						type = extras.getInt("type");
					}

					// checks the obtained code.
					switch (getResultCode()) {
					case Activity.RESULT_OK:

						// marks the message as delivered.
						updateStatus(type, id, Model.STATUS_CONFIRMED);

						Toast.makeText(arg0, "SMS delivered",
								Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(arg0, "SMS not delivered",
								Toast.LENGTH_SHORT).show();
						break;
					}

					// tracks the sync activity.
					ApplicationTracker.getInstance().logSyncEvent(
							EventType.SYNC, "DELIVERY",
							"type:" + type + ", id:" + id);
				}
			};

			// registers the delivered receiver.
			context.registerReceiver(mDeliveredBroadcastReceiver,
					new IntentFilter(DELIVERED));
		}
	}

	/**
	 * Updates the status of the entry in the database that matches the given
	 * type and id with the new status.
	 * 
	 * @param type
	 *            type of the Model to update.
	 * @param id
	 *            id of the Model to update.
	 * @param status
	 *            new status. It can be 0 (unsent), 1(sent) or 2(confirmed).
	 */
	private void updateStatus(int type, long id, int status) {

		switch (type) {
		case 1000: // Action
			// changes the status of the action
			mDataProvider.setActionStatus(id, status);
			break;
		case 1001: // Plot
			// changes the status of the plot
			mDataProvider.setPlotStatus(id, status);
			break;
		case 1002: // User
			// changes the status of the user.
			mDataProvider.setUserStatus(id, status);
			break;
		}
	}

	public TaskResult doWork(ContextWrapper ctx) {
		TaskResult res = new TaskResult();

		// adds the Broadcast receivers if needed.
		registerReceivers(ctx.getApplicationContext());

		// gets the database provider.
		mDataProvider = RealFarmProvider.getInstance(ctx);

		// gets all the data from the server that has not being sent.
		mActionList = mDataProvider.getActionsBySendStatus(Model.STATUS_UNSENT);
		mPlotList = mDataProvider.getPlotsBySendStatus(Model.STATUS_UNSENT);
		mUserList = mDataProvider.getUsersBySendStatus(Model.STATUS_UNSENT);

		// initializes the list used to send the messages.
		mMessageList = new ArrayList<Model>();

		// adds all the elements into the message list.
		mMessageList.addAll(mUserList);
		mMessageList.addAll(mPlotList);
		mMessageList.addAll(mActionList);

		// sends all the messages to the server via SMS.
		for (int i = 0; i < mMessageList.size(); i++) {
			// sends the message.
			sendMessage(ctx, mMessageList.get(i));
		}

		// sets the flag for the sent actions.
		for (int x = 0; x < mActionList.size(); x++) {
			mDataProvider.setActionStatus(mActionList.get(x).getId(),
					Model.STATUS_SENT);
		}
		// sets the flag for the sent plots.
		for (int x = 0; x < mPlotList.size(); x++) {
			mDataProvider.setPlotStatus(mPlotList.get(x).getId(),
					Model.STATUS_SENT);
		}
		// sets the flag for the sent users.
		for (int x = 0; x < mUserList.size(); x++) {
			mDataProvider.setUserStatus(mUserList.get(x).getId(),
					Model.STATUS_SENT);
		}

		return res;
	}

	public String getId() {
		return "reminder";
	}

	public String getTitle() {
		return "Reminder";
	}

	protected void sendMessage(ContextWrapper context, Model message) {

		Intent sentIntent = new Intent(SENT);
		sentIntent.putExtra("type", message.getModelTypeId());
		sentIntent.putExtra("id", message.getId());
		PendingIntent sentPI = PendingIntent.getBroadcast(
				context.getApplicationContext(), 0, sentIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent deliveredIntent = new Intent(DELIVERED);
		deliveredIntent.putExtra("type", message.getModelTypeId());
		deliveredIntent.putExtra("id", message.getId());
		PendingIntent deliveredPI = PendingIntent.getBroadcast(
				context.getApplicationContext(), 0, deliveredIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// gets the data to send.
		String sms = message.toSmsString();

		// tracks that the data that has been sent to the Server.
		ApplicationTracker.getInstance().logSyncEvent(EventType.SYNC,
				"UPSTREAM", sms);

		// gets the manager in charge of sending SMS.
		SmsManager sm = SmsManager.getDefault();
		// sends the messages from the phone number
		sm.sendTextMessage(SERVER_PHONE_NUMBER, null, sms, sentPI, deliveredPI);
	}
}
