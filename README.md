# Multi-objective optimization with an evolutionary algorithm, applied to image segmentation

A multi-objective evolutionary algorithm (MOEA) can be used to optimize multiple objectives simultaneously. 
Here, a MOEA is applied to image segmentation by optimizing three objective values at the same time. 
The MOEA implemented here is very similar to NSGA-II [1], famous for fast non-dominated search and use of elitism.


#### Pareto front and image segmentation
The Pareto-optimal front (or just Pareto front) is the set of the Pareto-optimal solutions.
Based on the objective measures, no solution in the Pareto front can be said to better than the other.
The goal of an MOEA is to find a Pareto front close to the true Pareto front, meaning that the algorithm
will try to find many Pareto-optimal solutions in the same run.
Optimally, the Pareto front obtained should contain an even spread of solutions.
For this, an explicit diversity-preserving mechanism called Crowding Distance is used.

In the context of image segmentation, a given image can be segmented in different equally good ways.
For this reason it is a fitting problem to test a MOEA.

#### Objectives
The three objectives used in the image segmentation are
- **Edge value**:  
  ![EdgeValue](/images/EdgeValue.PNG)
- 2
- 3



## Results



### References
[1] Kalyanmoy Deb, Amrit Pratap, Sameer Agarwal and T. Meyarivan: A Fast and Elitist Multiobjective Genetic Algorithm: NSGA-II.
