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

package corner.orm.tapestry.page;

import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.BaseEngine;
import org.apache.tapestry.listener.ListenerMethodInvoker;
import org.apache.tapestry.listener.ListenerMethodInvokerImpl;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import corner.demo.model.one.A;

/**
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @version $Revision$
 * @since 2.2.1
 */
public class PoFormPageTest extends CornerPageTestCase {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testEntityPage() throws Exception{
	    IPage listPage=newMock(IPage.class);
        IRequestCycle cycle = newCycle();
        
        EasyMock.expect(cycle.getListenerParameters()).andReturn(new Object[]{}).anyTimes();
        EasyMock.expect(cycle.getPage("AList")).andReturn(listPage).anyTimes();
        cycle.activate(listPage);
		
        
        replay();
        
        PoFormPage page = (PoFormPage) newInstance(PoFormPage.class,new Object[]{"pageName","AForm","entityService",entityService});
        page.attach(new BaseEngine(), cycle);
        
        
        ListenerMethodInvoker invoker = 
            new ListenerMethodInvokerImpl("doSaveEntityAction", page.getClass().getMethods());
        
        
        
        A a=new A();
        page.setEntity(a);
        int star=entityService.findAll(A.class).size();
        invoker.invokeListenerMethod(page, cycle);
        
        verify();
        assertEquals(star+1,entityService.findAll(A.class).size());
        entityService.deleteEntities(a);
	}
	
	

}
