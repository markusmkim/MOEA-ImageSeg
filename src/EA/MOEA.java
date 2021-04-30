package EA;

import EA.Components.Comparators.*;
import EA.Components.Individual;
import EA.Operations.Crossover;
import EA.Operations.Mutation;
import EA.Operations.Selection;

import java.util.*;
import java.util.stream.Collectors;


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


    public List<Individual> run(List<Individual> population) {
        NonDominatedObjectivesComparator nonDominatedObjectivesComparator = new NonDominatedObjectivesComparator(this.minSeg, this.maxSeg);
        this.evaluatePopulation(population);

        population.sort(nonDominatedObjectivesComparator);

        List<List<Individual>> fronts = frontsByRank(population, nonDominatedObjectivesComparator);

        System.out.println("hei");
        System.out.println("" + fronts.size());
        for (List<Individual> front : fronts) {
            System.out.println("" + front.size());
        }

        List<Integer> nFrontsHistory = new ArrayList<>();
        this.printGenerationStats(0, population, nFrontsHistory);

        List<Individual> oldPopulation = new ArrayList<>(population);

        for (int i = 1; i <= this.generations; i++) {
            List<Individual> parents = new ArrayList<>(this.populationSize);
            while (parents.size() < this.populationSize) {
                Individual[] parentPair = Selection.tournamentSelection(population);

                Individual[] offspring = this.crossover.apply(parentPair[0], parentPair[1]);

                Individual mutatedOffspring1 = this.mutation.applySingleBitMutation(offspring[0]);
                Individual mutatedOffspring2 = this.mutation.applySingleBitMutation(offspring[1]);

                parents.addAll(Arrays.asList(mutatedOffspring1, mutatedOffspring2));
            }
            population.addAll(parents);
            Map<String, double[]> minMaxValues = this.evaluatePopulation(population);
            try {
                population.sort(nonDominatedObjectivesComparator);
            } catch (Exception e) {
                System.out.println("Sorting feil");
                population = oldPopulation;
            }

            fronts = frontsByRank(population, nonDominatedObjectivesComparator);
            // System.out.println("hei");
            //System.out.println("" + fronts.size());
            nFrontsHistory.add(fronts.size());

            List<Individual> survivors;
            if (i == this.generations) {
                survivors = fronts.get(0);
            }
            else {
                survivors = new ArrayList<>();
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

                if (survivors.size() < this.populationSize) {
                    List<Individual> frontToDifferentiate = fronts.get(frontIndex);
                    this.calculateCrowdingDistances(frontToDifferentiate, minMaxValues);
                    frontToDifferentiate.sort(new CrowdingDistanceComparator());
                    while (survivors.size() < this.populationSize) {
                        survivors.add(frontToDifferentiate.remove(0));
                    }
                }
            }


            // System.out.println("dudu - " + survivors.size());

            population = survivors;

            oldPopulation = new ArrayList<>(population);

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

        int rankCounter = 1;

        List<Individual> front = new ArrayList<>();

        while (population.size() > 0) {
            if (isDominated(population.get(0), front, comparator)) {
                fronts.add(front);
                front = new ArrayList<>();
                rankCounter += 1;
            }
            Individual next = population.remove(0);
            next.setRank(rankCounter);
            front.add(next);
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

        // System.out.println("calculateCrowding");

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

        // System.out.println("--------------------------------------------------------------------------------");
    }

    private void printAllSegmentations(List<Individual> population) {
        List<Integer> segmentations = population.stream().map(i -> i.getSegmentsRGBCentroids().size()).collect(Collectors.toList());
        System.out.println("Segmentations: " + segmentations);
    }



    private void printGenerationStats(int generation, List<Individual> population, List<Integer> nFrontsHistory) {
        String space = generation < 100 ? generation < 10 ? "       " : "      " : "     ";
        String gen = generation > 0 ? "Generation " + generation + space : "Initial generation ";

        double bestEdgeValue = 0.0;
        double bestConnectivity = 1000000000.0;
        double bestDeviation = 1000000000.0;

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

            if (edgeValue > bestEdgeValue) {
                bestEdgeValue = edgeValue;
            }
            if (connectivity < bestConnectivity) {
                bestConnectivity = connectivity;
            }
            if (deviation < bestDeviation) {
                bestDeviation = deviation;
            }
        }

        double avgEdgeValue = sumEdgeValue / population.size();
        double avgConnectivity = sumConnectivity / population.size();
        double avgDeviation = sumDeviation / population.size();

        System.out.println(gen +
                "  | Population size: " + Utils.formatValue(population.size()) +
                "  ||  Best ev: " + Utils.formatValue(bestEdgeValue) +
                "  |  Avg ev: " + Utils.formatValue(avgEdgeValue) +
                "  ||  Best cm: " + Utils.formatValue(bestConnectivity) +
                "  |  Avg cm: " + Utils.formatValue(avgConnectivity) +
                "  ||  Best dev: " + Utils.formatValue(bestDeviation) +
                "  |  Avg dev: " + Utils.formatValue(avgDeviation) +
                "  |||  Fronts history: " + nFrontsHistory
        );
    }
}
