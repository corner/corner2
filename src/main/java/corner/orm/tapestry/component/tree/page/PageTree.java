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

package corner.orm.tapestry.component.tree.page;

import java.util.Map;

import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectScript;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.engine.DirectServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.json.JSONArray;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.link.DirectLink;
import org.apache.tapestry.listener.ListenerInvoker;

import corner.model.tree.ILeftMenu;
import corner.model.tree.ITreeAdaptor;
import corner.orm.tapestry.component.tree.BaseLeftTree;
import corner.orm.tapestry.component.tree.ITreeSelectModel;



/**
 * 弹出窗
 * @author "<a href=\"mailto:xf@bjmaxinfo.com\">xiafei</a>"
 * @version $Revision$
 * @since 2.5
 */
public abstract class PageTree extends BaseLeftTree{
	
	/** 选择的过滤器 **/
	@Parameter(defaultValue="ognl:new corner.orm.tapestry.component.tree.page.PageTreeSelectModel()")
	public abstract ITreeSelectModel getSelectModel();
	
	/**
	 * @see corner.orm.tapestry.component.tree.BaseLeftTree#constructSelectModel()
	 */
	@Override
	protected ITreeSelectModel constructSelectModel() {
		return this.getSelectModel();
	}
	
	/**
	 * @see corner.orm.tapestry.component.tree.BaseLeftTree#appendParamet(java.util.Map)
	 */
	@Override
	public void appendParamet(Map<String, Object> parms) {
		parms.put("actionFrame", getActionFrame());
		parms.put("queryClassName", getQueryClassName());
		parms.put("actionUrl", getActionUrl());
	}

	/**
	 * @see org.apache.tapestry.IDirect#trigger(org.apache.tapestry.IRequestCycle)
	 */
	public void trigger(IRequestCycle cycle) {
		IActionListener listener = getListener();
	
		if (listener == null  && getQueryPageName() == null)
			throw Tapestry.createRequiredParameterException(this, "listener or QueryPageName");
	
		getListenerInvoker().invokeListener(listener, this, cycle);
	}
	
	/**
	 * 返回url
	 */
	public String getActionUrl() {
		String url = null;
		
		if(this.getQueryPageName() != null){
			url = getPageService().getLink(false, this.getQueryPageName()).getAbsoluteURL();
		}else{
			Object[] parameters = new Object[]{getParameters()};
			
			Object[] serviceParameters = DirectLink
					.constructServiceParameters(parameters);
	
			DirectServiceParameter dsp = new DirectServiceParameter(this,
					serviceParameters);
			
			url = getDirectService().getLink(false, dsp).getAbsoluteURL();
		}
		
		return url;
	}
	
	/**
	 * @see corner.orm.tapestry.component.tree.BaseLeftTree#leftTreeNodeJson(corner.orm.tapestry.component.tree.ITreeSelectModel)
	 */
	protected JSONObject leftTreeNodeJson(ITreeSelectModel model) {
		JSONArray jsonArr = new JSONArray();
		
		JSONObject subjson = null;
		
		JSONObject datejson = null;
		
		for(ITreeAdaptor node : model.getTreeList()){
			subjson = new JSONObject();
			subjson.put("id", node.getId());
			subjson.put("type", "pageTreeSite");
			
			datejson = new JSONObject();
			datejson.put("name", node.getNodeName());
			datejson.put("left", node.getLeft());
			datejson.put("right", node.getRight());
			datejson.put("depth", node.getDepth());
			datejson.put("thisEntity", getDataSqueezer().squeeze(node));
			
			if(node instanceof ILeftMenu){
				datejson.put("actionPage", ((ILeftMenu)node).getActionPage());
			}
			
			subjson.put("data", datejson);
			
			jsonArr.put(subjson);
		}
		
		JSONObject json = new JSONObject();
		json.put("nodes", jsonArr);
		return json;
	}

	/**
	 * @see corner.orm.tapestry.component.tree.BaseLeftTree#getScript()
	 */
	@InjectScript("PageTree.script")
	public abstract IScript getScript();
	
	@Parameter
	public abstract String getQueryPageName();
	
	@Parameter
	public abstract Object getParameters();
	
	@Parameter(required = true)
	public abstract String getActionFrame();
	
	@Parameter(required = true)
	public abstract String getQueryClassName();
	
	@Parameter
	public abstract String getParentPage();
	
	/**
	 * 监听调用函数
	 */
	@Parameter
	public abstract IActionListener getListener();

	/**
	 * 获得监听
	 */
	@InjectObject("infrastructure:listenerInvoker")
	public abstract ListenerInvoker getListenerInvoker();

	@InjectObject("service:tapestry.services.Page")
	public abstract IEngineService getPageService();
}
