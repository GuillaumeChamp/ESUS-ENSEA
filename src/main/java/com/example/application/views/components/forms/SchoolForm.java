package com.example.application.views.components.forms;

import com.example.application.data.entity.Country;
import com.example.application.data.entity.School;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import java.util.List;

public class SchoolForm extends AbstractForm<School>{
    TextField name = new TextField("School Name");
    TextField city = new TextField("City");
    TextField contact = new TextField("International relation office address");
    ComboBox<Country> country = new ComboBox<>("Country");

    public SchoolForm(List<Country> countries){
        this.binder = new BeanValidationBinder<>(School.class);
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getCountry_name);
        binder.bindInstanceFields(this);
        add(name,city,contact,country,createButtonsLayout());
    }
}
