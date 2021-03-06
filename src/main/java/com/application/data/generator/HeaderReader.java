package com.application.data.generator;

import com.application.views.components.ExclusiveCheckboxes;
import com.application.views.components.FlightLayout;
import com.application.views.components.MajorLayout;
import com.application.views.components.forms.AbstractForm;
import com.application.data.service.PathFinder;
import com.application.data.service.MailSender;
import com.application.views.MainLayout;
import com.application.views.UserPage.TextView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.StreamResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HeaderReader {

    /**
     * Seek a particular file in the right directory
     * @param file name of the file
     * @return inputStream of the file
     */
    public static InputStream findFile(String file){
        try{
            if (MainLayout.EN) {
                return new FileInputStream("./drive/resources/"+file);
            }
            try {
                return new FileInputStream("./drive/resources/FR/"+file);
            }catch (FileNotFoundException ee){
                return new FileInputStream("./drive/resources/"+file);
            }
        } catch (FileNotFoundException e) {
            if (MainLayout.EN) return (HeaderReader.class.getResourceAsStream("/META-INF/resources/"+file));
            InputStream ans = (HeaderReader.class.getResourceAsStream("/META-INF/resources/FR/"+file));
            if (ans == null) ans = (HeaderReader.class.getResourceAsStream("/META-INF/resources/"+file));
            return ans;
        }
    }

    /**
     * Add all the element text, hyperlink and image contained in the heard
     * className of the view must be the name of the file
     * @param layout all elements will be added here
     * @param file filename of the file to read
     */
    public static void headerRead(TextView layout,String file){
        layout.buttonNext = new Button("next");
        if(!MainLayout.EN) layout.buttonNext.setText("suivant");
        layout.buttonPrevious = new Button("previous");
        if(!MainLayout.EN) layout.buttonPrevious.setText("pr??c??dent");
        layout.back = new Button("Back to progression");
        if(!MainLayout.EN) layout.back.setText("retour");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((findFile("header/"+file+".txt")), StandardCharsets.UTF_8));
            String line = reader.readLine();

            while(line!=null){
                readLine(layout,line);
                line = reader.readLine();
            }
        }
        catch(IOException|NullPointerException e) {
            layout.add(new Paragraph(findFile("header/"+file+".txt")+" not found"));
        }
        catch (ArrayIndexOutOfBoundsException e){
            layout.add(new Paragraph(findFile("header/"+file+".txt") +"check format (missing second arguments)"));
        }
        try {
            navigationButton(layout);
        }catch (NullPointerException exception){
            layout.add(new Paragraph("No button for non student"));
        }
    }

    /**
     * Read a header without the page constraint
     * @param layout layout to populate
     * @param file describer
     */
    public static void headerReader(VerticalLayout layout,String file){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((findFile("header/"+file+".txt")), StandardCharsets.UTF_8));
            String line = reader.readLine();

            while(line!=null){
                readLine(layout,line);
                line = reader.readLine();
            }
        }
        catch(IOException|NullPointerException e) {
            layout.add(new Paragraph("Header file " +findFile("header/"+file+".txt")+" not found"));
        }
        catch (ArrayIndexOutOfBoundsException e){
            layout.add(new Paragraph(findFile("header/"+file+".txt") +"check format (missing second arguments)"));
        }

    }

    /**
     * Decrypt a line and add the content to the layout
     * @param layout where to add the content
     * @param line the line to translate
     */
    @SuppressWarnings("SpellCheckingInspection")
    private static void readLine(VerticalLayout layout,String line){
        String[] parameter = line.split(",",-1);

        if(parameter[0].contains("text")){
            TextArea textArea = new TextArea();
            textArea.setValue(TextConverter.ConvertFile("text/"+parameter[1]));
            textArea.setReadOnly(true);
            textArea.setWidthFull();
            textArea.addClassName("text");
            layout.add(textArea);
            return;
        }

        if(parameter[0].contains("para")){
            if (!parameter[1].contains(".txt")){
                Paragraph textArea = new Paragraph(parameter[1]);
                textArea.addClassName("paragraph");
                textArea.getElement().getStyle().set("white-space", "pre-wrap");
                textArea.getElement().getStyle().set("text-align","center");
                layout.add(textArea);
                return;
            }
            Paragraph textArea = new Paragraph(TextConverter.ConvertFile("text/"+parameter[1]));
            textArea.addClassName("paragraph");
            textArea.getElement().getStyle().set("white-space", "pre-wrap");
            textArea.getElement().getStyle().set("text-align","center");
            layout.add(textArea);
            return;
        }
        if (parameter[0].contains("span")){
            Span span = new Span(parameter[1]);
            layout.add(span);
            return;
        }

        if(parameter[0].contains("ima")){
            addImage(layout,parameter);
            return;
        }
        if(parameter[0].contains("contact")){
            addContact(layout,parameter[1],parameter[2],parameter[3],parameter[4]);
            return;
        }

        if (parameter[0].contains("link")){
            Anchor anchor = new Anchor(parameter[1],parameter[2]);
            anchor.setTarget("_blank");
            layout.add(anchor);
            return;
        }
        if(parameter[0].contains("check")){
            ((TextView) layout).addCheck(parameter[1]);
            return;
        }
        if(line.contains("lock") && !PathFinder.isNotFurther( ((TextView) layout).currentPageIndex,((TextView) layout).user.getStudent().getProgress(),((TextView) layout).user.getStudent().getExchangeType().getName())){
            ((TextView) layout).buttonNext.setEnabled(false);
            Button generateUnlockRequest = new Button("Click me to ask unlock");
            if (!MainLayout.EN) generateUnlockRequest.setText("Demande de d??bloquage");
            generateUnlockRequest.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
            generateUnlockRequest.addClickListener(e->{
                generateUnlockRequest.setEnabled(false);
                ((TextView) layout).sendInformation();
            });
            layout.add(generateUnlockRequest);
            return;
        }
        if(parameter[0].contains("ask") && layout instanceof TextView){
            questionnaire((TextView) layout,parameter);
            return;
        }
        if(parameter[0].contains("form")&& layout instanceof TextView) {
            addForm((TextView) layout,parameter[1]);
        }
    }

    /**
     * Add an image using drive or resources as backup
     * @param layout the view
     * @param parameter parameters
     */
    public static void addImage(VerticalLayout layout,String[] parameter){
        Image img;
        File imageFile = new File("./drive/resources/images/"+parameter[1]);
        StreamResource imageResource = new StreamResource(imageFile.getName(), () -> {
            try {
                return new FileInputStream(imageFile);
            } catch (FileNotFoundException e) {
                return null;
            }
        });
        if (!imageFile.exists()) imageResource = new StreamResource(parameter[1],
                () -> HeaderReader.class.getResourceAsStream("/META-INF/resources/images/"+parameter[1]));
        img = new Image(imageResource, "error in Image name " + parameter[1]);
        img.addClassName("square");
        try {
            img.setWidth(parameter[2]);
            img.setHeight(parameter[3]);
        }catch (Exception ignored) {}
        layout.add(img);
    }

    /**
     * Add a question to the view
     * @param layout the view
     * @param parameter list of question and answer
     */
    private static void questionnaire(TextView layout,String[] parameter){
        String question = parameter[1];
        List<String> answers = new ArrayList<>(List.of(parameter));
        answers.remove(1);
        answers.remove(0);
        ExclusiveCheckboxes form = new ExclusiveCheckboxes(answers);
        layout.buttonNext.setVisible(false);
        form.overrideConfirm(
            e->{
                if(form.getValue().contains("\n\n")) {
                    Dialog dataPicker = new Dialog();
                    dataPicker.setHeaderTitle("Please select your desired period");
                    DatePicker from = new DatePicker("FROM");
                    DatePicker to = new DatePicker("TO");
                    from.setValue(LocalDate.now());
                    to.setValue(LocalDate.now());
                    dataPicker.add(from,to);
                    Button confirm = new Button("SEND",ee->{
                        try {
                            MailSender.sendAnswer(layout.user,question, form.getValue()+" FROM "+ from.getValue()+ " TO "+ to.getValue());
                        } catch (Exception ignored) {
                        }
                        dataPicker.close();
                        layout.remove(dataPicker);
                        layout.next();
                    });
                    confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
                    Button cancel = new Button("CANCEL",ee->{
                        dataPicker.close();
                        layout.remove(dataPicker);
                    });
                    cancel.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_PRIMARY);
                    dataPicker.getFooter().add(new HorizontalLayout(confirm,cancel));
                    layout.add(dataPicker);
                    dataPicker.open();
                }
                else {
                    try {
                        MailSender.sendAnswer(layout.user,question, form.getValue());
                    } catch (Exception ignored) {
                    }
                    layout.next();
                }
            }
        );
        if (!layout.user.getStudent().getSchool().getCountry().getCountry_name().contains("Argentina")) form.disableAnOption("^[***]");
        else form.removeMarker("*****");
        if (PathFinder.isNotFurther(layout.user.getStudent().getProgress(), layout.currentPageIndex, layout.user.getStudent().getExchangeType().getName())) form.disabled();
        layout.add(form);
    }

    /**
     * Add a contact card
     * @param layout the view
     * @param name contact name
     * @param phoneNumber contact numbers
     * @param email contact email
     * @param address contact address
     */
    private static void addContact(VerticalLayout layout,String name,String phoneNumber,String email,String address){
        HorizontalLayout subLayout = new HorizontalLayout();
        subLayout.add(new HorizontalLayout(new Icon(VaadinIcon.USER_CARD),new Paragraph(name)));
        subLayout.setWidthFull();
        layout.add(subLayout);
        if (!phoneNumber.isEmpty()) {
            subLayout = new HorizontalLayout();
            subLayout.add(new HorizontalLayout(new Icon(VaadinIcon.PHONE),new Anchor("tel:"+phoneNumber,phoneNumber)));
            subLayout.setWidthFull();
            layout.add(subLayout);
        }
        if (!email.isEmpty()){
            subLayout = new HorizontalLayout();
            subLayout.add(new HorizontalLayout(new Icon(VaadinIcon.ENVELOPE),new Anchor("mailto:"+email,email)));
            subLayout.setWidthFull();
            layout.add(subLayout);
        }
        if(!address.isEmpty()){
            subLayout = new HorizontalLayout();
            subLayout.add(new HorizontalLayout(new Icon(VaadinIcon.MAP_MARKER),new Paragraph(address)));
            subLayout.setWidthFull();
            layout.add(subLayout);
        }

    }

    private static void addForm(TextView layout, String type){
        layout.trigger++;
        layout.verify();
        if (type.contains("flight")){
            FlightLayout flightLayout = new FlightLayout(layout.user, layout.crmService);
            flightLayout.form.addListener(AbstractForm.SaveEvent.class, e->{
                flightLayout.save(e);
                layout.trigger--;
                layout.verify();
            });
            layout.add(flightLayout);
            return;
        }
        if (type.contains("major")){
            MajorLayout majorLayout = new MajorLayout(layout.crmService, layout.user);
            majorLayout.confirm.addClickListener(e->{
                majorLayout.submit();
                layout.trigger--;
                layout.verify();
            });
            layout.add(majorLayout);
        }
    }

    /**
     * Show/hide button depending on progression
     * @param layout textView
     */
    private static void navigationButton(TextView layout){
        String exchange = layout.user.getStudent().getExchangeType().getName();
        int currentPageIndex = PathFinder.index(exchange,layout.currentPageIndex);
        HorizontalLayout buttonBox = new HorizontalLayout();

        layout.buttonNext.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        layout.buttonPrevious.addClickListener(e-> layout.previous());
        layout.back.addClickListener(e->layout.back());
        layout.back.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        layout.buttonPrevious.setEnabled(false);
        layout.buttonPrevious.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        if (currentPageIndex!=0){
            layout.previousId = PathFinder.getPrevious(layout.currentPageIndex,exchange);
            layout.buttonPrevious.setEnabled((true));
        }

        if (currentPageIndex != PathFinder.lastIndex(exchange)){
            layout.nextId = PathFinder.getNext(layout.currentPageIndex,
                    layout.user.getStudent().getExchangeType().getName());
            layout.buttonNext.addClickListener(e-> layout.next());
        }
        else{
            layout.buttonNext.setVisible(false);
        }
        buttonBox.add(layout.buttonPrevious,layout.back,layout.buttonNext);
        layout.add(buttonBox);
    }
}
