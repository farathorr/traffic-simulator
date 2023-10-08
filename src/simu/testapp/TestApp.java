package simu.testapp;

import simu.dao.Level_variableDao;
import simu.dao.ResultsDao;
import simu.entity.Level_variables;
import simu.entity.Results;

public class TestApp {
    public static void main(String[] args) {

        Level_variableDao level_variableDao = new Level_variableDao();
        ResultsDao resultsDao = new ResultsDao();


        //Results result1 = new Results(5, 0.5, 0.5, "Level2");
        //Results result2 = new Results(5, 0.5, 0.5, "Level2");
        //resultsDao.persist(result1);
        //resultsDao.persist(result2);

        //Level_variables levelVariable1 = new Level_variables(1, "A", 0.5, 0.5);
        //Level_variables levelVariable2 = new Level_variables(2, "B", 0.5, 0.5);
        //level_variableDao.persist(levelVariable1);
        //level_variableDao.persist(levelVariable2);

        Level_variables levelVariables = level_variableDao.find(1);
        Results results = resultsDao.find(1);
        //System.out.println(levelVariables.getLevelId() + " " + levelVariables.getServicePointName() + " " + levelVariables.getEventInterval() + " " + levelVariables.getLeadTime());
        //System.out.println(results.getId() + " " + results.getCarCount() + " " + results.getAverageTime() + " " + results.getSimulationTime() + " " + results.getSimulationLevel());
        


    }
}
