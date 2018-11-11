package com.me.diy.calendar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.quartz.Calendar;
import org.quartz.impl.calendar.BaseCalendar;

/**
 * 自定义Calendar
 * HourlyCalender ，我们能用它让排除小时中的一些分钟
 * 
 * @author 清明
 *
 */
public class MyCalendar extends BaseCalendar {
	private static final long serialVersionUID = 1L;
	
	//  Array of Integer from 0 to 59  
	private List<Integer> excludedSeconds = new ArrayList<>();

	public MyCalendar() {
		super();
	}

	public MyCalendar(Calendar baseCalendar) {
		super(baseCalendar);
	}

	public List<Integer> getSecondsExcluded() {
		return excludedSeconds;
	}

	public boolean isSecondExcluded(int second) {
		Iterator<Integer> iter = excludedSeconds.iterator();
		while (iter.hasNext()) {
			Integer excludedSec = iter.next();

			if (second == excludedSec.intValue()) {
				return true;
			}
			continue;
		}
		return false;
	}

	public void setSecondsExcluded(List<Integer> seconds) {
		if (seconds == null) {
			return;
		}
		excludedSeconds.addAll(seconds);
	}

	public void setSecondExcluded(int second) {
		if (isSecondExcluded(second)) {
			return;
		}

		excludedSeconds.add(second);
	}

	public boolean isTimeIncluded(long timeStamp) {
		if (super.isTimeIncluded(timeStamp) == false) {
			return false;
		}

		java.util.Calendar cal = createJavaCalendar(timeStamp);
		int minute = cal.get(java.util.Calendar.SECOND);
		return !(isSecondExcluded(minute));
	}

	public long getNextIncludedTime(long timeStamp) {
		java.util.Calendar cal = createJavaCalendar(timeStamp);
		int second = cal.get(java.util.Calendar.SECOND);

		if (isSecondExcluded(second) == false) {
			return timeStamp;
		}

		while (isSecondExcluded(second) == true) {
			cal.add(java.util.Calendar.SECOND, 1);
		}
		return cal.getTime().getTime();
	}

}
