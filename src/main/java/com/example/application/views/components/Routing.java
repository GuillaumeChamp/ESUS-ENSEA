package com.example.application.views.components;

import com.example.application.data.PathFinder;
import com.example.application.security.SecurityService;
import com.example.application.security.UserPrincipal;
import com.example.application.views.list.ListView;
import com.example.application.views.MainLayout;
import com.example.application.views.UserPage.GeneralInformation;
import com.example.application.views.UserPage.RegisterView;
import com.example.application.views.UserPage.TextView;
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
        if (PathFinder.index(user.getStudent().getExchangeType().getName(),user.getStudent().getProgress()) == PathFinder.lastIndex(user.getStudent().getExchangeType().getName())) {
            beforeEnterEvent.rerouteTo(GeneralInformation.class);
            return;
        }
        beforeEnterEvent.rerouteTo(TextView.class);
    }
}
