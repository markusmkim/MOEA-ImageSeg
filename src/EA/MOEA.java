package EA;

import EA.Components.Comparators.*;
import EA.Components.Individual;
import EA.Operations.Crossover;
import EA.Operations.Mutation;
import EA.Operations.Selection;

import java.util.*;


public class MOEA {

    private final int populationSize;
    private final int generations;
    private final Crossover crossover;
    private final Mutation mutation;


    public MOEA(int populationSize, int generations, Crossover crossover, Mutation mutation) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.crossover = crossover;
        this.mutation = mutation;
    }


    public List<Individual> run(List<Individual> population) {
        NonDominatedObjectivesComparator nonDominatedObjectivesComparator = new NonDominatedObjectivesComparator();
        this.evaluatePopulation(population);

        population.sort(nonDominatedObjectivesComparator);

        List<List<Individual>> fronts = frontsByRank(population, nonDominatedObjectivesComparator);

        System.out.println("hei");
        System.out.println("" + fronts.size());
        for (List<Individual> front : fronts) {
            System.out.println("" + front.size());
        }

        this.printGenerationStats(0, population);

        for (int i = 1; i <= this.generations; i++) {
            List<Individual> parents = new ArrayList<>(this.populationSize);
            while (parents.size() < this.populationSize) {
                Individual[] parentPair = Selection.selectRandomPair(population);

                Individual[] offspring = this.crossover.applyUniformCrossover(parentPair[0], parentPair[1]);

                Individual mutatedOffspring1 = this.mutation.applySingleBitMutation(offspring[0]);
                Individual mutatedOffspring2 = this.mutation.applySingleBitMutation(offspring[1]);

                parents.addAll(Arrays.asList(mutatedOffspring1, mutatedOffspring2));
            }
            population.addAll(parents);
            Map<String, double[]> minMaxValues = this.evaluatePopulation(population);

            population.sort(nonDominatedObjectivesComparator);
            fronts = frontsByRank(population, nonDominatedObjectivesComparator);
            System.out.println("hei");
            System.out.println("" + fronts.size());

            List<Individual> survivors = new ArrayList<>();
            int frontIndex = 0;
            boolean addNextFrontToSurvivors = fronts.get(frontIndex).size() <= this.populationSize;
            while (addNextFrontToSurvivors) {
                survivors.addAll(fronts.get(frontIndex));
                frontIndex++;

                if (frontIndex < fronts.size()) {
                    if (fronts.get(frontIndex).size() + survivors.size() > this.populationSize) {
                        addNextFrontToSurvivors = false;
                    }
                }
                else {
                    addNextFrontToSurvivors = false;
                }
            }
            // System.out.println("dddd - " + survivors.size());

            if (survivors.size() < this.populationSize) {
                // System.out.println("fffug");
                List<Individual> frontToDifferentiate = fronts.get(frontIndex);
                this.calculateCrowdingDistances(frontToDifferentiate, minMaxValues);
                frontToDifferentiate.sort(new CrowdingDistanceComparator());
                while (survivors.size() < this.populationSize) {
                    survivors.add(frontToDifferentiate.remove(0));
                }
            }

            // System.out.println("dudu - " + survivors.size());

            population = survivors;

            if (i % 10 == 0) {
                this.printGenerationStats(i, population);
            }
        }

        return population;
    }

    /*
    Returns tuples of [min, max] values for the three objective functions
     */
    private Map<String, double[]> evaluatePopulation(List<Individual> population) {
        double minEdgeValue = 1000000;
        double maxEdgeValue = -1000000;
        double minConnectivity = 1000000;
        double maxConnectivity = -1000000;
        double minDeviation = 1000000;
        double maxDeviation = -1000000;

        for (Individual individual : population) {
            double[] objectiveValues = this.evaluateIndividual(individual);
            if (objectiveValues[0] < minEdgeValue) {
                minEdgeValue = objectiveValues[0];
            }
            if (objectiveValues[0] > maxEdgeValue) {
                maxEdgeValue = objectiveValues[0];
            }
            if (objectiveValues[1] < minConnectivity) {
                minConnectivity = objectiveValues[1];
            }
            if (objectiveValues[1] > maxConnectivity) {
                maxConnectivity = objectiveValues[1];
            }
            if (objectiveValues[2] < minDeviation) {
                minDeviation = objectiveValues[2];
            }
            if (objectiveValues[2] > maxDeviation) {
                maxDeviation = objectiveValues[2];
            }
        }
        Map<String, double[]> minMaxValues = new HashMap<>();
        minMaxValues.put("edgeValue", new double[]{minEdgeValue, maxEdgeValue});
        minMaxValues.put("connectivity", new double[]{minConnectivity, maxConnectivity});
        minMaxValues.put("deviation", new double[]{minDeviation, maxDeviation});

        return minMaxValues;
    }


    private double[] evaluateIndividual(Individual individual) {
        if (! individual.isEvaluated()) {
            individual.computeSegments();
            Objectives.evaluateIndividual(individual);
        }
        /*
        else {
            System.out.println("Individual did not need evaluation");
        }
         */

        return new double[]{individual.getEdgeValue(), individual.getConnectivity(), individual.getDeviation()};
    }


    private List<List<Individual>> frontsByRank(List<Individual> sortedPopulation, Comparator<Individual> comparator) {
        List<Individual> population = new ArrayList<>(sortedPopulation);
        List<List<Individual>> fronts = new ArrayList<>();

        List<Individual> front = new ArrayList<>();

        while (population.size() > 0) {
            if (isDominated(population.get(0), front, comparator)) {
                fronts.add(front);
                front = new ArrayList<>();
            }
            front.add(population.remove(0));
        }
        if (front.size() > 0) {
            fronts.add(front);
        }

        return fronts;
    }

    /*
    Returns true if individual is dominated by at least one of the individuals in front
     */
    private boolean isDominated(Individual individual, List<Individual> front, Comparator<Individual> comparator) {
        for (Individual dominator : front) {
            if (comparator.compare(dominator, individual) < 0) {
                return true;
            }
        }
        return false;
    }


    public void calculateCrowdingDistances(List<Individual> front, Map<String, double[]> minMaxValues) {
        double infinityNumber = 1000000000;
        List<Individual> frontMembers = new ArrayList<>(front);

        // Reset crowding distances
        for (Individual individual : frontMembers) {
            individual.setCrowdingDistance(0.0);
        }

        Comparator<Individual> edgeValueComparator = new EdgeValueComparator();
        Comparator<Individual> connectivityComparator = new ConnectivityComparator();
        Comparator<Individual> deviationComparator = new DeviationComparator();

        Comparator<Individual>[] comparators = new Comparator[]{edgeValueComparator, connectivityComparator, deviationComparator};
        String[] objectiveKeys = new String[]{"edgeValue", "connectivity", "deviation"};

        System.out.println("calculateCrowding");

        for (int i = 0; i < comparators.length; i++) {
            frontMembers.sort(comparators[i]);
            frontMembers.get(0).setCrowdingDistance(infinityNumber);
            frontMembers.get(frontMembers.size() - 1).setCrowdingDistance(infinityNumber);
            double increment;
            for (int index = 1; index < frontMembers.size() - 1; index++) {
                ///// Husk at edgeValueComparator sorterer stigende på edgeValue (altså fra værst til best)
                Individual next = frontMembers.get(index + 1);
                Individual prev = frontMembers.get(index - 1);
                double diff;
                if (objectiveKeys[i].equals("edgeValue")) {
                    diff = next.getEdgeValue() - prev.getEdgeValue();
                }
                else if (objectiveKeys[i].equals("connectivity")) {
                    diff = next.getConnectivity() - prev.getConnectivity();
                }
                else {
                    diff = next.getDeviation() - prev.getDeviation();
                }
                if (diff < 0) {
                    System.out.println("Diff < 0 i calculateCrowdingDistance");
                }
                double[] minMax = minMaxValues.get(objectiveKeys[i]);
                increment = diff / (minMax[1] - minMax[0]);
                frontMembers.get(index).incrementCrowdingDistance(increment);
            }
        }
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
