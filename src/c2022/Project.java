package c2022;

import java.util.ArrayList;


public class Project implements Comparable<Project> {
    String name;
    int daysToComplete;
    int points;
    int bestBefore;
    int rolesNum;
    ArrayList<Integer> rolesMap;
    ArrayList<String> roles;
    boolean badProj;
    ArrayList<Contributor> contributors;
    int startDate;
    int finishDate;
    boolean finished;
    static final int THRESHOLD = 5000;

    public Project(String name, int daysToComplete, int points, int bestBefore, int rolesNum) {
        this.name = name;
        this.daysToComplete = daysToComplete;
        this.points = points;
        this.bestBefore = bestBefore;
        this.rolesNum = rolesNum;
        rolesMap = new ArrayList<>();
        contributors = new ArrayList<>();
        roles = new ArrayList<>();
    }

    public void addRole(String name, int level) {
        roles.add(name);
        rolesMap.add(level);
    }

    public void invalid() {
        badProj = true;
    }

    public void setStartDate(int start) {
        for (Contributor c : contributors) {
            c.busy = true;
        }
        startDate = start;
        finishDate = startDate + daysToComplete;
    }

    public void setFinished() {
        finished = true;
        for (int i = 0; i < contributors.size(); i++) {
            contributors.get(i).busy = false;
            contributors.get(i).levelUp(roles.get(i), rolesMap.get(i));
        }
    }

    public void addContributor(Contributor c) {
        contributors.add(c);
        c.busy = true;
    }

    public void flush() {
        for (Contributor c : contributors) {
            c.busy = false;
        }
        contributors.clear();
    }

    public boolean pointsThreshold(int day) {
        int finishBy = day + daysToComplete;
        int daysAfter = finishBy - bestBefore;
        int pointsLeft = this.points - daysAfter;
        if (daysAfter <= 0)
            return true;
        return pointsLeft > THRESHOLD;
    }

    @Override
    public int compareTo(Project project) {
        return bestBefore - project.bestBefore;
    }
}
