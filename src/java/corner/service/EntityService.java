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

package corner.service;



import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import corner.orm.hibernate.ObjectRelativeUtils;
import corner.util.PaginationBean;

/**
 * 
 * Entity Service.
 * <p>�ṩ�˶�ʵ��Ļ�������.
 * Ʃ��:����,ɾ��,�޸ĵ�.
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision$
 * @since 2005-11-2
 */
public class EntityService {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(EntityService.class);

	/**�����ϵutils**/
	protected ObjectRelativeUtils oru;

	/**
	 * �趨O/R M��util��.
	 * @param oru O/R M��util��.
	 */
	public void setObjectRelativeUtils(ObjectRelativeUtils oru) {
		this.oru = oru;
	}
	/**
	 * �õ� O/R M��util��.
	 * @return O/R M��util��.
	 */
	public ObjectRelativeUtils getObjectRelativeUtils() {
		return oru;
	}
	
	/**
	 * ����һ��ʵ��
	 * @param <T> ʵ��
	 * @param entity �������ʵ��
	 * @return ������ʵ��.
	 */
	public <T> Serializable saveEntity(T entity) {
		return oru.save(entity);
	}
	/**
	 * ������߸���ʵ��.
	 * @param <T> ʵ��.
	 * @param entity ����������ʵ��.
	 */
	public <T> void saveOrUpdateEntity(T entity) {
		oru.saveOrUpdate(entity);
	}
	/**
	 * ����һ��ʵ��.
	 * @param <T> ʵ��.
	 * @param entity �����µ�ʵ��.
	 */
	public <T> void updateEntity(T entity) {
		oru.update(entity);
	}
	
	/**
	 * װ��һ��ʵ��.
	 * @param <T> ʵ��.
	 * @param clazz װ��ʵ�����.
	 * @param keyValue ����ֵ.
	 * @return �־û���ʵ��.
	 */
	public <T> T loadEntity(Class<T> clazz, Serializable keyValue) {
		return oru.load(clazz, keyValue);
	}
	/**
	 * ��ѯһ��ʵ��.
	 * @param <T> ʵ��
	 * @param clazz ʵ����.
	 * @param keyValue ����ֵ.
	 * @return ʵ��,���δ�ҵ�,����null.
	 * @since 2.0
	 */
	public <T> T getEntity(Class<T> clazz, Serializable keyValue) {
		return oru.get(clazz, keyValue);
	}
	
	
	/**
	 * ����ɾ��ʵ��.
	 * @param <T> ʵ��.
	 * @param ts ʵ������.
	 */
	public <T> void deleteEntities(T... ts) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("deleteEntities(List),delete entity list size [" + ts.length + "]"); //$NON-NLS-1$
		}

		for (T entity : ts) {
			try{
				
				oru.delete(entity);
			}catch(Exception e){
				logger.warn(e.getMessage());
				//donoting
			}
		}
	}
	/**
	 * ͨ��������ʵ��ID��ɾ��ʵ��.
	 * @param <T> ʵ��. 
	 * @param clazz ��ɾ����ʵ��.
	 * @param keyValue ����ֵ.
	 */
	public <T> void deleteEntityById(Class<T> clazz,Serializable keyValue) {
		T entity=this.getEntity(clazz,keyValue);
		if(entity!=null){
			oru.delete(entity);
		}
	}
	/**
	 * ͨ��������������ƺ�����ֵ��ɾ��ʵ��.
	 * @param clazzName ����.
	 * @param key ����ֵ.
	 */
	@SuppressWarnings("unchecked")
	public void deleteEntityById(String clazzName, Serializable key) {
		try {
			Class clazz=Class.forName(clazzName);
			this.deleteEntityById(clazz,key);
		} catch (ClassNotFoundException e) {
			//do noting 
		}
		
	}
	/**
	 * ͨ��������������ƺ�����ֵ���õ�ʵ��.
	 * @param clazzname ʵ���������.
	 * @param key ����ֵ.
	 * @return ʵ��,����������δ����ʱ,����null.
	 */
	public Object getEntity(String clazzname, Serializable key) {
		Class clazz;
		try {
			clazz = Class.forName(clazzname);
			return this.getEntity(clazz,key);
		} catch (ClassNotFoundException e) {
			//do nothing
			logger.warn(e.getMessage());
			return null;
		}
		
		
	}
	/**
	 * ͨ�����������Լ���ҳ�õ�bean���õ�list��
	 * @param <T> ��Ҫ���ҵ��ࡣ
	 * @param clazz �ࡣ
	 * @param pb ��ҳ��bean��
	 * @return �б�.
	 */
	public <T> List<T> find(Class<T> clazz, PaginationBean pb) { 
		return oru.find("from " + clazz.getName(), pb); 
	} 
}
