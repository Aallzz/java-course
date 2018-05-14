package ru.ifmo.rain.Petrovski.studentdb;

import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentGroupQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class StudentDB implements StudentGroupQuery {

    private final Comparator<Student> fullNameComparator = Comparator.comparing(Student::getLastName)
            .thenComparing(Student::getFirstName)
            .thenComparing(Student::getId);

    private List<Group> getGroupsBy(Collection<Student> collection, Comparator<Student> cmp) {
        return collection
                .stream()
                .collect(Collectors.groupingBy(Student::getGroup, Collectors.toList()))
                .entrySet()
                .stream()
                .map(v -> new Group(v.getKey(), v.getValue()
                        .stream()
                        .sorted(cmp)
                        .collect(Collectors.toList())))
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> collection) {
        return getGroupsBy(collection, fullNameComparator);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> collection) {
        return getGroupsBy(collection, Comparator.comparing(Student::getId));
    }

    private String getLargestGroupBy(Collection<Student> collection, Comparator<Group> comparator) {
        return collection.stream().collect(Collectors.groupingBy(Student::getGroup)).entrySet().stream()
                .map(entry -> new Group(entry.getKey(), entry.getValue()))
                .max(comparator)
                .map(Group::getName).orElse("");
    }

    @Override
    public String getLargestGroup(Collection<Student> collection) {
        return getLargestGroupBy(collection, Comparator.<Group, Integer>comparing(group -> group.getStudents().size())
                .thenComparing(Comparator.comparing(Group::getName).reversed()));

    }

    @Override
    public String getLargestGroupFirstName(Collection<Student> collection) {
        return getLargestGroupBy(collection, Comparator.<Group, Integer>comparing(group -> getDistinctFirstNames(group.getStudents()).size())
                .thenComparing(Comparator.comparing(Group::getName).reversed()));
    }

    private <T extends Collection<String>> T mapStudentList(List<Student> list, Function<Student, String> mapFunction, Supplier<T> collection) {
        return list
                .stream()
                .map(mapFunction)
                .collect(Collectors.toCollection(collection));
    }

    private List<String> mapStudentList(List<Student> list, Function<Student, String> mapFunction) {
        return mapStudentList(list, mapFunction, ArrayList::new);
    }

    private <T extends Collection<Student>> T filterStudentList(Collection<Student> collection, Predicate<Student> predicateFunction, Supplier<T> supl) {
        return collection
                .stream()
                .filter(predicateFunction)
                .sorted(fullNameComparator)
                .collect(Collectors.toCollection(supl));
    }

    private List<Student> filterStudentList(Collection<Student> collection, Predicate<Student> predicateFunction) {
        return filterStudentList(collection, predicateFunction, ArrayList::new);
    }

    @Override
    public List<String> getFirstNames(List<Student> list) {
        return mapStudentList(list, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> list) {
        return mapStudentList(list, Student::getLastName);
    }

    @Override
    public List<String> getGroups(List<Student> list) {
        return mapStudentList(list, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> list) {
        return mapStudentList(list, student -> student.getFirstName() + " " + student.getLastName());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> list) {
        return mapStudentList(list, Student::getFirstName, TreeSet::new);
    }

    @Override
    public String getMinStudentFirstName(List<Student> list) {
        return list.stream().min(Student::compareTo).map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> collection) {
        return collection.stream().sorted(comparing(Student::getId)).collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> collection) {
        return collection
                .stream()
                .sorted(fullNameComparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> collection, String s) {
        return filterStudentList(collection, student -> student.getFirstName().equals(s));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> collection, String s) {
        return filterStudentList(collection, student -> student.getLastName().equals(s));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> collection, String s) {
        return filterStudentList(collection, student -> student.getGroup().equals(s));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> collection, String s) {
        return collection
                .stream()
                .filter(v -> v.getGroup().equals(s))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }
}

