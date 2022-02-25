package c2022;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    //Input names
    static String inputDir = "src\\c2022\\data";
    static String inputExtension = ".txt";

    //Control variables
    static int C; //contributors
    static int P; //projects

    //List data
    static ArrayList<Contributor> contributors;
    static ArrayList<Project> projects;
    static HashSet<String> skills;
    static HashMap<String, ArrayList<Contributor>> skillsMap;
    static ArrayList<Project> finishedProjects;

    public static void main(String[] args) throws FileNotFoundException {
        Parser[] parsers = getParsers();
        for(int i = 0; i < parsers.length; i++){
            solve(parsers[i].getRawData());
            writeOutput(parsers[i].name);
        }
    }

    public static void solve(String[][] rawData) {
        //Clear the static lists first for every iteration
        C = Integer.parseInt(rawData[0][0]);
        P = Integer.parseInt(rawData[0][1]);
        contributors = new ArrayList<>();
        skills = new HashSet<>();
        projects = new ArrayList<>();
        skillsMap = new HashMap<>();
        int pos = 1;
        int cont = 0;
        int maxDay = 0;
        //Create Data
        while (cont < C) {
            String name = rawData[pos][0];
            int skillsNum = Integer.parseInt(rawData[pos][1]);
            Contributor c = new Contributor(name, skillsNum);
            for (int j = 1; j <= skillsNum; j++) {
                String skill = rawData[pos + j][0];
                if (skillsMap.containsKey(skill)) {
                    skillsMap.get(skill).add(c);
                } else {
                    skillsMap.put(skill, new ArrayList<>());
                    skillsMap.get(skill).add(c);
                }
                int level = Integer.parseInt(rawData[pos + j][1]);
                skills.add(skill);
                c.addSkill(skill, level);
            }
            contributors.add(c);
            pos += skillsNum + 1;
            cont++;
        }
        int proj = 0;
        while (proj < P) {
            String name = rawData[pos][0];
            int daysToComplete = Integer.parseInt(rawData[pos][1]);
            int points = Integer.parseInt(rawData[pos][2]);
            int bestBefore = Integer.parseInt(rawData[pos][3]);
            int roles = Integer.parseInt(rawData[pos][4]);
            Project p = new Project(name, daysToComplete, points, bestBefore, roles);
            for (int j = 1; j <= roles; j++) {
                String skill = rawData[pos + j][0];
                if (!skills.contains(skill)) {
                    p.invalid();
                    break;
                }
                int level = Integer.parseInt(rawData[pos + j][1]);
                skills.add(skill);
                p.addRole(skill, level);
            }
            if (!p.badProj) {
                projects.add(p);
                maxDay += daysToComplete;
            }
            proj++;
            pos += roles + 1;
        }
        /**
         * Sorting bit
         */
        //Collections.sort(projects);
        for (Map.Entry<String, ArrayList<Contributor>> e : skillsMap.entrySet()) {
            Contributor.skillCompared = e.getKey();
            Collections.sort(e.getValue());
        }
        ArrayList<Project> notAssigned = new ArrayList<>(projects);
        ArrayList<Project> assigned = new ArrayList<>();
        finishedProjects = new ArrayList<>();
        int day = 0;

        while (day < maxDay) {
            ArrayList<Project> pToRem = new ArrayList<>();
            boolean somethingFinished = false;
            for (Project p : assigned) {
                if (day == p.finishDate) {
                    p.setFinished();
                    finishedProjects.add(p);
                    pToRem.add(p);
                    somethingFinished = true;
                }
            }
            assigned.removeAll(pToRem);
            pToRem = new ArrayList<>();
            if (somethingFinished || day == 0) {
                for (Project p : notAssigned) {
                    if (!p.pointsThreshold(day)) {
                        pToRem.add(p);
                        continue;
                    }
                    for (int i = 0; i < p.roles.size(); i++) {
                        String r = p.roles.get(i);
                        int levelRequired = p.rolesMap.get(i);
                        ArrayList<Contributor> possibleCs = skillsMap.get(r);
                        for (Contributor c : possibleCs) {
                            if (c.busy) {
                                continue;
                            }
                            if (c.skills.get(r) >= levelRequired) {
                                p.addContributor(c);
                                break;
                            }
                        }
                    }
                    if (p.rolesNum == p.contributors.size()) {
                        p.setStartDate(day);
                        assigned.add(p);
                        pToRem.add(p);
                    } else {
                        p.flush();
                    }
                }
            }
            notAssigned.removeAll(pToRem);
            day++;
        }
    }

    //Parsers to get data from every input file
    public static Parser[] getParsers() {
        File[] folder = new File(inputDir).listFiles((dir, name) -> name.endsWith(inputExtension));
        List<Parser> parsers = Arrays.stream(folder).map(o -> new Parser(o)).collect(Collectors.toList());
        return parsers.toArray(new Parser[0]);
    }

    //Write to output files
    public static void writeOutput(String outName) throws FileNotFoundException {
        PrintStream out = new PrintStream(new FileOutputStream("out_" + outName)); //change output name here
        System.setOut(out);
        out.println(finishedProjects.size());
        for (Project p : finishedProjects) {
            out.println(p.name);
            String cunts = "";
            for (int i = 0; i < p.contributors.size() - 1; i++) {
                cunts += p.contributors.get(i).name + " ";
            }
            cunts += p.contributors.get(p.contributors.size() - 1).name;
            out.println(cunts);
        }
    }
}
