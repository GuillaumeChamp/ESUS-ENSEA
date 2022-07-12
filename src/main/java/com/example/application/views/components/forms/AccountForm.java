package com.example.application.views.components.forms;

import com.example.application.data.entity.User;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import net.bytebuddy.utility.RandomString;

public class AccountForm extends AbstractForm<User>{
    TextField username = new TextField("username");
    PasswordField password = new PasswordField("password");
    Button generate = new Button(new Icon(VaadinIcon.RANDOM));

    /**
     * This form is used to allow change password
     * @param user active user
     */
    public AccountForm(User user){
        this.binder = new BeanValidationBinder<>(User.class);
        addClassName("AccountForm");
        translate();
        binder.bindInstanceFields(this);
        username.setEnabled(false);
        generate.addClickListener(e->{
            String password = new RandomString(12).nextString();
            this.password.setValue(password);
        });
        HorizontalLayout horizontalLayout = new HorizontalLayout(password,generate);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        add(    username,
                horizontalLayout,
                createButtonsLayout());
        setObject(user);
    }

    /**
     * Translate the fields label
     */
    private void translate(){
        if(MainLayout.EN) return;
        username.setLabel("Nom d'utilisateur");
        password.setLabel("Mot de passe");
    }

    /**
     * clear password field used to mask password
     */
    public void clear(){
        this.password.clear();
    }

    public TextField getUsername() {
        return username;
    }
}
