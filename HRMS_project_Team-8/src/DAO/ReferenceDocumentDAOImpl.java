package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import DAO_Interfaces.ReferenceDocumentDAOInterface;
import models.EmployeeRefDocuments;

@Repository
public class ReferenceDocumentDAOImpl implements ReferenceDocumentDAOInterface {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public EmployeeRefDocuments findById(String id) {
		return entityManager.find(EmployeeRefDocuments.class, id);
	}

	@Override
	@Transactional
	public void save(EmployeeRefDocuments document) {
		entityManager.persist(document);
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		EmployeeRefDocuments document = entityManager.find(EmployeeRefDocuments.class, id);
		if (document != null) {
			entityManager.remove(document);
		}
	}

	@Override
	@Transactional
	public List<EmployeeRefDocuments> getAllDocs() {
		String query = "SELECT doc FROM EmployeeRefDocuments doc";
		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public int getIndex() {
		String query = "SELECT MAX(doc.id) FROM EmployeeRefDocuments doc";
		Integer maxId = entityManager.createQuery(query, Integer.class).getSingleResult();
		return maxId != null ? maxId + 1 : 1;
	}

}
