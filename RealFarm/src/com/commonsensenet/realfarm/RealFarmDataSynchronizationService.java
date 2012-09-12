package com.commonsensenet.realfarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.commonsensenet.realfarm.dataaccess.RealFarmProvider;

public class RealFarmDataSynchronizationService extends BroadcastReceiver {
	private static final String DELIVERED = "SMS_DELIVERED";
	private static final String SENT = "SMS_SENT";

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private RealFarmProvider mDataProvider;
	private BroadcastReceiver mSmsDeliveredReceiver;
	private BroadcastReceiver mSmsSentReceiver;

	@SuppressLint("ParserError")
	@Override
	public void onReceive(Context context, Intent intent) {
		// RealFarmDatabase db = new RealFarmDatabase(context);
		// mDataProvider = new RealFarmProvider(db);

		mDataProvider = RealFarmProvider.getInstance(context);
		Log.d("Received : ", "recvd Msg ..");
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				if (i == 0) {
					// ---get the sender address/phone number---
					str += msgs[i].getOriginatingAddress();
					// str += ": ";
				}
				// ---get the message body---
				str += msgs[i].getMessageBody().toString();
				// System.out.println(10);
				Log.d("Received : ", str);
			}

			// ---display the new SMS message---
			// System.out.print(str);

			Log.d("Processing Msg : ", "Begining to Process ..");

			/*
			 * String[] separated = str.split(" "); String phonesent =
			 * separated[0]; // int phonerecvd= new Integer(phonesent);
			 * Log.d("Senders number: ", phonesent); // 41786798503- 41762066040
			 * // if(separated[0].toString().equalsIgnoreCase("+41762066040")) {
			 * // Log.d("Senders number: ", phonerecvd); //
			 * Log.d("first sep : ", separated[0]); // Log.d("second sep: ",
			 * separated[1]); // Log.d("third sep : ", separated[2]); //
			 * Log.d("fourth sep: ", separated[3]); // Log.d("fifth sep : ",
			 * separated[4]); // Log.d("6 sep: ", separated[5]); //
			 * Log.d("7 sep : ", separated[6]); // Log.d("8 sep: ",
			 * separated[7]);
			 * 
			 * // Sensed Data format - Type, 1, 25,0-10, 2, 25, 0-10, 1, rain,
			 * // val, MP, GN, 3400, SM, SM1, SM2, Cluster // 1- today,
			 * 2-tomorrow // 1 1 25 1 2 25 2 1 rain 2 MP GN 3400 SM 56 23 1 //
			 * Check the type of the message received // 1 = WF, MP, SM; // 2 =
			 * Actions Taken; // 3 = Recommendations; // 4 = Problems and
			 * solutions; // 5 = Updates to users, action types, units, plots,
			 * pesticides, // fertilizer, seeds, stages etc.. String type =
			 * separated[1];
			 * 
			 * int typeval = Integer.valueOf(type); Log.d("Type val: ", type);
			 * 
			 * if (typeval == 1) { // Its a WF, MP, SM message String cond =
			 * separated[4]; Log.d("before int conversion ", "before"); int
			 * condval = Integer.valueOf(cond); Log.d("after int conversion ",
			 * "after"); String type1 = null; switch (condval) { case 0: type1 =
			 * "Chance of rain"; break; case 1: type1 = "Cloudy "; break; case
			 * 2: type1 = "Fog"; break; case 3: type1 = "Sunny"; break; case 4:
			 * type1 = "Rain"; break; case 5: type1 = "Partly Cloudy"; break; }
			 * Log.d("cond val: ", type1); String cond1 = separated[7]; int
			 * condval1 = Integer.valueOf(cond1); String type2 = null; switch
			 * (condval1) { case 0: type2 = "Chance of rain"; break; case 1:
			 * type2 = "Cloudy "; break; case 2: type2 = "Fog"; break; case 3:
			 * type2 = "Sunny"; break; case 4: type2 = "Rain"; break; case 5:
			 * type2 = "Partly Cloudy"; break; } Log.d("cond1 val: ", type2); /*
			 * String cond2=separated[11]; int condval2= new Integer(cond2);
			 * String type3 = null; switch(condval2){ case 0: type3="Sunny";
			 * break; case 1: type3="Clear"; break; case 2: type3="Overcast";
			 * break; case 3: type3="Mostly Cloudy"; break; case 4:
			 * type3="Partly Cloudy"; break; }
			 */

