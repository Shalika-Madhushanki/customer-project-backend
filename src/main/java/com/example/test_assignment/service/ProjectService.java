package com.example.test_assignment.service;

import com.example.test_assignment.dao.ProjectRequest;
import com.example.test_assignment.model.Customer;
import com.example.test_assignment.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.test_assignment.repository.ProjectRepository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    ProjectRepository projectRepository;
    CustomerService customerService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, CustomerService customerService) {
        this.projectRepository = projectRepository;
        this.customerService = customerService;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(ProjectRequest projectRequest) {
        Optional<Customer> customer = customerService.getCustomerById(projectRequest.getCustomerId());
        if (customer.isPresent()) {
            Project project = new Project(projectRequest.getName(), projectRequest.getDescription());
            project.setCustomer(customer.get());
            return projectRepository.save(project);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer with ID " + projectRequest.getCustomerId() + " not found");
        }
    }

    public Project updateProject(ProjectRequest projectRequest, Long id) {
        Project existingProject = projectRepository.findById(id).orElseThrow();
        existingProject.setName(projectRequest.getName());
        existingProject.setDescription(projectRequest.getDescription());
        if (existingProject.getCustomer().getId() != projectRequest.getCustomerId()) {
            Optional<Customer> customer = customerService.getCustomerById(projectRequest.getCustomerId());
            existingProject.setCustomer(customer.get());
        }
        return projectRepository.save(existingProject);
    }

    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    public List<Project> getProjectsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return projectRepository.findAllByCreationDateBetween(startDate, endDate);
    }
}
