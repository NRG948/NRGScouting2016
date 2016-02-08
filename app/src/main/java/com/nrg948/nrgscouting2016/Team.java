package com.nrg948.nrgscouting2016;

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
    public int numberOfBoulders;
    public MethodOfScoring methodOfScoring;
    public MethodOfShooting methodOfShooting;
    public SpeedOfClimb speedOfClimb;
    public String comments;
    public String exceptionalCircumstances;
}
