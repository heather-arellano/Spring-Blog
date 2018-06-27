package com.codeup.blog.controllers;

import com.codeup.blog.models.Post;
import com.codeup.blog.models.User;
import com.codeup.blog.services.PostSvc;
import com.codeup.blog.services.UserSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class PostController {

    private final UserSvc usrSvc;
    private final PostSvc pstSvc;

    @Autowired
    public PostController(PostSvc pstSvc, UserSvc usrSvc) {
        this.pstSvc = pstSvc;
        this.usrSvc = usrSvc;
    }

    @GetMapping("/posts")
    public String posts( Model model) {
//        retrieves all posts
        Iterable<Post> all = pstSvc.findAll();
        model.addAttribute("Posts", all);
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String postId(@PathVariable int id, Model model) {
//        retrieves one single post
        Post onePost = pstSvc.onePost(id);
        model.addAttribute("post", onePost);
        return "posts/show";
    }

    @GetMapping("/posts/create")
    public String createGet( Model view) {
//        redirect to create form
        view.addAttribute("post", new Post());
        return "/posts/create";
    }

    @PostMapping("/posts/create")
    public String createPost(@Valid Post post, Errors validation, Model viewModel) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(validation.hasErrors()) {
            viewModel.addAttribute("errors", validation);
            viewModel.addAttribute("post", post);
            return "posts/create";
        }

        post.setUser(loggedInUser);
        pstSvc.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String mapCreate(Model model, @PathVariable long id) {
        model.addAttribute("post", pstSvc.onePost(id));
        return "posts/edit";
    }

}
