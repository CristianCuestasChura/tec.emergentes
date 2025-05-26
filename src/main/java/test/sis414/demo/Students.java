package test.sis414.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class Students {

    @GetMapping("/{id}")
    public String getStudent(String id)
    {
        return "Juanito";
    }

    @GetMapping
    public String getStudents()
    {
        return "List Students";
    }

    @PostMapping
    public String SetStudent()
    {
        return "Create Student";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(String id)
    {
        return "Delete Student";
    }

    @PutMapping("/{id}")
    public String updateStudent(String id)
    {
        return "Update Student";
    }

    @PatchMapping("/{id}")
    public String updatePartialStudent(String id)
    {
        return "Update Partial Student";
    }
}
