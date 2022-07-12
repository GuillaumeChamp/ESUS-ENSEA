package com.example.application.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class ExclusiveCheckboxes extends VerticalLayout {
    List<Checkbox> checkboxes;
    private String value;
    private Button confirm;

    /**
     * Create a list of checkboxes which only one option can be selected
     * @param arg list of available option
     */
    public ExclusiveCheckboxes(List<String> arg){
        checkboxes = new ArrayList<>();
        for (String a : arg){
            Checkbox checkbox = new Checkbox(a);
            checkboxes.add(checkbox);
        }
        for (Checkbox c : checkboxes){
            add(c);
            c.addClickListener(e->{
                if (c.getValue()){
                    value = c.getLabel();
                    for (Checkbox nc : checkboxes){
                        if (!nc.equals(c)) nc.setValue(false);
                    }
                }
                confirm.setEnabled(true);
            });
        }
        confirm = new Button("send Answer");
        confirm.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
        confirm.setEnabled(false);
        add(confirm);
        setSizeFull();

    }

    /**
     * use this methode to define what the confirm button do (maybe fire event is better to stuck to OOP but not really efficiency here)
     * @param e actions performed by the button
     */
    public void overrideConfirm(ComponentEventListener<ClickEvent<Button>> e){
        confirm.addClickListener(e);
    }

    public String getValue() {
        return value;
    }
    public void disabled(){
        for (Checkbox c : checkboxes) c.setEnabled(false);
        confirm.setEnabled(false);
    }

    /**
     * Used to remove an option using filter
     * @param filter regex to detect which option removed
     */
    public void disableAnOption(String filter){
        for(Checkbox c : checkboxes)
            if (c.getLabel().matches(filter))
                c.setVisible(false);
        checkboxes.removeIf(c -> c.getLabel().contains(filter));
    }

    /**
     * Used to remove a marker that might be used to disable an option in certain situation
     * @param marker marker to remove
     */
    public void removeMarker(String marker){
        for (Checkbox c : checkboxes)
            if (c.getLabel().contains(marker)){
                c.setLabel(c.getLabel().replace(marker,"\n\n"));
            }

    }
}
