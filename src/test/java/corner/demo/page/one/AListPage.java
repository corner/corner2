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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;

import corner.demo.model.one.A;
import corner.orm.tapestry.jasper.IJasperParameter;
import corner.orm.tapestry.page.PoListPage;

/**
 * @author <a href=mailto:xf@bjmaxinfo.com>xiafei</a>
 * @version $Revision$
 * @since 2.3.7
 */
public abstract class AListPage extends PoListPage implements IJasperParameter{
	
	
	@Asset("classpath:/jasper/Atest.jasper")
	public abstract IAsset getJasperAsset();
	
	@Asset("classpath:/jasper/Btest.jasper")
	public abstract IAsset getJasperBAsset();
	
//	/**
//	 * @return
//	 */
//	public InputStream getReportStream(){
//		return this.getJasperAsset().getResourceAsStream();
//	}
	
	public JRDataSource getSubdataSourceA(){
		return new JRBeanCollectionDataSource(getCollectionTest());
	}
	
	/**
	 * @throws JRException 
	 * @see corner.orm.tapestry.jasper.IJasperParameter#getJasperParameters()
	 */
	public Map getJasperParameters() throws JRException {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("jasperSubReport", getJasperReport());
		m.put("subdataSource",new JRBeanCollectionDataSource(getCollection()));
		m.put("jasperSubReportA", getJasperAsset().getResourceLocation().getResourceURL());
//		m.put("subdataSourceA",new JRBeanCollectionDataSource(getCollectionTest()));
		return m;
	}
	
	
	
	/**
	 * @return
	 * @throws JRException
	 */
	public JasperReport getJasperReport() throws JRException{
		return (JasperReport)JRLoader.loadObject(getJasperBAsset().getResourceAsStream());
	}
	
	private List getCollection() {
		List<Object> rs = new ArrayList<Object>();
		A c = new A();
		c.setName("beijing");
		rs.add(c);
		
		c = new A();
		c.setName("shanghai");
		rs.add(c);
		
		c = new A();
		c.setName("tanjing");
		rs.add(c);
		return rs;
	}
	
	/**
	 * @return
	 */
	public abstract A getAtest();
	
	/**
	 * 测试使用的
	 */
	public List getCollectionTest() {
		List<Object> rs = new ArrayList<Object>();
		A c = new A();
		c.setName("中文");
		rs.add(c);
		
		c = new A();
		c.setName("b");
		rs.add(c);
		
		c = new A();
		c.setName("c");
		rs.add(c);
		
		c = new A();
		c.setName("d");
		rs.add(c);
		
		c = new A();
		c.setName("e");
		rs.add(c);
		
		c = new A();
		c.setName("f");
		rs.add(c);
		
		return rs;
	}
}
