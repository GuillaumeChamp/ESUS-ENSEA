package com.example.application.views.UserPage;

import com.example.application.data.generator.HeaderReader;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("Contact")
@Route(value = "/contact",layout = MainLayout.class)
public class ContactView extends VerticalLayout {
    @SuppressWarnings("SpellCheckingInspection")
    public ContactView(){
        Paragraph paragraph;
        Span context = new Span("Context");
        context.addClassName("span");
        add(context);
        if (MainLayout.EN) paragraph = new Paragraph("This application was developed by Guillaume Champtoussel in 2022 during an internship\n" +
                "for ENSEA and funded by CAMPUS FRANCE\n" +
                "The app is operated by international relations office contact : ri@ensea.fr");
        else paragraph = new Paragraph("Cette application a été developpé par Guillaume Champtoussel en 2022 durant un stage\n" +
                "pour l'ENSEA et financé par CAMPUS FRANCE\n" +
                "L'application est administré par le bureau des relations internationales contact : ri@ensea.fr");
        add(paragraph);
        HeaderReader.addImage(this, new String[]{"image","logo-campus-france.png"});
        add(new Paragraph("dev : Guillaume Champtoussel"));
        HeaderReader.addImage(this, new String[]{"image","my-logo.png"});
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }
}
