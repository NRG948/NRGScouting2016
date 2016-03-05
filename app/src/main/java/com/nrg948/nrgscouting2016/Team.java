package com.nrg948.nrgscouting2016;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * Created by Sean on 2/6/2016.
 */
public class Team {
    public enum MethodOfScoring {
        SHOOTING("Shooting"),
        DAMAGING("Damaging"),
        BOTH("Both");
        private String s;
        private MethodOfScoring(String s){
            this.s = s;
        }
        public String getMethod(){
            return s;
        }
    }
    public enum MethodOfShooting {
        HIGH("High"),
        LOW("Low"),
        HIGH_AND_LOW("High and Low"),
        NEITHER("Neither");
        private String s;
        private MethodOfShooting(String s){
            this.s = s;
        }
        public String getMethod(){
            return s;
        }
    }
    public enum SpeedOfClimb{
        VERY_FAST(5),
        FAST(4),
        AVERAGE(3),
        SLOW(2),
        VERY_SLOW(1),
        CANNOT_CLIMB(0);
        private int s;
        private SpeedOfClimb(int s){
            this.s = s;
        }
        public int getSpeed(){
            return s;
        }
    }
    public int teamNumber;
    public int matchNumber;
    public int numberOfBoulders;
    public MethodOfScoring methodOfScoring;
    public MethodOfShooting methodOfShooting;
    public SpeedOfClimb speedOfClimb;
    public String comments;
    public String exceptionalCircumstances;
    public Boolean[] defenses = new Boolean[9];

    public String serialize(){
        String s = "/";
        s += teamNumber + "/";
        s += matchNumber +"/";
        s += numberOfBoulders + "/";
        s += methodOfScoring.name() + "/";
        s += methodOfShooting.name() + "/";
        s += speedOfClimb.name() + "/";
        for(int i = 0; i < 9; i ++){
            s += (defenses[i]? 1: 0) + "/";
        }
        if(comments.length() != 0) {
            s += comments + "/";
        }else {
            s += " /";
        }
        if(exceptionalCircumstances.length() != 0) {
            s += exceptionalCircumstances + "/NEWTEAM";
        }else {
            s += " /NEWTEAM";
        }
        return s;
    }

    public static Team deSerialize(String s){
        Team t = new Team();
        String[] data = s.split("/");
        t.teamNumber = Integer.parseInt(data[1]);
        t.matchNumber = Integer.parseInt(data[2]);
        t.numberOfBoulders = Integer.parseInt(data[3]);
        t.methodOfScoring = MethodOfScoring.valueOf(data[4]);
        t.methodOfShooting = MethodOfShooting.valueOf(data[5]);
        t.speedOfClimb = SpeedOfClimb.valueOf(data[6]);
        for(int i = 0; i < 9; i ++){
            t.defenses[i] = (data[i+7].equals("1"))? true : false;
        }
        t.comments = data[16];
        t.exceptionalCircumstances = data[17];
        return t;
    }
}
