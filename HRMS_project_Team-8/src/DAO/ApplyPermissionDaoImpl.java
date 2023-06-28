package DAO;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import DAO_Interfaces.ApplyPermissionDao;
import models.ApplyPermissions;
import models.Employee;

@Component
@Transactional
public class ApplyPermissionDaoImpl implements ApplyPermissionDao {
	private final Logger logger = LoggerFactory.getLogger(ApplyPermissionDaoImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(ApplyPermissions apppermission) {
		logger.info("an employee applied for permission");
		em.persist(apppermission);
	}

	@Override
	public int getNextPermissionIndex(int employeeId) {
		logger.info("Executing getNextPermissionIndex method.");

		String queryString = "SELECT COALESCE(MAX(pr.id.ep_index), 0) + CASE WHEN COUNT(pr) > 0 THEN 1 ELSE 0 END "
				+ "FROM ApplyPermissions pr WHERE pr.id.empl_id = :empl_id";
		Query query = em.createQuery(queryString);
		query.setParameter("empl_id", employeeId);
		return (Integer) query.getSingleResult();
	}

	@Override
	public ApplyPermissions getPermissionByIdAndIndex(int employeeId, int index) {
		logger.info("Executing getPermissionByIdAndIndex method");

		String queryString = "SELECT ap FROM ApplyPermissions ap WHERE ap.id.empl_id = :empl_id AND ap.id.ep_index = :ep_index";
		TypedQuery<ApplyPermissions> query = em.createQuery(queryString, ApplyPermissions.class);
		query.setParameter("empl_id", employeeId);
		query.setParameter("ep_index", index);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null; // Return null when no permission is found
		}
	}

	@Override
	public List<Employee> getEmployeesByHRAndManager(int employeeId) {
		logger.info("Executing getEmployeesByHRAndManager method");

		String query = "SELECT e FROM Employee e "
				+ "WHERE e.emplHrEmplId = :employeeId OR e.emplRmanagerEmplId = :employeeId";

		return em.createQuery(query, Employee.class).setParameter("employeeId", employeeId).getResultList();
	}

	@Override
	public ApplyPermissions getEmployeeAndPermissionRequestData(int id, Date current) {
		try {
			logger.info("Executing getEmployeeAndPermissionRequestData method if any permission requests are there");

			String jpqlQuery = "SELECT elrq FROM ApplyPermissions elrq " + "WHERE elrq.id.empl_id = :employeeIds "
					+ "AND elrq.eprq_approvedby is null" + " AND elrq.current_date = :currentdate ";
			TypedQuery<ApplyPermissions> query = em.createQuery(jpqlQuery, ApplyPermissions.class);
			query.setParameter("employeeIds", id);
			query.setParameter("currentdate", current);
			ApplyPermissions result = query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			logger.info("Executing getEmployeeAndPermissionRequestData method if there are no permissions");

			// Handle the case when no entity is found
			return null; // or throw a custom exception, log a message, etc.
		}
	}

	@Override
	public Long getEmployeeAndPermissionRequestDataCountPerDay(int id, Date current) {

		logger.info("Executing getEmployeeAndPermissionRequestDataCountPerDay method");

		String jpqlQuery = "SELECT COUNT(elrq) FROM ApplyPermissions elrq " + "WHERE elrq.id.empl_id = :employeeIds "
				+ "AND elrq.current_date = :currentdate";
		TypedQuery<Long> query = em.createQuery(jpqlQuery, Long.class);
		query.setParameter("employeeIds", id);
		query.setParameter("currentdate", current);
		Long count = query.getSingleResult();

		return count;
	}

	@Override
	public long getEmployeeAndPermissionRequestDataCountPerMonth(int id, int month, int year) {
		logger.info("Executing getEmployeeAndPermissionRequestDataCountPerMonth method");
		String jpqlQuery = "SELECT COUNT(elrq) FROM ApplyPermissions elrq " + "WHERE elrq.id.empl_id = :employeeIds "
				+ "AND EXTRACT(MONTH FROM elrq.current_date) = :month "
				+ "AND EXTRACT(YEAR FROM elrq.current_date) = :year";
		TypedQuery<Long> query = em.createQuery(jpqlQuery, Long.class);
		query.setParameter("employeeIds", id);
		query.setParameter("month", month);
		query.setParameter("year", year);
		Long count = query.getSingleResult();
		return count;
	}

	@Override
	public long getEmployeeApprovedPermissionsCount(int id, int year) {
		logger.info("Executing getEmployeeApprovedPermissionsCount method");
		String jpqlQuery = "SELECT COUNT(elrq) FROM ApplyPermissions elrq " + "WHERE elrq.id.empl_id = :employeeIds "
				+ "AND EXTRACT(YEAR FROM elrq.current_date) = :year " + "AND elrq.eprq_status = 'accept' ";
		TypedQuery<Long> query = em.createQuery(jpqlQuery, Long.class);
		query.setParameter("employeeIds", id);
		query.setParameter("year", year);
		Long count = query.getSingleResult();
		return count;
	}

}