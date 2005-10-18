//==============================================================================
//file :        AcegiWorkflowInterceptor.java
//project:      poisoning
//
//last change:	date:       $Date$
//           	by:         $Author$
//           	revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.acegi;

import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.context.security.SecureContext;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springmodules.workflow.osworkflow.OsWorkflowContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �ṩ��osworkflow��caller֧��.
 * <p>
 * ��Ϊÿ��workflow��ʼ��ʱ����Ҫָ��caller,���ô�interceptor֮����Զ԰󶨵�threadlocal��
 * workflow��caller���а󶨡�
 * 
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision$
 * @since 2005-9-12
 */
public class AcegiWorkflowInterceptor implements MethodInterceptor {
	private static final Log log = LogFactory
		.getLog(AcegiWorkflowInterceptor.class);
	/**
	 * �趨workflow��caller.
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {
		SecureContext context = (SecureContext) ContextHolder.getContext();
		
		if (log.isDebugEnabled()) {
			log.debug("get principal from securecontext ["+context.getAuthentication().getPrincipal()+"]");
		}
		OsWorkflowContextHolder.getWorkflowContext().setCaller(
				context.getAuthentication().getPrincipal() + "");
		return mi.proceed();
	}

}
