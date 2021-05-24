

public class Config {

    static final String FILEPATH = "data/42044/Test image.jpg";

    static final boolean RUN_MOEA = true;

    static final int POPULATION_SIZE = 40; // 40
    static final int GENERATIONS = 80; // 60
    static final double CROSSOVER_RATE = 0.9;
    static final double MUTATION_RATE = 0.8;

    static final int MIN_SEG = 6;
    static final int MAX_SEG = 13;


    // GA
    static final double[] FITNESS_WEIGHTS = new double[]{0.3, 0.4, 0.3};  // [edgeValue, connectivity, deviation]



    // Filepaths for results
    static final String optimalBlackWhitePath = "C:/Users/marku/kode/MOEA-ImageSeg/src/results/optimal/blackWhite/";
    static final String studentBlackWhitePath = "C:/Users/marku/kode/MOEA-ImageSeg/src/results/student/blackWhite/";
    static final String optimalColorPath = "C:/Users/marku/kode/MOEA-ImageSeg/src/results/optimal/color/";
    static final String studentColorPath = "C:/Users/marku/kode/MOEA-ImageSeg/src/results/student/color/";
}
