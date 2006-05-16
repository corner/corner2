//==============================================================================
//file :        BlobPageDelegate.java
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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.request.IUploadFile;
import org.springframework.util.FileCopyUtils;

import corner.model.AbstractBlobModel;
import corner.service.EntityService;
import corner.util.BeanUtils;

/**
 * ���blob�ֶεĴ����ί����.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2006-1-20
 */
public class BlobPageDelegate <T extends AbstractBlobModel>{
	private static final Log log=LogFactory.getLog(BlobPageDelegate.class);
	private IUploadFile uploadFile;
	private String keyValue;
	private EntityService service;
	private Class<T> clazz;

	/**
	 * ����һ��ί�������.
	 * @param clazz ��.
	 * @param uploadFile �ϴ��ļ�����.
	 * @param keyValue ������ֵ.
	 * @param service ʵ�����.
	 * @see EntityService
	 */
	public BlobPageDelegate(Class<T> clazz,IUploadFile uploadFile, String keyValue,EntityService service) {
		this.clazz=clazz;
		this.keyValue=keyValue;
		this.uploadFile=uploadFile;
		this.service=service;
	}
	/**
	 * ����blob����.
	 * @param callback �ص�����.
	 * @see org.springframework.orm.hibernate3.support.BlobByteArrayType
	 * 
	 */
	public void save(IBlobBeforSaveCallBack<T> callback) {
		//����ϴ�Ϊ��.
		if(uploadFile==null){
			if(keyValue!=null) //������ֵ,�����ɾ��.
				service.deleteEntityById(clazz,keyValue);
			return;
		}
		//����blob����.
		T blob = null;
		//���������ֵ��Ϊ��,���ȴӱ�����ȡ����.
		if(keyValue!=null){
			blob=service.getEntity(clazz,keyValue);
		}
		//�������������,����������һ������.
		if(blob == null){
			blob=BeanUtils.instantiateClass(clazz);
		}
		
		//�趨blob���ֽ�����ֵ.
		try {
			blob.setBlobData(FileCopyUtils
					.copyToByteArray(uploadFile.getStream()));
		} catch (IOException e) {
			log.warn("fail to set blob data ["+e.getMessage()+"]");
			return;
		}
		
		//�趨content����.
		blob.setContentType(this.uploadFile.getContentType());
		
		//ִ�лص�����,���ж������ݵĲ���.
		if(callback!=null){
			callback.doBeforeSaveBlob(blob);
		}
		
		//�������.
		this.service.saveOrUpdateEntity(blob);
	}
	/**
	 * ����blob.
	 * @see #save(IBlobBeforSaveCallBack)
	 */
	public void save() {
		this.save(null);
	}

}
