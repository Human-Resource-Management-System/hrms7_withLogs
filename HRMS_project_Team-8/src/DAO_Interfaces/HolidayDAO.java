package DAO_Interfaces;

import java.util.List;

import models.GradeHoliday;
import models.Holiday;

public interface HolidayDAO {

	/**
	 * Retrieves a list of all holidays.
	 *
	 * @return A list of all holidays.
	 */
	List<Holiday> findAllHolidays();

	/**
	 * Retrieves a list of all grade holidays.
	 *
	 * @return A list of all grade holidays.
	 */
	List<GradeHoliday> findAllGradeHolidays();
}
