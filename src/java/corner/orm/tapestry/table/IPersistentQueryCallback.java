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
public interface IPersistentQueryCallback {

	public DetachedCriteria createDetachedCriteria();
	public void appendDetachedCriteria(DetachedCriteria criteria);
}
