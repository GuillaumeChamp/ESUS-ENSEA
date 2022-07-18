package com.application.views;

import com.application.views.components.Prompter;
import com.application.views.components.forms.AbstractForm;
import com.application.views.components.forms.CreateAccountForm;
import com.application.data.entity.Request;
import com.application.data.entity.Student;
import com.application.data.entity.User;
import com.application.data.service.CrmService;
import com.application.data.service.MailSender;
import com.application.security.SecurityService;
import com.application.views.components.forms.AccountForm;
import com.application.views.components.forms.RequestForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;


@RolesAllowed("ADMIN")
@Route(value = "admin", layout = MainLayout.class)
@PageTitle("admin")
@SuppressWarnings("SpellCheckingInspection")
public class AdminView extends VerticalLayout {
    CrmService service;
    CreateAccountForm accountForm;
    RequestForm requestForm;
    SecurityService securityService;
    Grid<Request> grid = new Grid<>(Request.class);
    public AdminView(CrmService service,SecurityService securityService){
        this.securityService = securityService;
        this.service = service;
        addClassName("admin");
        setSizeFull();
        configureForm();
        configureGrid();
        addContent();
        updateRequestList();
    }

    private void addContent(){
        Paragraph paragraph = new Paragraph("dev by guillaume Champtoussel "+ MailSender.getDev()+"\nDisponible,\nCode source partiellement censuré");
        Anchor anchor = new Anchor("https://github.com/GuillaumeChamp/ESUS-ENSEA","GitHub");
        anchor.setTarget("_blank");
        Anchor map = new Anchor("http://umap.openstreetmap.fr/fr/map/anonymous-edit/777866:S29ISI9HqyK8lItAMatlwk5Z3rY","umap");
        map.setTarget("_blank");
        H2 span = new H2("Create new account");
        H2 title = new H2("Request Manager");
        Button button = new Button("try mail");
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        button.addClickListener(e-> {
            try {
                MailSender.TestMail(securityService.getAuthenticatedUser().getUser());
            } catch (Exception ex) {
                Notification notification = Notification.show("Error mail not send check RI address or smtp configuration");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        HorizontalLayout request = new HorizontalLayout(grid,requestForm);
        request.setFlexGrow(2,grid);
        request.setFlexGrow(1,requestForm);
        request.setSizeFull();
        HorizontalLayout layout = new HorizontalLayout(new VerticalLayout(span,accountForm),new VerticalLayout(new H2("Information and test"),paragraph,anchor,button,map));
        layout.setSizeFull();
        add(layout,title,request);
    }

    private void configureForm(){
        accountForm = new CreateAccountForm();
        accountForm.setWidth("25em");
        accountForm.addListener(AccountForm.SaveEvent.class, this::createAccount);
        accountForm.addListener(AccountForm.CloseEvent.class, e -> this.closeEditor());
        accountForm.addListener(AbstractForm.DeleteEvent.class, e-> ask());
        requestForm = new RequestForm();
        requestForm.setWidth("25em");
        requestForm.addListener(RequestForm.SaveEvent.class, this::validate);
        requestForm.addListener(AbstractForm.DeleteEvent.class, this::delete);
        requestForm.addListener(AbstractForm.CloseEvent.class, e->closeRequestEditor());
        requestForm.setVisible(false);
    }
    private void closeEditor() {
        accountForm.clear();
        accountForm.setObject(new User());
    }
    private void createAccount(AccountForm.SaveEvent event) {
        accountForm.generatePassword();
        User user = (User) event.getObject();
        user.setRole("ROLE_USER");
        user.setActive(1);
        user.setPassword(accountForm.getPassword());
        if (service.accountExist(user.getUsername())) {
            Prompter.prompt(this,"this user already exist");
            return;
        }
        if (accountForm.getEmail().isEmpty()) {
            Notification notification = Notification.show("Error mail not send check email adress");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        try {
            MailSender.accountCreation(user.getUsername(), accountForm.getPassword(), accountForm.getEmail());
            service.updateAccount(user);
            Prompter.prompt(this,"user successfully created");
            closeEditor();
        } catch (Exception e) {
            Notification notification = Notification.show("Email not send Test mailSender or check email adress");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

    }

    private void deleteAccount(){
        service.deleteUser(accountForm.getUsername());
        closeEditor();
    }
    private void validate(AbstractForm.SaveEvent event){
        service.terminateRequest((Request) event.getObject());
        updateRequestList();
        try {
            MailSender.InformRequestSucess((Request) event.getObject());
        } catch (Exception e) {
            Notification.show("User not notified check his email address or Mail sender");
        }
        closeRequestEditor();
    }
    private void delete(AbstractForm.DeleteEvent event){
        service.deleteRequest((Request) event.getObject());
        Prompter.promptComment(this,(Request) event.getObject());
        updateRequestList();
        closeRequestEditor();
    }

    private void configureGrid() {
        grid.addClassNames("student-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(request -> request.getStudent().getFirstName()).setHeader("First Name");
        grid.addColumn(request -> request.getStudent().getLastName()).setHeader("Last Name");
        grid.addColumn(Request::getProgress).setHeader("Step");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editRequest(event.getValue()));
    }
    private void updateRequestList() {
        grid.setItems(service.findAllRequests());
    }
    public void editRequest(Request request) {
        if (request == null) {
            closeEditor();
        } else {
            requestForm.setObject(request);
            requestForm.setField(request.getStudent());
            requestForm.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeRequestEditor() {
        requestForm.setObject(null);
        requestForm.setVisible(false);
        removeClassName("editing");
    }

    /**
     * Confirmation menu
     * Allow to delete all data  and check account existence
     */
    private void ask(){
        Dialog confirm = new Dialog();
        Button cancelButton = new Button("cancel",e -> confirm.close());
        confirm.setHeaderTitle("Ce compte n'existe pas");

        if (service.accountExist(accountForm.getUsername() ) ){

            confirm.setHeaderTitle("Voulez-vous aussi effacer le fiche étudiante ?");
            Button noButton = new Button("DeleteAccount", e -> {
                confirm.close();
                deleteAccount();
            });

            confirm.getFooter().add(noButton);
            Student associatedStudent = service.findUser(accountForm.getUsername()).getStudent();

            if (associatedStudent!=null) {
                Button confirmButton = new Button("DeleteAccount and student", e -> {
                    confirm.close();
                    deleteAccount();
                    service.deleteStudent(associatedStudent);
                });
                confirm.getFooter().add(confirmButton);
            }
        }
        confirm.getFooter().add(cancelButton);
        add(confirm);
        confirm.open();
    }
}
