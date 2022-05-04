package kib.lab7.server.db_utils;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.util.console_workers.ErrorMessage;
import kib.lab7.server.utils.Config;
import kib.lab7.server.utils.MD2Encryptor;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private final DBConnector dbConnector = new DBConnector();
    private final MD2Encryptor encryptor = new MD2Encryptor();

    public void initializeDB() throws SQLException {
        String idSequenceCreationQuery = DBQueries.CREATE_SEQUENCE.getQuery();
        String userTableCreationQuery = DBQueries.CREATE_USERS_TABLE.getQuery();
        String humanTableCreationQuery = DBQueries.CREATE_HUMAN_TABLE.getQuery();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement idSequenceStatement = connection.prepareStatement(idSequenceCreationQuery);
             PreparedStatement userTableStatement = connection.prepareStatement(userTableCreationQuery);
             PreparedStatement humanTableStatement = connection.prepareStatement(humanTableCreationQuery)
        ) {
            idSequenceStatement.execute();
            userTableStatement.execute();
            humanTableStatement.execute();
        }
    }

    public boolean checkIfUserRegistered(String username, String password) {
        String query = DBQueries.FIND_USER_BY_LOG_AND_PASS.getQuery();

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, username);
            statement.setString(2, encryptor.encrypt(password));
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Произошла ошибка при работе с базой данных при проверке зарегистрированного пользователя"));
            return false;
        } catch (NoSuchAlgorithmException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Произошла ошибка при работе с базой данных при хешировании пароля"));
            return false;
        }
    }

    public boolean registerNewUser(String userLogin, String userPassword) {
        String query = DBQueries.ADD_USER.getQuery();

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, userLogin);
            statement.setString(2, encryptor.encrypt(userPassword));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Config.getTextSender().printMessage(new ErrorMessage("Произошла ошибка при работе с базой данных при добавлении нового пользователя"));
            return false;
        } catch (NoSuchAlgorithmException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Произошла ошибка при работе с базой данных при хешировании пароля"));
            return false;
        }
    }

    public Long[] clearByUserName(String userLogin) {
        String query = DBQueries.CLEAR_ALL_HUMAN_BEINGS_BY_USER.getQuery();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, userLogin);
            ResultSet rs = statement.executeQuery();
            List<Long> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getLong("id"));
            }
            Long[] ids = new Long[result.size()];
            ids = result.toArray(ids);
            return ids;
        } catch (SQLException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Произошла ошибка при работе с базой данных при удалении всех экземпляров одного пользователя"));
            return null;
        }

    }

    public long clearByIdAndUserName(long id, String userName) {
        String query = DBQueries.DELETE_HUMAN_BEING_BY_ID.getQuery();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            statement.setString(2, userName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Config.getTextSender().printMessage(new ErrorMessage("Произошла ошибка при удалении человека "));
            return -1;
        }
    }

    public long addHumanBeingToDB(HumanBeing human) {
        String query = DBQueries.ADD_HUMAN_BEING_TO_DB.getQuery();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            long id = generateId();
            int paramCounter = 1;
            statement.setLong(paramCounter++, id);
            paramCounter = setHumanInfoToStatementFromNameToCar(statement, human, paramCounter);
            statement.setString(paramCounter, human.getAuthor());
            statement.executeUpdate();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            Config.getTextSender().printMessage(new ErrorMessage("Произошла ошибка при работе с базой данных при добавлении нового HumanBeing"));
            return -1;
        }
    }

    private long generateId() {
        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(DBQueries.GENERATE_NEXT_ID.getQuery());
            if (rs.next()) {
                return rs.getInt("nextval");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Возникла ошибка при генерации нового ID"));
            return -1;
        }
    }

    public long updateByIdAndUser(HumanBeing human, long id, String user) {
        String query = DBQueries.UPDATE_BY_ID_AND_USER.getQuery();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            int paramCounter = setHumanInfoToStatementFromNameToCar(statement, human, 1);
            statement.setLong(paramCounter++, id);
            statement.setString(paramCounter, user);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Возникла ошибка при обновленнии человека по ID"));
            return -1;
        }
    }

    private int setHumanInfoToStatementFromNameToCar(PreparedStatement statement, HumanBeing human, int paramCounterStart) throws SQLException {
        int paramCounter = paramCounterStart;
        statement.setString(paramCounter++, human.getName());
        statement.setLong(paramCounter++, human.getCoordinates().getX());
        statement.setFloat(paramCounter++, human.getCoordinates().getY());
        statement.setDate(paramCounter++, Date.valueOf(LocalDate.now()));
        statement.setBoolean(paramCounter++, human.isRealHero());
        statement.setBoolean(paramCounter++, human.isHasToothpick());
        statement.setInt(paramCounter++, human.getImpactSpeed());
        if (human.getWeaponType() != null) {
            statement.setString(paramCounter++, String.valueOf(human.getWeaponType()));
        } else {
            statement.setNull(paramCounter++, Types.VARCHAR);
        }
        if (human.getMood() != null) {
            statement.setString(paramCounter++, String.valueOf(human.getMood()));
        } else {
            statement.setNull(paramCounter++, Types.VARCHAR);
        }
        if (human.getCar() != null) {
            statement.setBoolean(paramCounter++, human.getCar().getCarCoolness());
            statement.setInt(paramCounter++, human.getCar().getCarSpeed());
        } else {
            statement.setNull(paramCounter++, Types.BOOLEAN);
            statement.setNull(paramCounter++, Types.INTEGER);
        }
        return paramCounter;
    }

}
