package com.example.application.views.list;

import com.example.application.data.entity.Country;
import com.example.application.data.service.CrmService;
import com.example.application.data.service.CsvExportService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
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
@Route(value="Country/list", layout = MainLayout.class)
@PageTitle("Country board| admin")
public class CountryList extends VerticalLayout {
    Grid<Country> grid = new Grid<>(Country.class);
    TextField filterText = new TextField();
    CrmService service;

    public CountryList(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), getContent());
        updateList();
    }
    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureGrid() {
        grid.addClassNames("Country-grid");
        grid.setSizeFull();
        grid.setColumns("id","country_name","studentCount","phone_code","country_code");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
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
            File file = new File("country.csv");
            file.createNewFile();
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            CsvExportService.writeCountry(service.findAllCountries(""),writer);
            wrapper = new FileDownloadWrapper("country.csv",file);
            wrapper.wrapComponent(download);
        }catch (Exception e){
            e.printStackTrace();
        }
        Span stats = new Span(service.countStudents() + " étudiants inscrit");

        HorizontalLayout toolbar = new HorizontalLayout(filterText,stats,wrapper);
        toolbar.setWidthFull();
        toolbar.expand(stats);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    private void updateList() {
        grid.setItems(service.findAllCountries(filterText.getValue()));
    }

}
