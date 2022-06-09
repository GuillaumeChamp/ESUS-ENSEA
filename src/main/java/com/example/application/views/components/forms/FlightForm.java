package com.example.application.views.components.forms;

import com.example.application.data.entity.Flight;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import java.time.Duration;

public class FlightForm extends AbstractForm<Flight>{
    TextField place = new TextField("Arrival station/airport");
    TextField phoneNumber = new TextField("Phone Number");
    DateTimePicker date = new DateTimePicker("Arrival day");
    ComboBox<String> meansOfTransport = new ComboBox<>("Means of transport");
    TextField airportTerminal = new TextField("Airport Terminal (if applicable)");
    TextField network = new TextField("Are you using any social network ENSEA’s students could use to join you (WhatsApp, Facebook, Telegram…)?");
    TextField departure = new TextField("Departure City");
    TextField transportID = new TextField("Transport ID");

    public FlightForm(){
        this.binder = new BeanValidationBinder<>(Flight.class);
        binder.bindInstanceFields(this);
        phoneNumber.setPattern("^[+][0-9]{2,3}[ ][0-9]{9,10}$");
        phoneNumber.setHelperText("Format : +123 456789000");
        meansOfTransport.setItems("Plane", "Train","Cars","Other");
        date.setStep(Duration.ofMinutes(10));
        add(network,phoneNumber,meansOfTransport,place,airportTerminal,departure,date,transportID,createButtonsLayout());
        removeDelete();
    }
}
