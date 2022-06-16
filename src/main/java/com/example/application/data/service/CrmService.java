package com.example.application.data.service;

import com.example.application.data.PathFinder;
import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;


@Service 
public class CrmService {
    @Autowired
    private final DataSource dataSource;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final ExchangeTypeRepository exchangeTypeRepository;
    private final CountryRepository countryRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final RequestRepository requestRepository;
    private final TriggersRepository triggersRepository;
    private final ParkourRepository parkourRepository;

    public CrmService(DataSource dataSource, StudentRepository studentRepository, SchoolRepository schoolRepository, ExchangeTypeRepository exchangeTypeRepository, CountryRepository countryRepository, JobRepository jobRepository, UserRepository userRepository, FlightRepository flightRepository, RequestRepository requestRepository, TriggersRepository triggersRepository, ParkourRepository parkourRepository) {
        this.dataSource = dataSource;
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
        this.exchangeTypeRepository = exchangeTypeRepository;
        this.countryRepository = countryRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.requestRepository = requestRepository;
        this.triggersRepository = triggersRepository;
        this.parkourRepository = parkourRepository;
        init();
    }

    private void init(){
        if (userRepository.count()<1) {
            User admin = new User();
            admin.setPassword("adminENSEA");
            admin.setUsername("admin");
            admin.setActive(1);
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }
        if (countryRepository.count()<3){
            try {
                ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("data.sql"));
                resourceDatabasePopulator.execute(dataSource);
            }catch (Exception ignored){}

        }
        if (parkourRepository.count()<3){
            try{
                ResourceDatabasePopulator resourceDatabasePopulator;
                try{
                    resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new FileUrlResource("./drive/resources/parkour.sql"));
                    resourceDatabasePopulator.execute(dataSource);
                }catch (Exception e) {
                    resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("/META-INF/resources/parkour.sql"));
                    System.out.println("Path.properties not found in drive, using backup");
                    resourceDatabasePopulator.execute(dataSource);
                }
            }catch (Exception ee){
                ee.printStackTrace();
                System.out.println("Error : parkour.sql not Found");
            }
        }
    }
    public void dropAll(boolean deleteUser,boolean deleteStudent,boolean deleteStudentData,boolean deleteSchool){
        if (deleteUser) userRepository.deleteAll();
        if (deleteStudent) studentRepository.deleteAll();
        if (deleteStudentData) {
            flightRepository.deleteAll();
            requestRepository.deleteAll();
            triggersRepository.deleteAll();
        }
        if(deleteSchool) schoolRepository.deleteAll();

        init();
    }

    //STUDENT
    public List<Student> findAllStudents(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty())
            return studentRepository.findAll();
        return studentRepository.search(stringFilter);
    }
    public long countStudents() {
        return studentRepository.count();
    }
    public long countCas(){
        List<Student> students = studentRepository.findAll();
        students.removeIf(s -> s.getProgress().contains("0")||s.getProgress().contains("1"));
        return students.size();
    }
    public long countFlight(){
        return flightRepository.count();
    }
    public void saveStudent(Student student) {
        if (student == null) {
            return;
        }
        studentRepository.save(student);
    }
    public void deleteStudent(Student student) {
        Triggers studentTriggers = student.getTriggers();
        Flight studentFlight = student.getFlight();
        studentRepository.delete(student);
        triggersRepository.delete(studentTriggers);
        if (studentFlight!=null) flightRepository.delete(studentFlight);
    }

    //SCHOOL
    public List<School> findAllSchools(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty())
            return schoolRepository.findAll();
        return schoolRepository.search(stringFilter);
    }
    public void saveSchool(School school){
        if(!(school==null)) schoolRepository.save(school);
    }
    public void deleteSchool(School school){
        schoolRepository.delete(school);
    }

    //EXCHANGE
    public List<ExchangeType> findAllExchanges(){
        return exchangeTypeRepository.findAll();
    }
    //COUNTRY
    public List<Country> findAllCountries(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty())
            return countryRepository.findAll();
        return countryRepository.search(stringFilter);
    }
    //JOB
    public List<Job> findAllJobs(){return jobRepository.findAll();}
    //PARKOUR
    public List<Parkour> findAllParkour(){
        return parkourRepository.findAll();
    }
    public List<Parkour> findAllParkour(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty())
            return parkourRepository.findAll();
        return parkourRepository.search(stringFilter);
    }
    public Parkour findParkour(String semester,String major, String option){
        if (major==null) major="";
        if (option==null) option="";
        List<Parkour> list = findAllParkour();
        for (Parkour p : list){
            if (p.getMajor().equals(major) && p.getSemester().equals(semester)&& p.getOption_suivi().equals(option))
                return p;
        }
        return null;
    }
    public void saveParkour(Parkour parkour){
        if (parkour==null) return;
        if (parkour.getOption_suivi()==(null)) parkour.setOption_suivi("");
        if (parkour.getMajor()==null) parkour.setMajor("");
        parkourRepository.save(parkour);
    }
    public void deleteParkour(Parkour parkour){
        parkourRepository.delete(parkour);
    }
    //REQUEST
    public List<Request> findAllRequests()
    {
        return requestRepository.findAll();
    }
    public void createRequest(Request request){
        requestRepository.save(request);
    }
    public void terminateRequest(Request request){
        Student student = request.getStudent();
        student.setProgress(PathFinder.getNext(request.getProgress(),student.getExchangeType().getName()));
        studentRepository.save(student);
        requestRepository.delete(request);
    }
    public void deleteRequest(Request request){
        requestRepository.delete(request);
    }

    //Flight
    public void saveFlight(Flight flight){
        if (flight ==null) return;
        flightRepository.save(flight);
    }

    //USER
    public void updateAccount(User user){
        if (user == null){
            System.out.println("Error no user selected");
            return;
        }
        userRepository.save(user);
    }
    public void deleteUser(String username){
        User find = userRepository.findByUsername(username);
        if (find != null) userRepository.delete(find);
    }
    public boolean accountExist(String username){
        User find = userRepository.findByUsername(username);
        return (find!=null);
    }

    public User findUser(String username){
        if (!accountExist(username)) return null;
        return userRepository.findByUsername(username);
    }

    //TRIGGERS
    public void updateTriggers(Triggers triggers){
        triggersRepository.save(triggers);
    }
}