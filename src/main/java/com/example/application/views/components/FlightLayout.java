package com.example.application.views.components;

import com.example.application.data.entity.Flight;
import com.example.application.data.entity.User;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.example.application.views.components.forms.AbstractForm;
import com.example.application.views.components.forms.FlightForm;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("Flight")
@Route(value = "flight", layout = MainLayout.class)
public class FlightLayout extends VerticalLayout {
    public FlightForm form;
    User user;
    CrmService service;

    public FlightLayout(User user, CrmService service){
        this.service = service;
        this.user = user;
        setSizeFull();
        configureForm();
        add(form);

    }

    protected void configureForm() {
        form = new FlightForm();
        form.setWidth("25em");
        try {
            Flight flight = user.getStudent().getFlight();
            if (flight!=null)  form.setObject(flight);
            else form.setObject(new Flight());
            form.removeDelete();
            form.removeClose();
        }catch (NullPointerException e){
            add(new Paragraph("non-student so you might be an administrator, delete your flight once finished"));
            form.setObject(new Flight());
        }
    }
    public void save(AbstractForm.SaveEvent event){
        service.saveFlight((Flight) event.getObject());
        user.getStudent().setFlight((Flight) event.getObject());
        service.saveStudent(user.getStudent());
    }

}
