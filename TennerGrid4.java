import java.util.*;
public class TennerGrid4 {
   private static int ROW = 4;
   private static int COLUMN = 10;
   private static int consistencyChecks;
   private static int assignments;
   private static boolean found;
   private static boolean[][][] domains;
   private static long startTime,endTime,elapsedTimeMRV,elapsedTimeB,elapsedTimeFC;

   public static int[][] createGrid() {
      int[][] grid = new int[4][10];
   
        // fill first three rows with unique random numbers in each row
      for (int row = 0; row < 3; row++)
         fillRow(grid, row);
                 // Calculate and assign column sums
      calculateColumnSums(grid);
        // Remove numbers to create the puzzle
      removeNumbers(grid);
      return grid;
   }
   
   private static void removeNumbers(int[][] grid) {
      Random random = new Random();
   
      for (int row = 0; row < 3; row++) {
         int countToRemove = random.nextInt(10); // Number of elements to be removed from row, has to keep 1 left at least
         for (int i = 0; i < countToRemove; i++){
            int col;
            do{
               col = random.nextInt(10);
            }while (grid[row][col] == -1); // Ensures we're not removing already replaced numbers
            grid[row][col] = -1;
         }
      }
   }
   
   private static void fillRow(int[][] grid, int row) {
      boolean[] usedNumbers = new boolean[10];
      for (int col = 0; col < 10; col++){
         int num;
         do{
            num = (int) (Math.random() * 10);
         }while(usedNumbers[num] ||
                    (col > 0 && grid[row][col - 1] == num) ||
                    (row > 0 && grid[row - 1][col] == num) ||
                    (col > 0 && row > 0 && grid[row - 1][col - 1] == num) ||
                    (col < 9 && row > 0 && grid[row - 1][col + 1] == num));
         grid[row][col] = num;
         usedNumbers[num] = true;
      }
   }
   
