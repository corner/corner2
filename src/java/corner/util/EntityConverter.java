//==============================================================================
// file :       EntityConverter.java
// project:     corner-utils
//
// last change: date:       $Date: 2005-06-15 16:52:07 +0800 (Wed, 15 Jun 2005) $
//              by:         $Author: jcai $
//              revision:   $Revision: 35 $
//------------------------------------------------------------------------------
//copyright:	China Java Users Group http://cnjug.dev.java.net
//License:      the Apache License, Version 2.0 (the "License")
//==============================================================================
package corner.util;

/**
 * ת�����һЩ���ú���.
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision: 35 $
 */
public class EntityConverter {
	/**
	 * ת���������.
	 * @param name ��Ҫת����ֵ.
	 * @param wimpyCaps �Ƿ���Ҫ����ĸ��д,���Ϊfalse,����ĸ��д,����Сд.
	 * @return ת����Ľ��.
	 */
	public static String convertName(String name, boolean wimpyCaps) {
		StringBuffer buffer = new StringBuffer(name.length());
		char[] list = name.toLowerCase().toCharArray();

		for (int i = 0; i < list.length; i++) {
			if ((i == 0) && !wimpyCaps) {
				buffer.append(Character.toUpperCase(list[i]));
			} else if (
				(list[i] == '_') && ((i + 1) < list.length) && (i != 0)) {
				buffer.append(Character.toUpperCase(list[++i]));
			} else {
				buffer.append(list[i]);
			}
		}

		return buffer.toString();
	}
}
