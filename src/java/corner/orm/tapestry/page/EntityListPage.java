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
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.components.IPrimaryKeyConverter;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.event.PageAttachListener;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;

import corner.orm.hibernate.expression.ExpressionExample;
import corner.orm.hibernate.v3.HibernateObjectRelativeUtils;
import corner.orm.tapestry.ReflectPrimaryKeyConverter;
import corner.util.PaginationBean;

/**
 * ��Ե�һʵ�������Page�ࡣ
 * 
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision$
 * @since 2005-11-3
 */
public abstract class EntityListPage<T> extends AbstractEntityPage<T> implements
		PageBeginRenderListener, PageDetachListener, PageAttachListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(EntityListPage.class);

	/**
	 * ��ѯ��ʵ��.
	 * 
	 * @return ��ѯʵ��.
	 */
	public abstract T getQueryEntity();

	public abstract void setQueryEntity(T obj);

	protected void appendCriteria(Criteria criteria) {
		if (this.getQueryEntity() != null)
			criteria.add(ExpressionExample.create(getQueryEntity()).enableLike()
					.ignoreCase());
	}

	public abstract String getKeyName();

	public abstract void setKeyName(String keyName);

	/** ����ѡ�е�list* */
	@InitialValue("new java.util.ArrayList()")
	public abstract List<T> getSelectedEntities();

	public abstract void setSelectedEntities(List<T> list);

	/** ���ڷ�ҳ��bean* */
	@Persist("client")
	@InitialValue("new corner.util.PaginationBean()")
	public abstract PaginationBean getPaginationBean();

	public abstract void setPaginationbean(PaginationBean pb);

	/** �õ���������Converter* */
	public IPrimaryKeyConverter getConverter() {
		return new ReflectPrimaryKeyConverter<T>(getEntity().getClass(),
				getKeyName());
	}

	/** �õ�ʵ�������.* */
	protected int getEntityRowCount() {
		return ((Integer) ((HibernateObjectRelativeUtils) getEntityService()
				.getObjectRelativeUtils()).getHibernateTemplate().executeFind(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Criteria criteria = session.createCriteria(
								getEntity().getClass()).setProjection(
								Projections.rowCount());
						appendCriteria(criteria);
						return criteria.list();

					}
				}).iterator().next()).intValue();

	}

	/** �õ���ǰ��ҳ������* */
	@SuppressWarnings("unchecked")
	protected Iterator<? extends Object> getCurrentPageRows(final int nFirst,
			final int nPageSize, final ITableColumn column, final boolean sort) {
		return ((HibernateObjectRelativeUtils) getEntityService()
				.getObjectRelativeUtils()).getHibernateTemplate().executeFind(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						Criteria criteria = session.createCriteria(getEntity()
								.getClass());
						appendCriteria(criteria);

						criteria.setFirstResult(nFirst);
						criteria.setMaxResults(nPageSize);
						if (column != null) {

							criteria.addOrder(sort ? Order.asc(column
									.getColumnName()) : Order.desc(column
									.getColumnName()));
						}

						return criteria.list();

					}
				}).iterator();

	}

	class BasicTableModelProxy implements IBasicTableModel {
		/**
		 * Logger for this class
		 */
		private final Log logger = LogFactory
				.getLog(BasicTableModelProxy.class);

		public int getRowCount() {
			return getPaginationBean().getRowCount();
		}

		public Iterator getCurrentPageRows(int nFirst, int nPageSize,
				ITableColumn column, boolean flag) {
			if (getRequestCycle().isRewinding()) {
				return null;
			}

			if (logger.isDebugEnabled()) {
				logger
						.debug("getCurrentPageRows(int, int, ITableColumn, boolean) -  : nFirst=" + nFirst + ", nPageSize=" + nPageSize + ", column=" + column + ", sortable=" + flag); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}

			return EntityListPage.this.getCurrentPageRows(nFirst, nPageSize,
					column, flag);
		}
	}

	/**
	 * �õ�Table��source��
	 * 
	 * @return
	 * @see IBasicTableeModel
	 */
	public abstract IBasicTableModel getSource();

	public abstract void setSource(IBasicTableModel btm);

	public boolean getCheckboxSelected() {
		return false;
	}

	public void setCheckboxSelected(boolean bSelected) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("setCheckboxSelected(boolean) bSelected [" + bSelected + "]"); //$NON-NLS-1$
		}

		if (bSelected) {
			this.getSelectedEntities().add(getEntity());

			if (logger.isDebugEnabled()) {
				logger
						.debug("get SelectedEntities size [" + getSelectedEntities().size() + "]"); //$NON-NLS-1$
			}
		}
	}

	/*-------------------------------------------------------------------------
	 * ��ʵ���ҳ���������Ӧ.
	 * ------------------------------------------------------------------------
	 */
	/**
	 * ����ɾ��ʵ��.
	 */
	public void deleteEntities(IRequestCycle cycle) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("deleteNodes(IRequestCycle),getSelectedEntities size [" + getSelectedEntities().size() + "]"); //$NON-NLS-1$
		}

		getEntityService().deleteEntities(this.getSelectedEntities().toArray());
		this.getPaginationBean().setRowCount(this.getEntityRowCount());
	}

	/**
	 * ѡ��ĳһ��ʵ��.
	 * 
	 * @param key
	 *            ����ֵ.
	 * @return ҳ��.
	 */
	public IPage selectEntity(Serializable key) {
		if (logger.isDebugEnabled()) {
			logger.debug("selectEntity(String) - start"); //$NON-NLS-1$
		}
		EntityPage<T> page = getEntityFormPage();
		page.loadEntity(key);

		return page;
	}

	/**
	 * ת������ʵ���ҳ��.
	 * 
	 * @return ����ʵ��ҳ��.
	 * @deprecated ����2.1 ��ɾ������ʹ�� {@link #doNewEntityAction()}
	 */
	public IPage go2AddEntityForm() {
		return this.getEntityFormPage();
	}

	@SuppressWarnings("unchecked")
	public EntityPage<T> getEntityFormPage() {
		return (EntityPage<T>) this.getRequestCycle().getPage(
				this.getPageName().substring(0,
						this.getPageName().lastIndexOf("List"))
						+ "Form");
	}

	/**
	 * �༭ʵ��.
	 * 
	 * @param key
	 * @return
	 * @deprecated �� {@link #doEditEntityAction(T)}����.
	 */
	public IPage go2EditEntityForm(Serializable key) {
		EntityPage<T> page = this.getEntityFormPage();
		page.loadEntity(key);
		return page;
	}

	/**
	 * ɾ��ʵ�����.
	 * 
	 * @param key
	 * @deprecated ���� 2.1��ɾ��,�� {@link #doDeleteEntityAction(T)}����.
	 */
	public void deleteEntityAction(Serializable key) {
		getEntityService().deleteEntityById(getEntity().getClass(), key);
	}

	// ��ѯʵ��.
	public void queryEntity() {

	}

	/**
	 * @see org.apache.tapestry.event.PageDetachListener#pageDetached(org.apache.tapestry.event.PageEvent)
	 */
	public void pageDetached(PageEvent event) {

	}

	/**
	 * @see org.apache.tapestry.event.PageBeginRenderListener#pageBeginRender(org.apache.tapestry.event.PageEvent)
	 */
	public void pageBeginRender(PageEvent event) {
		if (!event.getRequestCycle().isRewinding()) {
			getPaginationBean().setRowCount(getEntityRowCount());
		}
	}

	/**
	 * @see org.apache.tapestry.event.PageAttachListener#pageAttached(org.apache.tapestry.event.PageEvent)
	 */
	public void pageAttached(PageEvent event) {
		if (this.getSource() == null) {
			setSource(new BasicTableModelProxy());

		}
	}

	// -------------------since 2.0
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
