//==============================================================================
//file :       $Id$
//project:     corner
//
//last change:	date:       $Date$
//           	by:         $Author$
//           	revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:		the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �����DateTimeGenerator����.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2005-11-30
 */
public abstract class AbstractDateTimeIDGenerator {
	private static final Log log=LogFactory.getLog(AbstractDateTimeIDGenerator.class);
	protected String upTime = null;
	/* get now time formatted*/
	protected String getNowTimeFormatted() {
		Calendar rightNow = Calendar.getInstance();
		DateFormat formator = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String currentTime = formator.format(rightNow.getTime());
		if (currentTime.equals(upTime)) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
				//log.error(e.getMessage(), e);
			}
			currentTime = getNowTimeFormatted();
		}
		upTime=currentTime;
		return currentTime;
	}
	
}
