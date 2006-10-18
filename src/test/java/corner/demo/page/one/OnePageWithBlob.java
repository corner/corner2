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

package corner.demo.page.one;

import corner.demo.model.one.A;
import corner.orm.hibernate.v3.MatrixRow;
import corner.orm.tapestry.page.AbstractEntityFormPage;
import corner.orm.tapestry.service.blob.IBlobPageDelegate;
import corner.orm.tapestry.service.blob.SqueezeBlobPageDelegate;

/**
 * 对blob的处理
 * 
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @version $Revision$
 * @since 2.2.1
 */
public abstract class OnePageWithBlob extends AbstractEntityFormPage<A> {

	/**
	 * @see corner.orm.tapestry.page.AbstractEntityPage#saveOrUpdateEntity()
	 */
	@Override
	protected void saveOrUpdateEntity() {

		super.saveOrUpdateEntity();

		if (isEditBlob()) {
			IBlobPageDelegate<A> delegate = new SqueezeBlobPageDelegate<A>(
					A.class, getUploadFile(), this.getEntity(), this
							.getEntityService());
			delegate.save();
		}
	}
	
	public MatrixRow getRefVector(){
//		only for test
		MatrixRow matrix=new MatrixRow();
		matrix.add("L");
		matrix.add("XL");
		return matrix;
	}

}
