//==============================================================================
//file :        BeanUtils.java
//project:      corner-utils
//
//last change:	date:       $Date: 2005-06-15 16:52:07 +0800 (Wed, 15 Jun 2005) $
//           	by:         $Author: jcai $
//           	revision:   $Revision: 35 $
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================

package corner.util;

import java.util.Map;

import corner.util.ognl.OgnlUtil;



/**
 * �趨bean�ĳ��ú�������Ҫ������Ognl��
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision: 35 $
 */
public class BeanUtils {
	static {

		//only for opensymphony oscore
		System.setProperty("bean.provider", "ognl");

		//only form apache beanutils
		//		Converter igConverter = new IntegerConverter(null); 
		//		ConvertUtils.register(igConverter, Integer.TYPE); // Native type
		//		ConvertUtils.register(igConverter, Integer.class);
		//		
		//		Converter dateConverter= new DateConverter();
		//		ConvertUtils.register(dateConverter,Timestamp.class);
		//		ConvertUtils.register(dateConverter, Date.class);

	}
	/**
	 * �趨����
	 * @param bean bean
	 * @param pro ��������
	 * @param value ֵ
	 */
	public static void setProperty(Object bean, String pro, Object value) {
		OgnlUtil.setProperty(bean, pro, value);
		//				com.opensymphony.util.BeanUtils.setValue(bean, pro, value);
		//		if (value == null)
		//			return;
		//		try {
		//			org.apache.commons.beanutils.BeanUtils.setProperty(
		//				bean,
		//				pro,
		//				value);
		//		} catch (IllegalAccessException e) {
		//			e.printStackTrace();
		//		} catch (InvocationTargetException e) {
		//			e.printStackTrace();
		//		}
	}

	/**
	 * �İ�bean������ֵ��
	 * @param bean bean.
	 * @param pro ���Ե�����.
	 * @return ����ֵ.
	 */
	public static Object getProperty(Object bean, String pro) {

				try {
					return org.apache.commons.beanutils.BeanUtils.getProperty(
						bean,
						pro);
				} catch (Exception e) {
					return null;
				}
	}

	/**
	 * ����һ��orig�����Ե�dest��.
	 * @param dest ��Ҫ��ֵ��bean.
	 * @param orig ԭʼbean.
	 */
	public static void setProperties(Object dest, Object orig) {
		OgnlUtil.copy(orig, dest, null);
		//		com.opensymphony.util.BeanUtils.setValues(dest, orig,null);
		//		try {
		//			org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
		//		} catch (IllegalAccessException e) {
		//			e.printStackTrace();
		//		} catch (InvocationTargetException e) {
		//			e.printStackTrace();
		//		}
	}

	

	/**
	 * ��map�еõ�ֵ���и�ֵ��bean.
	 * @param bean ��Ҫ��ֵ��bean.
	 * @param map ����ֵ��map.
	 */
	public static void setProperties(Object bean, Map map) {

		
		OgnlUtil.setProperties(map, bean);

		//		com.opensymphony.util.BeanUtils.setValues(bean, map, null);
		//		try {
		//			org.apache.commons.beanutils.BeanUtils.populate(bean, map);
		//		} catch (IllegalAccessException e) {
		//			e.printStackTrace();
		//		} catch (InvocationTargetException e) {
		//			e.printStackTrace();
		//		}
	}

	/**
	 * ʵ����һ����.
	 * @param clazz �������.
	 * @return ʵ��������.
	 */
	public static Object instantiateClass(String clazz) {
		try {
			return Class.forName(clazz).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}

