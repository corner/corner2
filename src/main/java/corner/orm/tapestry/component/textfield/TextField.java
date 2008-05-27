// Copyright 2007 the original author or authors.
// site: http://www.bjmaxinfo.com
// file: $Id$
// created at:2006-10-09
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package corner.orm.tapestry.component.textfield;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;

import corner.util.StringUtils;

/**
 * 复写Tapestry的TextField,提供指定TextField默认值的能力
 * 
 * @author Ghost
 * @version $Revision$
 * @since 2.2.1
 */
public abstract class TextField extends org.apache.tapestry.form.TextField {

	/**
	 * @see org.apache.tapestry.form.TextField#renderFormComponent(org.apache.tapestry.IMarkupWriter,
	 *      org.apache.tapestry.IRequestCycle)
	 */
	@Override
	protected void renderFormComponent(IMarkupWriter writer, IRequestCycle cycle) {
		Object defaultValue = this.getDefaultValue();
		boolean isReadOnly = this.getOnlyRead() != null ? this.getOnlyRead()
				.booleanValue() : false;
		if (isReadOnly) {
			String value = null;
			if (defaultValue == null || defaultValue.toString().trim().length() < 1) {
				value = getTranslatedFieldSupport().format(this, getValue());
			} else {
				if (this.getValue() != null) {
					value = getTranslatedFieldSupport()
							.format(this, getValue());
				} else {
					value = getTranslatedFieldSupport().format(this,
							this.getDefaultValue());
				}
			}
			renderDelegatePrefix(writer, cycle);

			writer.beginEmpty("input");

			writer.attribute("type", isHidden() ? "password" : "text");

			writer.attribute("name", getName());

			if (isDisabled())
				writer.attribute("disabled", "disabled");

			if (value != null)
				writer.attribute("value", value);

			// 把该TextArea设置成ReadOnly
			writer.attribute("readonly", "true");

			renderIdAttribute(writer, cycle);

			renderDelegateAttributes(writer, cycle);

			getTranslatedFieldSupport()
					.renderContributions(this, writer, cycle);
			getValidatableFieldSupport().renderContributions(this, writer,
					cycle);

			renderInformalParameters(writer, cycle);

			writer.closeTag();

			renderDelegateSuffix(writer, cycle);
		} else {
			if (defaultValue == null
					|| defaultValue.toString().trim().length() < 1) {
				super.renderFormComponent(writer, cycle);
			} else {
				String value = getTranslatedFieldSupport().format(this,
						getValue());
				if (value != null && value.trim().length() > 0 && !isUseDefValue()) {
					super.renderFormComponent(writer, cycle);
				} else {
					this.setValue(this.getDefaultValue());
					super.renderFormComponent(writer, cycle);
				}
			}
		}
	}
	
	/**
	 * 如果refValue类型为Number(long,double等等),而defValue也是Number类型
	 * 那么:
	 * true:1.refValue=0 同时 defValue!=0 此时属于新增状态，使用defValue 
	 * false:2.refValue>0 此时属于编辑状态，此时使用refValue
	 * @return boolean值
	 */
	private boolean isUseDefValue(){
		Object defValue = getDefaultValue();//默认值
		Object refValue = getValue();//字段已经保存的值
		if(StringUtils.isNumber(defValue,refValue)){
			double defV = Double.valueOf(defValue.toString());
			double refV = Double.valueOf(refValue.toString());
			if(refV == 0 && defV!=0){
				return true;
			} else{
				return false;
			}
		} else{
			return false;
		}
		
	}
	
	/**
	 * 取得默认值
	 * 
	 * @return
	 */
	@Parameter
	public abstract Object getDefaultValue();

	/**
	 * 取得该TextField的属性 true:该TextField为readonly;false:该TextField不是readonly
	 * 
	 * @return
	 */
	@Parameter(defaultValue = "ognl:false")
	public abstract Boolean getOnlyRead();
}
