package com.example.application.views.components.forms;

import com.example.application.data.entity.Request;
import com.example.application.data.entity.Student;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;


public class RequestForm extends AbstractForm<Request>{
    TextField firstname = new TextField("firstname");
    TextField lastname = new TextField("lastname");
    TextField progress = new TextField("step");

    public RequestForm(){
        this.binder = new BeanValidationBinder<>(Request.class);
        this.save.setText("Validate");
        binder.bindInstanceFields(this);
        add(firstname,lastname,progress,createButtonsLayout());
    }
    public void setField(Student student){
        firstname.setValue(student.getFirstName());
        lastname.setValue(student.getLastName());
    }

}
