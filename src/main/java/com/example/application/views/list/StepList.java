package com.example.application.views.list;

import com.example.application.data.entity.Student;
import com.example.application.data.generator.CheckListBuilder;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@RolesAllowed("ROLE_ADMIN")
//path and mother page
@Route(value="checklist/list", layout = MainLayout.class)
@PageTitle("School Board | admin")
public class StepList extends VerticalLayout {
    Grid<Student> grid = new Grid<>(Student.class);
    TextField filterText = new TextField();
    CrmService service;

    public StepList(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), getContent());
        updateList();
    }
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        toolbar.setWidth("100%");
        return toolbar;
    }
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void updateList() {
        grid.setItems(service.findAllStudents(filterText.getValue()));
    }
    private void configureGrid() {
        grid.addClassNames("Student-grid");
        grid.setSizeFull();
        grid.setColumns("civility", "firstName", "lastName");
        grid.addColumn(student -> student.getNationality().getCountry_name()).setHeader("Nationalité");
        grid.addColumn(student -> student.getExchangeType().getName()).setHeader("Type d'échange");
        CheckListBuilder.configureView(grid);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(
                e -> UI.getCurrent().getPage().executeJs("const textarea = document.createElement(\"textarea\");\n" +
                        "  textarea.value = $0;\n" +
                        "  textarea.style.position = \"absolute\";\n" +
                        "  textarea.style.opacity = \"0\";\n" +
                        "  document.body.appendChild(textarea);\n" +
                        "  textarea.select();\n" +
                        "  document.execCommand(\"copy\");\n" +
                        "  document.body.removeChild(textarea);\n", e.getValue().getEmail())
        );
    }
}
