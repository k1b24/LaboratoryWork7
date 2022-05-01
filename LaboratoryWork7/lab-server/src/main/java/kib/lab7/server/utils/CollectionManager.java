package kib.lab7.server.utils;

import kib.lab7.common.entities.HumanBeing;
import kib.lab7.common.entities.enums.Mood;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Класс для работы с коллекцией экземпляров HumanBeing
 */
public class CollectionManager {

    private final PriorityQueue<HumanBeing> humanQueue = new PriorityQueue<>();
    private final LocalDate initializationDate;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public CollectionManager() {
        initializationDate = LocalDate.now();
    }

    public ArrayList<HumanBeing> getSortedArrayListFromQueue() {
        try {
            readLock.lock();
            return (ArrayList<HumanBeing>) humanQueue.stream().sorted().collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    public void addHuman(HumanBeing human) {
        try {
            writeLock.lock();
            humanQueue.add(human);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeHumanByIdAndUserName(long id, String user) {
        try {
            writeLock.lock();
            humanQueue.removeIf(human -> human.getId() == id && human.getAuthor().equals(user));
        } finally {
            writeLock.unlock();
        }
    }

    public long getHumanIdByAnyMoodAndUserName(Mood mood, String user) {
        try {
            readLock.lock();
            long id = 0;
            for (HumanBeing human : humanQueue) {
                if (human.getAuthor().equals(user)) {
                    if (human.getMood() == null) {
                        id = human.getId();
                    } else if (human.getMood().equals(mood)) {
                        id = human.getId();
                    }
                }
            }
            return id;
        } finally {
            readLock.unlock();
        }
    }

    public void setHumanById(long id, HumanBeing humanToSet) throws IllegalArgumentException {
        try {
            writeLock.lock();
            humanToSet.setId(id);
            humanQueue.add(humanToSet);
        } finally {
            writeLock.unlock();
        }
    }

    public void fillWithArray(ArrayList<HumanBeing> arrayOfPeople) {
        try {
            writeLock.lock();
            humanQueue.addAll(arrayOfPeople);
        } finally {
            writeLock.unlock();
        }
    }

    public HumanBeing returnHead() {
        try {
            readLock.lock();
            return humanQueue.peek();
        } finally {
            readLock.unlock();
        }
    }

    public ArrayList<HumanBeing> returnDescending() {
        try {
            readLock.lock();
            ArrayList<HumanBeing> descendingList = getSortedArrayListFromQueue();
            Collections.reverse(descendingList);
            return descendingList;
        } finally {
            readLock.unlock();
        }
    }

    public ArrayList<HumanBeing> filterByCarSpeed(int speed) {
        try {
            readLock.lock();
            return (ArrayList<HumanBeing>) humanQueue.stream().filter(human -> (human.getCar() != null && human.getCar().getCarSpeed() < speed)).collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    public boolean checkIfMin(HumanBeing newHuman) {
        try {
            readLock.lock();
            for (HumanBeing human : humanQueue) {
                if (newHuman.compareTo(human) > 0) {
                    return false;
                }
            }
            return true;
        } finally {
            readLock.unlock();
        }
    }

    public void clearCollectionByIDs(Long[] ids) {
        try {
            writeLock.lock();
            for (long id : ids) {
                humanQueue.removeIf(human -> human.getId() == id);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public String getInfoAboutCollection() {
        try {
            readLock.lock();
            return "Информация о коллекции. Тип: " + humanQueue.getClass() + " Дата инициализации: "
                    + initializationDate.toString() + " Количество элементов: " + humanQueue.size();
        } finally {
            readLock.unlock();
        }
    }
}
