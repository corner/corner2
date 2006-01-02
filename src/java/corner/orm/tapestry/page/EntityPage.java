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

import org.apache.tapestry.IPage;

/**
 * 
 * ������ʵ������ӿڡ�
 * @author Jun Tsai
 * @since 0.1
 * @param <T> ʵ����
 */
public interface EntityPage<T  extends Object> extends IPage {
	/**
	 * �õ�ʵ�塣
	 * @return ʵ�塣
	 */
	public  abstract <E extends T> E  getEntity();
	/***
	 * �趨ʵ�塣
	 * @param entity ʵ�塣
	 */
	public abstract <E extends T> void  setEntity(E entity);

	/**
	 * ����ʵ����������õ�һ��ʵ�壬���趨ʵ�塣
	 * @param key ����ֵ
	 */
	public abstract void loadEntity(Serializable key);
}
