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
Let N be the number of pixels in the image, C be the set of all segments and C<sub>k</sub> denote a specific segment.  
The three objectives used to segment the image are
- **Edge value**:  
  The edge value is a measure of the difference in the boundary between the segments. It should be maximized and is defined as
  Edge(C) = &Sigma;<sub>i&#1013;N</sub>(&Sigma;<sub>j&#1013;F<sub>i</sub></sub> x<sub>i, j</sub>)  
  where F<sub>i</sub> indicates the 4 nearest neighbour of pixel i.
- 2 
- 3



## Results



### References
[1] Kalyanmoy Deb, Amrit Pratap, Sameer Agarwal and T. Meyarivan: A Fast and Elitist Multiobjective Genetic Algorithm: NSGA-II.
