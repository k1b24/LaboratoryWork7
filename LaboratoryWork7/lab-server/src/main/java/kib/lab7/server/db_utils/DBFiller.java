package kib.lab7.server.db_utils;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.entities.HumanValidator;
import kib.lab7.common.entities.enums.Mood;
import kib.lab7.common.entities.enums.WeaponType;
import kib.lab7.server.utils.Config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBFiller {

    private List<HumanBeing> createHumanBeingListFromResultSet(ResultSet resultSet) throws SQLException {
        HumanValidator validator = new HumanValidator(Config.getTextSender());
        List<HumanBeing> resultList = new ArrayList<>();
        while (resultSet.next()) {
            HumanBeing newHuman = new HumanBeing();
            newHuman.setId(resultSet.getLong("id"));
            newHuman.setName(resultSet.getString("name"));
            newHuman.getCoordinates().setX(resultSet.getLong("x"));
            newHuman.getCoordinates().setY(resultSet.getFloat("y"));
            newHuman.setCreationDate(resultSet.getDate("creationDate").toLocalDate());
            newHuman.setRealHero(resultSet.getBoolean("realHero"));
            newHuman.setHasToothpick(resultSet.getBoolean("hasToothpick"));
            newHuman.setImpactSpeed(resultSet.getInt("impactSpeed"));
            if (resultSet.getString("weaponType") == null) {
                newHuman.setWeaponType(null);
            } else {
                newHuman.setWeaponType(WeaponType.valueOf(resultSet.getString("weaponType")));
            }
            if (resultSet.getString("mood") == null) {
                newHuman.setMood(null);
            } else {
                newHuman.setMood(Mood.valueOf(resultSet.getString("mood")));
            }
            newHuman.setAuthor(resultSet.getString("author"));
            if (resultSet.getString("carCoolness") == null && resultSet.getString("carSpeed") == null) {
                newHuman.setCar(null);
            } else {
                newHuman.getCar().setCarCoolness(resultSet.getBoolean("carCoolness"));
                newHuman.getCar().setCarSpeed(resultSet.getInt("carSpeed"));
            }
            if (validator.validateHuman(newHuman)) {
                resultList.add(newHuman);
            }
        }
        return resultList;
    }

    public List<HumanBeing> getArrayListOfHumanBeings() throws SQLException {
        DBConnector dbConnector = new DBConnector();
        String query = "select * from s335106humanbeings";
        PreparedStatement statement = dbConnector.getConnection().prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        return createHumanBeingListFromResultSet(rs);
    }
}
