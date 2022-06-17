package com.example.application.views.UserPage;

import com.example.application.data.generator.HeaderReader;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@PermitAll
@PageTitle("general information")
@Route(value = "general",layout = MainLayout.class)
public class GeneralInformation extends VerticalLayout {
    com.vaadin.flow.component.menubar.MenuBar menuBar = new com.vaadin.flow.component.menubar.MenuBar();
    VerticalLayout content = new VerticalLayout();

    /**
     * Display all constant information that might be useful later
     */
    public GeneralInformation(){
        menuBar.addThemeVariants(MenuBarVariant.LUMO_PRIMARY);
        try {
            buildDrawer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HeaderReader.headerReader(content,"actuality");
        content.setSizeFull();
        menuBar.setWidthFull();
        add(menuBar,content);
        setSizeFull();
        content.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getElement().getStyle().set("background-image","url('images/test.png')");
        getElement().getStyle().set("background-repeat", "no-repeat");
    }

    /**
     * Create an homemade drawer
     * @throws IOException if the file general.properties have moved
     */
    private void buildDrawer() throws IOException {
        Properties properties = new Properties();
        InputStream is;
        try{
            is = new  FileInputStream("./drive/resources/general.properties");
        }catch (Exception e) {
            is = getClass().getResourceAsStream("/META-INF/resources/general.properties");
        }
        properties.load(is);
        for (String prop: properties.stringPropertyNames()) {
            menuBar.addItem(properties.getProperty(prop),e-> {
                content.removeAll();
                HeaderReader.headerReader(content,prop);
            });
        }
    }

}
