package com.example.application.data.generator;

import com.example.application.data.entity.Student;
import com.example.application.data.entity.Triggers;
import com.example.application.views.UserPage.CheckList;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;

import java.io.*;

public class CheckListBuilder {

    public static void fill(CheckList checkList, Triggers studentTriggers){
        InputStream stream = HeaderReader.findFile("checklist.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            String line = reader.readLine();
            while (line!=null){
                String[] parameters = line.split(";");
                if(parameters[0].contains("check")){
                    Checkbox checkbox = new Checkbox(parameters[2]);
                    checkbox.getStyle().set("lumo-disabled-text-color","rgba(149, 196, 31, 0.5)");
                    checkbox.addClickListener(e->{
                        if (checkbox.getValue()) studentTriggers.add(parameters[1]);
                    });
                    try{
                        checkbox.setValue(studentTriggers.getChecked().contains(parameters[1]));
                    }catch (NullPointerException ignored){}
                    checkList.add(checkbox);
                }
                if (parameters[0].contains("para"))
                    checkList.add(new Paragraph(parameters[1]));
                line = reader.readLine();
            }
        }catch (IndexOutOfBoundsException e){
            checkList.add(new Paragraph("check for missing parameters in checklist.txt"));
        }catch (IOException e){
            checkList.add(new Paragraph("check for missing file or invalid format"));
        }
    }
    public static void configureView(Grid<Student> grid){
        InputStream stream = HeaderReader.findFile("checklist.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            String line = reader.readLine();
            while (line!=null){
                String[] parameters = line.split(";");
                if(parameters[0].contains("check")){
                    try{
                        String generateException = parameters[3];
                        grid.addColumn(student -> student.getTriggers().getChecked().contains(parameters[1])).setHeader(parameters[1]);
                    }catch (ArrayIndexOutOfBoundsException ignored){}
                }
                line = reader.readLine();
            }
        }catch (IOException e){
            System.out.println("File CheckList not found in any language");
        }
    }
}
