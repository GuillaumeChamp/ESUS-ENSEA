package com.application.views.list;

import com.application.data.entity.Student;
import com.application.data.service.CrmService;
import com.application.views.MainLayout;
import com.application.views.components.forms.StudentForm;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

//security
@RolesAllowed("ROLE_ADMIN")
//path and mother page
@Route(value="list", layout = MainLayout.class)
@PageTitle("Student Board | admin")
public class ListView extends VerticalLayout {
    Grid<Student> grid = new Grid<>(Student.class);
    TextField filterText = new TextField();
    StudentForm studentForm;
    CrmService service;
    TextField progress;

    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid,studentForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, studentForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        studentForm = new StudentForm(service.findAllSchools(""),service.findAllExchanges(),service.findAllCountries(""),service.findAllJobs());
        studentForm.setWidth("25em");
        studentForm.addListener(StudentForm.SaveEvent.class, this::saveContact);
        studentForm.addListener(StudentForm.DeleteEvent.class, this::deleteContact);
        studentForm.addListener(StudentForm.CloseEvent.class, e -> closeEditor());
        progress = new TextField("step");
        studentForm.add(progress);
    }

    private void saveContact(StudentForm.SaveEvent event) {
        Student student =(Student) event.getObject();
        if (progress.getValue()!=null && !progress.getValue().isEmpty()) student.setProgress(progress.getValue());
        service.saveStudent(student);
        updateList();
        closeEditor();
    }

    private void deleteContact(StudentForm.DeleteEvent event) {
        service.deleteStudent((Student) event.getObject());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("Student-grid");
        grid.setSizeFull();
        grid.setColumns("civility","firstName", "lastName");
        grid.addColumn(student -> student.getNationality().getCountry_name()).setHeader("Nationalité");
        grid.addColumn(student -> student.getExchangeType().getName()).setHeader("Type d'échange");
        grid.addColumn(Student::getProgress).setHeader("Etape");
        grid.addColumn(contact -> contact.getSchool().getName()).setHeader("School");
        grid.addColumn(student -> student.getCountry().getCountry_name()).setHeader("Country");
        grid.addColumn(Student::getBorn).setHeader("Born date");
        grid.addColumn(Student::getBornPlace).setHeader("Born Place");
        grid.addColumn(Student::getPhoneNumber).setHeader("Phone Number");
        grid.addColumn(Student::getEndYear).setHeader("End of secondary");
        grid.addColumn(Student::getStartSuperior).setHeader("Start Superior");
        grid.addColumn(student -> student.getJob1().getJob()).setHeader("Job First Parent");
        grid.addColumn(student -> student.getJob2().getJob()).setHeader("Job Second Parent");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Span stats = new Span(service.countStudents() + " étudiants inscrit");

        HorizontalLayout toolbar = new HorizontalLayout(filterText,stats);
        toolbar.setWidthFull();
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editContact(Student student) {
        if (student == null) {
            closeEditor();
        } else {
            studentForm.setObject(student);
            studentForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        studentForm.setObject(null);
        studentForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllStudents(filterText.getValue()));
    }
}