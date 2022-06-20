package com.example.application.data.entity;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Triggers extends AbstractEntity{

    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(name="listOfTrigger")
    private List<String> checked = new ArrayList<>();

    public void setChecked(ArrayList<String> checked) {
        this.checked = checked;
    }

    public void add(String s){
        if (checked.contains(s)) return;
        checked.add(s);
    }

    public List<String> getChecked() {
        return checked;
    }
}
