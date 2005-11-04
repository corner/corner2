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

package corner.orm.tapestry.page;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.html.BasePage;

import corner.service.BaseService;
import corner.util.BeanUtils;

public abstract class EntityFormPage<T> extends BasePage implements EntityPage<T>{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(EntityFormPage.class);

	private Class<T> clazz;

	private String keyName;

	public EntityFormPage(Class<T> clazz, String keyName) {
		this.clazz = clazz;
		this.keyName = keyName;
	}

	

	/**
	 * @see corner.orm.tapestry.page.EntityPage#loadEntity(java.io.Serializable)
	 */
	public void loadEntity(Serializable key) {
		this.setEntity(getBaseService().loadEntity(clazz,key));
	}



	@InjectObject("spring:baseService")
	public abstract BaseService getBaseService();

	public abstract IPage getListEntityPage();

	/**
	 * �˳���ǰҳ��ı༭.
	 * 
	 * @return ת���ҳ��.
	 */
	public IPage cancleEntity() {
		if (logger.isDebugEnabled()) {
			logger.debug("cancleEntity()"); //$NON-NLS-1$
		}

		return getListEntityPage();
	}

	/**
	 * ȷ����ǰҳ.
	 * 
	 * @param cycle
	 *            ҳ������.
	 * @return ת���ҳ��.
	 */
	public IPage okayEntity(IRequestCycle cycle) {
		if (logger.isDebugEnabled()) {
			logger.debug("okayEntity(IRequestCycle)"); //$NON-NLS-1$
		}

		saveOrUpdateEntity();

		return getListEntityPage();
	}
	

	/**
	 * Ӧ�õ�ǰҳ.
	 * 
	 * @param cycle
	 *            ҳ������.
	 */
	public void applyEntity(IRequestCycle cycle) {
		if (logger.isDebugEnabled()) {
			logger.debug("applyEntity(IRequestCycle)"); //$NON-NLS-1$
		}
		saveOrUpdateEntity();
	}
	
	public IPage deleteEntity(IRequestCycle cycle){
		getBaseService().deleteEntities(getEntity());
		return getListEntityPage();
	}

	protected void saveOrUpdateEntity() 
	{
		
		if (BeanUtils.getProperty(getEntity(), keyName) == null) {
			getBaseService().saveEntity(getEntity());
		} else {
			getBaseService().updateEntity(getEntity());
		}
	}

}
