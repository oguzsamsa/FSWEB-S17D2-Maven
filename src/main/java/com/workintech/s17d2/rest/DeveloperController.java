package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Experience;
import jakarta.annotation.PostConstruct;
import com.workintech.s17d2.model.Developer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DeveloperController {
    public Map<Integer, Developer> developers;

    private Taxable taxable;
    public DeveloperController(Taxable taxable){
        this.taxable = taxable;
    }

    @PostConstruct
    public void init(){
        this.developers = new HashMap<>();
        this.developers.put(1,new Developer(1,"Oğuz", 60000, Experience.MID));
    }

    @GetMapping("/developers")
    public List<Developer> getDevelopers(){
        return developers.values().stream().toList();
    }

    @GetMapping("/developers/{id}")
    public Developer getDeveloper(@PathVariable int id){
        return developers.get(id);
    }

    @PostMapping("/developers")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDeveloper(@RequestBody Developer developer){
        double taxRate = 0;
        switch (developer.getExperience()){
            case JUNIOR:
                taxRate = taxable.getSimpleTaxRate();
                break;
            case MID:
                taxRate = taxable.getMiddleTaxRate();
                break;
            case SENIOR:
                taxRate = taxable.getUpperTaxRate();
                break;
        }

        developer.setSalary(developer.getSalary() - developer.getSalary() * taxRate);
        developers.put(developer.getId(), developer);
    }

    @PutMapping("/developers/{id}")
    public void updateDeveloper(@PathVariable int id, @RequestBody Developer developer){
        developers.put(id, developer);
    }

    @DeleteMapping("/developers/{id}")
    public void deleteDeveloper(@PathVariable int id){
        developers.remove(id);
    }


}
