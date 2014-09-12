package org.bluerays.validater;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import org.bluerays.config.Constants;

public class Validate implements Validater {

	public Date date(String date) {
		
		
		if(StringUtils.contains(date, Constants.plus)){
			
			String yyyyMmDD=StringUtils.split(date, Constants.plus)[0];
			String[] yMd=StringUtils.split(yyyyMmDD,Constants.space);
			
			if(yMd.length==2){
				
				String[] yyMd=StringUtils.split(yMd[0], Constants.HIPEN);
				String[] dayTime=StringUtils.split(yMd[1], Constants.colon);
				
				if(yyMd.length==3 && dayTime.length==3){
					
					int yyyy=Integer.parseInt(yyMd[0].trim());
					int MM=Integer.parseInt(yyMd[1].trim())-1;
					int dd=Integer.parseInt(yyMd[2].trim());

					Calendar calendar2=GregorianCalendar.getInstance();
					
					int HH=Integer.parseInt(dayTime[0].trim());
					int mm=Integer.parseInt(dayTime[1].trim());
					int ss=Integer.parseInt(dayTime[2].trim());
					
					calendar2.set(yyyy, MM, dd, HH, mm, ss);
					
					return calendar2.getTime();
				}
				
				
			}else{
				try {
					throw new Exception("In Valid Date Formate");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return null;
	}

	public String message(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
}
