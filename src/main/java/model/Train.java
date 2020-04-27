package model;

import data.*;

import java.util.Vector;

public class Train {
    public static final int M = 5; //teacher size
    public static final int K = 20; //subject size
    public static final int N = 37; //class size

    public Schedule scheduleFrame;
    public Visualize visualize;
    Vector<OutputRecord> data;

    public Train() {
        this.scheduleFrame = new Schedule();
        this.visualize = new Visualize("GA visualization", "Without Minimizing standard deviation");
        data = new Vector<>();
    }

    public void notify(Chromosome c, int genId, double bestFitness, double avgFitness, int violation, double objectiveValue) {
        scheduleFrame.addSchedule(c);
        this.visualize.add(objectiveValue, avgFitness, violation);
        data.add(new OutputRecord(genId, bestFitness, objectiveValue, violation, avgFitness));
        if (genId % 50 == 0) {
            DataWriter.writeToCsv(data, "out_1_3.csv");
        }
    }

    public static void main(String[] args) {

//        Model model = DataGenerator.Gendata(M, K, N);

        Train train = new Train();
        Model model = DataReader.getData();
        System.out.println(model.getTeachers().size());
        System.out.println(model.getSubjects().size());
        System.out.println(model.getClasses().size());

//        for(Subject subject:model.getSubjects()) {
//            System.out.println(subject.getName());
//        }

        GeneticAlgorithm ga = new GeneticAlgorithm(model, train);
        ga.start();
    }
}

