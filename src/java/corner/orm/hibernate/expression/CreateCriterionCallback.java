package corner.orm.hibernate.expression;

/**
 * �����﷨�������Ļص�.
 * <p>�ṩ�˷����û�����Ĳ�ѯ�ַ���.Ʃ��: asdf or asdf and fda ��.
 * 
 * @author Jun Tsai
 *
 */
public interface CreateCriterionCallback {
	/**
	 * ����hibernate��Criterion,���ݸ����ı��ʽ�Լ���Ӧ��ֵ.
	 * @param expression ���ʽ����,Ʃ��: or and ��.
	 * @param value ��Ӧ��ֵ.
	 */
	public void doCreateCriterion(String expression,String value);
}
