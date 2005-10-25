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

package corner.util;

/**
 * ���ڷ�ҳ��bean.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2005-10-25
 */
public class PaginationBean {
	/***Ĭ�ϵ�ÿҳ��¼��*/
	public static final int DEFAULT_PAGE_SIZE=10;
	/** ��ʼ�ļ�¼��,��0��ʼ **/
	private int first;
	/** ÿҳ��ʾ��¼��,ͨ��Ĭ��Ϊ{@link #DEFAULT_PAGE_SIZE}**/
	private int pageSize=10;
	/**�õ��ܵļ�¼��**/
	private int rowCount;
	/**
	 * @return Returns the first.
	 */
	public int getFirst() {
		return first;
	}
	/**
	 * @param first The first to set.
	 */
	public void setFirst(int first) {
		this.first = first;
	}
	/**
	 * @return Returns the pageSize.
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize The pageSize to set.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return Returns the rowCount.
	 */
	public int getRowCount() {
		return rowCount;
	}
	/**
	 * @param rowCount The rowCount to set.
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	
}
