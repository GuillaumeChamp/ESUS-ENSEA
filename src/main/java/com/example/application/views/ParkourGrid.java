package com.example.application.views;

import com.example.application.data.entity.Parkour;
import com.example.application.data.service.CrmService;
import com.example.application.views.components.forms.AbstractForm;
import com.example.application.views.components.forms.ParkourForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;


@RolesAllowed("ROLE_ADMIN")
@Route(value="parkour/list", layout = MainLayout.class)
@PageTitle("Parkour Board | admin")
public class ParkourGrid extends VerticalLayout {
    Grid<Parkour> grid = new Grid<>(Parkour.class);
    TextField filterText = new TextField();
    ParkourForm parkourForm;
    CrmService service;

    public ParkourGrid(CrmService service) {
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
        HorizontalLayout content = new HorizontalLayout(grid, parkourForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, parkourForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        parkourForm = new ParkourForm();
        parkourForm.setWidth("25em");
        parkourForm.addListener(AbstractForm.SaveEvent.class, this::saveParkour);
        parkourForm.addListener(AbstractForm.DeleteEvent.class, this::deleteParkour);
        parkourForm.addListener(AbstractForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveParkour(AbstractForm.SaveEvent event) {
        service.saveParkour((Parkour) event.getObject());
        updateList();
        closeEditor();
    }

    private void deleteParkour(AbstractForm.DeleteEvent event) {
        service.deleteParkour((Parkour) event.getObject());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("Parkour-grid");
        grid.setSizeFull();
        grid.setColumns("semester","major","option_suivi", "studentCount");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editParkour(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Span stats = new Span(service.countStudents() + " étudiants inscrit");

        Button addParkourButton = new Button("Add parkour");
        addParkourButton.addClickListener(click -> addParkour());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addParkourButton,stats);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editParkour(Parkour parkour) {
        if (parkour == null) {
            closeEditor();
        } else {
            parkourForm.setObject(parkour);
            parkourForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        parkourForm.setObject(null);
        parkourForm.setVisible(false);
        removeClassName("editing");

    }

    private void addParkour() {
        grid.asSingleSelect().clear();
        editParkour(new Parkour());
    }

    private void updateList() {
        grid.setItems(service.findAllParkours());
    }
}
