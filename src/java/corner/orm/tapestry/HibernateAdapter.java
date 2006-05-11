package corner.orm.tapestry;

import java.io.Serializable;

import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import corner.orm.hibernate.IPersistent;
import corner.orm.hibernate.v3.HibernateObjectRelativeUtils;
import corner.service.EntityService;
import corner.util.BeanUtils;

/**
 * ��hibernate����������л�.
 * <p>�ṩ�����е�hibernate��bean�������л�����.
 * ע��hibernate��model�����ʵ�� IPersistent�ӿ�.
 * 
 * @author jun
 * @see IPersistent
 */
public class HibernateAdapter implements SqueezeAdaptor {
	/** ���л���ǰ׺ **/
	public static final String PREFIX = "C";
	/** ʵ�������. **/
	private EntityService entityService;

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public String getPrefix() {

		return PREFIX;
	}

	public Class getDataClass() {
		return IPersistent.class;
	}

	/**
	 * �õ����л����ַ���.
	 */
	public String squeeze(DataSqueezer squeezer, final Object data) {
		String name = data.getClass().getCanonicalName();
		Serializable id = (Serializable) ((HibernateObjectRelativeUtils) getEntityService()
				.getObjectRelativeUtils()).getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						try {
							return session.getIdentifier(data);
						} catch (HibernateException hex) {
							// ok then the entity was loaded with a different
							// session
							// it is too risky to try to attach it, it may
							// already be loaded
							try {
								Object copy = session.merge(data);
								return session.getIdentifier(copy);
							} catch (Exception hex1) {
								return null;
							}
						}
					}
				});

		StringBuffer sb = new StringBuffer();
		sb.append(PREFIX);
		sb.append(name);
		sb.append(":");
		sb.append(squeezer.squeeze(id));
		return sb.toString();
	}

	/**
	 * �����л�
	 * ������ܴӿ��з����л��ö���,�򷵻�һ���½�ʵ��.
	 */
	@SuppressWarnings("unchecked")
	public Object unsqueeze(DataSqueezer squeezer, String string) {
		string = string.substring(1);

		int pos = string.indexOf(':');
		String clazzName = string.substring(0, pos);
		String idString = string.substring(pos + 1);
		Serializable id = (Serializable) squeezer.unsqueeze(idString);

		Class c;
		try {
			c = Class.forName(clazzName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			Object obj = this.getEntityService().loadEntity(c, id);
			if (obj != null)
				return obj;
		} catch (Throwable t) {
			// Handle error
		}

		return BeanUtils.instantiateClass(c);
	}

}
