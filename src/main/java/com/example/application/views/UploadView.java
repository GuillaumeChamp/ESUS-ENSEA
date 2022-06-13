package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.components.forms.FileForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.FileUtils;
import org.atmosphere.util.IOUtils;
import org.vaadin.filesystemdataprovider.FilesystemData;
import org.vaadin.filesystemdataprovider.FilesystemDataProvider;

import javax.annotation.security.PermitAll;
import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.List;

@PermitAll
@Route(value = "upload",layout = MainLayout.class)
public class UploadView extends VerticalLayout {
    private final SecurityService service;
    private String path = "./drive/";
    private FileForm form;
    private TreeGrid<File> tree;

    public UploadView(SecurityService service){
        this.service = service;
        setSizeFull();

        add(getUpload());
        add(getContent());
    }

    /**
     *Create the layout for the upload menu
     * @return upload layout
     */
    private Div getUpload(){

        if (!service.getAuthenticatedUser().isAdmin())
            path = path.concat(service.getAuthenticatedUser().getUsername()+"/");

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
        upload.setMaxFileSize(maxFileSizeInBytes);

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(
                    errorMessage,
                    5000,
                    Notification.Position.MIDDLE
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            try {
                processFile(inputStream, fileName);
                refreshAll();
            }catch (Exception e){
                e.printStackTrace();
                add(new Paragraph("unable to upload file"));
            }
        });

        H4 title = new H4("Upload file");
        Paragraph hint = new Paragraph("Maximum file size: 10 MB");
        return new Div(title,hint,upload);
    }

    /**
     * Configure the file explorer and the form
     * @return configured layout
     */
    private HorizontalLayout getContent() {
        add(new H4("file explorer"));
        FTPManager();
        configureForm();
        HorizontalLayout content = new HorizontalLayout(tree,form);
        content.setFlexGrow(2, tree);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        form.setVisible(false);
        content.setSizeFull();
        return content;
    }

    /**
     * Use to upload a file
     * @param stream file stream
     * @param fileName name
     * @throws IOException in case of file issue
     */
    private void processFile(InputStream stream,String fileName) throws IOException {
        String directoryName = path;
        File directory = new File(directoryName);
        if (! directory.exists()){
            //noinspection ResultOfMethodCallIgnored
            directory.mkdirs();
        }

        File targetFile = new File(directoryName+"/"+fileName);
        java.nio.file.Files.copy(stream,targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        IOUtils.close(stream);
    }

    /**
     * Manage the drive (create directory if they do not exist)
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void FTPManager(){
        File rootFile = new File(path);
        if (! rootFile.exists()){
            System.out.println("creating repository "+rootFile.mkdirs());
            File resource = new File("./drive/resources/header");
            resource.mkdirs();
            resource = new File("./drive/resources/images");
            resource.mkdirs();
            resource = new File("./drive/resources/mail");
            resource.mkdirs();
            resource = new File("./drive/resources/text");
            resource.mkdirs();
            resource = new File("./drive/resources/FR/text");
            resource.mkdirs();
            resource = new File("./drive/resources/FR/header");
            resource.mkdirs();
        }
        FilesystemData root = new FilesystemData(rootFile, true);
        FilesystemDataProvider fileSystem = new FilesystemDataProvider(root);
        tree = new TreeGrid<>();
        tree.setSizeFull();
        tree.setDataProvider(fileSystem);
        tree.addHierarchyColumn(File::getName).setHeader("Name").setAutoWidth(true).setResizable(true);
        tree.addColumn(file -> FileUtils.sizeOf(file)>>10).setHeader("Size (ko)");
        tree.addColumn(file -> file.isDirectory() ? "Directory" : "File").setHeader("Type");
        tree.asSingleSelect().addValueChangeListener(e->fileSelected(e.getValue()));
    }

    /**
     * Add event on the form
     */
    private void configureForm(){
        form = new FileForm();
        form.setWidth("25em");
        form.addListener(FileForm.SaveEvent.class, this::saveFile);
        form.addListener(FileForm.DeleteEvent.class, this::deleteFile);
        form.addListener(FileForm.CloseEvent.class, e -> closeEditor());
    }

    /**
     * reload the page to make upload visually effective
     */
    private void refreshAll(){
        UI.getCurrent().getPage().reload();
    }
    @SuppressWarnings("all")
    private void saveFile(FileForm.SaveEvent event){
        File file = event.getObject();
        File source = new File(file.getParent()+"/"+form.name.getValue());
        file.renameTo(source);
        closeEditor();
    }
    @SuppressWarnings("all")
    private void deleteFile(FileForm.DeleteEvent event){
        try {
            FileUtils.forceDelete(event.getObject());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        closeEditor();
    }
    private void closeEditor(){
        refreshAll();
        form.setVisible(false);
    }
    private void fileSelected(File file){
        if (file.isDirectory()) path = file.getPath();
        List<String> forbidden = List.of(new String[]{service.getAuthenticatedUser().getUsername(),
                "drive", "resources","header","images","mail","text"});
        if (forbidden.contains(file.getName())) return;
        form.setVisible(true);
        form.setFile(file);
    }
}
