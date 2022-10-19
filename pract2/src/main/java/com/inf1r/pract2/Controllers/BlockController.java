package com.inf1r.pract2.Controllers;

import com.inf1r.pract2.Models.Post;
import com.inf1r.pract2.Models.Prepod;
import com.inf1r.pract2.Models.Student;
import com.inf1r.pract2.repo.PostRepos;
import com.inf1r.pract2.repo.PrepodRepos;
import com.inf1r.pract2.repo.StudentRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BlockController {
    @Autowired
    private PostRepos postRepos;

    @Autowired
    private PrepodRepos prepodRepos;

    @Autowired
    private StudentRepos studentRepos;

    @GetMapping("/")
    public String Main(Model model){
        return "Home";
    }

    @GetMapping("/blog")
    public String GetBlog(Model model){
        Iterable<Post> posts = postRepos.findAll();
        model.addAttribute("posts", posts);
        return "block-main";

    }
    @GetMapping("/students")
    public String GetStudent(Model model){
        Iterable<Student> students = studentRepos.findAll();
        model.addAttribute("students",students);
        return "student-main";
    }
    @GetMapping("/prepods")
    public String GetPrepod(Model model){
        Iterable<Prepod> prepods = prepodRepos.findAll();
        model.addAttribute("prepods",prepods);
        return "prepod-main";
    }
    @GetMapping("/blog/add")
    public String blogAdd(Post post, Model model){
        return "block-add";
    }
    @GetMapping("/students/add")
    public String studentAdd(Student student, Model model){
        return "student-add";
    }
    @GetMapping("/prepods/add")
    public String prepodAdd(Prepod prepod, Model model){
        return "prepod-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@ModelAttribute("post")@Validated Post post, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "block-add";
        }
        postRepos.save(post);
        return "redirect:/blog";
    }
    @PostMapping("/students/add")
    public Object studentAdd(@ModelAttribute("student")@Validated Student student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "student-add";
        }
        studentRepos.save(student);
        return "redirect:/students";
    }
    @PostMapping("/prepods/add")
    public String prepodAdd(@ModelAttribute("prepod")@Validated Prepod prepod, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "prepod-add";
        }
        prepodRepos.save(prepod);
        return "redirect:/prepods";
    }
    @GetMapping("/blog/filter")
    public String blogFilter(Model model){
        return "block-filter";
    }
    @GetMapping("/students/filter")
    public String studentFilter(Model model){
        return "student-filter";
    }
    @GetMapping("/prepods/filter")
    public String prepodFilter(Model model){
        return "prepod-filter";
    }

    @PostMapping("/blog/filter/result")
    public String blogResult(@RequestParam String title, Model model){
        List<Post> result = postRepos.findByTitleContains(title);
        model.addAttribute("result", result);
        return "block-filter";
    }
    @PostMapping("/students/filter/result")
    public String studentResult(@RequestParam String surname, Model model){
        List<Student> result = studentRepos.findBySurnameContains(surname);
        model.addAttribute("result", result);
        return "student-filter";
    }
    @PostMapping("/prepods/filter/result")
    public String prepodResult(@RequestParam String surname, Model model){
        List<Prepod> result = prepodRepos.findBySurname(surname);
        model.addAttribute("result", result);
        return "prepod-filter";
    }
    
    @GetMapping("/prepods/{id}")
    public String prepodViewing(@PathVariable(value = "id") long id, Model model){
        Optional<Prepod> prepodOptional = prepodRepos.findById(id);
        ArrayList<Prepod> res = new ArrayList<>();
        prepodOptional.ifPresent(res::add);
        model.addAttribute("prepod",res);
        if(!prepodRepos.existsById(id)){
            return "redirect:/prepods";
        }
        return "prepod-viewing";
    }
    @GetMapping("/prepods/{id}/edit")
    public String prepodEdit(@PathVariable("id") long id, Model model){
        Prepod res = prepodRepos.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: "+id));
        model.addAttribute("prepod", res);
        return "prepod-edit";
    }
    @PostMapping("/prepods/{id}/edit")
    public String prepodUpdate(@PathVariable("id") long id, @ModelAttribute("prepod")@Validated Prepod prepod, BindingResult bindingResult){
        prepod.setId(id);
        if(bindingResult.hasErrors()){
            return "prepod-edit";
        }
        prepodRepos.save(prepod);
        return "redirect:/prepods";
    }

    @PostMapping("/prepods/{id}/remove")
    public String prepodDelete(@PathVariable("id")long id, Model model){
        Prepod prepod = prepodRepos.findById(id).orElseThrow();
        prepodRepos.delete(prepod);
        return "redirect:/prepods";
    }




    @GetMapping("/students/{id}")
    public String studentViewing(@PathVariable(value = "id") long id, Model model){
        Optional<Student> studentOptional = studentRepos.findById(id);
        ArrayList<Student> res = new ArrayList<>();
        studentOptional.ifPresent(res::add);
        model.addAttribute("student",res);
        if(!studentRepos.existsById(id)){
            return "redirect:/students";
        }
        return "student-viewing";
    }
    @GetMapping("/students/{id}/edit")
    public String studentEdit(@PathVariable("id") long id, Model model){
        Student res = studentRepos.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: "+id));
        model.addAttribute("student", res);
        return "student-edit";
    }
    @PostMapping("/students/{id}/edit")
    public String studentUpdate(@PathVariable("id") long id, @ModelAttribute("student") @Validated Student student, BindingResult bindingResult){
        student.setId(id);
        if(bindingResult.hasErrors()){
            return "student-edit";
        }
        studentRepos.save(student);
        return "redirect:/students";
    }

    @PostMapping("/students/{id}/remove")
    public String studentDelete(@PathVariable("id")long id, Model model){
        Student student = studentRepos.findById(id).orElseThrow();
        studentRepos.delete(student);
        return "redirect:/students";
    }

    @GetMapping("/blog/{id}")
    public String blogViewing(@PathVariable(value = "id") long id, Model model){
        Optional<Post> postOptional = postRepos.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        postOptional.ifPresent(res::add);
        model.addAttribute("post",res);
        if(!postRepos.existsById(id)){
            return "redirect:/blog";
        }
        return "block-viewing";
    }
    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable("id") long id, Model model){
        Post res = postRepos.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id: "+id));
        model.addAttribute("post", res);
        return "block-edit";
    }
    @PostMapping("/blog/{id}/edit")
    public String blogUpdate(@PathVariable("id") long id, @ModelAttribute("post")@Validated Post post, BindingResult bindingResult){
        post.setId(id);
        if(bindingResult.hasErrors()){
            return "block-edit";
        }
        postRepos.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogDelete(@PathVariable("id")long id, Model model){
        Post post = postRepos.findById(id).orElseThrow();
        postRepos.delete(post);
        return "redirect:/blog";
    }

}
