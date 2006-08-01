//==============================================================================
//file :       $Id$
//project:     corner
//
//last change:	date:       $Date$
//           	by:         $Author$
//           	revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	Beijing Maxinfo Technology Ltd. http://www.bjmaxinfo.com
//license:	Apache License(http://www.apache.org/licenses/LICENSE-2.0)
//==============================================================================

package corner.orm.hibernate.v3;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;

import corner.orm.hibernate.AbstractDateTimeIDGenerator;

/**
 * 一个根据时间来自动生成主键的类。
 * 生成的ID为:prefix+yyyyMMddHHmmssSSS.
 * 
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision$
 * @since 2005-3-3
 */

public class DateTimeIDGenerator extends AbstractDateTimeIDGenerator implements IdentifierGenerator, Configurable {
	private String prefix;
	private static final String PREFIX="prefix";

	/**
	 * @see net.sf.hibernate.id.IdentifierGenerator#generate(net.sf.hibernate.engine.SessionImplementor,
	 *      java.lang.Object)
	 */
	public Serializable generate(SessionImplementor session, Object obj)
			throws  HibernateException {
		String tempTime = getNowTimeFormatted();
		if(this.prefix!=null){
			tempTime=prefix+tempTime;
		}
		return tempTime;
	}
	

	public void configure(Type type, Properties params, Dialect dialect) {
		this.prefix = PropertiesHelper.getString(PREFIX, params, "");
	}
}
