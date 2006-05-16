package corner.model;

/**
 * ����Ĵ����
 * @author Jun Tsai
 * @version $Revison$
 * @since 2006-4-18
 */
public abstract class AbstractMarkCodeModel {
	/**
	 * code�����������ݿ���Ϊ��������,Ʃ��:2345��
	 * @hibernate.property column="MARK_Code" length="20"
	 * 
	 */
	private String code=null;
	/**
	 * ƴ��������ѯʹ�ã����������ݿ���Ϊ��ĸ���ͣ�Ʃ�� abcdj��
	 * @hibernate.property column="MARK_PINYIN" length="20"
	 * 
	 */
	private String pinyin;
	/**
	 * 
	 * ��Ŀ������ѯʹ�ã����������ݿ���Ϊ�������ͣ�Ʃ�� ���ǡ�
	 * @hibernate.property column="MARK_ITEM" length="20"
	 * 
	 */
	private String item;
	
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

	
	
}
