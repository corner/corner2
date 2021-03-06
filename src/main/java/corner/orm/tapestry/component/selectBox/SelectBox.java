//==============================================================================
// file :       $Id$
// project:     corner2.5
//
// last change: date:       $Date$
//              by:         $Author$
//              revision:   $Revision$
//------------------------------------------------------------------------------
//copyright:	Beijing Maxinfo Technology Ltd. http://www.bjmaxinfo.com
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.tapestry.component.selectBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectScript;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.IValidationDelegate;

import corner.service.EntityService;
import corner.util.StringUtils;

/**
 * 两个select互选选择
 * @author <a href="mailto:xf@bjmaxinfo.com">xiafei</a>
 * @version $Revision$
 * @since 2.5
 */
public abstract class SelectBox extends BaseComponent implements IFormComponent{
	
	/**
	 * Invoked from {@link #renderComponent(IMarkupWriter, IRequestCycle)} to
	 * rewind the component. If the component is
	 * {@link IFormComponent#isDisabled() disabled} this will not be invoked.
	 * 
	 * @param writer
	 * @param cycle
	 */
	protected void rewindFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		String [] values = cycle.getParameters(this.getToField());
		this.setToSource(new ArrayList<ISelectBox>());
		
		if(values == null) return;
		
		//判断是否是空
		if(StringUtils.notBlank(this.getEntityClassName())){
			loadEntitys(values);
		}else{
			reLoadSource(values);
		}
	}
	
	/**
	 * 通过value重新整理toSource
	 * @param values 
	 * @return
	 */
	private void reLoadSource(String[] values) {
		for(String s:values){
			for(ISelectBox sb : this.getFromSource()){
				if(sb.getValue().equals(s)){
					this.getToSource().add(sb);
				}
			}
		}
	}

	/**
	 * 重新抓取实体整理toSource
	 * @param values 
	 * @return
	 */
	private void loadEntitys(String[] values) {
		for(String id : values){
			ISelectBox o = (ISelectBox) this.getEntityService().load(this.getEntityClassName(), id);
			this.getToSource().add(o);
		}
	}

	/**
	 * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
	 */
	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		
		/**
		 * 处理form
		 */
		IForm form = TapestryUtils.getForm(cycle, this);

		setForm(form);

		if (form.wasPrerendered(writer, this))
			return;

		IValidationDelegate delegate = form.getDelegate();

		delegate.setFormComponent(this);

		setName(form);
		
		/**
		 * 当提交时和为显示时
		 */
		if (cycle.isRewinding()) {
			if (!isDisabled()) {
				rewindFormComponent(writer, cycle);
			}

			// This is for the benefit of the couple of components (LinkSubmit)
			// that allow a body.
			// The body should render when the component rewinds.

			if (getRenderBodyOnRewind())
				renderBody(writer, cycle);
		}
		
		if (!cycle.isRewinding()) {
	        Map<String,Object> parms = new HashMap<String,Object>();
	        parms.put("clientId", this.getClientId());
	        parms.put("formId", getFormId());
	        parms.put("fromField", getFromField());
	        parms.put("toField", getToField());
	        parms.put("fromSource", getFromList().toString());
	        parms.put("toSource", getToList().toString());
	        
	        getScript().execute(this, cycle, TapestryUtils.getPageRenderSupport(cycle, this), parms);
	    }
	}
	
	/**
	 * @return
	 */
	private JSONObject getToList() {
		JSONObject json = new JSONObject();
		for(ISelectBox sb : this.getToSource()){
			json.put(sb.getValue(), sb.getLabel());
		}
		return json;
	}

	/**
	 * 获得源
	 */
	private JSONObject getFromList() {
		JSONObject json = new JSONObject();;
		for(ISelectBox sb : this.getFromSource()){
			if(isNotExist(sb)){
				json.put(sb.getValue(), sb.getLabel());
			}
		}
		return json;
	}

	/**
	 * 判断是否在ToList里面存在
	 * @param sb
	 * @return
	 */
	private boolean isExist(ISelectBox sb) {
		for(ISelectBox o : this.getToSource()){
			if(o.getValue().equals(sb.getValue())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否在ToList里面存在
	 * @param sb
	 * @return
	 */
	private boolean isNotExist(ISelectBox sb) {
		return !this.isExist(sb);
	}

	/**
	 * 
	 */
	public abstract IForm getForm();

	/**
	 * @param form
	 */
	public abstract void setForm(IForm form);

	protected boolean getRenderBodyOnRewind() {
		return false;
	}

	/**
	 * @param form
	 */
	protected void setName(IForm form) {
		setName(form.getElementId(this));
	}

	/**
	 * FormId
	 */
	@Parameter(required = true)
	public abstract String getFormId();
	
	/**
	 * 
	 */
	@Parameter(required = true)
	public abstract String getFromField();
	
	/**
	 * 数据源，用于回显或者显示
	 */
	@Parameter(required = true)
	public abstract List<ISelectBox> getFromSource();
	
	/**
	 * FormId
	 */
	@Parameter(required = true)
	public abstract String getToField();
	
	/**
	 * 已经选中的
	 */
	@Parameter(required = true)
	public abstract List<ISelectBox> getToSource();
	public abstract void setToSource(List<ISelectBox> ls);
	
	/**
	 * 使用的实体
	 */
	@Parameter
	public abstract String getEntityClassName();
	
	/**
	 * 写入js
	 */
	@InjectScript("SelectBox.script")
	public abstract IScript getScript();
	
	/**
	 * 得到EntityService.
	 * <p>提供基本的操作.
	 * @return entityService 实体服务类
	 */
	@InjectObject("spring:entityService")
	public abstract EntityService getEntityService();
}