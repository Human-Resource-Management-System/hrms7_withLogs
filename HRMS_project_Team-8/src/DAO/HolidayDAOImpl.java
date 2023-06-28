package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import DAO_Interfaces.HolidayDAO;
import models.GradeHoliday;
import models.Holiday;

@Repository
public class HolidayDAOImpl implements HolidayDAO {

	private final Logger logger = LoggerFactory.getLogger(HolidayDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public List<Holiday> findAllHolidays() {

		logger.info("Request received for listing all the holidays ");

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Holiday> cq = cb.createQuery(Holiday.class);
		Root<Holiday> root = cq.from(Holiday.class);
		cq.select(root);
		cq.orderBy(cb.asc(root.get("hday_date")));
		return entityManager.createQuery(cq).getResultList();
	}

	@Transactional
	public List<GradeHoliday> findAllGradeHolidays() {
		logger.info("Request received for listing all the holidays grade wise");
		TypedQuery<GradeHoliday> query = entityManager.createQuery("SELECT gh FROM GradeHoliday gh",
				GradeHoliday.class);
		return query.getResultList();
	}

}
