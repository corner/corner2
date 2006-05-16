//==============================================================================
//file :        IBlobImageProvider.java
//project:      poison-system
//
//last change:	date:       $Date$
//           	by:         $Author$
//           	revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.tapestry.service.blob;

import corner.service.EntityService;

/**
 * �ṩһ��blobͼ����Դ.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2006-1-19
 */
public interface IBlobProvider {
	/**
	 * ����blob����Ϊ�ֽ�����.
	 * @return �ֽ�����.
	 */
	public byte[] getBlobAsBytes();
	/**
	 * blob������,ͨ��������web�ϵ���ʾ.
	 * @return blob������.
	 */
	public String getContentType();
	/**
	 * blob������ֵ.
	 * @param tableKey ����ֵ.
	 */
	public void setKeyValue(String tableKey);
	/**
	 * ����ʵ�����.
	 * 
	 * @param entityService ʵ�����.
	 */
	public void setEntityService(EntityService entityService);
}
