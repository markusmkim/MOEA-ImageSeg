# A multi-objective evolutionary algorithm applied to image segmentation

A multi-objective evolutionary algorithm (MOEA) can be used to optimize multiple objectives simultaneously. 
Here, a multi-objective genetic algorithm (MOGA) is applied to image segmentation by optimizing three objective values at the same time. 
The MOGA implemented here is inspired by the work of Ripon et.al [1], and is a variant of
NSGA-II [2], famous for fast non-dominated search and use of elitism. 


#### Pareto front and image segmentation
The Pareto-optimal front (or just Pareto front) is the set of the Pareto-optimal solutions.
Based on the objective measures, no solution in the Pareto front can be said to better than the other.
The goal of an MOEA is to find a Pareto front close to the true Pareto front, meaning that the algorithm
will try to find many Pareto-optimal solutions in the same run.
Optimally, the Pareto front obtained should contain an even spread of solutions.
For this, an explicit diversity-preserving mechanism called Crowding Distance is used.

In the context of image segmentation, a given image can be segmented in different equally good ways (different number of segments).
For this reason it is a fitting problem to test a MOEA.

#### Objectives
Let N be the number of pixels in the image, C be the set of all segments and C<sub>k</sub> denote a specific segment. A pixel is always assigned to exactly one segment. 
The three objectives used to segment the image are
- **Edge value**:  
  The edge value is a measure of the difference in the boundary between the segments. It should be **maximized** and is defined as  
  *Edge(C) = &Sigma;<sub>i&#1013;N</sub>(&Sigma;<sub>j&#1013;F<sub>i</sub></sub> x<sub>i, j</sub>)*,  
  where *F<sub>i</sub>* indicates the 4 nearest neighbour of pixel, and  
  *x<sub>i, j</sub> = dist(i, j)* if pixel *i* and pixel *j* belongs to different segments, else 0.  
  The distance function *dist()* is defined as the Euclidean distance in RGB space.
- **Connectivity measure**:  
  The connectivity measure evaluates the degree to which neighbouring pixels have been placed in different segments, given by:  
  *Conn(C) = &Sigma;<sub>i&#1013;N</sub>(&Sigma;<sub>j&#1013;F<sub>i</sub></sub> x<sub>i, j</sub>)*,  
  where *F<sub>i</sub>* indicates the 8 nearest neighbour of pixel, and  
  *x<sub>i, j</sub>* = 1/8 if pixel *i* and pixel *j* belongs to different segments, else 0.  
  Connectivity measure is subject to **minimization**.
- **Overall deviation**:  
  Overall deviation measures the difference of the pixels in the same segment, and is subject to **minimization**:  
  *OD(C) = &Sigma;<sub>C<sub>k</sub>&#1013;C</sub>&Sigma;<sub>i&#1013;C<sub>k</sub></sub> dist(i, &#956;<sub>k</sub>)*,  
  where *&#956;<sub>k</sub>* is the centroid of the pixels (average pixel value) in the segment *C<sub>k</sub>*.

## Results
The images are taken from DATASET.   
For each image, the original image is shown together with 4 solutions proposed by the algorithm. 
All 4 solutions are selected from same Pareto front from a single run.


#### Image 147091
Original | 8 segmentations | 10 segmentations | 20 segmentations
------------ | ------------- | ------------- | -------------    
![original](/src/data/147091/Test%20image.jpg) | ![19](/src/results/147091/c_19-(8).png) | ![5](/src/results/147091/c_5-(10).png) | ![0](/src/results/147091/c_27-(20).png)


#### Image 118035
Original | 8 segmentations | 13 segmentations | 57 segmentations
------------ | ------------- | ------------- | -------------    
![original](/src/data/118035/Test%20image.jpg) | ![19](/src/results/118035/c_10-(8).png) | ![5](/src/results/118035/c_28-(13).png) | ![0](/src/results/118035/c_3-(57).png)


#### Image 299086
Original | 10 segmentations | 18 segmentations | 25 segmentations
------------ | ------------- | ------------- | -------------    
![original](/src/data/299086/Test%20Image.jpg) | ![19](/src/results/299086/c_3-(10).png) | ![5](/src/results/299086/c_31-(18).png) | ![0](/src/results/299086/c_11-(25).png)


#### Image 76002
Original | 12 segmentations | 14 segmentations | 20 segmentations | 26 segmentations
------------ | ------------- | ------------- | ------------- | -------------    
![original](/src/data/76002/Test%20image.jpg) | ![19](/src/results/76002/c_9-(12).png) | ![5](/src/results/76002/c_22-(14).png) | ![27](/src/results/76002/c_31-(20).png) | ![0](/src/results/76002/c_10-(26).png)


#### Image 353013
Original | 6 segmentations | 9 segmentations | 14 segmentations | 19 segmentations
------------ | ------------- | ------------- | ------------- | -------------    
![original](/src/data/353013/Test%20image.jpg) | ![19](/src/results/353013/c_49-(6).png) | ![5](/src/results/353013/c_30-(9).png) | ![27](/src/results/353013/c_26-(14).png) | ![0](/src/results/353013/c_31-(19).png)


## References
[1] Kazi Shah Nawaz Ripon, Lasker Ershad Ali, Sarfaraz Newaz and Jinwen Ma: A Multi-Objective Evolutionary Algorithm for Color Image Segmentation. 2017

[2] Kalyanmoy Deb, Amrit Pratap, Sameer Agarwal and T. Meyarivan: A Fast and Elitist Multiobjective Genetic Algorithm: NSGA-II. 2002.
