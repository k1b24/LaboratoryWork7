package kib.lab7.server.utils;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.entities.enums.Mood;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Класс для работы с коллекцией экземпляров HumanBeing
 */
public class CollectionManager {

    private PriorityQueue<HumanBeing> humanQueue = new PriorityQueue<>();
    private final LocalDate initializationDate;

    public CollectionManager() {
        initializationDate = LocalDate.now();
    }

    public ArrayList<HumanBeing> getSortedArrayListFromQueue() {
        return (ArrayList<HumanBeing>) humanQueue.stream().sorted().collect(Collectors.toList());
    }

    public void addHuman(HumanBeing human) {
        humanQueue.add(human);
    }

    public void removeHumanByIdAndUserName(long id, String user) {
        humanQueue.removeIf(human -> human.getId() == id && human.getAuthor().equals(user));
    }

    public long getHumanIdByAnyMoodAndUserName(Mood mood, String user) {
        long id = 0;
        for (HumanBeing human : humanQueue) {
            if (human.getMood() == null && human.getAuthor().equals(user)) {
                id = human.getId();
            } else if (human.getMood().equals(mood) && human.getAuthor().equals(user)) {
                id = human.getId();
            }
        }
        return id;
    }

    public void setHumanById(long id, HumanBeing humanToSet) throws IllegalArgumentException {
        if (humanQueue.removeIf(human ->  human.getId() == id)) {
            humanToSet.setId(id);
            humanQueue.add(humanToSet);
        } else {
            throw new IllegalArgumentException("Человек с таким ID не найден");
        }
    }

    public void fillWithArray(ArrayList<HumanBeing> arrayOfPeople) {
        humanQueue.addAll(arrayOfPeople );
    }

    public HumanBeing returnHead() {
        return humanQueue.peek();
    }

    public ArrayList<HumanBeing> returnDescending() {
        ArrayList<HumanBeing> descendingList = getSortedArrayListFromQueue();
        Collections.reverse(descendingList);
        return descendingList;
    }

    public ArrayList<Long> getIDs() {
        return (ArrayList<Long>) humanQueue.stream().map(HumanBeing::getId).collect(Collectors.toList());
    }

    public ArrayList<HumanBeing> filterByCarSpeed(int speed) {
        return (ArrayList<HumanBeing>) humanQueue.stream().filter(human -> (human.getCar() != null && human.getCar().getCarSpeed() < speed)).collect(Collectors.toList());
    }

    public boolean checkIfMin(HumanBeing newHuman) {
        for (HumanBeing human : humanQueue) {
            if (newHuman.compareTo(human) > 0) {
                return false;
            }
        }
        return true;
    }

    public void clearCollectionByIDs(Long[] ids) {
        for (long id : ids) {
            humanQueue.removeIf(human -> human.getId() == id);
        }
    }

    public String getInfoAboutCollection() {
        return "Информация о коллекции. Тип: " + humanQueue.getClass() + " Дата инициализации: "
                + initializationDate.toString() + " Количество элементов: " + humanQueue.size();
    }
}
