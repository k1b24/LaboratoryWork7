package kib.lab7.server.utils;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.entities.enums.Mood;
import kib.lab7.server.db_utils.DBManager;

import java.time.LocalDate;

public class    DataManager {

    private final CollectionManager collectionManager;
    private final DBManager dbManager;

    public DataManager() {
        this.collectionManager = new CollectionManager();
        this.dbManager = new DBManager();
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public boolean addHumanBeing(HumanBeing human) {
        long id = dbManager.addHumanBeingToDB(human);
        if (id == -1) {
            return false;
        } else {
            human.setId(id);
            human.setCreationDate(LocalDate.now());
            collectionManager.addHuman(human);
            return true;
        }
    }

    public boolean addHumanBeingIfMinimal(HumanBeing human) {
        if (collectionManager.checkIfMin(human)) {
            return addHumanBeing(human);
        } else {
            return false;
        }
    }

    public long removeHumanByIdAndUserName(long id, String username) {
        long removedId = dbManager.clearByIdAndUserName(id, username);
        if (removedId != -1) {
            collectionManager.removeHumanByIdAndUserName(removedId, username);
            return removedId;
        } else {
            return -1;
        }
    }

    public boolean loginUser(String userLogin, String userPassword) {
        return dbManager.checkIfUserRegistered(userLogin, userPassword);
    }

    public boolean createUser(String userLogin, String userPassword) {
        return dbManager.registerNewUser(userLogin, userPassword);
    }

    public Long[] clearByName(String userLogin) {
        Long[] ids = dbManager.clearByUserName(userLogin);
        if (ids == null) {
            return null;
        } else {
            collectionManager.clearCollectionByIDs(ids);
            return ids;
        }
    }

    public long removeHumanByAnyMoodAndUserName(Mood moodArgumentToSend, String user) {
        long id = collectionManager.getHumanIdByAnyMoodAndUserName(moodArgumentToSend, user);
        if (id == 0) {
            return 0;
        } else {
            long resultId = dbManager.clearByIdAndUserName(id, user);
            if (resultId == -1) {
                return -1;
            } else {
                collectionManager.removeHumanByIdAndUserName(id, user);
                return id;
            }
        }
    }

    public long updateByIdAndUser(HumanBeing human, long id, String user) {
        long returnedId = dbManager.updateByIdAndUser(human, id, user);
        if (returnedId > 0) {
            collectionManager.setHumanById(id, human);
        }
        return returnedId;
    }
}
