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

package corner.orm.tapestry.translator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.form.translator.AbstractTranslator;
import org.apache.tapestry.json.JSONLiteral;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * 针对数字类型的translator
 * 
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @version $Revision$
 * @since 2.3
 */
public class NumTranslator extends AbstractTranslator {
	private RegexpMatcher _matcher = new RegexpMatcher();

	private static final String DEFINE_PATTERN = "^\\{(\\d+):(\\d+)\\}$";
	private static final String NEGATIVE_STR="-?";
	private static final String REPLACE_STR="\\\\d{0,$1}(\\\\.\\\\d{0,$2})?\\";

	private String srcPattern;

	private String validatePattern;

	private String formatPattern;
	
	private boolean _omitZero = true;
	private boolean _negative=true;

	public NumTranslator() {
		this.setPattern(getDefaultPattern());
	}

	// TODO: Needed until HIVEMIND-134 fix is available
	public NumTranslator(String initializer) {
		PropertyUtils.configureProperties(this, initializer);
		 if (HiveMind.isBlank(srcPattern))
	        {
	            this.setPattern(getDefaultPattern());
	        }
	}

	/**
	 * 得到默认的pattern
	 * @return
	 */
	protected String getDefaultPattern() {
		return "{100:0}";
	}

	@Override
	protected String formatObject(IFormComponent field, Locale locale,
			Object object) {
		
		if(_omitZero){
			Number number = (Number) object;
			if (number.doubleValue() == 0) { //加入值为0的时候，不显示该值
	
				return "";
			}
		}
		Format format = getFormat(locale);

		return format.format(object);
	}

	@Override
	protected Object parseText(IFormComponent field,
			ValidationMessages messages, String text) throws ValidatorException {
		Format format = getFormat(messages.getLocale());

		try {
			return format.parseObject(text);
		} catch (ParseException ex) {
			throw new ValidatorException(buildMessage(messages, field));
		}
	}

	protected Format getFormat(Locale locale) {
		//初始化pattern
		initPattern();
		return getDecimalFormat(locale);
	}

	public DecimalFormat getDecimalFormat(Locale locale) {
		return new DecimalFormat(formatPattern,
				new DecimalFormatSymbols(locale));
	}

	/**
	 * @see org.apache.tapestry.form.translator.AbstractTranslator#renderContribution(org.apache.tapestry.IMarkupWriter,
	 *      org.apache.tapestry.IRequestCycle,
	 *      org.apache.tapestry.form.FormComponentContributorContext,
	 *      org.apache.tapestry.form.IFormComponent)
	 */
	@Override
	public void renderContribution(IMarkupWriter writer, IRequestCycle cycle,
			FormComponentContributorContext context, IFormComponent field) {
		super.renderContribution(writer, cycle, context, field);
		//初始化pattern
		initPattern();
		String pattern = _matcher.getEscapedPatternString(validatePattern);

		JSONObject profile = context.getProfile();

		if (!profile.has(ValidationConstants.CONSTRAINTS)) {
			profile.put(ValidationConstants.CONSTRAINTS, new JSONObject());
		}
		JSONObject cons = profile
				.getJSONObject(ValidationConstants.CONSTRAINTS);

		//通过正则表达式，对前台输入数据进行验证.
		accumulateProperty(cons, field.getClientId(),
				new JSONLiteral("[tapestry.form.validation.isValidPattern,\""
						+ pattern + "\"]"));

		accumulateProfileProperty(field, profile,
				ValidationConstants.CONSTRAINTS, buildMessage(context, field));
	}

	private String buildMessage(ValidationMessages messages,
			IFormComponent field) {
		return messages.formatValidationMessage(getMessage(field.getDisplayName()),
				ValidationStrings.PATTERN_MISMATCH, new Object[] {
						field.getDisplayName(), validatePattern });
	}

	
	public String getMessage(String filedName) {
		if (this.srcPattern == null) {
			return super.getMessage();
		}
		return MessageFormat.format(srcPattern.replaceAll(DEFINE_PATTERN,
				"{0}是错误的数字格式，正确的为：小数点前面至多$1位，后面至多$2位."+(this.isNegative()?"":"且不能是负数.")),filedName);
	}

	/**
	 * 得到被替换的字符串
	 * @return 
	 */
	private String getReplaceString(){
		StringBuffer sb=new StringBuffer();
		sb.append("^");
		if(this.isNegative()){ //判断是否有负数
			sb.append(NEGATIVE_STR);
		}
		sb.append(REPLACE_STR);
		sb.append("$");
		return sb.toString();
	}
	
	public void setPattern(String pattern) {
		if (!pattern.matches(DEFINE_PATTERN)) {
			throw new ApplicationRuntimeException("错误的pattern定义，正确的格式应该为：{x:x}");
		}
		this.srcPattern = pattern;
		
		
	}
	/**
	 * 对pattern进行初始化处理
	 *
	 */
	private void initPattern(){
		if(this.srcPattern==null){
			throw new ApplicationRuntimeException("初始化pattern错误，没有定义pattern!");
		}
		
		this.validatePattern = srcPattern.replaceAll(DEFINE_PATTERN,getReplaceString());
		int places = Integer.parseInt(srcPattern.replaceAll(DEFINE_PATTERN,
				"$2"));
		formatPattern = "#";
		if (places > 0) {
			formatPattern += ".";
		}
		while (places-- > 0) {
			formatPattern += "0";
		}
	}
	 /**
     * If true (which is the default for the property), then values that are 0 are rendered to an
     * empty string, not "0" or "0.00". This is useful in most cases where the field is optional; it
     * allows the field to render blank when no value is present.
     */

    public void setOmitZero(boolean omitZero)
    {
        _omitZero = omitZero;
    }

	/**
	 * @return Returns the _negative.
	 */
	public boolean isNegative() {
		return _negative;
	}

	/**
	 * @param _negative The _negative to set.
	 */
	public void setNegative(boolean _negative) {
		this._negative = _negative;
	}
}