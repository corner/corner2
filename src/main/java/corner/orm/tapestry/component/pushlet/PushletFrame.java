//==============================================================================
// file :       $Id$
// project:     corner
//
// last change: date:       $Date$
//              by:         $Author$
//              revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	Beijing Maxinfo Technology Ltd. http://www.bjmaxinfo.com
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.tapestry.component.pushlet;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectScript;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * @author <a href="mailto:Ghostbb@bjmaxinfo.com">Ghostbb</a>
 * @version $Revision$
 * @since 2.5
 */
public abstract class PushletFrame extends AbstractComponent implements IDirect{
	
	/**
	 * 查询消息使用的消息实体名称
	 * @return 保存消息的实体类名
	 */
	@Parameter(required=true)
	public abstract String getMessageClassName();
	
    @InjectObject("service:corner.pushlet.PushletService")
    public abstract IEngineService getPushletService();
    
	@InjectScript("PushletFrame.script")
	public abstract IScript getScript();
	
	/**
	 * @see org.apache.tapestry.IDirect#trigger(org.apache.tapestry.IRequestCycle)
	 */
	public void trigger(IRequestCycle cycle) {
		
	}
	
	/**
	 * @see org.apache.tapestry.AbstractComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
	 */
	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		
		boolean rewinding = cycle.isRewinding();
	   if (!rewinding)
	    {
		   writer.begin("iframe");
		   writer.appendAttribute("id", "PublishFrame");
		   writer.appendAttribute("name", "PublishFrame");
		   ILink link = getPushletService().getLink(true, new DirectServiceParameter(this,new Object[]{this.getMessageClassName()}));
		   writer.appendAttribute("src", link.getAbsoluteURL());
		   writer.appendAttribute("style", "visibility: hidden; width: 0px; height: 0px; border: 0px;");
		   writer.end("iframe");
		   
//	        Map<String,Object> parms = new HashMap<String,Object>();
	        getScript().execute(this, cycle, TapestryUtils.getPageRenderSupport(cycle, this), null);
	    }

	}

}
