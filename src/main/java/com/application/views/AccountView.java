package com.application.views;

import com.application.views.components.Prompter;
import com.application.views.components.forms.AccountForm;
import com.application.data.entity.Student;
import com.application.data.entity.User;
import com.application.data.service.CrmService;
import com.application.security.SecurityService;
import com.application.views.UserPage.RegisterView;
import com.application.views.components.forms.StudentForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("Account")
@Route(value = "Account",layout = MainLayout.class)
public class AccountView extends RegisterView {
    AccountForm accountForm;
    int oldExchange;

    /**
     * This view is an account view, used to change personal data and login
     * @param service database manager (used to save modifications and recover current state)
     * @param securityService use to recover user and permissions
     */
    public AccountView(CrmService service,SecurityService securityService){
        super(service,securityService);
        addClassName("size");
        removeClassName("Register");
        configureForm();
        removeAll();
        add(getContent());
    }

    /**
     * Manage content creation
     * @return formatted content
     */
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(accountForm,form);
        content.setFlexGrow(1, accountForm);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    /**
     * set up the forms
     */
    @Override
    protected void configureForm() {
        super.configureForm();
        Student student = user.getStudent();
        if (student != null) {
            form.setObject(student);
            oldExchange = student.getExchangeType().getId();
            addClassName("editing");
        }else form.setEnabled(false);
        accountForm = new AccountForm(user);
        accountForm.removeDelete();
        accountForm.clear();
        accountForm.addListener(AccountForm.SaveEvent.class, this::saveAccount);
    }

    /**
     * Save modified private data
     * @param event event which triggered the method
     */
    @Override
    protected void saveStudent(StudentForm.SaveEvent event) {
        Student formObject = (Student) event.getObject();
        if (user.getStudent().getExchangeType().getId()!=oldExchange) {
            Prompter.promptExchangeChanged(this,formObject,service);
            return;
        }
        service.saveStudent(formObject);
        closeEditor();
    }

    /**
     * save modified login info
     * @param event event which triggered the method
     */
    protected void saveAccount(AccountForm.SaveEvent event) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Account Updated");
        dialog.add("Please log out to make it effective");
        Button cancelButton = new Button("log out", e -> {
            dialog.close();
            UI.getCurrent().navigate("/");
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(
                    VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                    null);
        });
        dialog.getFooter().add(cancelButton);
        add(dialog);
        service.updateAccount((User) event.getObject());
        dialog.open();
    }
}
