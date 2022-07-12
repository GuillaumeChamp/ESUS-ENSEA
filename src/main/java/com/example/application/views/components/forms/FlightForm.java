package com.example.application.views.components.forms;

import com.example.application.data.entity.Flight;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import java.time.Duration;

public class FlightForm extends AbstractForm<Flight>{
    TextField place = new TextField("Arrival station/airport");
    TextField phoneNumber = new TextField("Phone Number");
    DateTimePicker date = new DateTimePicker("Arrival day");
    ComboBox<String> meansOfTransport = new ComboBox<>("Means of transport");
    TextField airportTerminal = new TextField("Airport Terminal (if applicable)");
    Paragraph networkLabel = new Paragraph("Are you using any social network ENSEA’s students could use to join you (WhatsApp, Facebook, Telegram…)?");
    TextField network = new TextField();
    TextField departure = new TextField("Departure City");
    TextField transportID = new TextField("Transport ID");

    /**
     * Create a flight form setting up the constraints
     */
    public FlightForm(){
        this.binder = new BeanValidationBinder<>(Flight.class);
        binder.bindInstanceFields(this);
        phoneNumber.setPattern("^[+][0-9]{2,3}[ ][0-9]{9,10}$");
        phoneNumber.setHelperText("Format : +123 456789000");
        meansOfTransport.setItems("Plane/avion", "Train/train","Car/voiture","Other/autre");
        this.translate();
        date.setStep(Duration.ofMinutes(10));
        add(networkLabel,network,phoneNumber,meansOfTransport,place,airportTerminal,departure,date,transportID,createButtonsLayout());
        removeDelete();
    }

    /**
     * This method is used to translate labels in French
     */
    private void translate(){
        if (MainLayout.EN){
            return;
        }
        place.setLabel("Station/aéoroport d'arrivé");
        phoneNumber.setLabel("Numéro de téléphone");
        date.setLabel("date d'arrivée");
        meansOfTransport.setLabel("Moyen de transport");
        airportTerminal.setLabel("Terminal (si existe)");
        networkLabel.setText("Utilisez vous un réseau social sur lequel les étudiants de l'ENSEA pourrait vous contacter (WhatsApp, Facebook, Telegram…) ?");
        departure.setLabel("Ville de départ");
        transportID.setLabel("Numéro de train/vol");
    }
}
