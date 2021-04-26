package EA;

import EA.Components.Comparators.FitnessComparator;
import EA.Components.Individual;
import EA.Operations.Crossover;
import EA.Operations.Mutation;
import EA.Operations.Selection;

import java.util.*;


public class GA {

    private final int populationSize;
    private final int generations;
    private final Crossover crossover;
    private final Mutation mutation;
    private final double[] fitnessWeights; // [edgeValue, connectivity, deviation]


    public GA(int populationSize, int generations, Crossover crossover, Mutation mutation, double[] fitnessWeights) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.crossover = crossover;
        this.mutation = mutation;
        this.fitnessWeights = fitnessWeights;
    }


    public List<Individual> run(List<Individual> population) {
        Comparator<Individual> fitnessComparator = new FitnessComparator();
        this.evaluatePopulation(population);
        population.sort(fitnessComparator);
        this.printGenerationStats(0, population);

        for (int i = 1; i <= this.generations; i++) {
            List<Individual> parents = new ArrayList<>(this.populationSize);
            while (parents.size() < this.populationSize) {
                Individual[] parentPair = Selection.selectRandomPair(population);

                Individual[] offspring = this.crossover.applyUniformCrossover(parentPair[0], parentPair[1]);

                Individual mutatedOffspring1 = this.mutation.applySingleBitMutation(offspring[0]);
                Individual mutatedOffspring2 = this.mutation.applySingleBitMutation(offspring[1]);

                if (mutatedOffspring1 != parentPair[0]) {
                    this.evaluateIndividual(mutatedOffspring1);
                }
                if (mutatedOffspring2 != parentPair[1]) {
                    this.evaluateIndividual(mutatedOffspring2);
                }

                parents.addAll(Arrays.asList(mutatedOffspring1, mutatedOffspring2));
            }
            population.addAll(parents);
            population.sort(fitnessComparator);
            population = population.subList(0, this.populationSize);

            if (i % 10 == 0) {
                this.printGenerationStats(i, population);
            }
        }

        return population;
    }


    private void evaluatePopulation(List<Individual> population) {
        for (Individual individual : population) {
            this.evaluateIndividual(individual);
        }
    }


    private void evaluateIndividual(Individual individual) {
        individual.computeSegments();
        Objectives.evaluateIndividual(individual);
        individual.calculateFitness(this.fitnessWeights);
    }


    private double getAverageFitness(List<Individual> population) {
        double sum = 0;
        for (Individual individual : population) {
            sum += individual.getFitness();
        }
        return sum / population.size();
    }


    private void printGenerationStats(int generation, List<Individual> population) {
        String space = generation < 100 ? generation < 10 ? "       " : "      " : "     ";
        String gen = generation > 0 ? "Generation " + generation + space : "Initial generation ";

        // Assume population is sorted
        double bestFitness = population.get(0).getFitness();
        double worstFitness = population.get(population.size() - 1).getFitness();
        double averageFitness = this.getAverageFitness(population);

        System.out.println(gen +
                " | Population size: " + population.size() +
                " | Average fitness: " + averageFitness +
                " | Best fitness: " + bestFitness +
                " | Worst fitness: " + worstFitness);
    }
}
