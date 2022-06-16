package com.example.application.views.list;

import com.example.application.data.entity.School;
import com.example.application.data.service.CrmService;
import com.example.application.data.service.CsvExportService;
import com.example.application.views.MainLayout;
import com.example.application.views.components.forms.AbstractForm;
import com.example.application.views.components.forms.SchoolForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.olli.FileDownloadWrapper;

import javax.annotation.security.RolesAllowed;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

@RolesAllowed("ROLE_ADMIN")
//path and mother page
@Route(value="School/list", layout = MainLayout.class)
@PageTitle("School Board | admin")
public class SchoolView extends VerticalLayout {
    Grid<School> grid = new Grid<>(School.class);
    TextField filterText = new TextField();
    SchoolForm schoolForm;
    CrmService service;

    /**
     * This view lead to the school database manager
     * @param service database manager
     */
    public SchoolView(CrmService service) {
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
        HorizontalLayout content = new HorizontalLayout(grid, schoolForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, schoolForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        schoolForm = new SchoolForm(service.findAllCountries());
        schoolForm.setWidth("25em");
        schoolForm.addListener(AbstractForm.SaveEvent.class, this::saveSchool);
        schoolForm.addListener(AbstractForm.DeleteEvent.class, this::deleteSchool);
        schoolForm.addListener(AbstractForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveSchool(AbstractForm.SaveEvent event) {
        service.saveSchool((School) event.getObject());
        updateList();
        closeEditor();
    }

    private void deleteSchool(AbstractForm.DeleteEvent event) {
        service.deleteSchool((School) event.getObject());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("School-grid");
        grid.setSizeFull();
        grid.setColumns("name","city", "studentCount","contact");
        grid.addColumn(school -> school.getCountry().getCountry_name()).setHeader("Country");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editSchool(event.getValue()));
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button download = new Button("DOWNLOAD");
        FileDownloadWrapper wrapper = null;
        try{
            download.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
            File file = new File("school.csv");
            file.createNewFile();
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            CsvExportService.writeSchool(service.findAllSchools(),writer);
            wrapper = new FileDownloadWrapper("school.csv",file);
            wrapper.wrapComponent(download);
        }catch (Exception e){
            e.printStackTrace();
        }
        Span stats = new Span(service.countStudents() + " étudiants inscrit");

        Button addSchoolButton = new Button("Add school");
        addSchoolButton.addClickListener(click -> addSchool());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addSchoolButton,stats,wrapper);
        toolbar.expand(stats);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editSchool(School school) {
        if (school == null) {
            closeEditor();
        } else {
            schoolForm.setObject(school);
            schoolForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        schoolForm.setObject(null);
        schoolForm.setVisible(false);
        removeClassName("editing");

    }

    private void addSchool() {
        grid.asSingleSelect().clear();
        editSchool(new School());
    }

    private void updateList() {
        grid.setItems(service.findAllSchools(filterText.getValue()));
    }

}
