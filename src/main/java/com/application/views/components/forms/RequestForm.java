package com.application.views.components.forms;

import com.application.data.entity.Request;
import com.application.data.entity.Student;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;


public class RequestForm extends AbstractForm<Request>{
    TextField firstname = new TextField("firstname");
    TextField lastname = new TextField("lastname");
    TextField progress = new TextField("step");

    /**
     * Create a request form for a list view
     */
    public RequestForm(){
        this.binder = new BeanValidationBinder<>(Request.class);
        this.save.setText("Validate");
        binder.bindInstanceFields(this);
        save.setText("Accept");
        delete.setText("Decline");
        add(firstname,lastname,progress,createButtonsLayout());
    }

    /**
     * fill field in more efficiency/easily way than a binder in the context
     * @param student student associated to the request
     */
    public void setField(Student student){
        firstname.setValue(student.getFirstName());
        lastname.setValue(student.getLastName());
    }

}
