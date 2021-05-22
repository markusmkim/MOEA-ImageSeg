package EA;

import EA.Components.Comparators.*;
import EA.Components.Individual;
import EA.Operations.Crossover;
import EA.Operations.Mutation;
import EA.Operations.NonDominatedRanking;
import EA.Operations.Selection;

import java.util.*;
import java.util.stream.Collectors;


/*
NSGA-II
 */
public class MOEA {

    private final int populationSize;
    private final int generations;
    private final Crossover crossover;
    private final Mutation mutation;
    private final int minSeg;
    private final int maxSeg;


    public MOEA(int populationSize, int generations, Crossover crossover, Mutation mutation, int minSeg, int maxSeg) {
        this.populationSize = populationSize;
        this.generations = generations;
        this.crossover = crossover;
        this.mutation = mutation;
        this.minSeg = minSeg;
        this.maxSeg = maxSeg;
    }


    /*
    Runs NSGA-II for multi-objective optimization
     */
    public List<Individual> run(List<Individual> population) {
        NonDominatedRanking ranker = new NonDominatedRanking(new NonDominatedObjectivesComparator(this.minSeg, this.maxSeg));

        this.evaluatePopulation(population);                                                                            //  Evaluate population

        List<List<Individual>> fronts = ranker.sort(population);                                                        //  Rank by non-dominated sorting

        System.out.println("" + fronts.size());
        for (List<Individual> front : fronts) {
            System.out.println("" + front.size());
        }

        List<Integer> nFrontsHistory = new ArrayList<>();
        this.printGenerationStats(0, population, nFrontsHistory);


        for (int i = 1; i <= this.generations; i++) {                                                                   //  For every generation
            List<Individual> children = new ArrayList<>(this.populationSize);
            while (children.size() < this.populationSize) {                                                             //    While number of children < population size
                Individual[] parentPair = Selection.tournamentSelection(population);                                    //      Select parents

                Individual[] offspring = this.crossover.apply(parentPair[0], parentPair[1]);                            //      Recombine parents and create offspring

                Individual mutatedOffspring1 = this.mutation.applySingleBitMutation(offspring[0]);                      //      Mutate offspring
                Individual mutatedOffspring2 = this.mutation.applySingleBitMutation(offspring[1]);

                children.addAll(Arrays.asList(mutatedOffspring1, mutatedOffspring2));                                   //      Children = children + offspring
            }
            population.addAll(children);                                                                                //    Population = population + children
            Map<String, double[]> minMaxValues = this.evaluatePopulation(population);                                   //    Evaluate population

            fronts = ranker.sort(population);                                                                           //    Rank by non-dominated sorting

            nFrontsHistory.add(fronts.size());

            List<Individual> survivors;
            if (i == this.generations) {
                survivors = fronts.get(0);
            }
            else {
                survivors = new ArrayList<>();
                int frontIndex = 0;                                                                                     // Front = first front (best front)
                boolean addNextFrontToSurvivors = fronts.get(frontIndex).size() <= this.populationSize;
                while (addNextFrontToSurvivors) {                                                                       //    While Front can be added to survivors without growing larger than population size
                    survivors.addAll(fronts.get(frontIndex));                                                           //      Add Front to survivors
                    frontIndex++;                                                                                       //      Front = next front

                    if (frontIndex < fronts.size()) {
                        if (fronts.get(frontIndex).size() + survivors.size() > this.populationSize) {
                            addNextFrontToSurvivors = false;
                        }
                    }
                    else {
                        addNextFrontToSurvivors = false;
                    }
                }

                if (survivors.size() < this.populationSize) {
                    List<Individual> frontToDifferentiate = fronts.get(frontIndex);                                     //  Only some members of current Front can survive
                    this.calculateCrowdingDistances(frontToDifferentiate, minMaxValues);                                //  Select survivors based on crowding distances
                    frontToDifferentiate.sort(new CrowdingDistanceComparator());
                    while (survivors.size() < this.populationSize) {
                        survivors.add(frontToDifferentiate.remove(0));
                    }
                }
            }

            population = survivors;

            if (i % 5 == 0) {
                this.printGenerationStats(i, population, nFrontsHistory);
                this.printAllSegmentations(population);
                nFrontsHistory.clear();
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
        return new double[]{individual.getEdgeValue(), individual.getConnectivity(), individual.getDeviation()};
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

        for (int i = 0; i < comparators.length; i++) {
            frontMembers.sort(comparators[i]);
            frontMembers.get(0).setCrowdingDistance(infinityNumber);
            frontMembers.get(frontMembers.size() - 1).setCrowdingDistance(infinityNumber);
            double increment;
            for (int index = 1; index < frontMembers.size() - 1; index++) {
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


    private void printAllSegmentations(List<Individual> population) {
        List<Integer> segmentations = population.stream().map(i -> i.getSegmentsRGBCentroids().size()).collect(Collectors.toList());
        System.out.println("Segmentations: " + segmentations);
    }


    private void printGenerationStats(int generation, List<Individual> population, List<Integer> nFrontsHistory) {
        String space = generation < 100 ? generation < 10 ? "       " : "      " : "     ";
        String gen = generation > 0 ? "Generation " + generation + space : "Initial generation ";

        double sumEdgeValue = 0.0;
        double sumConnectivity = 0.0;
        double sumDeviation = 0.0;

        for (Individual individual : population) {
            double edgeValue = individual.getEdgeValue();
            double connectivity = individual.getConnectivity();
            double deviation = individual.getDeviation();
            sumEdgeValue += edgeValue;
            sumConnectivity += connectivity;
            sumDeviation += deviation;
        }

        double avgEdgeValue = sumEdgeValue / population.size();
        double avgConnectivity = sumConnectivity / population.size();
        double avgDeviation = sumDeviation / population.size();

        System.out.println(gen +
                "  | Population size: " + Utils.formatValue(population.size()) +
                "  |  Avg ev: " + Utils.formatValue(avgEdgeValue) +
                "  |  Avg cm: " + Utils.formatValue(avgConnectivity) +
                "  |  Avg dev: " + Utils.formatValue(avgDeviation) +
                "  |||  Fronts history: " + nFrontsHistory
        );
    }
}
