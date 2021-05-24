package evaluator;


import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/*
Data wrapper class for a score from the Evaluator
 */
public class Score implements Comparable<Score> {
    private final double value;
    private final int index;


    public Score(double value, int index) {
        this.value = value;
        this.index = index;
    }

    public int getIndex()    { return index; }

    @Override
    public int compareTo(Score o) {
        return Double.compare(this.value, o.value);
    }


    public static List<Integer> findTopFiveIndexes(List<Score> scores) {
        if (scores.size() <= 5) {
            return scores.stream().map(Score::getIndex).collect(Collectors.toList());
        }

        PriorityQueue<Score> topFive = new PriorityQueue<>(scores.subList(0, 5));
        for (Score score : scores.subList(5, scores.size())) {
            assert topFive.peek() != null;
            if (score.value > topFive.peek().value) {
                topFive.poll();
                topFive.add(score);
            }
        }

        return new ArrayList<>(topFive).stream().sorted().map(Score::getIndex).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "" + this.value;
    }
}
