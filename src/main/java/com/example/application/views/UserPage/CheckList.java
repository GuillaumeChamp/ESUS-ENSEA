package com.example.application.views.UserPage;

import com.example.application.data.entity.Student;
import com.example.application.data.entity.Triggers;
import com.example.application.data.entity.User;
import com.example.application.data.service.CrmService;
import com.example.application.security.SecurityService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.lang.reflect.Field;
import java.util.ArrayList;

@PermitAll
@Route(value = "check", layout = MainLayout.class)
public class CheckList extends VerticalLayout {
    private final User logUser;
    private final CrmService database;
    private final ArrayList<Checkbox> list= new ArrayList<>();


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
        list.forEach(this::add);
        add(confirm);
        check();
        setSizeFull();
    }

    /**
     * Construct the elements
     */
    private void buildStudent(){
        Student student = logUser.getStudent();
        Field[]  fields= student.getTriggers().getClass().getFields();
        for (Field f :fields){
            if(f.getType().equals(Boolean.TYPE)) {
                try {
                    list.add(build(f,student.getTriggers()));
                } catch (IllegalAccessException ignored) {}
            }
        }
        H1 header = new H1("Student CheckList | " + student.getFirstName() + " " + student.getLastName());
        H3 subHeader = new H3("some information are shared with admin" );
        add(header,subHeader);
    }
    private Checkbox build(Field f,Triggers triggers) throws IllegalAccessException {
        Checkbox checkbox = new Checkbox(f.getName());
        checkbox.setValue(f.getBoolean(triggers));
        checkbox.getStyle().set("lumo-disabled-text-color","rgba(149, 196, 31, 0.5)");
        checkbox.addClickListener(e-> {
            try {
                f.setBoolean(triggers,checkbox.getValue());
            } catch (IllegalAccessException exception) {
                System.out.println("unable to set action on checkbox");
            }
        });
        return checkbox;
    }

    /**
     * Construct the admin check list
     */
    private void buildAdmin(){
        H1  header = new H1("Admin CheckList | " + logUser.getUsername());
        add(header);
    }

    /**
     * Check all CheckBox
     */
    private void check(){
        for (Checkbox c : list){
            c.setEnabled(!c.getValue());
        }
    }
    private void save(){
        database.updateTriggers(this.logUser.getStudent().getTriggers());
        check();
    }
}
