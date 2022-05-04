package kib.lab7.server.db_utils;

public enum DBQueries {
    ADD_HUMAN_BEING_TO_DB("INSERT INTO s335106humanbeings (id, name, x, y, creationDate, realHero,"
            + " hasToothpick, impactSpeed, weaponType, mood, carCoolness, carSpeed, author)"
            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),

    ADD_USER("INSERT INTO s335106users (username, password) VALUES (?, ?)"),

    DELETE_HUMAN_BEING_BY_ID("DELETE FROM s335106humanbeings WHERE id = ? AND author = ? RETURNING id"),

    CLEAR_ALL_HUMAN_BEINGS_BY_USER("DELETE FROM s335106humanbeings WHERE author = ? RETURNING s335106humanbeings.id"),

    GENERATE_NEXT_ID("SELECT nextval('ids')"),

    UPDATE_BY_ID_AND_USER("UPDATE s335106humanbeings SET (name, x, y, creationDate, realHero, hasToothpick, impactSpeed, weaponType, mood, carCoolness, carSpeed) = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) WHERE id = ? AND author = ? RETURNING s335106humanbeings.id"),

    FIND_USER_BY_LOG_AND_PASS("SELECT FROM s335106users WHERE username=? AND password=?"),

    CREATE_HUMAN_TABLE("CREATE TABLE IF NOT EXISTS s335106HumanBeings ("
            + "id bigint PRIMARY KEY DEFAULT (nextval('ids')),"
            + "name varchar(255) NOT NULL CHECK(name<>''),"
            + "x bigint NOT NULL CHECK(x > -759),"
            + "y real NOT NULL,"
            + "creationdate date NOT NULL DEFAULT(current_date),"
            + "realhero boolean NOT NULL,"
            + "hastoothpick boolean NOT NULL,"
            + "impactspeed int CHECK(impactSpeed < 712),"
            + "weapontype varchar(11) CHECK("
            + "weapontype='HAMMER' OR weapontype='AXE' OR "
            + "weapontype='RIFLE' OR weapontype='KNIFE' "
            + "OR weapontype='MACHINE_GUN'),"
            + "mood varchar(6) CHECK("
            + "mood='APATHY' OR mood='CALM' OR mood='RAGE'),"
            + "carcoolness boolean,"
            + "carspeed int,"
            + "author varchar(255) REFERENCES s335106Users (username)"
            + ")"),

    CREATE_USERS_TABLE("CREATE TABLE IF NOT EXISTS s335106Users("
            + "username varchar(255) PRIMARY KEY NOT NULL CHECK (TRIM(username) <> ''),"
            + "password char(128) DEFAULT(null)"
            + ")"),

    CREATE_SEQUENCE("CREATE SEQUENCE IF NOT EXISTS ids START 1");

    private final String query;

    DBQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
