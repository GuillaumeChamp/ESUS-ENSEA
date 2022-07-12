package com.application.views.components.forms;

import com.application.data.entity.User;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import net.bytebuddy.utility.RandomString;

public class CreateAccountForm extends AbstractForm<User>{
    TextField username = new TextField("username");
    TextField password = new TextField("password");
    TextField email = new TextField("Student email");

    /**
     * This form is used to create new account
     */
    public CreateAccountForm(){
        this.binder = new BeanValidationBinder<>(User.class);
        addClassName("AccountForm");
        binder.bindInstanceFields(this);

        add(    email,
                username,
                createButtonsLayout());
        setObject(new User());
    }

    public String getUsername() {
        return username.getValue();
    }

    /**
     * Clear email address filed (used as a minor optimization while creating several account)
     */
    public void clear(){
        email.clear();
    }

    public String getEmail(){
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    /**
     * Generate a random 12 length password
     */
    public void generatePassword(){
        String password = new RandomString(12).nextString();
        this.password.setValue(password);
    }
}
