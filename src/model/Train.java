package model;

import data.DataGenerator;
import data.DataReader;
import data.Schedule;
import data.Visualize;
import lib.Class;
import lib.Subject;
import lib.Teacher;

public class Train {
    public static final int M = 5; //teacher size
    public static final int K = 20; //subject size
    public static final int N = 37; //class size

    public Schedule scheduleFrame;
    public Visualize visualize;

    public Train() {
        this.scheduleFrame = new Schedule();
        this.visualize = new Visualize("GA visualization", "Without Minimizing standard deviation");
    }

    public void notify(Chromosome c, double bestFitness, double avgFitness, int violation) {
        scheduleFrame.addSchedule(c);
        this.visualize.add(bestFitness, avgFitness, violation);
    }

    public static void main(String[] args) {

//        Model model = DataGenerator.Gendata(M, K, N);

        Train train = new Train();
        Model model = DataReader.getData();

//        for(Subject subject:model.getSubjects()) {
//            System.out.println(subject.getName());
//        }

        GeneticAlgorithm ga = new GeneticAlgorithm(model, train);
        ga.start();
    }
}

