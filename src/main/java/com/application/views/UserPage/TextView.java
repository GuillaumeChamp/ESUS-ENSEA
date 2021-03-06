package com.application.views.UserPage;

import com.application.data.entity.Request;
import com.application.data.entity.Student;
import com.application.data.entity.User;
import com.application.data.generator.HeaderReader;
import com.application.data.service.CrmService;
import com.application.data.service.PathFinder;
import com.application.security.SecurityService;
import com.application.views.MainLayout;
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

    public TextView(SecurityService service, CrmService crmService) {
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
        addClassName("TextView");
        getElement().getStyle().set("background-image","url('images/test.png')");
        getElement().getStyle().set("background-repeat", "no-repeat");
        setSizeFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    /**
     * Make user go to the next page and update his progression if needed
     */
    public void next(){
        Student student = user.getStudent();
        if (PathFinder.isNotFurther(student.getProgress(), nextId,student.getExchangeType().getName())){
            student.setProgress(nextId);
            MainLayout.progress(PathFinder.index(student.getExchangeType().getName(),nextId));}
        currentPageIndex = nextId;
        this.removeAll();
        trigger=0;

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
        if (PathFinder.isNotFurther(currentPageIndex,user.getStudent().getProgress(),user.getStudent().getExchangeType().getName())) return;
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
