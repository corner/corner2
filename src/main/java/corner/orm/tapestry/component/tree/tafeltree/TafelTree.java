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

package corner.orm.tapestry.component.tree.tafeltree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IScript;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.InjectScript;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.json.JSONArray;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.web.WebRequest;
import corner.model.tree.ITreeAdaptor;
import corner.service.tree.TreeService;
import corner.util.StringUtils;

/**
 * 封装"js组件tafelTree_1_9_1" 部分功能
 * 
 * @author <a href=mailto:wlh@bjmaxinfo.com>wlh</a>
 * @version $Revision$
 * @since 2.5.2
 */
public abstract class TafelTree extends BaseComponent {

	@InjectObject("spring:treeService")
	public abstract TreeService getTreeService();

	@InjectObject("infrastructure:request")
	public abstract WebRequest getWebRequest();

	/**
	 * @see corner.orm.tapestry.component.tree.BaseLeftTree#getScript()
	 */
	@InjectScript("TafelTree.script")
	public abstract IScript getScript();

	/**
	 * 树的ID名.需要提供一个区域块 呈现树 如:<div id="myTree"></div>
	 * 
	 * @return
	 */
	@Parameter(defaultValue = "literal:myTree")
	public abstract String getTreeId();

	/**
	 * 默认组织树的 Json方法的this.constructorDefault(List list); 需不需要 组织叶节点。
	 * 
	 * @return
	 */
	@Parameter(defaultValue = "ognl:true")
	public abstract boolean isDsplyLeaf();

	/**
	 * 图片存放的基础地址
	 * 
	 * @return
	 */
	@Parameter(defaultValue = "literal:/images/treeImgs/")
	public abstract String getImgBase();

	@Parameter(defaultValue = "literal:page.gif")
	public abstract String getDefaultImg();

	/**
	 * 根节点的呈现文本。
	 * 
	 * @return
	 */
	@Parameter(defaultValue = "literal:root_1")
	public abstract String getRootText();

	/**
	 * 指定根节点ID。
	 * 
	 * @return
	 */
	@Parameter(defaultValue = "literal:root_1")
	public abstract String getRootId();

	/**
	 * 需要拥有展开所有节点功能控件的ID
	 * 
	 * @return
	 */
	@Parameter
	public abstract String getExpendElementId();

	/**
	 * 需要拥有关闭所有节点功能控件的ID
	 * 
	 * @return
	 */
	@Parameter
	public abstract String getCollapseElementId();

	/**
	 * 需要拥有展开和关闭所有节点功能控件的ID
	 * 
	 * @return
	 */
	@Parameter
	public abstract String getOptTreeCmpId();

	/**
	 * 获得单击节点事件函数名。该函数可以接受一个 节点实体
	 * 
	 * @return
	 */
	@Parameter(defaultValue = "literal:myClick")
	public abstract String getOnClickFun();

	/**
	 * 获得单击节点事件链接URL。、
	 * 
	 * @return
	 */
	@Parameter
	public abstract String getOpenLink();

	/**
	 * 需要拥有显示和隐藏所有叶节点功能控件的ID
	 * 
	 * @return
	 */
	@Parameter
	public abstract String getOptTreeLeafCmpId();

	/**
	 * 获得构造树型的 json串.为空折采用默认方法
	 * 
	 * @return
	 */
	@Parameter
	public abstract String getJSONStruct();

	/**
	 * 树型 json 配置串
	 * 
	 * @return
	 */
	@Parameter
	public abstract String getJSONCfg();

	/**
	 * 采用默认方法时必须提供的查询类.
	 * 
	 * @return
	 */
	@Parameter
	public abstract String getQueryClassName();

	/**
	 * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter,
	 *      org.apache.tapestry.IRequestCycle)
	 */
	@Override
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
		PageRenderSupport pageRenderSupport = TapestryUtils
				.getPageRenderSupport(cycle, this);
		Map<String, Object> parms = new HashMap<String, Object>();

		parms.put("treeId", this.getTreeId());
		parms.put("imgBase", this.getWebRequest().getContextPath()
				+ this.getImgBase());

