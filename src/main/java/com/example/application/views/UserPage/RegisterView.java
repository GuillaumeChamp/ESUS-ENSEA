package com.example.application.views.UserPage;

import com.example.application.data.entity.Student;
import com.example.application.data.entity.Triggers;
import com.example.application.data.entity.User;
import com.example.application.data.service.CrmService;
import com.example.application.security.SecurityService;
import com.example.application.views.components.Prompter;
import com.example.application.views.components.forms.StudentForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.time.Month;

@PermitAll
@PageTitle("Register")
@Route(value = "Register")
public class RegisterView extends VerticalLayout {
    protected StudentForm form;
    protected CrmService service;
    protected final User user;

    /**
     * This view is use as a first page that ask all needed information
     * @param service database manager
     * @param securityService use to recover active user
     */
    public RegisterView(CrmService service,SecurityService securityService){
        this.service = service;
        this.user = securityService.getAuthenticatedUser().getUser();
        addClassName("Register");
        setAlignItems(Alignment.CENTER);
        configureForm();
    }

    /**
     * Setup function
     */
    protected void configureForm() {
        form = new StudentForm(service.findAllSchools(""), service.findAllExchanges(),service.findAllCountries(""),service.findAllJobs());
        form.setWidth("25em");
        form.addListener(StudentForm.SaveEvent.class, this::saveStudent);
        form.addListener(StudentForm.CloseEvent.class, e -> this.closeEditor());
        form.removeDelete();
        Student newStudent =new Student();
        newStudent.setProgress("0");
        newStudent.setBorn(LocalDate.of(2000, Month.MAY,10));
        newStudent.setTriggers(new Triggers());
        form.setObject(newStudent);
        form.defineAddSchoolAction(e->Prompter.promptForm(this,service));
        add(form);
        setHorizontalComponentAlignment(Alignment.CENTER,form);
    }

    /**
     * save student in database
     * @param event event which triggered the methods
     */
    protected void saveStudent(StudentForm.SaveEvent event) {
        Student formObject = (Student) event.getObject();
        service.saveStudent(formObject);
        user.setStudent(formObject);
        service.updateAccount(user);
        closeEditor();
    }

    /**
     * Update school list
     * @param service database manager
     */
    public void updateSchool(CrmService service){
        form.updateSchool(service.findAllSchools(""));
    }

    /**
     * Close the student form and refresh the page to reroute to information pages
     */
    protected void closeEditor() {
        form.setObject(null);
        form.setVisible(false);
        UI.getCurrent().getPage().reload();
    }

}
