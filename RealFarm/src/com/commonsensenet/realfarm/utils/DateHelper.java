package com.commonsensenet.realfarm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

import com.commonsensenet.realfarm.R;
import com.commonsensenet.realfarm.dataaccess.RealFarmProvider;

/**
 * Helper functions to handle the date.
 * 
 * @author Oscar Bolanos (oscar.bolanos@epfl.ch)
 * @author Nguyen Lisa
 * 
 */
public class DateHelper {

	/**
	 * Calculates the difference in days between two dates. To do so, it
	 * substracts the milliseconds between each date and then divides this value
	 * by the length of a day in milliseconds.
	 * 
	 * @param dateEarly
	 *            initial date to calculate
	 * @param dateLater
	 *            later date to calculate
	 * 
	 * @return differente in dayts between the given dates
	 */
	public static long calculateDays(Date dateEarly, Date dateLater) {
		return (dateLater.getTime() - dateEarly.getTime())
				/ (24 * 60 * 60 * 1000);
	}

	/**
	 * Formats the date. The format corresponds only to reference dates: today,
	 * yesterday, X days ago and X weeks ago.
	 * 
	 * @param date
	 *            the date to format.
	 * @param context
	 *            application context used for localization.
	 * 
	 * @return a string with the formatted date.
	 */
	public static String formatDate(String date, Context context) {

		try {
			// extracts the date.
			Date dateTime = new SimpleDateFormat(RealFarmProvider.DATE_FORMAT)
					.parse(date);

			// calculates the difference
			long dayDif = calculateDays(dateTime, new Date());

			if (dayDif == 0)
				return context.getString(R.string.dateToday);
			else if (dayDif == 1)
				return context.getString(R.string.dateYesterday);
			else if (dayDif < 15)
				return String.format(context.getString(R.string.dateLastWeek),
						dayDif);
			else {
				return "";
				// return String.format(
				// context.getString(R.string.dateMoreThanAWeek),
				// (int) Math.floor(dayDif / 7));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}

	public static String formatDateShort(String date) {

		try {
			// extracts the date.
			Date dateTime = new SimpleDateFormat(RealFarmProvider.DATE_FORMAT)
					.parse(date);

			return new SimpleDateFormat("dd/MM").format(dateTime);

		} catch (ParseException e) {
			return date;
		}
	}

	public static String formatWithDay(String date) {

		try {

			// parses the giving date using the unified
			// date format.
			Date newDate = new SimpleDateFormat(RealFarmProvider.DATE_FORMAT)
					.parse(date);

			// reformats the date only to extract the day of the week.
			return new SimpleDateFormat("EEEE").format(newDate);

		} catch (ParseException e) {
			return date;
		}
	}

	public static String getDateNow() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(
				RealFarmProvider.DATE_FORMAT);
		formatter.setLenient(true);
		return formatter.format(currentDate.getTime()) + " 00:00:00";
	}

	public static String getDatePast(int offsetDays) {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(
				RealFarmProvider.DATE_FORMAT);
		formatter.setLenient(true);
		currentDate.add(Calendar.DATE, offsetDays);
		return formatter.format(currentDate.getTime()) + " 00:00:00";
	}

	public static long getBeginningYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 01);
		calendar.set(Calendar.MONTH, 00);
		return calendar.getTimeInMillis();
	}

	public static boolean validDate(int day, int month, int year) {
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		try {
			c.set(year, month, day);
			// getTime() will produce an exception if the date is invalid.

			c.getTime();
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}
}
