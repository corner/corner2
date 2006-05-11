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

/**
 * ������Entity��ҳ.
 * <P>
 * �ṩ����Ե�һʵ��Ĳ���,Ʃ��C/U/D����.
 * 
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision$
 * @since 2005-11-4
 */
public abstract class EntityFormPage<T> extends AbstractEntityPage<T>{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(EntityFormPage.class);
	/**
	 * �õ���Ҫ��ʾlist�б��ҳ.
	 * 
	 * @return
	 */
	public  IPage getListEntityPage(){
		if(this.getRelativePage()!=null){
			EntityFormPage page = (EntityFormPage) getRequestCycle().getPage(this.getRelativePage());
			page.loadEntity(this.getRelativeId());
			return page;
		}else{
			return this.getRequestCycle().getPage(this.getPageName().substring(0,this.getPageName().lastIndexOf("Form"))+"List");
		}
	}

	//�Թ���ҳ��Ĵ���
	/**
	 * ���������ʵ���ҳ��.
	 */
	public IPage go2AddRelativeEntityPage(String entityFormPage){
		//build form page
		entityFormPage=buildRelativePageName(entityFormPage);
		
		EntityFormPage page=(EntityFormPage) this.getRequestCycle().getPage(entityFormPage);
		constructParentPageInfo(page);
		
		return page;
	}
	/**
	 * ɾ��������ʵ��.
	 * @param clazzName ����ʵ������.
	 * @param key ����ֵ.
	 */
	public void deleteRelativeEntityAction(String clazzName,Serializable key){
		this.getEntityService().deleteEntityById(clazzName,key);
		this.loadEntity(this.getKey());
	}
	/**
	 * ת��༭����ʵ���ҳ��.
	 * @param entityFormPage ������ʵ��ҳ��.
	 * @param key ����ֵ.
	 * @return �༭ҳ��.
	 */
	public IPage go2EditRelativeEntityPage(String entityFormPage,Serializable key){
		entityFormPage=buildRelativePageName(entityFormPage);
		
		EntityFormPage page=(EntityFormPage) this.getRequestCycle().getPage(entityFormPage);
		constructParentPageInfo(page);
		
		page.loadEntity(key);
		return page;
	}
	public IPage go2ListEntityPage(){
		return this.getRequestCycle().getPage(this.getPageName().substring(0,this.getPageName().lastIndexOf("Form"))+"List");
	}
	/**
	 * ������ҳ���һЩ��Ϣ.
	 * @param page ����ҳ��.
	 */
	private void constructParentPageInfo(EntityFormPage page){
		page.setRelativePage(this.getPageName());
		page.setRelativeId(this.getKey());
		page.setRelativeClassName(this.getEntity().getClass().getName());
	}
	/**
	 * �õ�������ҳ������.
	 * @param entityFormPage ����ҳ.
	 * @return
	 */
	private String buildRelativePageName(String entityFormPage){
		if(entityFormPage.indexOf("/") == -1){
			entityFormPage=getPageName().substring(0,getPageName().lastIndexOf("/")+1)+entityFormPage;
		}
		return entityFormPage;
	}
	
	
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

	/**
	 * ɾ��ʵ��.
	 * 
	 * @param cycle
	 * @return ת���listҳ��.
	 */
	public IPage deleteEntity(IRequestCycle cycle) {
		getEntityService().deleteEntities(getEntity());
		return getListEntityPage();
	}
}
