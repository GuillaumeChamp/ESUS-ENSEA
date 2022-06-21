package com.example.application.views;

import com.example.application.data.entity.Student;
import com.example.application.data.entity.User;
import com.example.application.data.service.CrmService;
import com.example.application.security.SecurityService;
import com.example.application.views.UserPage.RegisterView;
import com.example.application.views.components.Prompter;
import com.example.application.views.components.forms.AccountForm;
import com.example.application.views.components.forms.StudentForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("Account")
@Route(value = "Account",layout = MainLayout.class)
public class AccountView extends RegisterView {
    AccountForm accountForm;
    int oldExchange;

    public AccountView(CrmService service,SecurityService securityService){
        super(service,securityService);
        addClassName("size");
        removeClassName("Register");
        configureForm();
        removeAll();
        add(getContent());
    }
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(accountForm,form);
        content.setFlexGrow(1, accountForm);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    @Override
    protected void configureForm() {
        super.configureForm();
        Student student = securityService.getAuthenticatedUser().getStudent();
        if (student != null) {
            form.setObject(student);
            oldExchange = student.getExchangeType().getId();
            addClassName("editing");
        }else form.setEnabled(false);
        accountForm = new AccountForm(securityService.getAuthenticatedUser().getUser());
        accountForm.removeDelete();
        accountForm.clear();
        accountForm.addListener(AccountForm.SaveEvent.class, this::saveAccount);
    }
    @Override
    protected void saveStudent(StudentForm.SaveEvent event) {
        Student formObject = (Student) event.getObject();
        User user = securityService.getAuthenticatedUser().getUser();
        if (user.getStudent().getExchangeType().getId()!=oldExchange) {
            Prompter.promptExchangeChanged(this,formObject,service);
            return;
        }
        service.saveStudent(formObject);
        closeEditor();
    }

    protected void saveAccount(AccountForm.SaveEvent event) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Account Updated");
        dialog.add("Please log out to make it effective");
        Button cancelButton = new Button("log out", e -> {
            dialog.close();
            securityService.logout();
        });
        dialog.getFooter().add(cancelButton);
        add(dialog);
        service.updateAccount((User) event.getObject());
        dialog.open();
    }
}
