package com.application.views.components.forms;

import com.application.data.entity.Country;
import com.application.data.entity.School;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import java.util.List;

public class SchoolForm extends AbstractForm<School>{
    TextField name = new TextField("School Name/Nom de l'école");
    TextField city = new TextField("City/Ville");
    TextField contact = new TextField("International relation office address/contact");
    ComboBox<Country> country = new ComboBox<>("Country/Pays");

    /**
     * Create a school form adapted to a list view or a creation prompt
     * @param countries list of all countries that can be selected
     */
    public SchoolForm(List<Country> countries){
        this.binder = new BeanValidationBinder<>(School.class);
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getCountry_name);
        binder.bindInstanceFields(this);
        add(name,city,country,contact,createButtonsLayout());
    }
}
