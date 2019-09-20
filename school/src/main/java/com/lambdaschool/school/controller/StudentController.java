package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController
{

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    // Please note there is no way to add students to course yet!


    @ApiOperation(value = "returns all students", response = Student.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")

    })
    // localhost:2019/students/students/?page=0&size=3
    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents(HttpServletRequest request, @PageableDefault(size = 5)
            Pageable pageable)
    {
        logger.info(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed on " + LocalDate.now() + " at " + LocalTime.now() + ".");
        List<Student> myStudents = studentService.findAll(pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @ApiOperation(value = "retrieves a student based upon student id", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student Found", response = Student.class),
            @ApiResponse(code = 404, message = "Student Not Found", response = ErrorDetail.class)
    })
    @GetMapping(value = "/Student/{StudentId}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentById(
            @PathVariable
                    Long StudentId, HttpServletRequest request)
    {
        logger.info(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed on " + LocalDate.now() + " at " + LocalTime.now() + ".");
        Student r = studentService.findStudentById(StudentId);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @ApiOperation(value = "retrieves a student based upon similar name based upon query", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student Found", response = Student.class),
            @ApiResponse(code = 404, message = "Student Not Found", response = ErrorDetail.class)
    })
    @GetMapping(value = "/student/namelike/{name}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(
            @PathVariable
                    String name, HttpServletRequest request)
    {
        logger.info(request.getMethod().toUpperCase() + " " + request.getRequestURI() + " accessed on " + LocalDate.now() + " at " + LocalTime.now() + ".");
        List<Student> myStudents = studentService.findStudentByNameLike(name);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }


    @PostMapping(value = "/Student",
                 consumes = {"application/json"},
                 produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@Valid
                                           @RequestBody
                                                   Student newStudent) throws URISyntaxException
    {
        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @PutMapping(value = "/Student/{Studentid}")
    public ResponseEntity<?> updateStudent(
            @RequestBody
                    Student updateStudent,
            @PathVariable
                    long Studentid)
    {
        studentService.update(updateStudent, Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/Student/{Studentid}")
    public ResponseEntity<?> deleteStudentById(
            @PathVariable
                    long Studentid)
    {
        studentService.delete(Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
