package com.example.test_assignment.controller;

import com.example.test_assignment.dao.ProjectRequest;
import com.example.test_assignment.dao.ProjectsDataDownloadRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.example.test_assignment.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.test_assignment.service.ProjectService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private ProjectService projectService;
    private ObjectMapper objectMapper;

    @Autowired
    public ProjectController(ProjectService projectService, ObjectMapper objectMapper) {
        this.projectService = projectService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequest projectRequest) {
        log.info("Creating a new project with name: {}", projectRequest.getName());
        Project createdProject = projectService.createProject(projectRequest);
        log.info("Project created with ID: {}", createdProject.getId());
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        log.info("Fetching all projects");
        List<Project> projects = projectService.getAllProjects();
        log.info("Fetched {} projects", projects.size());
        return projects;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        log.info("Fetching project with ID: {}", id);
        Optional<Project> project = projectService.getProjectById(id);
        log.info("Project with ID: {} found", id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@RequestBody ProjectRequest projectRequest, @PathVariable Long id) {
        log.info("Updating project with ID: {}", id);
        try {
            Project updatedProject = projectService.updateProject(projectRequest, id);
            log.info("Project with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedProject);
        } catch (Exception e) {
            log.warn("Project with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (projectService.getProjectById(id).isPresent()) {
            projectService.deleteProjectById(id);
            log.info("Project with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Project with ID: {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/download")
    public ResponseEntity<Resource> downloadProjects(@RequestBody ProjectsDataDownloadRequest request) {
        log.info("Projects downloading for date range: {} to {}", request.getStartDate(), request.getEndDate());

        List<Project> projects = projectService.getProjectsByDateRange(request.getStartDate(), request.getEndDate());

        if (projects.isEmpty()) {
            log.warn("No projects found for the specified date range: {} to {}", request.getStartDate(), request.getEndDate());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ByteArrayResource("No projects found for the specified date range.".getBytes()));
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {

            for (Project project : projects) {
                String jsonContent = convertProjectToJson(project);
                log.info("jsonContent: {}", jsonContent);

                ZipEntry zipEntry = new ZipEntry("project_" + project.getId() + ".json");
                zipOut.putNextEntry(zipEntry);
                zipOut.write(jsonContent.getBytes());
                zipOut.closeEntry();
            }

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=projects.zip")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (IOException e) {
            log.error("Error occurred during ZIP file creation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource("Error occurred while creating the zip file.".getBytes()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource("Error occurred here.".getBytes()));
        }
    }

    private String convertProjectToJson(Project project) throws JsonProcessingException {
        return objectMapper.writeValueAsString(project);
    }
}
