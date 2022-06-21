package com.example.application.views.components;

import com.vaadin.flow.component.ClickEvent;
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
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();

    }
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
    public void disableAnOption(String filter){
        for(Checkbox c : checkboxes)
            if (c.getLabel().contains(filter))
                c.setVisible(false);
        checkboxes.removeIf(c -> c.getLabel().contains(filter));
    }
    public void removeMarker(String marker){
        for (Checkbox c : checkboxes)
            if (c.getLabel().contains(marker)){
                c.setLabel(c.getLabel().replace(marker,"\n\n"));
            }

    }
}
