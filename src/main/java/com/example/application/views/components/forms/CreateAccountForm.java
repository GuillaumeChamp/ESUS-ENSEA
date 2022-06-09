package com.example.application.views.components.forms;

import com.example.application.data.entity.User;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import net.bytebuddy.utility.RandomString;

public class CreateAccountForm extends AbstractForm<User>{
    TextField username = new TextField("username");
    TextField password = new TextField("password");
    TextField email = new TextField("Student email");

    public CreateAccountForm(User user){
        this.binder = new BeanValidationBinder<>(User.class);
        addClassName("AccountForm");
        binder.bindInstanceFields(this);

        add(    email,
                username,
                createButtonsLayout());
        setObject(user);
    }

    public String getUsername() {
        return username.getValue();
    }
    public void clear(){
        email.clear();
    }

    public String getEmail(){
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }
    public void generatePassword(){
        String password = new RandomString(12).nextString();
        this.password.setValue(password);
    }
}
