//==============================================================================
//file :        IBlobBeforSaveCallBack.java
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

import corner.model.AbstractBlobModel;

/**
 * ���blob�����ڱ���ǰ�Ĳ����ص��ӿ�.
 * <p>�ṩ�ڱ���blob����֮ǰ�ĵ���.
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2006-2-6
 */
public interface IBlobBeforSaveCallBack<T  extends AbstractBlobModel> {
	/**
	 * ��blob����֮ǰ�Ĳ���.
	 * @param blob blob����.
	 */
	public void doBeforeSaveBlob(T blob);

}
