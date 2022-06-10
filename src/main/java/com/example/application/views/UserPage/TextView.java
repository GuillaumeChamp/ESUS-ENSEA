package com.example.application.views.UserPage;

import com.example.application.data.PathFinder;
import com.example.application.data.entity.Request;
import com.example.application.data.entity.Student;
import com.example.application.data.entity.User;
import com.example.application.data.generator.HeaderReader;
import com.example.application.data.service.CrmService;
import com.example.application.security.SecurityService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("Home")
@Route(value = "text",layout = MainLayout.class)
@CssImport("themes/myapp/components/vaadin-text-area.css")
public class TextView extends VerticalLayout implements BeforeLeaveObserver{

    public String currentPageIndex;
    public int trigger = 0; //checkbox counter
    public Button buttonNext;
    public Button buttonPrevious;
    public Button back;
    public String previousId;
    public String nextId;
    final SecurityService service;
    public final CrmService crmService;
    public final User user;

    public TextView(SecurityService service, CrmService crmService) throws Exception {
        this.service = service;
        this.crmService = crmService;
        this.user = service.getAuthenticatedUser().getUser();
        try {
            currentPageIndex = user.getStudent().getProgress();
            HeaderReader.headerRead(this,"page"+ currentPageIndex);
        }catch (NullPointerException exception){
            currentPageIndex="-1";
            HeaderReader.headerRead(this,"admin");
        }
        PathFinder.load();
        addClassName("TextView");
        getElement().getStyle().set("background-image","url('images/test.png')");
        getElement().getStyle().set("background-repeat", "no-repeat");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

    }

    public void next(){
        Student student = user.getStudent();
        if (PathFinder.isNotFurther(student.getProgress(), nextId,student.getExchangeType().getName())){
            student.setProgress(nextId);
            MainLayout.progress(PathFinder.index(user.getStudent().getExchangeType().getName(),nextId));}
        currentPageIndex = nextId;
        this.removeAll();

        HeaderReader.headerRead(this,"page"+nextId);
    }
    public void previous(){
        currentPageIndex = previousId;
        this.removeAll();
        HeaderReader.headerRead(this,"page"+previousId);
    }
    public void addCheck(String label){
        Checkbox checkbox = new Checkbox(label);
        trigger++;
        checkbox.addClickListener(e->{
            if (checkbox.getValue()) trigger--;
            if (!checkbox.getValue()) trigger++;
            verify();
        });
        verify();
        this.add(checkbox);
    }
    public void verify(){
        buttonNext.setEnabled(trigger <= 0);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        saveProgress(user);
    }
    private void saveProgress(User user){
        crmService.saveStudent(user.getStudent());
    }
    public void sendInformation(){
        Request req = new Request();
        req.setStudent(user.getStudent());
        req.setProgress(user.getStudent().getProgress());
        crmService.createRequest(req);
    }
    public void back(){
        this.removeAll();
        this.currentPageIndex = user.getStudent().getProgress();
        HeaderReader.headerRead(this,"page"+ currentPageIndex);
    }

}
