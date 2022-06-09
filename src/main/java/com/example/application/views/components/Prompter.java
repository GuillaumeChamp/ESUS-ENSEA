package com.example.application.views.components;

import com.example.application.data.entity.Request;
import com.example.application.data.entity.School;
import com.example.application.data.entity.User;
import com.example.application.data.service.CrmService;
import com.example.application.data.service.MailSender;
import com.example.application.views.UserPage.RegisterView;
import com.example.application.views.components.forms.AbstractForm;
import com.example.application.views.components.forms.SchoolForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
        SchoolForm schoolForm = new SchoolForm(service.findAllCountries());
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

}
