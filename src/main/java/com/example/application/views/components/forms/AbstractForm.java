package com.example.application.views.components.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public abstract class AbstractForm<T> extends FormLayout {
    protected Button save = new Button("Save");
    protected Button delete = new Button("Delete");
    protected Button close = new Button("Cancel");
    Binder<T> binder;
    protected T object;

    protected HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        close.addClickShortcut(Key.ESCAPE);
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, object)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));


        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }
    public void removeDelete(){
        delete.setEnabled(false);
        delete.setVisible(false);
    }
    public void removeClose(){
        close.setEnabled(false);
        close.setVisible(false);
    }

    protected void validateAndSave() {
        try {
            binder.writeBean(object);
            fireEvent(new SaveEvent(this, object));
        } catch (ValidationException e) {
            e.printStackTrace();
        }catch (NullPointerException ignored){
        }
    }
    public void setObject(T object) {
        this.object = object;
        binder.readBean(object);
    }

    // Events
    @SuppressWarnings("rawtypes")
    public static abstract class FormEvent extends ComponentEvent<AbstractForm> {
        private final Object object;

        protected FormEvent(AbstractForm source, Object object) {
            super(source, false);
            this.object = object;
        }

        public Object getObject() {
            return object;
        }
    }

    public static class SaveEvent extends FormEvent {
        SaveEvent(AbstractForm source, Object object) {
            super(source, object);
        }
    }

    public static class DeleteEvent extends FormEvent {
        DeleteEvent(AbstractForm source, Object object) {
            super(source, object);
        }

    }

    public static class CloseEvent extends FormEvent {
        CloseEvent(AbstractForm source) {
            super(source, null);
        }
    }

    public <t extends ComponentEvent<?> > Registration addListener(Class<t> eventType,
                                                                   ComponentEventListener<t> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
