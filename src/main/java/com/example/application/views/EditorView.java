package com.example.application.views;

import com.example.application.data.generator.TextConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextArea;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class EditorView extends Dialog {
    TextArea editor = new TextArea();
    Button save = new Button("SAVE");
    Button close = new Button("CLOSE",e->this.close());

    /**
     * A simple text editor layout for UTF8 files
     * @param file file to edit
     */
    public EditorView(File file) {
        setHeaderTitle("Editing " + file.getName());
        editor.setValue(TextConverter.ConvertFile(file));
        editor.setSizeFull();
        editor.getElement().getStyle().set("white-space", "pre-wrap");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        save.addClickListener(e->{
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(editor.getValue().getBytes(StandardCharsets.UTF_8));
                this.close();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        });
        setSizeFull();
        add(editor);
        getFooter().add(save,close);
        this.open();
    }

}
