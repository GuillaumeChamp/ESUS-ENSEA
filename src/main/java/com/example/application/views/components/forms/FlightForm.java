package com.example.application.views.components.forms;

import com.example.application.data.entity.Flight;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import java.time.Duration;

public class FlightForm extends AbstractForm<Flight>{
    DateTimePicker date = new DateTimePicker("Arrival day");
    TextField phoneNumber = new TextField("Phone Number");
    TextField place = new TextField("where ?");

    public FlightForm(){
        this.binder = new BeanValidationBinder<>(Flight.class);
        binder.bindInstanceFields(this);
        phoneNumber.setPattern("^[+][0-9]{2,3}[ ][0-9]{9,10}$");
        phoneNumber.setHelperText("Format : +123 456789000");
        date.setStep(Duration.ofMinutes(10));
        add(date,place,phoneNumber,createButtonsLayout());
        removeDelete();
    }


}
