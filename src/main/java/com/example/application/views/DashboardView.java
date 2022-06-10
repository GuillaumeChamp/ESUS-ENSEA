package com.example.application.views;

import com.example.application.data.service.CrmService;
import com.example.application.data.service.CsvExportService;
import com.example.application.data.service.MailSender;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.olli.FileDownloadWrapper;

import javax.annotation.security.PermitAll;
import java.io.*;
import java.util.Arrays;

@PermitAll
@Route(value = "dash", layout = MainLayout.class)
@PageTitle("Dashboard")
@SuppressWarnings("SpellCheckingInspection")
public class DashboardView extends VerticalLayout {
    private final CrmService service;

    public DashboardView(CrmService service) { 
        this.service = service;
        addClassName("dashboard-view");
        add(getContactStats());
        try {
            addDownloadButton();
        }catch (Exception e){
            add(new Paragraph(Arrays.toString(e.getStackTrace())));
        }
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void addDownloadButton() throws Exception {
        //AURION + Exchange
        Button downloadStudent = new Button("DOWNLOAD");
        downloadStudent.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        File file = new File("student.csv");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        CsvExportService.writeRI(service.findAllStudents(""),writer);
        FileDownloadWrapper wrapper = new FileDownloadWrapper("student.csv",file);
        wrapper.wrapComponent(downloadStudent);
        createSpan(new Paragraph("Utilisez ce boutton pour télécharger la base de données étudiante"),wrapper);

        //AURION
        Button send = new Button("SEND");
        Button downloadAurion = new Button("DOWNLOAD");

        downloadAurion.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        send.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
        File file1 = new File("studentAURION.csv");
        file1.createNewFile();
        CsvExportService.writeAurion(service.findAllStudents(""),new FileWriter(file1));
        send.addClickListener(e->{
            MailSender.sendService("aurion",file1);
            send.setEnabled(false);
        });
        FileDownloadWrapper wrapper1 = new FileDownloadWrapper("studentAURION.csv",file1);
        wrapper1.wrapComponent(downloadAurion);
        createSpan(new Paragraph("Envoie de mail pour création de compte CAS et fiche Aurion"),send,wrapper1);

        //MOODLE
        Button send2 = new Button("SEND");
        Button downloadMoodle = new Button("DOWNLOAD");
        downloadMoodle.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        FileDownloadWrapper wrapper2 = new FileDownloadWrapper("studentMOODLE.csv",file1);
        wrapper2.wrapComponent(downloadMoodle);
        send2.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        CsvExportService.writeAurion(service.findAllStudents(""),new FileWriter(file1));
        send2.addClickListener(e->{
            MailSender.sendService("moodle",file1);
            send2.setEnabled(false);
        });
        createSpan(new Paragraph("Envoie de mail pour création de compte Moodle"),send2,wrapper2);

        //MAJOR
        Button send3 = new Button("SEND");
        Button downloadMajor = new Button("DOWNLOAD");
        downloadMajor.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        File file3 = new File("studentMAJOR.csv");
        file3.createNewFile();
        FileDownloadWrapper wrapper3 = new FileDownloadWrapper("studentMAJOR.csv",file3);
        wrapper3.wrapComponent(downloadMajor);
        send3.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        CsvExportService.writeMajor(service.findAllStudents(""),new FileWriter(file3));
        send3.addClickListener(e->{
            MailSender.sendService("etude",file3);
            send3.setEnabled(false);
        });
        createSpan(new Paragraph("Envoie de mail pour les majeurs et options"),send3,wrapper3);

        //Vol et FIP
        Button send4 = new Button("SEND");
        Button downloadFIP = new Button("DOWNLOAD");
        downloadFIP.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        File file4 = new File("studentFIP.csv");
        file4.createNewFile();
        FileDownloadWrapper wrapper4 = new FileDownloadWrapper("studentFIP.csv",file4);
        wrapper4.wrapComponent(downloadFIP);
        send4.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        CsvExportService.writeFIP(service.findAllStudents(""),new FileWriter(file4));
        send4.addClickListener(e->{
            MailSender.sendService("fip",file4);
            send4.setEnabled(false);
        });
        createSpan(new Paragraph("Envoie à FIP avec les coordonées de vol"),send4,wrapper4);
    }

    private Component getContactStats() {
        Span stats = new Span(service.countStudents() + " étudiants inscrits\n"
            + service.countCas() + " comptes CAS créés\n"
            + service.countFlight() + " trajets programmés"
                );
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }
    private void createSpan(Component... components){
        HorizontalLayout layout = new HorizontalLayout(components);
        layout.setJustifyContentMode(JustifyContentMode.END);
        components[0].getElement().getStyle().set("margin-right", "auto");
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        layout.setSizeFull();
        add(layout);
    }


}