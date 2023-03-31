package com.example.footyaddicts.controller;


import com.example.footyaddicts.dto.response.MessageResponse;
import com.example.footyaddicts.models.ERole;
import com.example.footyaddicts.models.Post;
import com.example.footyaddicts.repos.PostRepository;
import com.example.footyaddicts.service.PostService;
import com.example.footyaddicts.service.StaffService;
import com.example.footyaddicts.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


import java.io.File;
import java.io.IOException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@AllArgsConstructor
public class TestController {



    @Autowired
    private PostService postService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private MultipartResolver ipartResolver;

@Autowired
private PostRepository postRepo;
    @PostMapping( value = "/create-post" ,  consumes = { "multipart/form-data" })

    public ResponseEntity<Post> createPost(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("coverImage") MultipartFile file) throws IOException {
        if (postRepo.existsByTitle(title)) {
          ResponseEntity.badRequest().body( new MessageResponse("A post with this title already exists"));
        }
        // handle the file upload
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String filePath = "C:\\Users\\Patty\\Downloads\\footyaddicts\\footyaddicts\\src\\main\\resources\\static\\images\\" + fileName;
        file.transferTo(new File(filePath));

        // create the postDTO object
        PostDto postDTO = new PostDto();
        postDTO.setTitle(title);
        postDTO.setContent(content);
        postDTO.setCoverImage(fileName);

        Post post = postService.createPost(postDTO);
        return ResponseEntity.ok().body(post);
    }

@GetMapping("/posts/{id}")
public ResponseEntity<Post> viewPost(@PathVariable Long id) {
    postService.incrementViews(id);
    Post post = postRepo.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    return ResponseEntity.ok(post);
}
    @GetMapping("/latest")
    public ResponseEntity<List<Post>> getLatestPosts() {
        List<Post> latestPosts = postRepo.findTop12ByOrderByCreatedAtDesc();
        if (latestPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latestPosts);
    }

    @GetMapping("/posts/user/{writtenBy}")
    public ResponseEntity<List<Post>> getAllPostsByUser( @PathVariable String writtenBy  ) {
        List<Post> allPostsByUser = postRepo.findAllPostWrittenBy(writtenBy);
        if (allPostsByUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allPostsByUser);
    }

    @GetMapping("/statistics")
    public Map<String, Long> getViewsAndPostCount() {
        return postService.getViewsAndPostCount();
    }


    @GetMapping("/top-viewed")

  public List<Post> getTopViewedPosts(){
        return  postService.findTopViewedByWrittenBy();
    }

    @GetMapping("/post/views/trend")
    public List<Map<String, Object>> getViewsTrend() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = userDetails.getName();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        List<Map<String, Object>> views = new ArrayList<>();
        for(int i = 0; i <= 6; i++) {
            LocalDateTime date = now.minusDays(i);
            LocalDateTime startOfDay = date.with(LocalTime.MIN).withSecond(0).withNano(0);
         LocalDateTime   endOfDay = date.with(LocalTime.MAX).withSecond(59).withNano(999999999);
            Long viewsOnDate = postRepo.findViewsByWrittenByAndCreatedAtBetween2(name, startOfDay, endOfDay);
            Map<String, Object> view = new HashMap<>();
            view.put("date", date);
            view.put("views", viewsOnDate);
            views.add(view);
        }
        return views;
    }


    @GetMapping("/post-stats")

    public List<Map<String, Object>> getAllPostsCreatedLast7Days() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        List<Map<String, Object>> posts = new ArrayList<>();
        for(int i = 0; i <= 6; i++) {
            LocalDateTime date = now.minusDays(i);
            LocalDateTime startOfDay = date.with(LocalTime.MIN).withSecond(0).withNano(0);
            LocalDateTime   endOfDay = date.with(LocalTime.MAX).withSecond(59).withNano(999999999);
            Long postsOnDate = postRepo.findCountOfPostsByCreatedAtBetween( startOfDay, endOfDay);
            Long viewsOnDate = postRepo.findSumOfViewsByCreatedAtBetween( startOfDay, endOfDay);
            Map<String, Object> post = new HashMap<>();
            post.put("date", date);
            post.put("posts", postsOnDate);
            post.put("views", viewsOnDate);
            posts.add(post);
        }
        return posts;
    }


    @GetMapping("/moderator-stats")
    public Map<String, Long> getStatsOfAllPosts (){
        return  postService.getModeratorStats(ERole.ROLE_WRITER);

    }
//    @GetMapping("/writers/count")
//    public Long getTotalWriters() {
//        return staffService.findSumOfStaffByRole(ERole.ROLE_WRITER);
//    }






}
