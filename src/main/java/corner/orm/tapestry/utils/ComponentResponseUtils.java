/*		
 * Copyright 2006-2007 The Beijing Maxinfo Technology Ltd. 
 * site:http://www.bjmaxinfo.com
 * file : $Id$
 * created at:2007-6-13
 */

package corner.orm.tapestry.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.web.WebResponse;

/**
 * 提供piano-core中自定义组件的一些通用的工具方法
 * 
 * @author <a href=mailto:Ghostbb@bjmaxinfo.com>Ghostbb</a>
 * @version $Revision$
 * @since 0.8.5.1
 */
public class ComponentResponseUtils {
	
	/**
	 * 生成Excel时候，指定数据的生成类型
	 * 
	 * full:当前查询的全部数据 page:当前查询，当前页面显示的数据
	 */
	public static final String EXCEL_DATA_GENERATE_TYPE_FULL = "full";// 全部数据

	public static final String EXCEL_DATA_GENERATE_TYPE_PAGE = "page";// 当前页面的数据

	/** 附件的定义 * */
	private final static String ATTACHEMENT_FILE = "attachment; filename=\"%s\";";

	/** firefox 下文件采用base64进行编码 * */
	private final static String MOZILLA_DOWNLOAD_FILE_NAME = "=?UTF-8?B?%s?=";

	// 对下载的文件名进行encode，
	// 参考：http://eddysheng.javaeye.com/blog/50414
	private static String processFileName(String fileName, String agent)
			throws IOException {
		String codedfilename = null;
		if (null != agent && -1 != agent.indexOf("MSIE")) {// IE
			codedfilename = URLEncoder.encode(fileName, "UTF-8");
		} else if (null != agent && -1 != agent.indexOf("Mozilla")) { // Mozilla
			// firefox
			codedfilename = String
					.format(MOZILLA_DOWNLOAD_FILE_NAME, new String(Base64
							.encodeBase64(fileName.getBytes("UTF-8"))));
		} else {
			codedfilename = fileName;
		}
		return codedfilename;
	}
	
	/**
	 * 根据给定的文件名称构造可以下载文件的resoonse
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public static void constructResponse(String fileName, String extensionName, IRequestCycle requestCycle, WebResponse response) throws IOException {

		if (fileName == null || fileName.length() == 0) {// 如果没有定义文件名称，使用时间作为文件名称
			Date date = new Date();
			fileName = String.valueOf(date.getTime());
		}
		// 加上后缀
		fileName += extensionName;
		// 得到用户的agent,用来判断浏览器的类型
		String userAgent = requestCycle.getInfrastructure().getRequest()
				.getHeader("USER-AGENT");
		response.setHeader("Content-Disposition", String.format(
				ATTACHEMENT_FILE, processFileName(fileName, userAgent)));
	}
}
