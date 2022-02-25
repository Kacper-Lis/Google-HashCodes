package c2022;

import java.util.HashMap;

public class Contributor implements Comparable<Contributor> {
    String name;
    int skillsNum;
    HashMap<String, Integer> skills;
    boolean busy;
    static String skillCompared;

    public Contributor(String name, int skillsNum){
        this.name = name;
        this.skillsNum = skillsNum;
        skills = new HashMap<>();
        busy = false;
    }

    public void addSkill(String name, int level){
        skills.put(name, level);
    }

    public void levelUp(String skill, int level){
        if(skills.get(skill) <= level){
            skills.put(skill, skills.get(skill)+1);
        }
    }

    @Override
    public int compareTo(Contributor c) {
        return skills.get(skillCompared) - c.skills.get(skillCompared);
    }
}
