//==============================================================================
// file :       $Id:ISelectFilter.java 2023 2006-10-17 05:18:45Z jcai $
// project:     corner
//
// last change: date:       $Date:2006-10-17 05:18:45Z $
//              by:         $Author:jcai $
//              revision:   $Revision:2023 $
//------------------------------------------------------------------------------
//copyright:	Beijing Maxinfo Technology Ltd. http://www.bjmaxinfo.com
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.orm.tapestry.component.select;

import java.util.Map;

/**
 * 对字符串进行查询的过滤器.
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @author ghostbb
 * @version $Revision:2023 $
 * @since 2.2.1
 */
public interface ISelectFilter {

	/**
	 * 对给定的字符串进行搜索。
	 * <p>
	 * 返回的map，key为选择器的下拉列表的显示的值，而value为待处理的数据.
	 * @param match 待搜索的字符串
	 * @param model 选择器的模型.
	 * @return 一个供选择的map。
	 */
	Map query(String match,IPoSelectorModel model);

	/**
	 * 返回label字段名称.
	 * @return label字段名称.
	 */
	public String getLabelField();

}