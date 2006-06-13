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

package corner.orm.tapestry;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.valid.RenderString;
/**
 * 可以显示raw书局的TableValueRenderer.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision$
 * @since	2005-10-21
 */
public class SimpleRawTableValueRendererSource implements ITableRendererSource {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -5955327358717411812L;
	private static final String EMPTY_REPRESENTATION = "&nbsp;";

	/**
	 * 
	 * @see org.apache.tapestry.contrib.table.model.ITableRendererSource#getRenderer(org.apache.tapestry.IRequestCycle, org.apache.tapestry.contrib.table.model.ITableModelSource, org.apache.tapestry.contrib.table.model.ITableColumn, java.lang.Object)
	 */
	public IRender getRenderer(IRequestCycle objCycle, ITableModelSource objSource,
			ITableColumn objColumn, Object objRow) {
		SimpleTableColumn objSimpleColumn=(SimpleTableColumn)objColumn;
		
		Object objValue = objSimpleColumn.getColumnValue(objRow);
		if(objValue==null)
			return new RenderString(EMPTY_REPRESENTATION,true);
		return new RenderString(objValue.toString(),true);
	}

}