			// Msg to display from www.tititudorancea.com
			/*
			 * String wfmsg = "Today's forecast in CKP is " + type1 +
			 * " with Max Temperature around " + separated[3] +
			 * "\u00b0 C and Tomorrow's forecast is " + type2 +
			 * " with temperature around" + separated[6] +
			 * "\u00b0 C - tititodurecea.com"; Toast.makeText(context, wfmsg,
			 * Toast.LENGTH_SHORT).show(); // Msg to display from Rain gauge in
			 * CKP String wfmsg2 = " In CKP " + separated[10] +
			 * " mm of rain was noticed yesterday."; Toast.makeText(context,
			 * wfmsg2, Toast.LENGTH_SHORT).show(); // Msg for Market Price "" if
			 * (separated[11].toString().equalsIgnoreCase("MP")) { String mpmsg
			 * = "Today's Groundnut price in Pavagada is " + separated[13] +
			 * " Rs"; Toast.makeText(context, mpmsg, Toast.LENGTH_SHORT)
			 * .show(); } if (separated[14].toString().equalsIgnoreCase("SM")) {
			 * String smmsg = "Today's Soil Mositure values are " +
			 * separated[15] + " and " + separated[16] + "% from cluster" +
			 * separated[17]; Toast.makeText(context, smmsg, Toast.LENGTH_SHORT)
			 * .show(); }
			 * 
			 * mDataProvider = RealFarmProvider.getInstance(context);
			 * 
			 * int temperature = Integer.valueOf(separated[3]);
			 * 
			 * mDataProvider.addWeatherForecast(separated[2], temperature,
			 * type1);
			 * 
			 * Log.d("inserted to DB ", "inserrted data");
			 * mDataProvider.getWeatherForecasts(); Log.d("inserted to DB ",
			 * "after inserting data"); }
			 * 
			 * if (typeval == 2) { // For Actions taken a message has to be sent
			 * from the // farmers phone to Server // ActiontypeId, User Id,
			 * localId, Quantity, units, Date, // Crop or plot. // To be on the
			 * server if (separated[2].toString().equalsIgnoreCase("AT")) {
			 * String[] actionsdone = str.split("/"); String atmsg =
			 * "User has performed some actions: localATID " + actionsdone[1] +
			 * "second " + actionsdone[2] + "third " + actionsdone[3];
			 * 
			 * // String atmsg= // "User has performed some actions: localATID "
			 * + // separated[3] + " Gowing ID:  " + separated[4] + //
			 * " Action name id: " // + separated[5] + " quantity " +
			 * separated[6] + // " unit id: " + separated[7] + " quantity 2 " +
			 * // separated[8] + " Date " + separated[9];
			 * Toast.makeText(context, atmsg, Toast.LENGTH_SHORT) .show(); } }
			 * 
			 * if (typeval == 3) { // For adding Recommendations to the user
			 * database }
			 * 
			 * if (typeval == 4) { // For adding problems and solution to the
			 * user database }
			 * 
			 * if (typeval == 5) { // For updating users, action types, units,
			 * plots, // pesticides, fertilizer, seeds, stages... // users=
			 * subtype, first name, last name, mobileno, plotid, // long, lat (
			 * users, plots) // actions= subtype, action name, // units=
			 * subtype, name, value // seeds= subtype, name, days to harvest,
			 * variety, // pesticides, fertilizer= subtype, name, units, stages
			 * // stages= subtype, stagename, value
			 * 
			 * // For users table updations
			 * 
			 * if (separated[2].toString().equalsIgnoreCase("US")) {
			 * Log.d("subtype: ", "in US"); String usmsg =
			 * "Updation to user table Firstname: " + separated[3] +
			 * " Last Name: " + separated[4] + " Mobile No: " + separated[5];//
			 * + // " and his plot longitude and latitude values are " // + //
			 * separated[6] // + ", " + // separated[7]; Toast.makeText(context,
			 * usmsg, Toast.LENGTH_SHORT) .show();
			 * 
			 * mDataProvider.setUserInfo(separated[5], separated[3],
			 * separated[4]); // TODO: why the return value is not stored?
			 * mDataProvider.getUsers();
			 * 
			 * System.out.println("User details is put to database"); //
			 * mDataProvider.setUserInfo(separated[5] ,separated[3], //
			 * separated[4]); // mDataProvider.getUser(); String insertedtodb =
			 * "inseted to DB"; Toast.makeText(context, insertedtodb,
			 * Toast.LENGTH_SHORT).show(); } // For Action type updates
			 * Log.d("subtype: ", separated[2]); Toast.makeText(context, str,
			 * Toast.LENGTH_SHORT).show(); if
			 * (separated[2].toString().equalsIgnoreCase("AC")) { String acmsg =
			 * "Updation to Actions Taken table-  Action name to be added is : "
			 * + separated[3]; Toast.makeText(context, acmsg,
			 * Toast.LENGTH_SHORT) .show(); }
			 * 
			 * if (separated[2].toString().equalsIgnoreCase("UT")) { String
			 * utmsg = "Updation to Units table-  unit name to be added is : " +
			 * separated[3] + " and value is " + separated[4];
			 * Toast.makeText(context, utmsg, Toast.LENGTH_SHORT) .show(); }
			 * 
			 * if (separated[2].toString().equalsIgnoreCase("SE")) { // String
			 * semsg= //
			 * "Updation to seed type table-  seed name to be added is : " // +
			 * separated[3] + "  , days to harvest is " + // separated[4] +
			 * " and variety type is  " + // separated[5];
			 * 
			 * System.out.println("User details is put to database"); //
			 * mDataProvider.setUserInfo(separated[5] //
			 * ,separated[3],separated[4]); // mDataProvider.getUser(); //
			 * String insertedtodb="inseted to DB"; // Toast.makeText(context,
			 * semsg, // Toast.LENGTH_SHORT).show(); }
			 * 
			 * if (separated[2].toString().equalsIgnoreCase("PE")) { String
			 * pemsg =
			 * "Updation to pesticides table-  pesticide name to be added is : "
			 * + separated[3]; Toast.makeText(context, pemsg,
			 * Toast.LENGTH_SHORT) .show(); }
			 * 
			 * if (separated[2].toString().equalsIgnoreCase("FE")) { String
			 * femsg =
			 * "Updation to fertilizer table-  fertilizer name to be added is : "
			 * + separated[3] + "  , stage_id " + separated[4] +
			 * " and units is  " + separated[5]; Toast.makeText(context, femsg,
			 * Toast.LENGTH_SHORT) .show(); } if
			 * (separated[2].toString().equalsIgnoreCase("ST")) { String stmsg =
			 * "Updation to stages table-  stage name to be added is : " +
			 * separated[3]; Toast.makeText(context, stmsg, Toast.LENGTH_SHORT)
			 * .show(); }
			 * /******************************************************
			 * *************************************
			 */

