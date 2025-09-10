package com.example.jsondatacreate.controllers;

import com.example.jsondatacreate.models.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@CrossOrigin(origins = "*")
public class StudentController {

	private final String FILE_NAME = "student.json";
	private final Gson gson = new Gson();
	
	@GetMapping("/students")
	public List<Student> getStudents() {
		List<Student> students = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
			String line;
			while ((line = br.readLine()) != null) {
				Student s = gson.fromJson(line, Student.class);
				students.add(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return students;
	}
	
//	 @GetMapping("/students/{id}")
//	    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
//	        List<Student> students = getStudents();
//	        return students.stream()
//	                       .filter(s -> s.getId() == id)
//	                       .findFirst()
//	                       .map(ResponseEntity::ok)
//	                       .orElse(ResponseEntity.notFound().build());
//	    }
	 
	@GetMapping("/students/{name}")
	public ResponseEntity<List<Student>> getStudentsByName(@PathVariable String name) {
	    List<Student> students = getStudents();

	    // Filter by case-insensitive and containing
	    List<Student> matchedStudents = students.stream()
	            .filter(s -> s.getName() != null && 
	                         s.getName().toLowerCase().contains(name.toLowerCase()))
	            .collect(Collectors.toList());

	    if (matchedStudents.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    } else {
	        return ResponseEntity.ok(matchedStudents);
	    }
	}

	 
	 
	 

//	@PostMapping("/students")
//	public String addStudent(@RequestBody Student student) {
//
//    	  // Get all existing students as a list
//        List<Student> students = getStudents();    	
//    	 // Generate new ID
//        int newId = students.stream()
//                            .mapToInt(Student::getId)
//                            .max()
//                            .orElse(100) + 1;
//
//        student.setId(newId);
//		
//
//		try (FileWriter writer = new FileWriter(FILE_NAME, true)) { // append mode
//			gson.toJson(student, writer);
//			writer.write("\n"); // separate multiple students
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "Error saving student!";
//		}
//		return "Student saved successfully!";
//	}
	
	
//	@PostMapping("/students")
//	public ResponseEntity<String> addStudent(@RequestBody Student student) {
//
//	    List<Student> students = getStudents();
//
//	    // Validation: Age must be >= 18
//	    if (student.getAge() < 18) {
//	        return ResponseEntity.badRequest().body("Error: Age must be 18 or above!");
//	    }
//
//	    // Validation: Email must be unique (case-insensitive)
//	    boolean emailExists = students.stream()
//	            .anyMatch(s -> s.getEmail() != null &&
//	                           s.getEmail().equalsIgnoreCase(student.getEmail()));
//	    if (emailExists) {
//	        return ResponseEntity.badRequest().body("Error: Email already exists!");
//	    }
//
//	    // Generate new ID
//	    int newId = students.stream()
//	                        .mapToInt(Student::getId)
//	                        .max()
//	                        .orElse(100) + 1;
//	    student.setId(newId);
//
//	    // Save new student
//	    try (FileWriter writer = new FileWriter(FILE_NAME, true)) { // append mode
//	        gson.toJson(student, writer);
//	        writer.write("\n");
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return ResponseEntity.internalServerError().body("Error saving student!");
//	    }
//
//	    return ResponseEntity.ok("Student saved successfully with ID: " + newId);
//	}
	
	
	@PostMapping("/students")
	public ResponseEntity<String> addStudent(@RequestBody Student student) {

	    List<Student> students = getStudents();

	    // Validation: Age must be >= 18
	    if (student.getAge() < 18) {
	        return ResponseEntity.badRequest().body("Error: Age must be 18 or above!");
	    }

	    // Validation: Email must be unique
	    boolean emailExists = students.stream()
	            .anyMatch(s -> s.getEmail() != null &&
	                           s.getEmail().equalsIgnoreCase(student.getEmail()));
	    if (emailExists) {
	        return ResponseEntity.badRequest().body("Error: Email already exists!");
	    }

	    // ✅ Validation: Batch must be one of the allowed values
	    List<String> allowedBatches = List.of("January", "February", "March", "April");
	    if (student.getBatch() == null || !allowedBatches.contains(student.getBatch())) {
	        return ResponseEntity.badRequest().body("Error: Batch must be January, February, March, or April!");
	    }

	    // Generate new ID
	    int newId = students.stream()
	                        .mapToInt(Student::getId)
	                        .max()
	                        .orElse(100) + 1;
	    student.setId(newId);

	    // Save new student
	    try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
	        gson.toJson(student, writer);
	        writer.write("\n");
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().body("Error saving student!");
	    }

	    return ResponseEntity.ok("Student saved successfully with ID: " + newId);
	}


	
	// PUT - update student by ID
    @PutMapping("/students/{id}")
    public String updateStudent(@PathVariable int id, @RequestBody Student newStudent) {
        List<Student> students = getStudents();
        boolean found = false;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                students.set(i, newStudent); // replace with updated student
                found = true;
                break;
            }
        }

        if (!found) return "Student not found!";
        writeAllStudents(students);
        return "Student updated successfully!";
    }

	// DELETE - delete student by ID
	@DeleteMapping("/students/{id}")
	public String deleteStudent(@PathVariable int id) {
		List<Student> students = getStudents();
		List<Student> updated = students.stream().filter(s -> s.getId() != id).collect(Collectors.toList());

		if (updated.size() == students.size()) {
			return "Student not found!";
		}

		writeAllStudents(updated);
		return "Student deleted successfully!";
	}
	
	// Helper method to overwrite all students in the JSON file
	private void writeAllStudents(List<Student> students) {
	    try (FileWriter writer = new FileWriter(FILE_NAME)) { // overwrite mode
	        for (Student s : students) {
	            gson.toJson(s, writer);
	            writer.write("\n"); // separate each student
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	 
	
}
