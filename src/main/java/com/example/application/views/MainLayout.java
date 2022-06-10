package com.example.application.views;

import com.example.application.data.PathFinder;
import com.example.application.data.entity.Student;
import com.example.application.security.SecurityService;
import com.example.application.views.UserPage.CheckList;
import com.example.application.views.UserPage.GeneralInformation;
import com.example.application.views.UserPage.TextView;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;


public class MainLayout extends AppLayout{
    private final SecurityService securityService;
    public static boolean EN = true;
    private static Div progressBarLabel;
    private static Student student;
    private static ProgressBar progressBar;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Welcome at ENSEA");
        logo.addClassNames("text-l", "m-m");
        ComboBox<String> language = new ComboBox<>("language");
        language.setItems("French", "English");
        if (EN) language.setValue("English");
        else language.setValue("French");
        language.addValueChangeListener(e->{
            EN = language.getValue().equals("English");
            UI.getCurrent().getPage().reload();
        });
        Button logout = new Button("Log out", e -> securityService.logout());
        logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout header = new HorizontalLayout(
          new DrawerToggle(), 
          logo
        );
        if (!securityService.getAuthenticatedUser().isAdmin() && securityService.getAuthenticatedUser().getStudent()!=null) {
            VerticalLayout progressBox = new VerticalLayout();
            progressBar = new ProgressBar();
            try {
                PathFinder.load();
            } catch (Exception ignored) {
            }
            student = securityService.getAuthenticatedUser().getStudent();
            progress(PathFinder.index(student.getExchangeType().getName(), student.getProgress()));
            progressBarLabel = new Div();
            progressBarLabel.setText("Admission process "+securityService.getAuthenticatedUser().getStudent().getProgress() +"/"+ PathFinder.lastStep);
            progressBox.add(progressBarLabel,progressBar);
            progressBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);
            header.add(progressBox);
            header.expand(progressBox);
        }
        header.add(logout,language);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");
        addToNavbar(header);
    }

    public static void progress(int value){
        int lastIndex = PathFinder.lastIndex(student.getExchangeType().getName());
        progressBar.setValue((float)value/lastIndex);
        if (value != 0) progressBarLabel.setText("Admission process "+value +"/"+ lastIndex);
    }

    private void createDrawer() {
        RouterLink textView = new RouterLink("Information for incoming student", TextView.class);
        if (!EN) textView.setText("Information d'inscription");
        textView.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink admin = new RouterLink("Admin", AdminView.class);
        RouterLink list = new RouterLink("Liste des étudiants", ListView.class);
        RouterLink page2 = new RouterLink("General information", GeneralInformation.class);
        if (!EN) page2.setText("Informations génerales");
        RouterLink account = new RouterLink("Account", AccountView.class);
        if (!EN) account.setText("Mon compte");
        RouterLink dashboard = new RouterLink("Dashboard", DashboardView.class);
        RouterLink schoolList = new RouterLink("Liste des Ecoles", SchoolView.class);
        RouterLink checkList = new RouterLink("CheckList", CheckList.class);
        RouterLink download = new RouterLink("My depository",UploadView.class);
        if (!EN) download.setText("Mon drive");
        RouterLink parkour = new RouterLink("Liste des majeurs/options",ParkourGrid.class);
        RouterLink map = new RouterLink("Map",MapView.class);
        if (securityService.getAuthenticatedUser().isAdmin())
        addToDrawer(new VerticalLayout(
                admin,
                dashboard,
                list,
                schoolList,
                parkour,
                textView,
                page2,
                download,
                map,
                account
        ));
        else
            addToDrawer(new VerticalLayout(
                    textView,
                    page2,
                    checkList,
                    download,
                    map,
                    account
            ));
    }
}