   private static void calculateColumnSums(int[][] grid) {
      for (int col = 0; col < 10; col++) {
         int sum = 0;
         for (int row = 0; row < 4; row++) {
            sum += grid[row][col];
         }
         grid[3][col] = sum;
      }
   }
   //This method checks the constrains that the Tenner grid has      
   public static boolean doesRulesApply (int [][] grid, int row, int column, int value) {
      //Checks for the constrain "Number appear only once in a row"
      for (int i =0; i<COLUMN; i++) {
         consistencyChecks++;
         if (grid[row][i] == value) 
            return false;
      }
      //Checks for the constrain "Numbers in connecting cells must be different"
      if (row == 0){ //first row checking
         if (column == 0) { 
            if (grid[row+1][column] == value || grid[row+1][column+1] == value)  {
               consistencyChecks++; 
               return false;
            }
         }
         else if((column+1) == COLUMN){
            if (grid[row+1][column-1] == value || grid[row+1][column] == value)  {
               consistencyChecks++; 
               return false;
            }
         }
         else {
            if (grid[row+1][column+1] == value || grid[row+1][column] == value || grid[row+1][column-1] == value){
               consistencyChecks++; 
               return false;
            }
         }        
      } 
      else if((row+2) == ROW){//last row checking
         if (column == 0)   {
            if (grid[row-1][column] == value ||grid[row-1][column+1] == value){
               consistencyChecks++; 
               return false;
            }
         }
         else if ((column+1) == COLUMN){
            if (grid[row-1][column] == value || grid[row-1][column-1] == value){
               consistencyChecks++; 
               return false;
            }
         }
         else {
            if (grid[row-1][column] == value || grid[row-1][column-1] == value || grid[row-1][column+1] == value){
               consistencyChecks++; 
               return false;
            }
         }
      }
      else{ //midle row checking
         if (column == 0){
            if (grid[row-1][column] == value || grid[row-1][column+1] == value ||
               grid[row+1][column] == value || grid[row+1][column+1] == value){
               consistencyChecks++; 
               return false;
            }
         }
         else if((column+1) == COLUMN){
            if (grid[row-1][column-1] == value || grid[row-1][column] == value ||
               grid[row+1][column-1] == value || grid[row+1][column] == value){
               consistencyChecks++; 
               return false;
            }
         }
         else{
            if(grid[row-1][column-1] == value || grid[row-1][column] == value || grid[row-1][column+1] == value || 
               grid[row+1][column-1] == value || grid[row+1][column] == value || grid[row+1][column+1] == value)  {
               consistencyChecks++; 
               return false;
            }
         }
      } 
      //Checks for the constrain "Numbers in columns must add up to the given sums"      
      int sum = 0;
      for (int i = 0; i <ROW-1; i++){
         if(grid[i][column]!=-1)
            sum += grid[i][column];
      }
      consistencyChecks++;
      sum += value;
      if (sum > grid[ROW -1][column])
         return false;
      if (row == ROW -2 && sum != grid[ROW -1][column])
         return false;
      return true;
   }
   //printing the Tenner grid
   public static void print(int[][] grid) {
      for (int i = 0; i < ROW; i++) {
         System.out.print("|");
         for (int j = 0; j < COLUMN; j++)
            System.out.printf( "%2d ", grid[i][j]);
         System.out.println("|");
      }
   
   }
   //Simple back tracking method 
   public static boolean simpleBacktracking(int[][] grid, int row, int column) {
      startTime = System.nanoTime();
      //base case: if we reached the last row and the last column
      if (row == ROW-1 && column == COLUMN )
         return true;
      //if we reached the last column so we should move on to the next row
      if (column == COLUMN)   {
         column = 0;
         row++ ;
      }
      //if the grid at this location is already filled
      if (grid [row][column] != -1)
         return simpleBacktracking(grid, row, column+1);
      //trying all possible allocations and checking the constrains as well  
      for (int value =0 ; value<=9; value++)  {
         if ( doesRulesApply (grid, row, column, value)) {
            grid[row][column] = value;
            assignments++;
            if (simpleBacktracking(grid, row, column+1)){
               endTime = System.nanoTime();
               elapsedTimeB = endTime - startTime;
               return true;
            }
         }
         grid[row][column] =-1;
         assignments++;   
      }
      endTime = System.nanoTime();
      elapsedTimeB = endTime - startTime;      
      return false;
         
   }
   //initialize Domains for the forward checking
   static void initializeDomains(boolean[][][] grid) {
      int rows = ROW-1;
      int cols = COLUMN;
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
            for (int num = 0; num < 10; num++) {
               grid[i][j][num] = true; // Initially, all numbers are valid options (for now)
            }
         }
      }
   }
   ////Forward checking method 
   public static boolean ForwardChecking(int[][] grid, boolean[][][] domains) {
      startTime = System.nanoTime();
    
      int row = -1, col = -1;
      boolean isEmpty = true;
    
      // Find an empty cell
      for (int i = 0; i < ROW; i++) {
         for (int j = 0; j < COLUMN; j++) {
            if (grid[i][j] == -1) {
               row = i;
               col = j;
               isEmpty = false;
               break;
            }
         }
         if (!isEmpty) 
            break;
      }
      // If there is no empty cell, the grid is solved
      if (isEmpty){
         endTime = System.nanoTime();
         elapsedTimeFC = endTime - startTime; 
         return true;
      }
      // Try filling the empty cell with valid numbers from domain
      for (int num = 0; num <= 9; num++) { 
         if (domains[row][col][num] && doesRulesApply(grid, row, col, num)) {
            grid[row][col] = num;
            assignments++;
            forwardCheckdomain(grid, domains, row, col, num, false);
         
            if (ForwardChecking(grid, domains)) {
               endTime = System.nanoTime();
               elapsedTimeFC = endTime - startTime;
               return true;
            }
         
            grid[row][col] = -1; // no solution found so backtrack
            assignments++;
            forwardCheckdomain(grid, domains, row, col, num, true);
         }
      }
    
      return false;
   } 
   
   static void safeUpdate(int r, int c, int n, boolean updatee) {
      if (r >= 0 && r < ROW && c >= 0 && c < COLUMN) {
         domains[r][c][n] = updatee;
      }
   }
   /*update the domain's array(eliminating the assigned 
   value from the possible options for related cells)*/
   static void forwardCheckdomain(int[][] grid, boolean[][][] domains, int row, int col, int num,boolean update) {
   // Update all columns in the given row for the specified number
      for (int j = 0; j < COLUMN; j++) {
         domains[row][j][num] = update;
      }
   // update the cell diagonally top left from the current cell
      safeUpdate(row - 1, col - 1, num, update);
   
   // top right from current cell
      safeUpdate(row - 1, col + 1, num, update);
   
   // bottom left from the current cell
      safeUpdate(row + 1, col - 1, num, update);
   
   // bottom right from current cell
      safeUpdate(row + 1, col + 1, num, update);
   
   // update the what's directly above the current cell
      safeUpdate(row - 1, col, num, update);
   
   // update the cell directly below the current cell
      safeUpdate(row + 1, col, num, update);
   
   }
   
   public static boolean ForwardCheckingMRV(int[][] grid, boolean[][][] domains) {
      startTime = System.nanoTime();
    // Find the unassigned variable with the fewest remaining values (MRV)
      int row = -1, col = -1;
      int minRemainingValues = Integer.MAX_VALUE;
      for (int i = 0; i < ROW; i++) {
         for (int j = 0; j < COLUMN; j++) {
            if (grid[i][j] == -1) {
               int countRemainingValues = 0;
               for (int num = 0; num <= 9; num++) {
                  if (domains[i][j][num]) {
                     countRemainingValues++;
                  }
               }
               if (countRemainingValues < minRemainingValues) {
                  minRemainingValues = countRemainingValues;
                  row = i;
                  col = j;
               }
            }
         }
      }
   
    // If there is no empty cell, the grid is solved
      if (row == -1 && col == -1) {
         endTime = System.nanoTime();
         elapsedTimeMRV = endTime - startTime;
         return true;
      }
   
    // Try filling the empty cell with valid numbers from domain
      for (int num = 0; num <= 9; num++) {
         if (domains[row][col][num] && doesRulesApply(grid, row, col, num)) {
            grid[row][col] = num;
            assignments++;
            forwardCheckdomain(grid, domains, row, col, num, false);
         
            if (ForwardCheckingMRV(grid, domains)) {
               endTime = System.nanoTime();
               elapsedTimeMRV = endTime - startTime;
               return true;
            }
         
            grid[row][col] = -1; // no solution found so backtrack
            assignments++;
            forwardCheckdomain(grid, domains, row, col, num, true);
         }
      }
      return false;
   }

   //To reset counters for the next allocation
   static void resetCounters(){
      consistencyChecks = 0;
      assignments = 0;
      startTime=0;
      endTime=0;
   }

   public static void main(String[] args) {
      
      int[][] samples = createGrid() ;
     
      int[][] Fc = new int[samples.length][];
      int[][] Bt = new int[samples.length][];
      int[][] FcMRV = new int[samples.length][];
      
    
      // Copy contents of grid for each algorithm 
      for (int i = 0; i < samples.length; i++) {
         Fc[i] = Arrays.copyOf(samples[i], samples[i].length);
         Bt[i] = Arrays.copyOf(samples[i], samples[i].length);
         FcMRV[i] = Arrays.copyOf(samples[i], samples[i].length);
      }
            
      resetCounters();
      System.out.println("\n ---------Initial State---------");
      print(samples);
      
      if (simpleBacktracking(Bt, 0, 0)) {
         System.out.println("\n ~~~~~~~~~Backtracking~~~~~~~~~");
         System.out.println("\n ---------Final State---------");
         print(Bt);
         System.out.println("consistency: " + consistencyChecks);
         System.out.println("assignments: " + assignments);
         System.out.println("Time Used: " + ((double)elapsedTimeB/1000000) + " milliseconds");
      }
      else
         System.out.println("No Solution exists"); 
             
      resetCounters();
      
      domains = new boolean[ROW][COLUMN][10]; // 3D array for domains
      initializeDomains(domains);
      
      if (ForwardChecking(Fc, domains)) {
         System.out.println("\n ~~~~~~~~ForwardChecking~~~~~~~~");
         System.out.println("\n ---------Final State---------");
         print(Fc);
         System.out.println("consistency: " + consistencyChecks);
         System.out.println("assignments: " + assignments);
         System.out.println("Time Used: " + ((double)elapsedTimeFC/1000000) + " milliseconds");
      }
      else
         System.out.println("No Solution exists");
                  
      resetCounters();
      initializeDomains(domains);
   
      if (ForwardCheckingMRV(FcMRV, domains)) {
         System.out.println("\n ~~~~~~ForwardCheckingMRV~~~~~~");
         System.out.println("\n ---------Final State---------");
         print(FcMRV);
         System.out.println("consistency: " + consistencyChecks);
         System.out.println("assignments: " + assignments);
         System.out.println("Time Used: " + ((double)elapsedTimeMRV/1000000) + " milliseconds");
      }
      else
         System.out.println("No Solution exists");
   }

}
