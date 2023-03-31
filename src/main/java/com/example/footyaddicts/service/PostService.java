package com.example.footyaddicts.service;

import com.example.footyaddicts.controller.PostDto;
import com.example.footyaddicts.models.ERole;
import com.example.footyaddicts.models.Post;
import com.example.footyaddicts.models.Staff;
import com.example.footyaddicts.repos.PostRepository;
import com.example.footyaddicts.repos.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.View;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SecurityContext securityContext;

    public Post createPost(PostDto postDTO) throws IOException {

//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String name = userDetails.getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getName();

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());

        post.setCoverImage(postDTO.getCoverImage());
        post.setWrittenBy(username);
        post.setCreatedAt(LocalDateTime.now());
        post.setModifiedAt(LocalDateTime.now());
        return postRepository.save(post);
    }


    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public void incrementViews(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }
    public Map<String, Long> getViewsAndPostCount() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        String username = userDetails.getUsername();
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = userDetails.getName();
        String writtenBy =name;
        Long totalPosts = postRepository.countByWrittenBy(writtenBy);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        List<Long> viewsThisWeek = postRepository.findViewsByWrittenByAndCreatedAtBetween(writtenBy, oneWeekAgo, now);
        Long totalViewsThisWeek = viewsThisWeek.stream().mapToLong(Long::longValue).sum();

        List<Long> views = postRepository.findViewsByWrittenBy(writtenBy);
        Long totalViews = views.stream().mapToLong(Long::longValue).sum();
        Map<String, Long> result = new HashMap<>();
        result.put("totalViews", totalViews);
        result.put("totalPosts", totalPosts);
        result.put("totalViewsThisWeek", totalViewsThisWeek);
        return result;
    }

    public List<Post> findTopViewedByWrittenBy() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String name = userDetails.getName();
        String name = getLoggedInUsername();
        return postRepository.findTopViewedByWrittenBy(name);
    }


private String getLoggedInUsername() {
    Authentication authentication = securityContext.getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return userDetails.getUsername();
}


    public Map<String, Long> getModeratorStats(ERole role) {
        Long totalPosts = postRepository.findSumOfPosts();
        Long totalViews = postRepository.findSumOfViews();
        Long totalWriters = staffRepository.findSumOfStaffByRole(role);
        Map<String, Long> result = new HashMap<>();
        result.put("totalPosts", totalPosts);
        result.put("totalViews", totalViews);
        result.put("totalWriters", totalWriters);

        return result;
    }


}


