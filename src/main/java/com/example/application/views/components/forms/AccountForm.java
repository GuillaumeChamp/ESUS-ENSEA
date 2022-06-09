package com.example.application.views.components.forms;

import com.example.application.data.entity.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import net.bytebuddy.utility.RandomString;

public class AccountForm extends AbstractForm<User>{
    TextField username = new TextField("username");
    TextField password = new TextField("password");
    Button generate = new Button(new Icon(VaadinIcon.RANDOM));

    public AccountForm(User user){
        this.binder = new BeanValidationBinder<>(User.class);
        addClassName("AccountForm");
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
    public void clear(){
        this.password.clear();
    }

    public TextField getUsername() {
        return username;
    }
}
