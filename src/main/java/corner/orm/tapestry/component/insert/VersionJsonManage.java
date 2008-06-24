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

package corner.orm.tapestry.component.insert;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectScript;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.json.JSONObject;

import corner.model.IBlobModel;
import corner.orm.tapestry.page.EntityPage;
import corner.service.EntityService;
import corner.service.svn.IVersionProvider;
import corner.service.svn.IVersionable;
import corner.service.svn.VersionSaveUpdateEventListener;
import corner.service.svn.XStreamDelegate;
import corner.util.StringUtils;

/**
 * 不带显示标题的版本管理器，用来返回相应的版本json串
 * 
 * @author <a href="mailto:Ghostbb@bjmaxinfo.com">Ghostbb</a>
 * @version $Revision$
 * @since 2.5
 */
public abstract class VersionJsonManage extends BaseComponent implements IFormComponent, IVersionProvider {
	
	@InjectScript("VersionJsonManage.script")
	public abstract IScript getScript();
	
	
	private static String NULL_JSON = "{\"entity\":{}}";
	
	/**
	 * 页面render出来的json串变量的名称
	 * @return {@link String}
	 */
	@Parameter
	public abstract String getJsonName();
	
	/**
	 * 操作的版本实体
	 * @return 实现{@link IVersionable}接口的实体
	 */
	@Parameter
	public abstract IVersionable getVersionEntity();
	
	/**
	 * 得到EntityService.
	 * <p>提供基本的操作.
	 * @return entityService 实体服务类
	 * @since 2.0
	 */
	@InjectObject("spring:entityService")
	public abstract EntityService getEntityService();
	
	/**
	 * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
	 */
	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		super.renderComponent(writer, cycle);
		
		if (!cycle.isRewinding()) {
			PageRenderSupport prs = TapestryUtils.getPageRenderSupport(cycle, this);
			Map<String, String> parms = new HashMap<String, String>();
			
			EntityPage page = (EntityPage)getPage().getRequestCycle().getPage();
			
			IVersionable entity;
			if(this.getVersionEntity() != null){
				entity = this.getVersionEntity();
			} else {
				entity = (IVersionable) page.getEntity();
			}
			
			IVersionProvider conf = (IVersionProvider)page;
			
			long v1 = conf.getVersionNum();
			long v2 = conf.getOtherVersionNum();
			
			if(v1 == 0) {v1 = -1;}
			
			String json1 = null;
			String json2 = null;
			
			if(conf.isCompareLastVer()){
				v2 = -1;
				json1 = getJsonVersion(entity,v2);
				json2 = XStreamDelegate.toJSON(entity);
			}else{
				json1 = getJsonVersion(entity,v1);
				json2 = getJsonVersion(entity,v2);
			}
			
			if(entity instanceof IBlobModel){	//删除blob属性
				json1 = removeBlobData(json1);
				json2 = removeBlobData(json2);
			}
			
			String jsonName = this.getJsonName()!=null?this.getJsonName():"";
			
			parms.put("json", json1);
			parms.put("json2", json2);
			parms.put("jsonName", jsonName);
			
			parms.put("type", v2 == 0 ?"show":"compare");	//记录是显示还是比较
			
			//增加显示信息
			String v2show = String.valueOf(conf.getOtherVersionShowNum());
			
			String showInfo = null;
			
//			if(conf.isCompareLastVer()){
//				showInfo = "版本: " + StringUtils.replace(
//						entity.getRevision(), VersionSaveUpdateEventListener.UNREVISION_VERSION, "")
//						 +" 与 当前版本 对比";
//			}else {
//				if(v2 == 0){
//					showInfo = "版本:" + conf.getVersionShowNum();
//					v2show = "";
//				}else{
//					showInfo = "版本: " + conf.getVersionShowNum() +" 与  版本: " + conf.getOtherVersionShowNum() + " 对比";
//				}
//			}
			
			parms.put("ver", String.valueOf(v1));
			parms.put("otherVer", v2show);
			parms.put("showInfo", showInfo);
			
			getScript().execute(this, cycle, prs, parms);
		}
	}
	
	/**
	 * 是否需要显示功能按钮，如果是返回true，反之亦然
	 */
	public boolean isShowButton(){
		IPage page = getPage().getRequestCycle().getPage();
		
		IVersionProvider conf = (IVersionProvider)page;
		
		long v2 = conf.getOtherVersionNum();
		
		if(v2 == 0){
			if(conf.isCompareLastVer())
				return true;
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 删除blobdata
	 * @param temp
	 * @return
	 */
	public static String removeBlobData(String temp){
		JSONObject parent = null;
		JSONObject json = null;
		try {
			parent = new JSONObject(temp);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		json = parent.getJSONObject("entity");
		json.remove(IBlobModel.BLOB_DATA_PRO_NAME);
		
		return parent.toString();
	}
	
	public abstract String getShowside();
	public abstract String getShowLine();
	
	/**
	 * 获得相应版本的json串
	 * @param entity 实体
	 * @param v 版本号
	 * @return 返回的json串
	 */
	private String getJsonVersion(IVersionable entity, long v) {
		
		if(v == 0){
			return NULL_JSON;
		}
		
		return getEntityService().isPersistent(entity)? this.getSubversionService().fetchObjectAsJson(entity, v):NULL_JSON;
	}
}
