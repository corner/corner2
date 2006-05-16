package corner.orm.tapestry.service.blob;

import java.io.Serializable;

import corner.model.AbstractBlobModel;
import corner.service.EntityService;
import corner.util.BeanUtils;

/**
 * �����blob�ṩ����.
 * <p>�ṩ��blob�Ķ�ȡ����.{@link #getBlobAsBytes() ��ȡblob�ֽ�}{@link #getContentType() �ֽ�������}
 * ����װ��blob����.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2006-1-20
 */
public abstract class AbstractBlobProvider<T extends AbstractBlobModel> implements IBlobProvider {
	
	/**blob����ģ�Ͷ���**/
	private T blobData;
	/**ʵ�����**/
	private EntityService service;
	/**ʵ����������ֵ**/
	private Serializable blobId;
	/**
	 * �õ�blob�������.
	 * @return blobģ�Ͷ������.
	 */
	protected abstract Class<T> getBlobDataClass();
	
	/**
	 * �趨blobģ�͵�����ֵ.
	 * @see corner.orm.tapestry.service.blob.IBlobProvider#setKeyValue(java.lang.String)
	 */
	public void setKeyValue(String tableKey) {
		this.blobId=tableKey;

	}
	/**
	 * �趨��̬ʵ��Service.
	 * @see corner.orm.tapestry.service.blob.IBlobProvider#setEntityService(corner.service.EntityService)
	 */
	public void setEntityService(
			EntityService entityService) {
		this.service=entityService;
	}
	/**
	 * �õ�blobģ�Ͷ���.
	 * @return blobģ�Ͷ���.
	 */
	private T getBlobObject(){
		loadData();
		return blobData;
	}
	/**
	 * ��blob������Ϊ�ֽ��������.
	 * @see corner.orm.tapestry.service.blob.IBlobProvider#getBlobAsBytes()
	 */
	public byte[] getBlobAsBytes() {
		return this.getBlobObject().getBlobData();
		
	}
	/**
	 * �õ�blob���ݵ�����,������ʾblob����.
	 * @see corner.orm.tapestry.service.blob.IBlobProvider#getContentType()
	 */
	public String getContentType() {
		return this.getBlobObject().getContentType();
	}
	/**
	 * װ��blob���������.
	 *
	 */
	private void loadData() {
		if (blobData == null&&blobId!=null) {
			blobData=this.service.getEntity(getBlobDataClass(),blobId);
			
		}
		if(blobData==null){
			blobData=BeanUtils.instantiateClass(getBlobDataClass());
		}

	}

}
