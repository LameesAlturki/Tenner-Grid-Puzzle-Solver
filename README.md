## Tenner Grid Puzzle Solver
# Overview
Tenner Grid puzzles present a fascinating challenge in combinatorial problem solving. The objective is to fill a grid with numbers while adhering to a set of constraints. This solver tackles the puzzle by modeling it as a Constraint Satisfaction Problem (CSP), utilizing different algorithms such as Backtracking, Forward Checking, and Forward Checking with MRV heuristics.
# Problem Formulation
Variables: cells represented by it's coordinate on a matrix.
Domain: the set from 0-9
Constraints:
1. Numbers appear only once in a row.
2. Numbers may be repeated in columns.
3. Numbers in the columns must add up to the given sums.
4. Numbers in contiguous cells must be different.

# Algorithms
1. Backtracking: The backtracking algorithm recursively explores possible assignments of values (from 0 to 9) to cells, ensuring that the puzzle's constraints are met.
The recursive step attempts to assign values to the current empty cell. If a value satisfies the puzzle's rules, it moves to the next cell and repeats. If no valid value is found, the algorithm backtracks, resetting the current cell to -1 and trying the next value in the previous recursive call. This continues until a solution is found, or it is determined that no solution exists.

3. Forward Checking: FC optimizes the backtracking approach by checking the validity of assignments as soon as a value is assigned to a cell; The algorithm first identifies the next empty cell. It checks each value in the domain (0-9) to see if it satisfies the puzzle's constraints, if placing a value is valid, the domains of neighboring cells are updated (i.e., removing invalid values due to the new assignment). If no valid assignment can be made, the algorithm backtracks, restoring the cell value and the affected domains. This process continues until a solution is found or no further valid assignments exist.
  
5. Forward Checking with MRV (Most Restrained Variable): This algorithm builds on Forward Checking by introducing the concept of the Most Restrained Variable (MRV). The MRV heuristic prioritizes the cell with the fewest remaining valid values in its domain by firstly finding the empty cell with the smallest domain size, it then proceeds as in Forward Checking, with the added benefit of pruning search branches more quickly by focusing on the most constrained cells first.

# Results and Analysis
It is observed that Simple Backtracking still solves the Tenner Grid problem despite being the least efficient among the three tested algorithms. Since, it requires the highest median number of consistency checks, indicating that it explores a larger portion of the search space before finding a solution. This suggests that Simple Backtracking may struggle with larger or more complex instances of the Tenner Grid problem due to its exhaustive search approach. 

Forward Checking improves upon Simple Backtracking by pruning the search space more aggressively. As a result, fewer consistency checks are required to find a solution to the Tenner Grid problem. However, it was still outperformed by Forward Checking with MRV, suggesting that there is still room for improvement in reducing the search space further.

Forward Checking with MRV stands out as the most efficient algorithm for solving the Tenner Grid problem in terms of the median number of consistency checks required. By prioritizing variables with the fewest remaining values in their domains, this algorithm effectively reduces the search space, leading to quicker convergence to a solution. This makes it particularly well suited for larger or more complex instances of the Tenner Grid problem, where efficiency is crucial.

To conclude, while all three algorithms can solve the Tenner Grid problem, Forward Checking with MRV emerges as the most efficient option, followed by Forward Checking and then Simple Backtracking. Depending on the size and complexity of the problem instance, choosing the appropriate algorithm can significantly impact the efficiency and scalability of the solution.
