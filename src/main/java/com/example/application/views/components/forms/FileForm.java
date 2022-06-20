package com.example.application.views.components.forms;

import com.example.application.data.generator.ZipDir;
import com.example.application.views.EditorView;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.*;


public class FileForm extends FormLayout {
    private final Button save = new Button("Rename");
    private final Button download = new Button("Download");
    private final Button delete = new Button("Delete");
    private final Button close = new Button("Cancel");
    private final Button edit = new Button("Edit");
    private File file;
    public TextField name = new TextField("File Name");

    public FileForm(){
        edit.addClickListener(e->new EditorView(file));
        add(name,
                createButtonsLayout());
    }

    public void setFile(File file) {
        this.file = file;
        delete.setVisible(true);
        edit.setVisible(false);
        FileDownloadWrapper wrapper = null;
        name.setValue(file.getName());
        if (file.isFile()) {
            download.setEnabled(true);
            wrapper =
                    new FileDownloadWrapper(file.getName(),file);

            if (file.getName().contains(".txt")||file.getName().contains(".properties")||file.getName().contains(".sql")){
                edit.setVisible(true);
            }
        }
        if (file.isDirectory()) {
             wrapper = new FileDownloadWrapper(new StreamResource(file.getName() + ".zip", () -> {
                try {
                    return ZipDir.Compress(file);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }));
        }
        assert wrapper != null;
        wrapper.wrapComponent(download);
        add(wrapper);
    }

    public void disableDelete(){
        delete.setVisible(false);
        save.setVisible(false);
    }
    private VerticalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        download.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);

        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(event -> fireEvent(new SaveEvent(this,file)));
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, file)));
        close.addClickListener(event -> fireEvent(new FileForm.CloseEvent(this)));

        return new VerticalLayout(new HorizontalLayout(save, delete, close),new HorizontalLayout(download,edit));
    }
    // Events
    public static abstract class FormEvent extends ComponentEvent<FileForm> {
        private final File object;

        protected FormEvent(FileForm source, File object) {
            super(source, false);
            this.object = object;
        }

        public File getObject() {
            return object;
        }
    }

    public static class SaveEvent extends FormEvent {
        SaveEvent(FileForm source, File object) {
            super(source, object);
        }
    }

    public static class DeleteEvent extends FileForm.FormEvent {
        DeleteEvent(FileForm source, File object) {
            super(source, object);
        }

    }

    public static class CloseEvent extends FileForm.FormEvent {
        CloseEvent(FileForm source) {
            super(source, null);
        }
    }

    public <t extends ComponentEvent<?> > Registration addListener(Class<t> eventType,
                                                                   ComponentEventListener<t> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
