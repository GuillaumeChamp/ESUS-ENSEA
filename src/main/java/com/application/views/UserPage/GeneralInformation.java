package com.application.views.UserPage;

import com.application.data.generator.HeaderReader;
import com.application.views.MainLayout;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.io.*;
import java.nio.charset.StandardCharsets;

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
     * @throws IOException if the file general.txt have moved
     */
    private void buildDrawer() throws IOException {
        InputStream is;
        int index =2;
        if (MainLayout.EN) index=1;
        try{
            is = new  FileInputStream("./drive/resources/general.txt");
        }catch (Exception e) {
            is = getClass().getResourceAsStream("/META-INF/resources/general.txt");
        }
        assert is != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line = reader.readLine();
        while (line!=null){
            String[] parameters = line.split(";");
            String name;
            try{
                name = parameters[index];
            }catch (IndexOutOfBoundsException ee){
                name = parameters[1];
            }
            menuBar.addItem(name,e-> {
                content.removeAll();
                HeaderReader.headerReader(content,parameters[0]);
            });
            line = reader.readLine();
        }

    }

}
