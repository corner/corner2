package corner.orm.tapestry;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.link.ILinkRenderer;

/**
 * ��չtapestry��renderer,��JavaScript�в���URL.
 * 
 * @author Jun Tsai
 * @version $Revison$
 * @since 2006-4-18
 */
public class RawURLLinkRenderer implements ILinkRenderer {
	
	public void renderLink(IMarkupWriter writer, IRequestCycle cycle,
			ILinkComponent linkComponent) {
		writer.print(linkComponent.getLink(cycle).getAbsoluteURL(), true);
	}
}
