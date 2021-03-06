package com.fbinsight.client.widgets;

import com.fbinsight.client.gin.Injector;
import com.fbinsight.client.images.ImageFactory;
import com.fbinsight.shared.config.Configuration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

public class FBButton extends Composite {

	private SimplePanel container;

	public FBButton() {
		container = new SimplePanel();
		initWidget(container);
		
		Injector.INSTANCE.getApplication().getConfig(new AsyncCallback<Configuration>() {
			public void onSuccess(Configuration result) {
				initAPI(result.getSocialParams().getFacebookApiKey());
			};
			public void onFailure(Throwable caught) {
			}
		});
		Image image = ImageFactory.facebookButton();
		image.addStyleName("hand");
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				login();
			}
		});
		container.setWidget(image);
	}
	
	private native void initAPI(String key) /*-{
		$wnd.FB.init({
			appId : key,
			channelURL : '//channel.html',
			status : true,
			cookie : true,
			oauth : true,
			xfbml : true
		});
	}-*/;

//	private native void login()/*-{
//		client = new Object();
//		$wnd.FB.login(function(response) {
//			if (response.authResponse) {
//				client.accessToken = response.authResponse.accessToken;
//				$wnd.FB.api('/me', function(response) {
//		    	    client.name = response.name;
//		    		@com.fbinsight.client.widgets.FBButton::onComplete(Lcom/fbinsight/client/widgets/SocialClient;)(client);
//	 			});
//			} else {
//				@com.fbinsight.client.widgets.FBButton::onComplete(Lcom/fbinsight/client/widgets/SocialClient;)(null);
//			}
//		});
//		
//	}-*/;
	
	private native void login()/*-{
	client = new Object();
	$wnd.FB.login(function(response) {
		if (response.authResponse) {
			client.accessToken = response.authResponse.accessToken;
			$wnd.FB.api('/me', function(response) {
	    	    client.name = response.name;
	    	    client.id = response.id;
	    		@com.fbinsight.client.widgets.FBButton::onComplete(Lcom/fbinsight/client/widgets/SocialClient;)(client);
 			});
		} else {
			@com.fbinsight.client.widgets.FBButton::onComplete(Lcom/fbinsight/client/widgets/SocialClient;)(null);
		}
	}, {scope: "user_about_me , friends_about_me, friends_birthday , friends_education_history , friends_events , friends_hometown , friends_interests , friends_likes , friends_location , friends_website , offline_access" });
		
	}-*/;
	
	private static native void logout()/*-{
		$wnd.FB.logout(function(response) {
	   });
	}-*/;

	
	public static void onComplete(SocialClient client) {
		GWT.log("sc = " + client.getName());
		Injector.INSTANCE.getEventBus().fireEvent(new LoginEvent(client));
//		logout();
	}

}
