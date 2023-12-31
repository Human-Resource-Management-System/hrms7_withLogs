package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import DAO_Interfaces.EmployeeDAO;
import DAO_Interfaces.EmployeeLeaveRequestDAO;
import models.ApprovedLeaveModel;
import models.Employee;
import models.EmployeeLeaveInputModel;
import models.EmployeeLeaveModel;
import models.EmployeeLeaveRequest;
import models.EmployeeLeaveRequestId;
import models.JobGradeWiseLeaves;
import models.LeaveValidationModel;
import models.input.output.JobGradeLeavesOutModel;
import service.EmployeeLeaveService;
import service_interfaces.EmployeeLeaveServiceInterface;

@Controller
public class LeaveController {

	@Autowired
	public LeaveController(EmployeeLeaveRequest leaveRequest, EmployeeLeaveRequestId leaveRequestId, Gson gson,
			EmployeeLeaveService employeeService) {
		this.leaveRequest = leaveRequest;
		this.leaveRequestId = leaveRequestId;
		this.gson = gson;
		this.employeeService = employeeService;
	}

	private EmployeeLeaveRequest leaveRequest;
	private EmployeeLeaveRequestId leaveRequestId;
	private Gson gson;
	private EmployeeLeaveServiceInterface employeeService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EmployeeLeaveRequestDAO leaveRequestDAO;

	@Autowired
	private EmployeeDAO employeeDAO;

	@Autowired
	private ApplicationContext context;