		parms.put("expendElementId", this.getExpendElementId());
		parms.put("collapseElementId", this.getCollapseElementId());
		parms.put("optTreeCmpId", this.getOptTreeCmpId());
		parms.put("optTreeLeafCmpId", this.getOptTreeLeafCmpId());

		parms.put("treeStruct", this.getTreeJsonStr(this.isDsplyLeaf()));
		parms.put("treeStruct1", this.getTreeJsonStr(false));
		parms.put("treeConfig", this.getTreeCfgJsonStr());
		parms.put("rootId", this.getRootId());
		parms.put("openLink", this.getWebRequest().getContextPath()
				+ this.getOpenLink());
		
		getScript().execute(this, cycle, pageRenderSupport, parms);
	}

	/**
	 * 树的一些配置信息
	 * 
	 * @return
	 */
	private String getTreeCfgJsonStr() {
		if (StringUtils.blank(this.getJSONCfg())) {
			// width default value : 100%
			// height default value : auto
			JSONObject json = new JSONObject();
			json.put("generate", "true");
			json.put("imgBase", this.getWebRequest().getContextPath()
					+ this.getImgBase());
			json.put("defaultImg", getDefaultImg());
			json.put("defaultImgOpen", "folderopen.gif");
			json.put("defaultImgClose", "folder.gif");
			json.put("width", "100%");
			json.put("height", "auto");
			json.put("openAtLoad", "false");
			json.put("cookies", "false");

			return json.toString();
		} else {
			return this.getJSONCfg();
		}
	}

	/**
	 * 获得树形结构JSON字符串
	 * 
	 * @return
	 */
	private String getTreeJsonStr(boolean isDsplyLeaf) {
		if (this.getJSONStruct() == null) {
			// String jsonStr = "[{'id' : 'root_1','txt' : 'Root 1','img' :
			// 'folder.gif',"
			// + "'imgopen' : 'folderopen.gif',"
			// + "'imgclose' : 'folder.gif','items' : [{'id' : 'branch_1','txt'
			// : 'Branch 1','items' : [{'id' : 'branch_1_1','txt' : 'Branch
			// 1_1'},{'id' : 'branch_1_2','txt' : 'Branch_1_2'}]"
			// + "},{'id' : 'branch_2','txt' : 'Branch 2'}]}]";

			JSONArray rootArray = new JSONArray();
			JSONObject root = new JSONObject();

			root.put("id", this.getRootId());
			root.put("txt", this.getRootText());
			root.put("img", "base.gif");
			root.put("imgopen", "base.gif");
			root.put("imgclose", "base.gif");
			root.put("items", constructorDefault(null, isDsplyLeaf));
			rootArray.put(root);
			// return jsonStr;
			return rootArray.toString();
		} else {
			return this.getJSONStruct();
		}
	}

	/**
	 * 递归构造json
	 * 
	 * @param isDsplyLeaf
	 * 
	 * @return
	 */
	private JSONArray constructorDefault(List list, boolean isDsplyLeaf) {

		if (list == null) {
			list = this.getTreeService().getDepthTree(this.getPage(),
					this.getQueryClassName(), null, 1, 0, 0);
		}

		JSONArray nodes = new JSONArray();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ITreeAdaptor node = (ITreeAdaptor) iterator.next();
			JSONObject jsonNode = new JSONObject();

			// 是否添加叶
			if (isDsplyLeaf || isBranch(node)) {
				jsonNode.put("id", node.getId());
				jsonNode.put("txt", node.getNodeName());

				// 不支持openLink
				if (StringUtils.notBlank(this.getOpenLink())) {
					// 换成单击事件
					jsonNode.put("onclick", getOnClickFun());
				}
			} else {
				continue;
			}

			if (isBranch(node)) {
				list = this.getTreeService().getDepthTree(this.getPage(),
						this.getQueryClassName(), null, node.getDepth() + 1,
						node.getLeft(), node.getRight());
				jsonNode.put("canhavechildren", "true");
				jsonNode.put("items", this
						.constructorDefault(list, isDsplyLeaf));
			}

			nodes.put(jsonNode);
		}

		return nodes;
	}

	/**
	 * 是不是枝
	 * 
	 * @param node
	 * @return
	 */
	protected boolean isBranch(ITreeAdaptor node) {
		return (node.getRight() - node.getLeft()) != 1 ? true : false;
	}
}
