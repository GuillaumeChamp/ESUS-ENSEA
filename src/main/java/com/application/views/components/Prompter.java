package com.application.views.components;

import com.application.views.components.forms.AbstractForm;
import com.application.data.entity.Request;
import com.application.data.entity.School;
import com.application.data.entity.Student;
import com.application.data.entity.User;
import com.application.data.service.CrmService;
import com.application.data.service.MailSender;
import com.application.views.UserPage.RegisterView;
import com.application.views.components.forms.SchoolForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import net.bytebuddy.utility.RandomString;

public class Prompter {
    public static void prompt(VerticalLayout layout,String text){
        Dialog prompt = new Dialog();
        Button button = new Button("ok", e -> {
            prompt.close();
            layout.remove(prompt);
        });
        prompt.setHeaderTitle(text);
        prompt.getFooter().add(button);
        layout.add(prompt);
        prompt.open();
    }

    /**
     * Display the creation of school form
     * @param layout layout to add this
     * @param service database service
     */
    public static void promptForm(RegisterView layout, CrmService service){
        Dialog prompt = new Dialog();
        SchoolForm schoolForm = new SchoolForm(service.findAllCountries(""));
        schoolForm.setWidth("25em");
        schoolForm.setObject(new School());

        schoolForm.removeDelete();
        schoolForm.addListener(AbstractForm.CloseEvent.class, e -> {
            prompt.close();
            layout.remove(prompt);
        });
        schoolForm.addListener(AbstractForm.SaveEvent.class, e -> {
            service.saveSchool((School) e.getObject());
            prompt.close();
            layout.updateSchool(service);
            layout.remove(prompt);
        });

        prompt.setHeaderTitle("Add Your School");
        prompt.add(schoolForm);
        layout.add(prompt);
        prompt.open();
    }

    /**
     * This prompter is used when an administrator decline an update request
     * @param layout attached layout
     * @param request hold student data
     */
    public static void promptComment(VerticalLayout layout, Request request){
        Dialog prompt = new Dialog();
        Button close = new Button("close", e -> {
            prompt.close();
            layout.remove(prompt);
        });
        TextField textField = new TextField("Motif");
        close.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
        Button send = new Button("Send Mail");
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        send.addClickListener(e-> MailSender.InformRequestFailure(request,textField.getValue()));
        prompt.setHeaderTitle("Motif de refus");
        prompt.getFooter().add(send,close);
        layout.add(prompt);
        prompt.open();
    }

    /**
     * Prompter with recover option
     * @param layout where to attach
     * @param service database manager
     */
    public static void promptRecover(VerticalLayout layout, CrmService service){
        Dialog prompt = new Dialog();
        TextField username = new TextField("Username");
        Button submit = new Button("Submit Request");
        Button ok = new Button("Cancel");
        ok.addClickListener(e->{
            prompt.close();
            layout.remove(prompt);
        });
        prompt.setHeaderTitle("Password Recovery");
        ok.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        prompt.add(new Paragraph("Please enter your username"));
        prompt.add(username);
        prompt.getFooter().add(new HorizontalLayout(submit,ok));
        submit.addClickListener(e->{
            try{
                User data = service.findUser(username.getValue());
                RandomString string = new RandomString(12);
                String password = string.nextString();
                MailSender.recover(data.getStudent().getEmail(),password);
                data.setPassword((password));
                service.updateAccount(data);
                prompt.close();
                layout.remove(prompt);
                Prompter.prompt(layout,"Everything seem to be ok, check your email");
            }catch (Exception ee){
                prompt.close();
                layout.remove(prompt);
                Prompter.prompt(layout,"Error while trying to recover, please contact the Admin at ri@ensea.fr");
            }
        });
        layout.add(prompt);
        prompt.open();
    }

    /**
     * Prompt a message to ask which data you want to purge
     * @param layout where to attach (active layout)
     * @param service database manager used to perform delete action
     */
    public static void askDeleteAll(VerticalLayout layout,CrmService service){

        Checkbox c0 = new Checkbox("delete Account");
        Checkbox c1 = new Checkbox("delete student");
        c1.setEnabled(false);
        c0.addValueChangeListener(e->{
            c1.setEnabled(e.getValue());
            if ((!e.getValue())) c1.setValue(false);
        });
        Checkbox c2 = new Checkbox("delete student associate data");
        Checkbox c3 = new Checkbox("delete school");
        c2.setEnabled(false);
        c1.addValueChangeListener(e->{
            c2.setEnabled(e.getValue());
            c3.setEnabled(e.getValue());
            if (!e.getValue()) {
                c2.setValue(false);
                c3.setValue(false);
            }
        });
        Dialog prompt = new Dialog(new VerticalLayout(c0,c1,c2,c3));
        prompt.setHeaderTitle("Selection d'option");
        Button confirm = new Button("Valider",e->{
            service.dropAll(c0.getValue(),c1.getValue(),c2.getValue(),c3.getValue());
            prompt.close();
            layout.remove(prompt);
        });
        confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancel = new Button("Annuler",e->{
            prompt.close();
            layout.remove(prompt);
        });
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        prompt.getFooter().add(new HorizontalLayout(confirm,cancel));
        layout.add(prompt);
        prompt.open();
    }

    /**
     * Used to prompt a warming when trying to change progression path
     * @param layout active layout
     * @param student student who want to change
     * @param service database manager (which change path and save student)
     */
    public static void promptExchangeChanged(VerticalLayout layout, Student student,CrmService service){
        Dialog prompt = new Dialog();
        Button confirm = new Button("confirm", e -> {
            prompt.close();
            layout.remove(prompt);
            student.setProgress("0");
            service.saveStudent(student);
            UI.getCurrent().getPage().reload();
        });
        confirm.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
        Button cancel = new Button("cancel", e -> {
            prompt.close();
            layout.remove(prompt);
        });
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_PRIMARY);
        prompt.setHeaderTitle("You have changed your exchange type");
        prompt.add(new Paragraph("Your action will reset all your progress.\nAre you sure ?"));
        prompt.getFooter().add(new HorizontalLayout(confirm,cancel));
        layout.add(prompt);
        prompt.open();
    }

}
