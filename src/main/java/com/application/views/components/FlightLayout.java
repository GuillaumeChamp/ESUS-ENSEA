package com.application.views.components;

import com.application.views.components.forms.AbstractForm;
import com.application.views.components.forms.FlightForm;
import com.application.data.entity.Flight;
import com.application.data.entity.User;
import com.application.data.service.CrmService;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class FlightLayout extends VerticalLayout {
    public FlightForm form;
    User user;
    CrmService service;

    public FlightLayout(User user, CrmService service){
        this.service = service;
        this.user = user;
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
