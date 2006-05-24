/**
 * 
 */
package corner.orm.tapestry.page;

import java.util.List;

import org.apache.tapestry.IPage;
import org.apache.tapestry.annotations.InitialValue;

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
	/** ����ѡ�е�list* */
	@InitialValue("new java.util.ArrayList()")
	public abstract List<T> getSelectedEntities();
	public abstract void setSelectedEntities(List<T> list);
	
	
	public boolean getCheckboxSelected() {
		return this.getSelectedEntities().contains(getEntity());
	}

	public void setCheckboxSelected(boolean bSelected) {
		if (bSelected) {
			this.getSelectedEntities().add(getEntity());
		}
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
	/**
	 * �ṩһ���checkbox��ѡ��.
	 * ����ɾ��ʵ��.
	 * 
	 * @return ��ǰҳ.
	 */
	public IPage doDeleteEntitiesAction(){
		this.getEntityService().deleteEntities(this.getSelectedEntities());
		return this;
	}
	/**
	 * ��Ӧ��ѯ�Ĳ���.
	 * @return ��ǰҳ
	 */
	public IPage doQueryEntityAction(){
		return this;
	}

}
