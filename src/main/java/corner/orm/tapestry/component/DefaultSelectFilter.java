//==============================================================================
// file :       $Id$
// project:     corner
//
// last change: date:       $Date$
//              by:         $Author$
//              revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	Beijing Maxinfo Technology Ltd. http://www.bjmaxinfo.com
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.tapestry.component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hivemind.ApplicationRuntimeException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import corner.orm.hibernate.v3.HibernateObjectRelativeUtils;
import corner.service.EntityService;
import corner.util.TapestryHtmlFormatter;

/**
 * 默认的SelectFilter实现
 * @author Ghost
 * @version $Revision$
 * @since 2.1.1
 * @deprecated
 */
public class DefaultSelectFilter implements ISelectFilter {
	
	
	/**
	 * 提供增加删除修改查询的基本操作
	 */
	private EntityService entityService;
	
	/**
	 * 被查询的实体类
	 */
	private Class queryClass;
	
	/**
	 * 被查询的实体的英文检索码名称
	 */
	private String labelField;
	
	/**
	 * 被查询的实体的中文检索码名称
	 */
	private String cnlabelField;
	
	
	/**
	 * 默认的构造函数
	 *
	 */
	public DefaultSelectFilter(){
		
	}
	
	
	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#getQueryClass()
	 */
	public Class getQueryClass() {
		
		return this.queryClass;
	}

	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#setQueryClass(java.lang.Class)
	 */
	public void setQueryClass(Class queryClass) {
		this.queryClass = queryClass;
	}

	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#getCnLabel()
	 */
	public String getCnLabel() {
		
		return this.cnlabelField;
	}

	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#getEntityService()
	 */
	public EntityService getEntityService() {
		
		return this.entityService;
	}

	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#getLabel()
	 */
	public String getLabel() {
		
		return this.labelField;
	}

	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#setCnLabel(java.lang.String)
	 */
	public void setCnLabel(String cnLabel) {
		
		this.cnlabelField =cnLabel;
	}

	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#setEntityService(corner.service.EntityService)
	 */
	public void setEntityService(EntityService entityService) {
		
		this.entityService = entityService;
	}

	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		
		this.labelField = label;
	}
	
	public DefaultSelectFilter(EntityService entityService,Class queryClass,String labelField,String cnlabelField){
		
	}

	/**
	 * @see corner.orm.tapestry.component.ISelectFilter#filterValues(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List filterValues(String match) {
		List ret = new ArrayList();
        
        if (match == null)
            return ret;
        
        StringBuffer buffer = new StringBuffer("");
        buffer.append(match.trim());
        buffer.append("%");
        String filter = buffer.toString();

        List _values = this.listAllMatchedValue(filter);
        for(Object obj:_values){

        	
        	String cnlabel = this.getCnLabelFor(obj);
        	ret.add(cnlabel);
        }
        return ret;
	}
	
	/**
	 * 根据给定的字符串在指定的中文检索码和英文检索码中进行查找
	 * @param searchParam
	 * @return
	 */
	public List listAllMatchedValue(final String... searchParam){
		return ((List) ((HibernateObjectRelativeUtils) this.entityService
				.getObjectRelativeUtils()).getHibernateTemplate()
				.execute(new HibernateCallback(){
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Criteria criteria=session.createCriteria(queryClass);
						criteria.add(Restrictions.or(Restrictions.like(labelField,searchParam[0]), Restrictions.like(cnlabelField,searchParam[0])));
						criteria.setFirstResult(nFirst);
						criteria.setMaxResults(nPageSize);
						return criteria.list();
					}}));
	}

	/**
	 * 得到cnlabelField的值
	 * @param value
	 * @return
	 */
    public String getCnLabelFor(Object value){
        try {
            
            if(value instanceof String){
            	return value.toString();
            }
            else{
            	return PropertyUtils.getProperty(value, TapestryHtmlFormatter.lowerFirstLetter(cnlabelField)).toString();
            }
            
        } catch (Exception e) {
            throw new ApplicationRuntimeException(e);
        }    	
    }

	/**
	 * @see org.apache.tapestry.dojo.form.IAutocompleteModel#getLabelFor(java.lang.Object)
	 */
	public String getLabelFor(Object value) {
		return getCnLabelFor(value);
	}	
	
	/**
	 * @return Returns the cnlabelField.
	 */
	public String getCnlabelField() {
		return this.cnlabelField;
	}

	/**
	 * @param cnlabelField The cnlabelField to set.
	 */
	public void setCnlabelField(String cnlabelField) {
		this.cnlabelField = cnlabelField;
	}

	/**
	 * @return Returns the labelField.
	 */
	public String getLabelField() {
		return this.labelField;
	}

	/**
	 * @param labelField The labelField to set.
	 */
	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

}
