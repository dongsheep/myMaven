package com.dong.constant;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.html.ExportTagToContentRule;
import org.sitemesh.tagprocessor.State;

/**
 * 自定义sitemesh标签
 * 
 * @author xiedongxiao
 *
 */
public class MySiteMeshFilter implements TagRuleBundle {

	public void install(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
		defaultState.addRule("myTag",
				new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("myTag"), false));
	}

	public void cleanUp(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
		// TODO Auto-generated method stub

	}

}
