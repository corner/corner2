package corner.demo.page.widget;

import org.apache.tapestry.html.BasePage;

public abstract class QueryBoxTestPage extends BasePage{
	public abstract String getInputValue();
	public void doSaveEntityAction(){
		System.out.println("input value ========="+this.getInputValue());
	}
}