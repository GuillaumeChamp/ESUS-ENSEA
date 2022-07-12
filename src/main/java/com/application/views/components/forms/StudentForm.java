package com.application.views.components.forms;

import com.application.data.entity.*;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import java.util.Arrays;
import java.util.List;
@SuppressWarnings("SpellCheckingInspection")
public class StudentForm extends AbstractForm<Student> {

    TextField firstName = new TextField("First name/prénom");
    TextField lastName = new TextField("Last name/nom");
    ComboBox<Country> nationality = new ComboBox<>("Nationality/nationalité");
    ComboBox<String> gender = new ComboBox<>("Gender/genre");
    EmailField email = new EmailField("Email");
    ComboBox<ExchangeType> exchangeType = new ComboBox<>("ExchangeType/type d'échange");
    ComboBox<School> school = new ComboBox<>("sending institut/institut d'origine");
    ComboBox<Country> country = new ComboBox<>("Born country/pays de naissance");
    ComboBox<String> civility = new ComboBox<>("Civility/civilité");
    ComboBox<Job> job1 = new ComboBox<>("First Parent's Job/profession parent 1");
    ComboBox<Job> job2 = new ComboBox<>("Second Parent's Job/profession parent 2");
    DatePicker born = new DatePicker("When were you born/date de naissance");
    TextField phoneNumber = new TextField("Phone number/numéro de téléphone");
    Paragraph paragraph = new Paragraph("Last year at hight school/dernière année de lycée");
    NumberField endYear = new NumberField("Last year of secondary /année de fin d'étude dans le secondaire");
    NumberField startSuperior = new NumberField("First year of superior/première année dans le supérior");
    TextField bornPlace = new TextField("BornPlace/lieu de naissance");
    Button addSchool;

    /**
     * Create a student form
     * @param schools all available school
     * @param exchangeTypes all available exchange types
     * @param countries all existing country
     * @param jobs all jobs that can be chosen as parents jobs
     */
    public StudentForm(List<School> schools, List<ExchangeType> exchangeTypes,List<Country> countries,List<Job> jobs) {
        this.binder = new BeanValidationBinder<>(Student.class);
        configureField(schools,exchangeTypes,countries,jobs);
        HorizontalLayout schoolLayout = new HorizontalLayout(school,addSchool);
        schoolLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(firstName,
            lastName,
            nationality,
            civility,
            gender,
            born,
            bornPlace,
            country,
            email,
            phoneNumber,
            paragraph,
            endYear,
            startSuperior,
            schoolLayout,
            exchangeType,
            job1,
            job2,
            createButtonsLayout());
    }

    /**
     * Update school list if modified during editing (case of adding a new school)
     * @param updatedSchoolList the new school list
     */
    public void updateSchool(List<School> updatedSchoolList){
        school.setItems(updatedSchoolList);
    }

    /**
     * Fill field possible answer and init other field's elements
     * @param schools list of all possible school
     * @param exchangeTypes all exchange type
     * @param countries all countries
     * @param jobs all jobs
     */
    private void configureField(List<School> schools, List<ExchangeType> exchangeTypes,List<Country> countries,List<Job> jobs){
        civility.setItems(Arrays.asList("Mr","Ms"));
        gender.setItems(Arrays.asList("Male","Female","Non-Binary"));
        school.setItems(schools);
        school.setItemLabelGenerator(School::getName);
        exchangeType.setItems(exchangeTypes);
        exchangeType.setItemLabelGenerator(ExchangeType::getName_ext);
        country.setItems(countries);
        country.setItemLabelGenerator(Country::getCountry_name);
        addSchool = new Button("School Not Found/école non trouvée");
        startSuperior.setMin(2000);
        endYear.setMin(2000);
        addSchool.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nationality.setItems(countries);
        nationality.setItemLabelGenerator(Country::getCountry_name);
        job1.setItems(jobs);
        job2.setItems(jobs);
        job1.setItemLabelGenerator(Job::getJob);
        job2.setItemLabelGenerator(Job::getJob);
        setResponsiveSteps();
        phoneNumber.setHelperText("Format : +123 456789000");
        phoneNumber.setRequired(true);
        phoneNumber.addValueChangeListener(e->{
            if(!phoneNumber.isInvalid()) binder.validate();
        });
        born.setI18n(new DatePicker.DatePickerI18n().setDateFormat("dd.MM.yyyy"));
        binder.bindInstanceFields(this);
    }


    /**
     * use this methode to define what the addSchool button do (maybe fire event is better to stuck to OOP but not really efficiency here)
     */
    public void defineAddSchoolAction(ComponentEventListener<ClickEvent<Button>> event){
        addSchool.addClickListener(event);
    }
}