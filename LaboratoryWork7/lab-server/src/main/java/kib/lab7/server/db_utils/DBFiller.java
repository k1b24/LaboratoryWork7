package kib.lab7.server.db_utils;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.util.StringToTypeConverter;
import kib.lab7.server.utils.Config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBFiller {

    //TODO ПРИ НАЛИЧИИ ВРЕМЕНИ ПЕРЕПИСАТЬ ЭТО НОРМАЛЬНО, БЕЗ КЕТЧЕЙ ОШИБОК ВО ВЛОЖЕННЫХ ПОЛЯХ
    private List<HumanBeing> createHumanBeingListFromResultSet(ResultSet resultSet) throws SQLException {
        List<HumanBeing> resultList = new ArrayList<>();
        Field[] humanBeingFields = HumanBeing.class.getDeclaredFields();
        ArrayList<String> humanInfo = new ArrayList<>();
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            humanInfo.add(resultSet.getMetaData().getColumnName(i));
        }
        while (resultSet.next()) {
            HumanBeing newHuman = new HumanBeing();
            for (String element : humanInfo) {
                for (Field field : humanBeingFields) {
                    Class<?> cl = field.getType();
                    try {
                        if (field.getName().equals(element)) {
                            Method setter = HumanBeing.class.getDeclaredMethod("set"
                                    + field.getName().substring(0, 1).toUpperCase()
                                    + field.getName().substring(1), field.getType());
                            setter.invoke(newHuman, (resultSet.getString(element) == null) ? null : StringToTypeConverter.toObject(field.getType(), resultSet.getString(element)));
                        } else {
                            Field[] innerFields = cl.getDeclaredFields();
                            for (Field innerField : innerFields) {
                                if (innerField.getName().equals(element)) {
                                    Method innerSetter = cl.getDeclaredMethod("set"
                                            + innerField.getName().substring(0, 1).toUpperCase()
                                            + innerField.getName().substring(1), innerField.getType());
                                    Method getter = HumanBeing.class.getDeclaredMethod("get"
                                            + cl.getSimpleName().substring(0, 1).toUpperCase()
                                            + cl.getSimpleName().substring(1));
                                    Method outerSetter = HumanBeing.class.getDeclaredMethod("set"
                                            + cl.getSimpleName().substring(0, 1).toUpperCase()
                                            + cl.getSimpleName().substring(1), cl);
                                    if ("".equals(resultSet.getString(element))) {
                                        outerSetter.invoke(newHuman, (Object) null);
                                    } else if (getter.invoke(newHuman) != null) {
                                        try {
                                            innerSetter.invoke(getter.invoke(newHuman), ("null".equals(resultSet.getString(element)) ? null : StringToTypeConverter.toObject(innerField.getType(), resultSet.getString(element))));
                                        } catch (
                                                NumberFormatException e) { // Чуть чуть костыль, не дает сто процентной универсальности
                                            outerSetter.invoke(newHuman, (Object) null);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(2);
                    }
                }
            }
            resultList.add(newHuman);
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
