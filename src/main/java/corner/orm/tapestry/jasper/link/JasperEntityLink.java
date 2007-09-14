/*		
 * Copyright 2007 The Beijing Maxinfo Technology Ltd. 
 * site:http://www.bjmaxinfo.com
 * file : $Id$
 * created at:2007-9-12
 */

package corner.orm.tapestry.jasper.link;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.AbstractLinkComponent;

/**
 * @author <a href=mailto:xf@bjmaxinfo.com>xiafei</a>
 * @version $Revision$
 * @since 2.3.7
 */
public abstract class JasperEntityLink extends AbstractLinkComponent{
	
	/**
	 * @see org.apache.tapestry.link.AbstractLinkComponent#getLink(org.apache.tapestry.IRequestCycle)
	 */
	@Override
	public ILink getLink(IRequestCycle cycle) {
		Object[] parameters = new Object[3];
		parameters[0] = getDownloadFileName();
		parameters[1] = getTaskType().toLowerCase();
		parameters[2] = getTemplate();
		
		return this.getJasperService().getLink(true, parameters);
	}
	
	@Parameter(required = true)
	public abstract String getTemplate();
	
	/**
	 * 指定下载的文件名称
	 */
	@Parameter
	public abstract String getDownloadFileName();
	
	/**
	 * 取得jasper数据生成类型
	 */
	@Parameter(defaultValue = "literal:pdf")
	public abstract String getTaskType();
	
	/**
	 * @return
	 */
	@InjectObject("engine-service:jasper")
	public abstract IEngineService getJasperService();
}
