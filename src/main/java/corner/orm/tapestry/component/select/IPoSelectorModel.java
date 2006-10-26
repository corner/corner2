package corner.orm.tapestry.component.select;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.dojo.form.IAutocompleteModel;
import org.apache.tapestry.services.DataSqueezer;

import corner.service.EntityService;

public interface IPoSelectorModel extends IAutocompleteModel{
	/**
	 * @return Returns the squeezer.
	 */
	public abstract DataSqueezer getSqueezer();

	/**
	 * @return Returns the entityService.
	 */
	public abstract EntityService getEntityService();

	/**
	 * @return Returns the field.
	 */
	public abstract String getLabelField();

	/**
	 * @return Returns the poClass.
	 */
	public abstract Class getPoClass();
	/**
	 * 得到返回值的字段列表.
	 * @return 返回字段值的列表。
	 */
	public String[] getReturnValueFields();

	/**
	 * @param field The field to set.
	 */
	public void setLabelField(String field);

	public void setEntityService(EntityService entityService);

	public void setDataSqueezer(DataSqueezer squeezer);

	public void setReturnValueFields(String ...strings);

	/**
	 * @param poClass The poClass to set.
	 */
	public void setPoClass(Class poClass);

	public void setSelectFilter(ISelectFilter filter);

	/**
	 * 得到此model所对应的组件.
	 * @return 组件对象
	 */
	public abstract IComponent getComponent();

	public abstract void setComponent(IComponent selector);

	public abstract void setUpdateFields(String[] updateFields);
	public abstract String[] getUpdateFields();

}