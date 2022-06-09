package com.example.application.views;

import com.example.application.data.service.CrmService;
import com.example.application.views.components.Prompter;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route("login") 
@PageTitle("Login | Welcome at ENSEA")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private final LoginForm login = new LoginForm();

	public LoginView(CrmService service){
		addClassName("login-view");
		setSizeFull(); 
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		login.setAction("login");
		Image logo = new Image("/images/logo_full.png","Le logo");
		logo.setHeight(200,Unit.PIXELS);
		logo.setWidth(200, Unit.PIXELS);
		H1 greeting = new H1("Welcome at ENSEA");
		add(logo,greeting, login);
		login.addForgotPasswordListener(e-> Prompter.promptRecover(this,service));
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if(beforeEnterEvent.getLocation()
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            login.setError(true);
        }
	}
}