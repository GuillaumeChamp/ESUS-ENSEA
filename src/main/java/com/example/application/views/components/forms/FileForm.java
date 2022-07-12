package com.example.application.views.components.forms;

import com.example.application.data.generator.ZipDir;
import com.example.application.views.EditorView;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.*;


public class FileForm extends FormLayout {
    private File file;
    private final Button save = new Button("Rename");
    private final Button download = new Button("Download");
    private FileDownloadWrapper wrapper;
    private final Button delete = new Button("Delete");
    private final Button close = new Button("Cancel");
    private final Button edit = new Button("Edit");

    public TextField name = new TextField("File Name");

    public FileForm(){
        edit.addClickListener(e->new EditorView(file));
        translate();
        add(name,
                createButtonsLayout());
    }
    @SuppressWarnings("SpellCheckingInspection")
    private void translate(){
        if (MainLayout.EN) return;
        delete.setText("Supprimer");
        close.setText("Fermer");
        edit.setText("Editer");
        save.setText("Renomer");
        name.setLabel("Nom du fichier");
        download.setText("Télécharger");
    }

    /**
     * Load a file in the form managing file typed options and preparing the download
     * @param file the drive's file
     */
    public void setFile(File file) {
        this.file = file;
        delete.setVisible(true);
        edit.setVisible(false);
        name.setValue(file.getName());
        if (file.isFile()) {
            wrapper.setResource(new StreamResource(file.getName(),()-> {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }));

            if (file.getName().contains(".txt")||file.getName().contains(".properties")||file.getName().contains(".sql")){
                edit.setVisible(true);
            }
        }
        if (file.isDirectory()) {
            wrapper.setResource(new StreamResource(file.getName() + ".zip", () -> {
                try {
                    return ZipDir.Compress(file);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }));
        }
    }

    /**
     * Remove the delete button (use to manage permission)
     */
    public void disableDelete(){
        delete.setVisible(false);
        save.setVisible(false);
    }

    /**
     * Create the layout of buttons formatted according the desired design
     * @return the formatted layout holding buttons
     */
    private VerticalLayout createButtonsLayout() {
        wrapper = new FileDownloadWrapper(null);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        download.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        wrapper.wrapComponent(download);
        download.setSizeFull();
        wrapper.setSizeFull();
        edit.setSizeFull();
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);

        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(event -> fireEvent(new SaveEvent(this,file)));
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, file)));
        close.addClickListener(event -> fireEvent(new FileForm.CloseEvent(this)));
        HorizontalLayout layout = new HorizontalLayout(save, delete, close);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return new VerticalLayout(wrapper,layout,edit);
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
