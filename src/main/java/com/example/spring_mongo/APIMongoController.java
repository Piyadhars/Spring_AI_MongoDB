package com.example.spring_mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class APIMongoController {
    @Autowired
    EmployeeRepository repo;

    @PostMapping("/api/employee")
    public ResponseEntity<Object> addEmployee(@RequestBody Employee emp) {
        if (emp.id != null && emp.name != null && emp.getAge()!= null) {
            List<Employee> result = new ArrayList<>();
            result = repo.findAll();
            List<String> idlist = new ArrayList<>();
            for (Employee e : result) {
                idlist.add(e.id);
            }
            if (idlist.contains(emp.id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Id is already exists");
            } else {
                repo.save(new Employee(emp.id, emp.name, emp.age));
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("New Employee is added");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enter all details");
        }

    }

    @GetMapping("/api/employee")
    public ResponseEntity<List<Employee>> showdetail() {
        return ResponseEntity.status(HttpStatus.OK).body(repo.findAll());
    }

    @PutMapping("/api/employee/{id}")
    public ResponseEntity<String> updatedetail(@PathVariable String id, @RequestBody Employee emp) {
        List<Employee> details = new ArrayList<>();
        details = repo.findAll();
        List<String> idlist = new ArrayList<>();
        for (Employee e : details) {
            idlist.add(e.id);
        }
        if (idlist.contains(id)) {
            Optional<Employee> result = repo.findById(id);
            String tell=result.get().age;
            if((emp.age != null ) && (!tell.equals(emp.age))&&(emp.name != null)&&(!result.get().name.equals(emp.name))) {
                result.get().age = emp.age;
                result.get().name=emp.name;
                repo.save(result.get());
                return ResponseEntity.status(HttpStatus.OK).body("Updated");
            }
            if((emp.age != null ) && (!tell.equals(emp.age))) {
                result.get().age = emp.age;
                repo.save(result.get());
                return ResponseEntity.status(HttpStatus.OK).body("Updated the age");
            }
            if ((emp.name != null)&&(!result.get().name.equals(emp.name))){
            result.get().name = emp.name;
            repo.save(result.get());
            return ResponseEntity.status(HttpStatus.OK).body("Updated the name");
            }
            else{
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Already up to date");
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Id Not Found");
    }
    @DeleteMapping("/api/employee")
    public ResponseEntity<String> del(@RequestBody Employee emp){
        List<Employee> result = new ArrayList<>();
        result = repo.findAll();
        List<String> idlist = new ArrayList<>();
        for (Employee e : result) {
            idlist.add(e.id);
        }
        if (idlist.contains(emp.id)){
            repo.deleteById(emp.id);
           return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted the employee details");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Does not exist");
    }
}
