/**
 * 
 */
package corner.orm.tapestry.page;

import org.apache.tapestry.IPage;

/**
 * @author Jun Tsai
 * @version $Revison$
 * @since 2006-5-24
 */
public abstract class AbstractEntityListPage<T> extends AbstractEntityPage<T> {
	/**
	 * ��ѯ��ʵ��.
	 * 
	 * @return ��ѯʵ��.
	 */
	public abstract T getQueryEntity();

	public abstract void setQueryEntity(T obj);
	@SuppressWarnings("unchecked")
	public EntityPage<T> getEntityFormPage() {
		return (EntityPage<T>) this.getRequestCycle().getPage(
				this.getPageName().substring(0,
						this.getPageName().lastIndexOf("List"))
						+ "Form");
	}
//	 -------------------since 2.0
	/**
	 * ɾ��һ��ʵ�塣
	 * 
	 * @param entity
	 *            ʵ�����
	 * @return ����ҳ��.
	 * @since 2.0
	 */
	public IPage doDeleteEntityAction(T entity) { // ɾ������
		this.getEntityService().deleteEntities(entity);
		return this;
	}

	/**
	 * �༭ʵ�����.
	 * 
	 * @param entity
	 *            ʵ��.
	 * @return ���ر༭ҳ��.
	 * @since 2.0
	 */

	public IPage doEditEntityAction(T entity) { // �༭����
		EntityPage<T> page = this.getEntityFormPage();
		page.setEntity(entity);
		return page;
	}

	/**
	 * ����ʬ�����.
	 * 
	 * @return ����ʵ�������ҳ��.
	 * @since 2.0
	 */
	public IPage doNewEntityAction() { // �����Ӳ���.
		return this.getEntityFormPage();
	}

}
