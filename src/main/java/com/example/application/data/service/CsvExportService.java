package com.example.application.data.service;

import com.example.application.data.entity.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
@SuppressWarnings("SpellCheckingInspection")
public class CsvExportService {

    /**
     * Create CSV describing students
     * @param writer service writer
     */
    public static void writeAurion(List<Student> students, Writer writer) {
        CSVFormat format = CSVFormat.Builder.create().setDelimiter(';').setHeader("UUID",
                "CIVILITE",
                "GENRE",
                "NATIONALITE",
                "PRENOM",
                "NOM",
                "EMAIL",
                "DATE_DE_NAISSANCE",
                "LIEU_DE_NAISSANCE",
                "PAYS",
                "ECOLE",
                "FIN_ETUDE_SECONDAIRE",
                "PROFESSION_PARENT1",
                "PROFESSION_PARENT2").build();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format))
        {
            for (Student student : students) {
                csvPrinter.printRecord(student.getId(),
                        student.getCivility(),
                        student.getGender(),
                        student.getNationality().getCountry_name(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getBorn(),
                        student.getBornPlace(),
                        student.getCountry().getCountry_name(),
                        student.getSchool().getName(),
                        student.getEndYear(),
                        student.getJob1().getJob(),
                        student.getJob2().getJob()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeRI(List<Student> students, Writer writer) {
        CSVFormat format = CSVFormat.Builder.create().setDelimiter(';').setHeader("UUID",
                "CIVILITE",
                "GENRE",
                "NATIONALITE",
                "PRENOM",
                "NOM",
                "EMAIL",
                "DATE_DE_NAISSANCE",
                "LIEU_DE_NAISSANCE",
                "PAYS",
                "ECOLE",
                "EXCHANGE_TYPE",
                "FIN_ETUDE_SECONDAIRE",
                "DEBUT_ETUDE_SUPERIEUR",
                "PROFESSION_PARENT1",
                "PROFESSION_PARENT2").build();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format))
        {
            for (Student student : students) {
                csvPrinter.printRecord(student.getId(),
                        student.getCivility(),
                        student.getGender(),
                        student.getNationality().getCountry_name(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getBorn(),
                        student.getBornPlace(),
                        student.getCountry().getCountry_name(),
                        student.getSchool().getName(),
                        student.getExchangeType().getName(),
                        student.getEndYear(),
                        student.getStartSuperior(),
                        student.getJob1().getJob(),
                        student.getJob2().getJob()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMajor(List<Student> students, Writer writer) {
        CSVFormat format = CSVFormat.Builder.create().setDelimiter(';').setHeader("UUID",
                "GENRE",
                "PRENOM",
                "NOM",
                "EMAIL",
                "MAJEUR",
                "OPTION"
                ).build();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format))
        {
            for (Student student : students) {
                if (student.getParkour()==null) student.setParkour(new Parkour());
                csvPrinter.printRecord(student.getId(),
                        student.getGender(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getParkour().getSemester(),
                        student.getParkour().getMajor(),
                        student.getParkour().getOption_suivi()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeFIP(List<Student> students, Writer writer) {
        CSVFormat format = CSVFormat.Builder.create().setDelimiter(';').setHeader("UUID",
                "GENRE",
                "PRENOM",
                "NOM",
                "ORIGINE",
                "EMAIL",
                "TELEPHONE",
                "RESEAU_SOCIAL",
                "MOYEN_DE_TRANSPORT",
                "LIEU_ARRIVEE",
                "TERNMINAL",
                "LIEU_DEPART",
                "DATE",
                "NUMERO_VOL"
        ).build();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format))
        {
            for (Student student : students) {
                if (student.getFlight()==null) student.setFlight(new Flight());
                csvPrinter.printRecord(student.getId(),
                        student.getGender(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getCountry().getCountry_name(),
                        student.getEmail(),
                        student.getFlight().getPhoneNumber(),
                        student.getFlight().getNetwork(),
                        student.getFlight().getMeansOfTransport(),
                        student.getFlight().getPlace(),
                        student.getFlight().getAirportTerminal(),
                        student.getFlight().getDeparture(),
                        student.getFlight().getDate(),
                        student.getFlight().getTransportID()

                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeCountry(List<Country> countries, Writer writer) {
        CSVFormat format = CSVFormat.Builder.create().setDelimiter(';').setHeader("ID",
                "NOM",
                "NB_ETUDIANTS"
        ).build();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format))
        {
            for (Country country : countries) {
                if (country.getStudentCount()!=0) csvPrinter.printRecord(country.getId(),
                        country.getCountry_name(),
                        country.getStudentCount()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeSchool(List<School> schools, Writer writer) {
        CSVFormat format = CSVFormat.Builder.create().setDelimiter(';').setHeader("ID",
                "NOM",
                "PAYS",
                "NB_ETUDIANTS",
                "CONTACT"
        ).build();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format))
        {
            for (School school : schools) {
                if (school.getStudentCount()!=0) csvPrinter.printRecord(school.getId(),
                        school.getName(),
                        school.getCountry().getCountry_name(),
                        school.getStudentCount(),
                        school.getContact()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeParkour(List<Parkour> parkours, Writer writer) {
        CSVFormat format = CSVFormat.Builder.create().setDelimiter(';').setHeader("ID",
                "NOM",
                "PAYS",
                "NB_ETUDIANTS",
                "CONTACT"
        ).build();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format))
        {
            for (Parkour parkour : parkours) {
                csvPrinter.printRecord(parkour.getId(),
                        parkour.getSemester(),
                        parkour.getMajor(),
                        parkour.getOption_suivi(),
                        parkour.getStudentCount()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}