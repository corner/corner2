//==============================================================================
//file :        UpdateSelect.java
//project:      poison-system
//
//last change:	date:       $Date$
//           	by:         $Author$
//           	revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.tapestry.component.select;

import java.util.HashMap;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.services.DataSqueezer;

import corner.service.EntityService;

/**
 * 提供一个供用户选择实体
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @version $Revision$
 * @since 2.1
 */
public abstract class UpdateSelect extends AbstractComponent {

	/**
     * Injected
     * 
     * 
     */
    public abstract IScript getScript();
    /**
     * 得到数据的squeezer
     * @return
     */
    public abstract DataSqueezer getDataSqueezer();
    @InjectObject("spring:entityService")
    public abstract EntityService getEntityService();
	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		PageRenderSupport pageRenderSupport = TapestryUtils.getPageRenderSupport(cycle, this);
		getScript().execute(this,cycle,pageRenderSupport,new HashMap());
		
		String element = getElement();

        if (element == null)
            throw new ApplicationRuntimeException("not defined!", this,
                    null, null);

        boolean rewinding = cycle.isRewinding();

        if (!rewinding)
        {
            writer.begin(element);
            
            
            
            StringBuffer sb=new StringBuffer();
            
            
            sb.append("UpdateForm('"+this.getFormName()+"',new Array(");
            List fields=consturctFields();
            
            for(int i=0;i<fields.size();i++){
            	if(i!=0){
            		sb.append(",");
            	}
            	if(i%2==0)
            		sb.append("'"+fields.get(i)+"'");
            		
            	else if(this.getEntityService().isPersistent(fields.get(i))){
            		sb.append("'"+this.getDataSqueezer().squeeze(fields.get(i))+"'");
            	}else{
            		sb.append("'"+fields.get(i)+"'");
            	}
            		
            }
            sb.append("))");
            writer.attribute("onClick",sb.toString());
            
            writer.attribute("style","cursor:hand;cursor:pointer");

            
            renderInformalParameters(writer, cycle);
        }

        renderBody(writer, cycle);

        if (!rewinding)
        {
            writer.end(element);
        }

        
		
	}
	public abstract String getElement();
	public abstract String getFormName();
	public abstract Object getFields();
	
	public List consturctFields(){
		return (List)getFields();
	}

}