	// to get the leave form
	@RequestMapping(value = "/leaveform", method = RequestMethod.GET)
	public String leaverequest(HttpSession session, Model model) {

		try {

			int id = (int) session.getAttribute("employeeId");
			// Retrieves the employee ID from the session

			Employee employee = employeeDAO.getEmployee(id);
			// Retrieves the employee information from the DAO using the employee ID

			JobGradeWiseLeaves leavesProvidedStatistics = leaveRequestDAO
					.getJobGradeWiseLeaves(employee.getEmplJbgrId().trim());
			// Retrieves the leaves provided statistics based on the job grade ID of the
			// employee from the DAO

			List<EmployeeLeaveRequest> leaves = leaveRequestDAO
					.getApprovedAndPendingEmployeeAndLeaveRequestData(employee.getEmplId(), Year.now().getValue());
			// Retrieves the approved and pending leave request data for the employee ID and
			// the current year from the DAO

			LeaveValidationModel validation = employeeService.calculateLeavesTaken(leaves, leavesProvidedStatistics);
			// Calculates the leaves taken based on the retrieved leave request data and
			// leaves provided statistics using
			// the service method

			model.addAttribute("validationData", validation);
			// Adds the validation data as an attribute named "validationData" to the model

			return "leaveform";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// to submit leave
	@Transactional
	@RequestMapping(value = "/submitleave", method = RequestMethod.POST)
	public ResponseEntity<String> submitLeaveRequest(@ModelAttribute EmployeeLeaveInputModel employeeLeaveInputModel) {

		try {

			// Set the leave request properties from the input model
			leaveRequest.setLeaveStartDate(
					LocalDate.parse(employeeLeaveInputModel.getLeaveStartDate(), DateTimeFormatter.ISO_DATE));
			leaveRequest.setLeaveEndDate(
					LocalDate.parse(employeeLeaveInputModel.getLeaveEndDate(), DateTimeFormatter.ISO_DATE));
			leaveRequest.setLeaveType(employeeLeaveInputModel.getLeaveType());
			leaveRequest.setReason(employeeLeaveInputModel.getReason());
			leaveRequest.setRequestDateTime(LocalDateTime.now());
			leaveRequestId.setEmployeeId(employeeLeaveInputModel.getEmployeeId());

			// Generate the next leave request index
			int nextLeaveRequestIndex = leaveRequestDAO
					.getNextLeaveRequestIndex(employeeLeaveInputModel.getEmployeeId());
			leaveRequestId.setLeaveRequestIndex(nextLeaveRequestIndex);

			// Set the leave request ID and save the leave request
			leaveRequest.setLeaveRequestId(leaveRequestId);
			leaveRequestDAO.saveEmployeeLeaveRequest(leaveRequest);

		} catch (Exception e) {
			System.out.println(e);
			String errorMessage = "Internal Server Error";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok("Success");
	}

	// to get leave requests at admin side
	@RequestMapping(value = "/leaveRequests", method = RequestMethod.GET)
	public String leaveRequests(HttpSession session, Model model) {

		try {
			// Get the admin ID from the session
			int id = (int) session.getAttribute("adminId");

			// Retrieve employees for the HR and manager ID
			List<Employee> employees = employeeDAO.getEmployeesByHRAndManager(id);

			// Create a list to hold the output models
			List<EmployeeLeaveModel> outputmodel = new ArrayList<>();

			// Iterate over each employee
			for (Employee employee : employees) {
				// Retrieve leave requests for the employee
				List<EmployeeLeaveRequest> leaves = leaveRequestDAO
						.getEmployeeAndLeaveRequestData(employee.getEmplId());
				// Iterate over each leave request
				for (EmployeeLeaveRequest leave : leaves) {
					// Create a new leave model
					EmployeeLeaveModel leavemodel = context.getBean(EmployeeLeaveModel.class);
					// Set the properties of the leave model
					leavemodel.setEmpId(employee.getEmplId());
					leavemodel.setName(employee.getEmplFirstname() + employee.getEmplLastname());
					leavemodel.setLeaveRequestIndex(leave.getLeaveRequestId().getLeaveRequestIndex());
					leavemodel.setLeaveType(leave.getLeaveType());
					leavemodel.setLeaveStartDate(leave.getLeaveStartDate());
					leavemodel.setLeaveEndDate(leave.getLeaveEndDate());
					leavemodel.setReason(leave.getReason());
					// Add the leave model to the output list
					outputmodel.add(leavemodel);
				}

			}
			 // Add the output model list to the model
			model.addAttribute("data", outputmodel);

			// Return the view name for rendering
			return "AdminLeaveRequests";

		} catch (Exception e) {
			// Handle any exceptions that may occur
			e.printStackTrace();
			return "error";
		}

	}

	// to reject leave
	@RequestMapping(value = "/rejectLeave", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<String> rejectLreave(@ModelAttribute EmployeeLeaveModel employeeLeaveModel) {
		try {
			
			// set the employee ID and leave request index to leave request id from the model
			leaveRequestId.setEmployeeId(employeeLeaveModel.getEmpId());
			leaveRequestId.setLeaveRequestIndex(employeeLeaveModel.getLeaveRequestIndex());

			// Retrieve the employee leave request using the leave request ID
			EmployeeLeaveRequest employeeLeaveRequest = leaveRequestDAO.getEmployeeLeaveRequest(leaveRequestId);

			if (employeeLeaveRequest != null) {
				 // Set the approvedBy field to -1 to indicate rejection
				employeeLeaveRequest.setApprovedBy(-1);
			}

		} catch (Exception e) {
			System.out.println(e);
			String errorMessage = "Internal Server Error";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return response
		return ResponseEntity.ok("successfully status updated");
	}

	// to accept leave
	@RequestMapping(value = "/acceptLeave", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<String> acceptLeave(@ModelAttribute EmployeeLeaveInputModel employeeLeaveInputModel,
			HttpSession session) {
		try {
			// set the employee ID and leave request index to leave request id from the model
			leaveRequestId.setEmployeeId(employeeLeaveInputModel.getEmployeeId());
			leaveRequestId.setLeaveRequestIndex(employeeLeaveInputModel.getLeaveRequestIndex());

			// Retrieve the employee leave request using the leave request ID
			EmployeeLeaveRequest employeeLeaveRequest = leaveRequestDAO.getEmployeeLeaveRequest(leaveRequestId);

			int adminId = (int) session.getAttribute("adminId");

			if (employeeLeaveRequest != null) {
				 // Set the approvedBy field to the admin ID
				employeeLeaveRequest.setApprovedBy(adminId);
				 // Set the approved leave start date, end date, and remarks from the input model
				employeeLeaveRequest.setApprovedLeaveEndDate(
						LocalDate.parse(employeeLeaveInputModel.getLeaveEndDate(), DateTimeFormatter.ISO_DATE));
				employeeLeaveRequest.setApprovedLeaveStartDate(
						LocalDate.parse(employeeLeaveInputModel.getLeaveStartDate(), DateTimeFormatter.ISO_DATE));
				employeeLeaveRequest.setApprovedRemarks(employeeLeaveInputModel.getRemarks());
			}

		} catch (Exception e) {
			// Handle any exceptions that may occur
		    e.printStackTrace();
			String errorMessage = "Internal Server Error";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// Return a success response
		return ResponseEntity.ok("successfully status updated");
	}

	// to get admin approved leaves
	@RequestMapping(value = "/AdminapprovedLeaves", method = RequestMethod.GET)
	public String adminApprovedLeaves(Model model, HttpSession session) {

		try {
		int id = (int) session.getAttribute("adminId");
		// Create a list to store the approved leave models
		List<ApprovedLeaveModel> approvedOutModel = new ArrayList<>();
        // Retrieve the approved employee and leave request data based on the admin ID
		List<EmployeeLeaveRequest> approvedLeaves = leaveRequestDAO.getApprovedEmployeeAndLeaveRequestData(id);
		for (EmployeeLeaveRequest leave : approvedLeaves) {
			 // Retrieve the employee information for each approved leave
			Employee employee = employeeDAO.getEmployee(leave.getLeaveRequestId().getEmployeeId());
			// Create a new approved leave model
			ApprovedLeaveModel approvedLeaveModel = context.getBean(ApprovedLeaveModel.class);
			approvedLeaveModel.setEmployeeId(employee.getEmplId());
			approvedLeaveModel.setEmployeeName(employee.getEmplFirstname() + " " + employee.getEmplLastname());
			approvedLeaveModel.setApprovedStartDate(leave.getApprovedLeaveStartDate());
			approvedLeaveModel.setApprovedEndDate(leave.getApprovedLeaveEndDate());
			// Add the approved leave model to the list
			approvedOutModel.add(approvedLeaveModel);
		}
		// Add the approved leaves to the model attribute
		model.addAttribute("approvedLeaves", approvedOutModel);
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		 // Return the view for displaying the admin-approved leaves
		return "AdminApprovedLeaves";
	}

	// to get employee leave history
	@RequestMapping(value = "/geEmployeeLeaves", method = RequestMethod.GET)
	public String getEmployeeLeavesHistory(Model model, HttpSession session) {

		try {
		int id = (int) session.getAttribute("employeeId");
		// Create a list to store the employee leave models
		List<EmployeeLeaveModel> history = new ArrayList<>();
		// Retrieve the leave request history for the employee
		List<EmployeeLeaveRequest> employeeLeavesData = leaveRequestDAO.getLeaveRequestHistory(id);
		for (EmployeeLeaveRequest leave : employeeLeavesData) {
			 // Create a new employee leave model
			EmployeeLeaveModel leavemodel = context.getBean(EmployeeLeaveModel.class);
			leavemodel.setLeaveRequestIndex(leave.getLeaveRequestId().getLeaveRequestIndex());
			leavemodel.setLeaveRequestDate(leave.getRequestDateTime());
			leavemodel.setLeaveStartDate(leave.getApprovedLeaveStartDate());
			leavemodel.setLeaveEndDate(leave.getApprovedLeaveEndDate());
			leavemodel.setLeaveType(leave.getLeaveType());
			leavemodel.setReason(leave.getReason());
			leavemodel.setStatus(leave.getApprovedBy());
			// Add the employee leave model to the list
			history.add(leavemodel);

		}
		// Add the leave history to the model attribute
		model.addAttribute("leavehistory", history);
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		// Return the view for displaying the employee's leave history
		return "employeeLeaveHistory";
	}

	// get jobgradewise leaves
	@RequestMapping(value = "/getJobGradeWiseLeaves", method = RequestMethod.GET)
	public String getJobGradeWiseLeaves(Model model) {
		try {
	        // Retrieve the job grade-wise leaves from the DAO
		List<JobGradeWiseLeaves> jobGradeLeaves = leaveRequestDAO.getJobGradeWiseLeaves();
        // Create a list to store the job grade leaves output models
		List<JobGradeLeavesOutModel> result = new ArrayList<>();
		for (JobGradeWiseLeaves leaves : jobGradeLeaves) {
            // Create a new job grade leaves output model
			JobGradeLeavesOutModel leavedata = context.getBean(JobGradeLeavesOutModel.class);
			leavedata.setJobGradeId(leaves.getJbgrId());
			leavedata.setTotalLeaves(leaves.getTotalLeavesPerYear());
			leavedata.setSickLeaves(leaves.getSickLeavesPerYear());
			leavedata.setCasualLeaves(leaves.getCasualLeavesPerYear());
			leavedata.setOtherLeaves(leaves.getOtherLeavesPerYear());
            // Add the job grade leaves output model to the list
			result.add(leavedata);
		}
        // Add the job grade leaves to the model attribute
		model.addAttribute("jobgradeleaves", result);
		
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
	    // Return the view for displaying the job grade-wise leaves
		return "jobGradeWiseLeaves";
	}

	// get leave statistics for dashboard
	@RequestMapping(value = "/getLeaveStatistics", method = RequestMethod.GET)
	public ResponseEntity<String> getLeaveStatistics(HttpSession session) {
		try {
		int id = (int) session.getAttribute("employeeId");
        // Retrieve the employee information from the DAO using the employee ID
		Employee employee = employeeDAO.getEmployee(id);

        // Retrieve the leaves provided statistics based on the job grade ID of the employee from the DAO
		JobGradeWiseLeaves leavesProvidedStatistics = leaveRequestDAO
				.getJobGradeWiseLeaves(employee.getEmplJbgrId().trim());

        // Retrieve the approved leave requests for the employee ID and the current year from the DAO
		List<EmployeeLeaveRequest> leaves = leaveRequestDAO.getApprovedLeaveRequests(employee.getEmplId(),
				Year.now().getValue());
        // Calculate the leaves taken based on the retrieved leave request data and leaves provided statistics using the service method
		LeaveValidationModel validation = employeeService.calculateLeavesTaken(leaves, leavesProvidedStatistics);
		return ResponseEntity.ok(gson.toJson(validation));
		}catch(Exception e) {
			e.printStackTrace();
			String errorMessage = "Internal Server Error";
	        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}