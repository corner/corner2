/**
 * 
 */
package corner.orm.tapestry.table;

import org.hibernate.criterion.DetachedCriteria;

/**
 * ���hibernate��ѯ�ṩ�Ľӿڷ���
 * @author Jun Tsai
 * @version $Revison$
 * @since 2006-5-24
 */
public interface IPersistentQueriable {
	/**
	 * ����һ��offline��Cirteria
	 * @return criteria detached
	 */
	public DetachedCriteria createDetachedCriteria();
	/**
	 * �Բ�ѯ����һ���Ի������á�
	 * @param criteria
	 */
	public void appendDetachedCriteria(DetachedCriteria criteria);
}
