//==============================================================================
//file :        AbstractBlobModel.java
//project:      poison-system
//
//last change:	date:       $Date$
//           	by:         $Author$
//           	revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.model;

/**
 * �����blobģ��,���е�blobģ�ͱ���̳д���.
 * <p>�����ṩ��blob��������Ķ���,һ������,һ��Ϊ����.
 *  TODO ����blob���ݴ�byte���鵽IO��.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2006-1-20
 */
public class AbstractBlobModel {

	/**
	 * blob����.
	 * @hibernate.property column="BlobData" length="2147483647"
	 *                     type="org.springframework.orm.hibernate3.support.BlobByteArrayType"
	 * 
	 */
	private byte[] blobData;
	/**
	 * blob���ݵ�����,����������webҳ�����ʾ,���ܵĽ��Ϊ:image/jpeg,image/gif,application/pdf ��.
	 * @hibernate.property column="ContentType" length="30"
	 */
	private String contentType;
	
	public byte[] getBlobData() {
		return blobData;
	}

	public void setBlobData(byte[] blobData) {
		this.blobData = blobData;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	

}
