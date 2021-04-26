

public class Config {

    static final String FILEPATH = "data/86016/Test image.jpg";

    static final int POPULATION_SIZE = 40;
    static final int GENERATIONS = 40;
    static final double CROSSOVER_RATE = 0.8;
    static final double MUTATION_RATE = 0.8;


    // GA
    static final double[] FITNESS_WEIGHTS = new double[]{0.24, 0.52, 0.24};  // [edgeValue, connectivity, deviation]
}
