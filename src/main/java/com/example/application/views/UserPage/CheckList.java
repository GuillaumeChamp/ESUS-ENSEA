package com.example.application.views.UserPage;

import com.example.application.data.entity.Student;
import com.example.application.data.entity.User;
import com.example.application.data.generator.CheckListBuilder;
import com.example.application.data.service.CrmService;
import com.example.application.security.SecurityService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "check", layout = MainLayout.class)
public class CheckList extends VerticalLayout {
    private final User logUser;
    private final CrmService database;


    public CheckList(SecurityService security, CrmService database) {
        logUser = security.getAuthenticatedUser().getUser();
        this.database = database;
        if (security.getAuthenticatedUser().isAdmin()) {
            buildAdmin();
        }
        else {
            buildStudent();
        }
        Button confirm = new Button("Validate");
        confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        confirm.addClickListener(buttonClickEvent -> save());
        add(confirm);
        setSizeFull();
    }

    /**
     * Construct the elements
     */
    private void buildStudent(){
        Student student = logUser.getStudent();
        H1 header = new H1("Student CheckList | " + student.getFirstName() + " " + student.getLastName());
        H3 subHeader = new H3("some information are shared with admin" );
        add(header,subHeader);
        CheckListBuilder.fill(this,student.getTriggers());
    }

    /**
     * Construct the admin check list
     */
    private void buildAdmin(){
        H1  header = new H1("Admin CheckList | " + logUser.getUsername());
        add(header);
    }

    private void save(){
        database.updateTriggers(this.logUser.getStudent().getTriggers());
    }
}
