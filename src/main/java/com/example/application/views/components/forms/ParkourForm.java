package com.example.application.views.components.forms;

import com.example.application.data.entity.Parkour;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

public class ParkourForm extends AbstractForm<Parkour> {
    TextField semester = new TextField("Semester");
    TextField option_suivi = new TextField("Options");
    TextField major = new TextField("Major");

    public ParkourForm(){
        this.binder = new BeanValidationBinder<>(Parkour.class);
        addClassName("AccountForm");
        binder.bindInstanceFields(this);
        add(semester, option_suivi,major,createButtonsLayout());
    }
}
