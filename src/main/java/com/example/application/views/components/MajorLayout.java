package com.example.application.views.components;

import com.example.application.data.entity.Parkour;
import com.example.application.data.entity.User;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.List;

public class MajorLayout extends VerticalLayout {
    private final CrmService service;
    private final User user;
    public ComboBox<String> major = new ComboBox<>("Major/Majeur");
    public ComboBox<String> option_suivi = new ComboBox<>("Option");
    public ComboBox<String> semester = new ComboBox<>("Semester/Semestre");
    public Button confirm = new Button("confirm/confirmer");

    public MajorLayout(CrmService service, User user) {
        this.service = service;
        this.user = user;
        Binder<Parkour> binder = new BeanValidationBinder<>(Parkour.class);
        binder.bindInstanceFields(this);
        confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        confirm.setEnabled(false);
        confirm.addClickListener(e-> submit());
        major.setVisible(false);
        option_suivi.setVisible(false);
        this.add(semester, major, option_suivi,confirm);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setup();
    }

    /**
     * Set up the drawer form
     */
    private void setup(){
        List<Parkour> list = service.findAllParkour();
        List<String> list_semester = new ArrayList<>();
        for(Parkour p : list){
            if (!list_semester.contains(p.getSemester())) list_semester.add(p.getSemester());
        }
        semester.setItems(list_semester);
        semester.addValueChangeListener(e->{
            String value = e.getValue();
            clear();
            List<String> list_major = new ArrayList<>();
            for(Parkour p : list){
                if(p.getSemester().equals(value) && !list_major.contains(p.getMajor()))
                    list_major.add(p.getMajor());
            }
            list_major.remove("");
            if (!list_major.isEmpty()) {
                major.setItems(list_major);
                major.setVisible(true);
                major.addValueChangeListener(ee->{
                    List<String> list_option = new ArrayList<>();
                    for(Parkour p : list){
                        if(p.getSemester().equals(value) && p.getMajor().equals(ee.getValue()) && !list_option.contains(p.getOption_suivi()))
                            list_option.add(p.getOption_suivi());
                    }
                    list_option.remove("");
                    if (list_option.isEmpty()) confirm.setEnabled(true);
                    else {
                        option_suivi.setItems(list_option);
                        option_suivi.setVisible(true);
                        option_suivi.addValueChangeListener(eee-> confirm.setEnabled(true));
                    }
                });
            }
            else {
                //process options
                List<String> list_option = new ArrayList<>();
                for(Parkour p : list){
                    if(p.getSemester().equals(value) && !list_option.contains(p.getOption_suivi()))
                        list_option.add(p.getOption_suivi());
                }
                list_option.remove("");
                if (list_option.isEmpty()) confirm.setEnabled(true);
                else {
                    option_suivi.setItems(list_option);
                    option_suivi.setVisible(true);
                    option_suivi.addValueChangeListener(ee-> confirm.setEnabled(true));
                }
            }
        });
    }

    public void submit(){
        Parkour parkour = service.findParkour(semester.getValue(),major.getValue(),option_suivi.getValue());
        if (user.getStudent()==null) {
            Prompter.prompt(this,"seem to work");
            return;
        }
        user.getStudent().setParkour(parkour);
        service.saveStudent(user.getStudent());
    }

    /**
     * Reset field
     */
    private void clear(){
        major.clear();
        major.setVisible(false);
        option_suivi.clear();
        option_suivi.setVisible(false);
        confirm.setEnabled(false);
    }
}
