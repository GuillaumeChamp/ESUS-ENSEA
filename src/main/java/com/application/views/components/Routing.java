package com.application.views.components;

import com.application.data.service.PathFinder;
import com.application.security.SecurityService;
import com.application.security.UserPrincipal;
import com.application.views.list.ListView;
import com.application.views.MainLayout;
import com.application.views.UserPage.GeneralInformation;
import com.application.views.UserPage.RegisterView;
import com.application.views.UserPage.TextView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "",layout = MainLayout.class)
public class Routing extends VerticalLayout implements BeforeEnterObserver {
    private final SecurityService service;

    /**
     * This page is used to route the user where he has to start
     * @param service security service which hold the active user (bean)
     */
    public Routing(SecurityService service){
        this.service = service;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        UserPrincipal user = service.getAuthenticatedUser();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            beforeEnterEvent.rerouteTo(ListView.class);
            return;
        }
        if(user.getStudent()==null) {
            beforeEnterEvent.rerouteTo(RegisterView.class);
            return;
        }
        String exchange = user.getStudent().getExchangeType().getName();
        if (PathFinder.index(exchange,user.getStudent().getProgress()) == PathFinder.lastIndex(exchange)) {
            beforeEnterEvent.rerouteTo(GeneralInformation.class);
            return;
        }
        beforeEnterEvent.rerouteTo(TextView.class);
    }
}
