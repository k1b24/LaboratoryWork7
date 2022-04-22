package kib.lab7.server.db_utils;

public enum DBQueries { //TODO Дописать все запросы
    ADD_HUMAN_BEING_TO_DB("INSERT INTO s335106humanbeings (\"id\", \"name\", \"x\", \"y\", \"creationDate\", \"realHero\"," +
            " \"hasToothpick\", \"impactSpeed\", \"weaponType\", \"mood\", \"carCoolness\", \"carSpeed\", \"author\")" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),

    ADD_USER("INSERT INTO s335106users (username, password) VALUES (?, ?)"),

    DELETE_HUMAN_BEING_BY_ID("DELETE FROM s335106humanbeings WHERE id = ? AND author = ? RETURNING id"),

    CLEAR_ALL_HUMAN_BEINGS_BY_USER("DELETE FROM s335106humanbeings WHERE author = ? RETURNING s335106humanbeings.id"),

    GENERATE_NEXT_ID("SELECT nextval('ids')"),

    UPDATE_BY_ID_AND_USER("UPDATE s335106humanbeings SET (\"name\", \"x\", \"y\", \"creationDate\", \"realHero\", \"hasToothpick\", \"impactSpeed\", \"weaponType\", \"mood\", \"carCoolness\", \"carSpeed\") = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) WHERE id = ? AND author = ? RETURNING s335106humanbeings.id"),

    FIND_USER_BY_LOG_AND_PASS("SELECT FROM s335106users WHERE username=? AND password=?");

    private final String query;

    DBQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
