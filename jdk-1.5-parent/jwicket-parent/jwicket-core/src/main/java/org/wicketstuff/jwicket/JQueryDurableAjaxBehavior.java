package org.wicketstuff.jwicket;


import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;


/**
 * Common superclass for all behaviors that last as long as the {@link Component} lives.
 * Every time the component is rendered (e.g. after an Ajax update of the {@link Component})
 * the javascript code for the behavior is rendered into it's {@link IHeaderResponse}.
 * To ensure that the DOM for the {@link Component} is already rendered, the javascript for the
 * behavior is rendered through the {@link IHeaderResponse#renderOnDomReadyJavascript(String)}
 * method.
 *
 * Each subclass that implements any such durable behavior must implement the method
 * {@link #getJsBuilder()}. The method returns a {@link JsBuilder} that must render all
 * needed javascript code for this behavior.
 */
public abstract class JQueryDurableAjaxBehavior extends JQueryAjaxBehavior {

	private static final long serialVersionUID = 1L;

	protected JQueryDurableAjaxBehavior(
			final JQueryResourceReference baseLibrary) {
		super(baseLibrary);
	}

	protected JQueryDurableAjaxBehavior(
			final JQueryResourceReference baseLibrary,
			final JQueryResourceReference... requiredLibraries) {
		super(baseLibrary, requiredLibraries);
	}


	protected abstract JsBuilder getJsBuilder();


	public void updateBehavior(final AjaxRequestTarget target) {
		target.appendJavascript(getJsBuilder().toString());
	}

	
	protected boolean rendered = false;
	public boolean isAlreadyRendered() {
		return this.rendered;
	}
	private boolean restoreAfterRedraw = true;
	public void setRestoreAfterRedraw(final boolean value) {
		this.restoreAfterRedraw = value;
	}

	@Override
	/**
	 * Do not override this method unless you know exactly what you do.
	 * It is an internal method that handles the rendering of the JavaScript
	 * stuff for the behavior.
	 *
	 * If you need to override this method, you must call {@code super.renderHead(response)}.
	 */
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		if (!rendered || restoreAfterRedraw)
			response.renderOnDomReadyJavascript(getJsBuilder().toString());
		
		rendered = true;
	}
}
