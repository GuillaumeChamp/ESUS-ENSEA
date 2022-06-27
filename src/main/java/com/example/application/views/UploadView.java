package com.example.application.views;

import com.example.application.data.generator.ZipDir;
import com.example.application.security.SecurityService;
import com.example.application.views.components.forms.FileForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.FileUtils;
import org.atmosphere.util.IOUtils;
import org.vaadin.filesystemdataprovider.FilesystemData;
import org.vaadin.filesystemdataprovider.FilesystemDataProvider;

import javax.annotation.security.PermitAll;
import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.List;

@PermitAll
@PageTitle("drive")
@Route(value = "upload",layout = MainLayout.class)
public class UploadView extends VerticalLayout {
    private final List<String> forbidden;
    private final SecurityService service;
    private String path = "./drive/";
    private FileForm form;
    private TreeGrid<File> tree;

    /**
     * This view lead to a drive
     * @param service security service to identify permissions
     */
    public UploadView(SecurityService service){
        this.service = service;
        forbidden = List.of(new String[]{service.getAuthenticatedUser().getUsername(),
                "drive", "resources","header","images","mail","text","public","FR"});
        setHeightFull();
        setWidth("100%");

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

        FileBuffer buffer = new FileBuffer();
        Upload upload = new Upload(buffer);

        int maxFileSizeInBytes = 5 * 1024 * 1024 ; // 5 Mo
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
            InputStream inputStream = buffer.getInputStream();
            try {
                processFile(inputStream, fileName);
                refreshAll();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        H4 title = new H4("Upload file");
        Paragraph hint = new Paragraph("Maximum file size: 5 MB");
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
        content.setWidth("100%");
        content.setHeightFull();
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
        if (! new File(path+File.separator+"public").exists()){
            System.out.println("creating repository "+rootFile.mkdirs());
            File resource = new File("./drive/resources/header");
            resource.mkdirs();
            resource = new File("./drive/resources/images");
            resource.mkdirs();
            resource = new File("./drive/resources/mail");
            resource.mkdirs();
            resource = new File("./drive/public");
            resource.mkdirs();
            resource = new File("./drive/resources/text");
            resource.mkdirs();
            resource = new File("./drive/resources/FR/text");
            resource.mkdirs();
            resource = new File("./drive/resources/FR/header");
            resource.mkdirs();
        }
        FilesystemData root = new FilesystemData(rootFile, true);
        if (!service.getAuthenticatedUser().isAdmin()) root.addRootItems(new File("./drive/public").listFiles());
        FilesystemDataProvider fileSystem = new FilesystemDataProvider(root);
        tree = new TreeGrid<>();
        tree.setWidth("100%");
        tree.setHeightFull();
        tree.setDataProvider(fileSystem);
        tree.addHierarchyColumn(File::getName).setHeader("Name").setWidth("25em").setFlexGrow(0);
        tree.addColumn(file -> FileUtils.sizeOf(file)>>10).setHeader("Size (ko)");
        tree.addColumn(file -> file.isDirectory() ? "Directory" : "File").setHeader("Type");
        tree.asSingleSelect().addValueChangeListener(e->fileSelected(e.getValue()));
        GridContextMenu<File> menu = tree.addContextMenu();
        menu.addItem("delete",e-> {
            File file =e.getItem().get();
            if (forbidden.contains(file.getName())||!service.getAuthenticatedUser().isAdmin() && file.getPath().contains("drive"+ File.separator +"public")) {
                Notification notification = Notification.show("file protected",2000, Notification.Position.BOTTOM_START);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            try {
                FileUtils.forceDelete(file);
                if(!service.getAuthenticatedUser().isAdmin()) refreshAll();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menu.addItem("download",e->{
            File file = e.getItem().get();
            StreamResource streamResource = generateResource(file);
            Anchor hiddenDownloadLink = new Anchor(streamResource, "Workaround");
            hiddenDownloadLink.getElement().setAttribute("style", "display: none");
            UI.getCurrent().getElement().appendChild(hiddenDownloadLink.getElement());
            UI.getCurrent().getPage().executeJs("$0.click();", hiddenDownloadLink.getElement());
        });
    }

    private InputStream generateFile(File f) throws Exception{
        if (f.isFile()) return new FileInputStream(f);
        return ZipDir.Compress(f);
    }
    private StreamResource generateResource(File file){
        if (file.isFile()) return new StreamResource(file.getName(), () -> {
            try {
                return generateFile(file);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return new StreamResource(file.getName()+".zip", () -> {
            try {
                return generateFile(file);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Add event on the form
     */
    private void configureForm(){
        form = new FileForm();
        form.addListener(FileForm.SaveEvent.class, this::saveFile);
        form.addListener(FileForm.DeleteEvent.class, this::deleteFile);
        form.addListener(FileForm.CloseEvent.class, e -> form.setVisible(false) );
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

    /**
     * Update the view to include change (no better option found than to refresh)
     */
    private void closeEditor(){
        refreshAll();
        form.setVisible(false);
    }

    /**
     * Apply the behaviour when a file is selected
     * If allowed directory, modify the upload destination
     * If forbidden or public repository apply restricted options
     * @param file selected file to analyse
     */
    private void fileSelected(File file){
        form.setVisible(true);
        if (file.isDirectory() && (!file.getPath().contains("public") || service.getAuthenticatedUser().isAdmin())) {
            path = file.getPath();
            //form.setVisible(false);
        }
        if (forbidden.contains(file.getName())) form.setVisible(false);
        form.setFile(file);
        if(!service.getAuthenticatedUser().isAdmin() && file.getPath().contains("drive"+ File.separator +"public")) form.disableDelete();
    }
}