			String[] separated1 = str.split("%");

			// System.out.println("Displaying separated1 of 3 split after %"+separated1[3]);

			// String[] separated2 = separated1[3].split("#");
			// System.out.println("Displaying separated2 split after #"+separated2[0]);
			if ((Integer.valueOf(separated1[1]) == 1000)
					| (Integer.valueOf(separated1[1]) == 1001)
					| (Integer.valueOf(separated1[1]) == 1002)
					| (Integer.valueOf(separated1[1]) == 1003)
					| (Integer.valueOf(separated1[1]) == 1004)) {
				String message_type = separated1[1];
				System.out.println("Displaying length:" + separated1.length);
				for (int i = 2; i < separated1.length; i++) {
					// System.out.println("Displaying i:"+i);

					String[] separated2 = separated1[i].split("#");

					// System.out.println("Displaying length:"+message_type);

					// System.out.println("Displaying msg length:"+Integer.valueOf(message_type));

					if (Integer.valueOf(message_type) == 1000) {
						// actions insertions

						System.out.println("In AT \n");
						Date date1 = null;
						try {
							date1 = dateFormat.parse(separated2[3]);

							System.out.println("try in date" + date1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("catch in date");

						}

						// Long.valueOf(separated2[0]),Long.valueOf(separated2[13]),

						System.out
								.println("date displayed in actions:" + date1);
						long result = mDataProvider.addAction(
								Long.valueOf(separated2[0]),
								Integer.valueOf(separated2[1]),
								Long.valueOf(separated2[2]), date1,
								Integer.valueOf(separated2[4]),
								Integer.valueOf(separated2[5]),
								Double.valueOf(separated2[6]),
								Double.valueOf(separated2[7]),
								Integer.valueOf(separated2[8]),
								Integer.valueOf(separated2[9]),
								Integer.valueOf(separated2[10]),
								Integer.valueOf(separated2[11]),
								Integer.valueOf(separated2[12]),
								Long.valueOf(separated2[13]), 0,
								Integer.valueOf(separated2[14]),
								Long.valueOf(separated2[15]));

						/*
						 * System.out.println(Long.valueOf(separated2[0])+"\n");
						 * System
						 * .out.println(Integer.valueOf(separated2[1])+"\n");
						 * System.out.println(Long.valueOf(separated2[2])+"\n");
						 * System.out.println(date1+"\n");
						 * System.out.println(Integer
						 * .valueOf(separated2[4])+"\n");
						 * System.out.println(Integer
						 * .valueOf(separated2[5])+"\n");
						 * System.out.println(Double
						 * .valueOf(separated2[6])+"\n");
						 * System.out.println(Double
						 * .valueOf(separated2[7])+"\n");
						 * System.out.println(Integer
						 * .valueOf(separated2[8])+"\n");
						 * System.out.println(Integer
						 * .valueOf(separated2[9])+"\n");
						 * System.out.println(Integer
						 * .valueOf(separated2[10])+"\n");
						 * System.out.println(Integer
						 * .valueOf(separated2[11])+"\n");
						 * System.out.println(Integer
						 * .valueOf(separated2[12])+"\n");
						 * System.out.println(Long
						 * .valueOf(separated2[13])+"\n");
						 * System.out.println(Integer
						 * .valueOf(separated2[14])+"\n");
						 * System.out.println(Long
						 * .valueOf(separated2[15])+"\n");
						 */

						System.out.println("result displayed in actions:"
								+ result);

					} // end of actions

					if (Integer.valueOf(message_type) == 1001) {
						// plots insertions
						System.out.println("In PT \n");

						mDataProvider.addPlot(Long.valueOf(separated2[0]),
								Long.valueOf(separated2[1]),
								Integer.valueOf(separated2[2]), separated2[4],
								Integer.valueOf(separated2[3]),
								Double.valueOf(separated2[5]), 0,
								Integer.valueOf(separated2[6]),
								Integer.valueOf(separated2[7]),
								Long.valueOf(separated2[8]),
								Integer.valueOf(separated2[9]));

					}

					if (Integer.valueOf(message_type) == 1002) {
						// users insertions

						System.out.println("In US \n");

						mDataProvider.addUser(separated2[0], separated2[1],
								separated2[2], separated2[3], separated2[4],
								separated2[5], separated2[6], 0,
								Integer.valueOf(separated2[7]),
								Integer.valueOf(separated2[8]),
								Long.valueOf(separated2[9]));
						/*
						 * mDataProvider.addUser("123456789123", "aa", "bb",
						 * "12345678","12345678","farmer_90_px_aa_bb",
						 * "CKPURA",0, 1, 1, 123456789);
						 */
					}

					if (Integer.valueOf(message_type) == 1003) {
						// wf insertions
						System.out.println("In WF \n");
						mDataProvider.addWeatherForecast(separated2[0],
								Integer.valueOf(separated2[1]), separated2[2]);

					}

					if (Integer.valueOf(message_type) == 1004) {

						System.out.println("In MP \n");
						String sep0 = separated2[0];
						int sep1 = Integer.valueOf(separated2[1]);
						int sep2 = Integer.valueOf(separated2[2]);
						String sep3 = separated2[3];
						mDataProvider.addMarketPrice(sep0, sep1, sep2, sep3);

					}

				} // End of for loop
			}

			System.out
					.println("##################DISPLAYING ACTIONS############################");
			mDataProvider.getActions();
			System.out
					.println("##################FINISHED ACTIONS############################");

			System.out
					.println("##################DISPLAYING PLOTS############################");
			mDataProvider.getPlots();
			System.out
					.println("##################FINISHED PLOTS############################");

			System.out
					.println("##################DISPLAYING USERS############################");
			mDataProvider.getUsers();
			System.out
					.println("##################FINISHED USERS############################");

			System.out
					.println("##################DISPLAYING WF############################");
			mDataProvider.getWeatherForecasts();
			System.out
					.println("##################FINISHED WF############################");

			System.out
					.println("##################DISPLAYING MP############################");
			mDataProvider.getMarketPrices();
			System.out
					.println("##################FINISHED MP############################");

			/*******************************************************************************************/

			// }
			/*
			 * if(separated[2].toString().equalsIgnoreCase("WF")) { String
			 * et=separated[4]; int typeval= new Integer(et);
			 * Log.d("Type val: ", et); String type = null; switch(typeval){
			 * case 0: type="Sunny"; break; case 1: type="Clear"; break; case 2:
			 * type="Overcast"; break; case 3: type="Mostly Cloudy"; break; case
			 * 4: type="Partly Cloudy"; break; } //
			 * Log.d("Its a weather forecast:", "WF"); String et1=separated[6];
			 * int typeval1= new Integer(et1); Log.d("Type val: ", et1); String
			 * type1 = null; switch(typeval1){ case 0: type1="Sunny"; break;
			 * case 1: type1="Clear"; break; case 2: type1="Overcast"; break;
			 * case 3: type1="Mostly Cloudy"; break; case 4:
			 * type1="Partly Cloudy"; break; }
			 * 
			 * String wfmsg= "Today's forecast in CKP is " + type +
			 * " with Max Temperature around " + separated[3] +
			 * "\u00b0 C and Tomorrow's forecast is " + type1 +
			 * " with temperature around" + separated[5] + "\u00b0 C";
			 * Toast.makeText(context, wfmsg, Toast.LENGTH_SHORT).show(); } //
			 * Log.d("Im out of WF:", "WF");
			 * if(separated[7].toString().equalsIgnoreCase("MP")) {
			 * Log.d("Its a market price", "MP"); String mpmsg=
			 * "Today's Groundnut price in Pavagada is " + separated[9] + " Rs";
			 * Toast.makeText(context, mpmsg, Toast.LENGTH_SHORT).show(); } //
			 * int len = str.length();
			 * 
			 * // Toast.makeText(context, len, Toast.LENGTH_SHORT).show();
			 */

			/**************************************************************
			 * These lines when commented does not display activity based text
			 * view
			 * */

			// ---stop the SMS message from being broadcasted---
			this.abortBroadcast();

			// ---launch the SMSActivity---
			// Intent mainActivityIntent = new Intent(context,
			// SMSActivity.class);
			// mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(mainActivityIntent);

			// ---send a broadcast intent to update the SMS received in the
			// activity---
			// Intent broadcastIntent = new Intent();
			// broadcastIntent.setAction("SMS_RECEIVED_ACTION");
			// broadcastIntent.putExtra("sms", str);
			// context.sendBroadcast(broadcastIntent);
			/*************************************************************
                         */
		}

	}

	public void onResume() {
		// super.onResume();

		// ---register the receiver---
		// registerReceiver(intentReceiver, intentFilter);
		System.out.println(10);
		Log.d("Sent  : ", "sent Msg ..");
		// ---create the BroadcastReceiver when the SMS is sent---
		mSmsSentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {

			}
		};

		// ---create the BroadcastReceiver when the SMS is delivered---
		mSmsDeliveredReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {

			}
		};

		// ---register the two BroadcastReceivers---
		registerReceiver(mSmsDeliveredReceiver, new IntentFilter(DELIVERED));
		registerReceiver(mSmsSentReceiver, new IntentFilter(SENT));
	}

	private void registerReceiver(BroadcastReceiver smsDeliveredReceiver2,
			IntentFilter intentFilter) {
		// TODO Auto-generated method stub

	}
}