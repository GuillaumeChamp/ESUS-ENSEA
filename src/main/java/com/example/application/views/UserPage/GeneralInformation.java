package com.example.application.views.UserPage;

import com.example.application.data.generator.HeaderReader;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
public class GeneralInformation extends HorizontalLayout {
    VerticalLayout drawer = new VerticalLayout();
    VerticalLayout content = new VerticalLayout();

    /**
     * Display all constant information that might be useful later
     */
    public GeneralInformation(){
        setSizeFull();
        try {
            buildDrawer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HeaderReader.headerReader(content,"contact");
        setFlexGrow(1,drawer);
        setFlexGrow(5,content);
        drawer.setWidth(20,Unit.PERCENTAGE);
        content.setSizeFull();
        add(drawer,content);
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
        drawer.setSizeFull();
        properties.load(is);
        for (String prop: properties.stringPropertyNames()) {
            Button button = new Button(properties.getProperty(prop));
            button.setSizeFull();
            button.addClickListener(e-> {
                content.removeAll();
                HeaderReader.headerReader(content,prop);
                manageButton(button);
            });
            drawer.add(button);
        }
    }

    /**
     * This methode reactive the previous button and deactivate the new
     * @param activated button clicked
     */
    private void manageButton(Button activated){
        for (int i = 0;i<drawer.getComponentCount();i++){
            Component component = drawer.getComponentAt(i);
            if (component instanceof Button){
                ((Button) component).setEnabled(true);
            }
        }
        activated.setEnabled(false);
    }
}
