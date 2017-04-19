/**
 * @author 	   :	ZHIKANG TIAN 
 * @student ID :	16060288
 * */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.PrintWriter;
 
public class is16060288{
	public static void main(String []args){
		ParameterGetter.getUserInput();
		new GenerateData(	ParameterGetter.get_G(),
							ParameterGetter.get_P(), 
							ParameterGetter.get_S(), 
							ParameterGetter.get_M(), 
							ParameterGetter.get_C() 
							);
		 
		
	}
}

class ParameterGetter{
	private static int G, P, S, M, C ;
	
	public static void getUserInput(){		
		Scanner in = new Scanner(System.in);
		try{
			System.out.println(">>Enter the number of Genetations:\t");
			G = Integer.parseInt( in.nextLine() );
			System.out.println(">>Enter the Population size:\t");
			P = Integer.parseInt( in.nextLine() );
			System.out.println(">>Enter the number of Students:\t");
			S = Integer.parseInt( in.nextLine() );
			System.out.println(">>Enter The Total number of Modules:\t");
			M = Integer.parseInt( in.nextLine() );
			System.out.println(">>Enter The number of Modules in a Course:\t");
			C = Integer.parseInt( in.nextLine() );
			if( 
				(G >= 0 && P > 0 && S > 0 && M > 0 && C > 0 )
				&& ( M % 2 == 0 )
				&& ( M >= C)
			){}
			else { 
				System.out.println("***Please Notice the Following Points: ***");
				System.out.println(">> 1. Please Enter all positive Integer.");
				System.out.println(">> 2. The Total number of Modules must be an EVEN.");
				System.out.println(">> 3. The Total number of Modules should always GRETER OR EQUAL to The number of Modules in a Course.\n");
				ParameterGetter.getUserInput(); 
			}
		}
		catch(Exception e){	// if $#@ inputed.
			System.out.println("***Please Enter a positive Integer!***\n");
			ParameterGetter.getUserInput( );		
		}
		finally{
			in.close();
		}
	}
	public static int get_G(){
		return G;
	}
	public static int get_P(){
		return P;
	}
	public static int get_S(){
		return S;
	}
	public static int get_M(){
		return M;
	}
	public static int get_C(){
		return C;
	}
}

class Ordering {
	private int cost;	
	private int [][] ording_timetable;
	private int size;
	private int session = 2;
	
	public Ordering(int cost, int [][] ording_timetable){
		this.cost = cost;
		this.ording_timetable = ording_timetable ;
		this.size = ording_timetable[0].length; 
	}
	
	public int size(){
		return size;
	}
	
	public int session(){
		return session;
	}
	
	public int getCost(){
		return this.cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}

	public int [][] getOrding_timetable(){
		return this.ording_timetable;
	}
	
	public void setOrding_timetable( int[][] ording_timetable ){
		this.ording_timetable = ording_timetable;
	}
	
	public int[] getSession(int session_in){
		if(session_in == 0 || session_in == 1 )
			return ording_timetable[session_in];
		else
			return null;
	}
	
	public int get(int i, int j){ 
		return ording_timetable[i][j];
	}
}

class GenerateData{
	private int G_GenetaionsNum;
	private int P_PopulationSize;
	private int S_StudentNum;
	private int M_TotalModules;
	private int C_inCourseModules;
	private int D_ExamDays;				
	private int numOfSessionEachDay = 2;
	
	private int [][] StuTimetable;		// The first record from index ZERO 0 rather than 1 !
	private int [][][] ExamTimetable;									
	private int [] costFxResult_Arr;
	private ArrayList<Ordering> Population;
		
	GenerateData(int G, int P, int S, int M, int C){
		G_GenetaionsNum		= G;
		P_PopulationSize	= P;
		S_StudentNum		= S;
		M_TotalModules		= M;
		C_inCourseModules	= C;
		D_ExamDays 			= M_TotalModules / numOfSessionEachDay;
		Population = new ArrayList<Ordering>();
		
		//Init 
		generate_StuTimetable();
		
		generate_ExamTimetable(numOfSessionEachDay);	
		
		costFxResult_Arr = Fx_fitness.calculate(StuTimetable, ExamTimetable);
		//End Init
		
		init_Population();
		
		generateOptimalPopulation(StuTimetable);
		outputToConsole();
	}
	
	private void generate_StuTimetable(){
		StuTimetable = new int[S_StudentNum][C_inCourseModules];
		for( int i = 0; i < StuTimetable.length; i++){
			StuTimetable[i] = Algo.generate_AStuTimeTable(new int[ C_inCourseModules ],  C_inCourseModules, M_TotalModules ) ;
		}
	}
	
	private void generate_ExamTimetable(int numOfSessionEachDay){
		ExamTimetable = new int[P_PopulationSize][numOfSessionEachDay][ D_ExamDays ];
		/* * Initalize each ordering with timetable template 
		 * & Mess the order without same sequence 
		 * */
		for(int i = 0 ; i < P_PopulationSize; i++){
			int moduleCount = numOfSessionEachDay * D_ExamDays;
			// generate a ordered 2D array
			for(int row = 0 ; row < numOfSessionEachDay ; row++ ){
				for(int col = 0; col < D_ExamDays; col++){
					ExamTimetable[i][row][col] = moduleCount-- ;
				}
			}
			// mix the element ( disorder-lazation )
			ExamTimetable[i] = Algo.shuffle_2D_Arr(ExamTimetable[i], D_ExamDays, numOfSessionEachDay);	// Ordering Rationalization
		} 
	} 
	
	private void init_Population(){
		for(int i = 0 ; i < P_PopulationSize; i++){
			Population.add(
				new Ordering( costFxResult_Arr[i], ExamTimetable[i] )
			);
		}	 
		Population.trimToSize();
	}
	
	private void generateOptimalPopulation(int [][] StuTimetable){
		for(int count = 0; count < G_GenetaionsNum; count++){
			geneticAlgo.natureSelection(Population);
			geneticAlgo.geneticEvolution(Population);
			Fx_fitness.calculate(Population,StuTimetable);
			outputToConsole();
		}
	}
	
	private void outputToConsole(){ 
		System.out.println("\n*******Student timetable********");
		for(int stu_index = 1; stu_index <= S_StudentNum ; stu_index++){
			System.out.print("Student "+ stu_index +":");
			for(int module_index = 0 ; module_index < C_inCourseModules; module_index++){
				System.out.print( " M" + StuTimetable[stu_index - 1][module_index] );
			}
			System.out.println();
		}
		 
		System.out.println("\n*******Population size:"+Population.size() +"********");
 		for(int i = 0 ; i < Population.size(); i++){
			Ordering individual = Population.get(i);
			System.out.println("Ord "+ i +":\t");
			for(int j = 0 ; j < individual.session(); j++){
				for(int k = 0 ; k < individual.size(); k++){
					System.out.print(" m" + individual.get(j,k)+" ");	
				}
				System.out.println();
			}
			System.out.println( "cost = " + individual.getCost()+ "\n"); 
		}
	}  
}
 
class geneticAlgo{
		public static void natureSelection(ArrayList<Ordering> Population){
			int size = Population.size();
			Population = sortByCost( Population , 0 , size - 1 ); // quick sort in populartion size : [0,size)
			
			int eachGroupNum = size / 3; 
			if( size % 3 == 0){
				weedOutWorstGene(Population, eachGroupNum, 2 * eachGroupNum);
			}
			else if( size % 3 == 1){ // put the one in the middle group  
				weedOutWorstGene(Population, eachGroupNum, 2 * eachGroupNum + 1);
			}
			else{ // separe each one into the best one and the worest one
				weedOutWorstGene(Population, eachGroupNum + 1 , 2 * eachGroupNum + 1);
			}
		}
		
		private static void weedOutWorstGene ( ArrayList<Ordering> Population, int worstGroupLength, int startIndexForWorst ){
			int count = 1;
			while( count <= worstGroupLength){				// remove eachGroupNum times
				Population.remove(startIndexForWorst); 		// notice that 2*eachGroupNum+1 is the first element of the worst group : the middle group has one more
				count++;
			} 												// elimanate the worst gene
			Population.addAll(Population.subList(0 , worstGroupLength));	//copy the best gene into this population
		}
		
		public static void geneticEvolution(ArrayList<Ordering> Population){
			int size = Population.size(); 
			for(int i = 0; i < size; ){
				double dice = Math.random();
				 if( dice < 0.80 ){ 		//80%
//					 reproduction( Population.get(i) );
//					 do nothing
					 i++;
				 }
				 else if( dice > 0.95){		//5% 
					 mutation( Population, i );
					 System.out.println("i = "+i+" MUTATION!!!!");
					 i++;
						if(i>1)  {
							System.out.println("-------------Start-------------------------");
							Ordering individual = Population.get(i-2);
							System.out.println("Ord "+ (i-2) +":\t");
							for(int j = 0 ; j < individual.session(); j++){
								for(int k = 0 ; k < individual.size(); k++){
									System.out.print(" m" + individual.get(j,k)+" ");	
								}
								System.out.println();
							}
							System.out.println( "cost = " + individual.getCost()+ "\n");  

							individual = Population.get(i-1);
							System.out.println("Ord "+ (i-1) +":\t");
							for(int j = 0 ; j < individual.session(); j++){
								for(int k = 0 ; k < individual.size(); k++){
									System.out.print(" m" + individual.get(j,k)+" ");	
								}
								System.out.println();
							}
							System.out.println( "cost = " + individual.getCost()+ "\n"); 
							if(i!=8){
								individual = Population.get(i);
								System.out.println("Ord "+ (i) +":\t");
								for(int j = 0 ; j < individual.session(); j++){
									for(int k = 0 ; k < individual.size(); k++){
										System.out.print(" m" + individual.get(j,k)+" ");	
									}
									System.out.println();
								}
								System.out.println( "cost = " + individual.getCost()+ "\n");  
							}
							System.out.println("--------------END------------------------");
							
						}
					 
				 }
				 else{						//15%
					 if( i != size - 1){	//if it is not the last one
						 crossover( Population.get(i) , Population.get(i+1) );
						 i += 2;					 
					 }
				 }
			}
			
		}
		
//		public static void reproduction( Ordering ordering ){
//			 
//		}
		
		//..未测试的代码
		public static void mutation( ArrayList< Ordering > Population, int index ){
			 
			 int [][] orderingTimetable = Population.get(index).getOrding_timetable();
			 int days = orderingTimetable[0].length; 
			 int index_pair[] = new int[2];
			 index_pair = Algo.generateTwoDiffInt(index_pair,days);
			 int row1 = index_pair[0]/days, col1 = index_pair[0]%days;
			 int row2 = index_pair[1]/days, col2 = index_pair[1]%days;
			 int temp = orderingTimetable[row1][col1];
			 orderingTimetable[row1][col1] = orderingTimetable[row2][col2];
			 orderingTimetable[row2][col2] = temp;
			 //ordering.setOrding_timetable( orderingTimetable );
			 System.out.print("a="+index_pair[0]+" b="+index_pair[1]+"\n");
		}

		//..未测试的代码
		public static void crossover(Ordering ordering1 , Ordering ordering2 ){
			 
		}
		
		private static ArrayList<Ordering> sortByCost(ArrayList<Ordering> Population, int start, int end){
			if (start < end){   
				Ordering base = Population.get(start);    
				Ordering temp;  
				int i = start, j = end;   
				do{
					while ((Population.get(i).getCost() < base.getCost()) && (i < end))   
						i++;   
					while ((Population.get(j).getCost() > base.getCost()) && (j > start))   
						j--;   
					if (i <= j) {   
						temp = Population.get(i);   
						Population.set(i,Population.get(j));   
						Population.set(j,temp);   
						i++;   
						j--;   
					}   
				}while (i <= j);   
				if (start < j)   
					sortByCost(Population, start, j);   
				if (end > i)   
					sortByCost(Population, i, end);   
			}
			return Population;
		}
} 

class Fx_fitness{ 
	public static int[] calculate(int[][] stuTimetable, int [][][] ExamTimetable){
		int P_PopulationSize	= ExamTimetable.length;
		int SessionNumEachDay	= ExamTimetable[0].length;
		int D_ExamDays			= ExamTimetable[0][0].length;
		int []FxCostList 		= new int[ P_PopulationSize ];
		for(int p = 0 ; p < P_PopulationSize ; p++){
			int thisPopulation_overlapping_Num = 0;
			for(int day = 0 ; day < D_ExamDays; day++){
				ArrayList<Integer> today_overlappingStuName_Arr = new ArrayList<Integer>();	 // potential name list, but just for one day .
					
				for(int session = 0 ; session < SessionNumEachDay ; session++){
					int ExamCode = ExamTimetable[p][session][day];
					// for each module exam , check whether it has overlapping for all student.
					checkOverlapStuNameList_withExamCode(ExamCode, stuTimetable, ExamTimetable[p],today_overlappingStuName_Arr );
				}	
				thisPopulation_overlapping_Num += today_overlappingStuName_Arr.size();
			}
			  FxCostList [p] = thisPopulation_overlapping_Num;
		}
		return FxCostList ;
	}

	//..未测试的代码
	public static void calculate(ArrayList<Ordering> Population, int[][] stuTimetable){
		Ordering ordering = Population.get(0);
		int P_PopulationSize	= Population.size();
		int SessionNumEachDay	= ordering.session();
		int D_ExamDays			= ordering.size();
		int [][][]ExamTimetable = new int[P_PopulationSize][SessionNumEachDay][D_ExamDays];
		int []FxCostList 		= new int[ P_PopulationSize ];
		
		for(int i = 0; i < P_PopulationSize; i++){
			ExamTimetable[i] = Population.get(i).getOrding_timetable();
		}
		
		for(int p = 0 ; p < P_PopulationSize ; p++){
			int thisOrdering_overlapping_Num = 0;
			for(int day = 0 ; day < D_ExamDays; day++){
				ArrayList<Integer> today_overlappingStuName_Arr = new ArrayList<Integer>();	 // potential name list, but just for one day .
				for(int session = 0 ; session < SessionNumEachDay ; session++){
					int ExamCode = ExamTimetable[p][session][day];
					// for each module exam , check whether it has overlapping for all student.
					checkOverlapStuNameList_withExamCode(ExamCode, stuTimetable, ExamTimetable[p],today_overlappingStuName_Arr );
				}	
				thisOrdering_overlapping_Num += today_overlappingStuName_Arr.size();
			}
			  FxCostList[p] = thisOrdering_overlapping_Num;
		}
		
		for(int i = 0; i < P_PopulationSize ; i++){
			Population.get(i).setCost(FxCostList[i]);
			System.out.println(FxCostList[i]);
		}
	}
	
	private static void checkOverlapStuNameList_withExamCode(int examCode , int[][] stuTimetable, int [][] A_ExamTimetable, ArrayList<Integer> potentialNameList) {
		
		// 'if' brance is going to generate the potential student name list 
		if( potentialNameList.isEmpty() 
			&& 	stuTimetable[0].length != 1 // if each student just have ONE Module: Cost = 0 ! So -> Do not considered in my Algorithm.
			){		
			int studentName = 0;
			for(int[] A_student : stuTimetable){
				for( int stuModuleCode : A_student ){
					if( examCode == stuModuleCode ){
						potentialNameList.add( studentName );
						break;
					}
				}
				studentName++;
			}
		}
		// 'else' brance use to eliminate the student, in potential student name list, whose don't have overlapping .
		else{  		
			for(Iterator<Integer> iterator = potentialNameList.iterator()
					; iterator.hasNext()
					; ) {
				int candidateStu = iterator.next() ;
				int [] candidateStu_timetable = stuTimetable[ candidateStu ];
				boolean isOverlapping = false;
				
				for(int i = 0; i < candidateStu_timetable.length ; i++){
					if( examCode == candidateStu_timetable[i] ){
						isOverlapping = true;
						break;
					}
				}
				if( !isOverlapping )
					iterator.remove();   		
			}
		}
		return;
	}
	
}

class Algo{  
	/**
	 * @describe: 	Used for storing the generated Random number sequence -- in case of duplicated ordering.
	 * 				A sequence stand for a population ( Ordering ), beacuse the initial sequence are same ordered.
	 * 				If there is a same generated sequence , that to say, there is a same ordering. That's why using HashSet. 				
	 */
	private static HashSet<String> shuffleIndex_generationSequence_Set = new HashSet<String>();	
		
	public static void clearHashSet(){
		shuffleIndex_generationSequence_Set.clear();
	}
	/**
	 * @descibe: using hashset property: no duplicated element in set,
	 * 			 generate the module in course ( There is no two duplicated module for each student )
	 * */
	public static int [] generate_AStuTimeTable(int[] AstudentTimeTable, int C_inCourseModules, int M_TotalModules){
		HashSet<Integer> AstudentTimetable_generationSequence_Set = new HashSet<Integer>();
		
		for(int i = 0; i < C_inCourseModules; i++){
			int module = Algo.random(M_TotalModules) + 1;
			while( ! AstudentTimetable_generationSequence_Set.add(module) ){ /* if There is a duplicative module in his TT, re-generate a module */
				module = Algo.random(M_TotalModules) + 1;
			}
			AstudentTimeTable[i] = module;
		}
		return AstudentTimeTable;
	}
	
	/**
	 * @param init_Arr	: The ordered 2 dimentions array.
	 * @param colNum	: The column number of init_Arr.
	 * @param numOfSessionEachDay	: How many Exam Session does a day have.
	 * @return			: Non-redundant scrambled 2 dimentions array.
	 * @desciprtion		: shuffle 2d array with shuffle algorithm 
	 * 						-> foreach element from 0 to n-1: 
	 * 							generating a random number for the index where it should be swaped.
	 *				
	 *					 e.g. initial 2D array is  							[  [1, 2], [3, 4] ]  
	 * 					 And Random Sequence is: "2,3,1,1" for example, the element array[0][0] shuold swap with the second one in array, the element array[0][1] swap with the third, and array[1][0] swap with the first (array[0][0] ) and the array[1][1] swap with the first array[0][0]
	 * 					 the sequence after shuffled should be:			[  [4, 3], [2, 1] ]  
	 */
	public static int[][] shuffle_2D_Arr( int[][] init_Arr, int colNum, int numOfSessionEachDay ){
		int rangeBound = colNum * numOfSessionEachDay;
		StringBuffer stringBuffer = new StringBuffer( rangeBound  );
		// i stand for index rather than Module number
		for(int i = 0; i < rangeBound; i++){
			int shuffledIndex, shuffledIndex_row , shuffledIndex_col, indexRow, indexCol;  
			indexRow = i / colNum;
			indexCol = i % colNum;
			shuffledIndex = random(rangeBound);
			shuffledIndex_row =  shuffledIndex / colNum ;
			shuffledIndex_col =  shuffledIndex % colNum ;
			
			int temp										= 	init_Arr[indexRow][indexCol];
			init_Arr[indexRow][indexCol] 					= 	init_Arr[shuffledIndex_row][shuffledIndex_col];
			init_Arr[shuffledIndex_row][shuffledIndex_col] 	= 	temp;
			
			stringBuffer.append(shuffledIndex + ",");	// ',': For : in case of ambigous
		}
		if( !shuffleIndex_generationSequence_Set.add( stringBuffer.toString() ) ){ // if it is a duplicated array, regenerate it.
			return shuffle_2D_Arr( init_Arr , colNum, numOfSessionEachDay );
		}
		else return init_Arr;
	}
	
	/**
	 * @param rangeBound : The maximum range bound excluded.
	 * @return [0, rangeBound-1]
	 */
	public static int random(int rangeBound){ 
		return (int)( Math.random() * rangeBound );
	}	
	public static int[] generateTwoDiffInt(int[] pair, int range){ 
		if(pair == null || pair.length < 2 )
			pair = new int[2];
		pair[0] = random(range);
		pair[1] = random(range);
		if(pair[0] == pair[1])
			pair = generateTwoDiffInt(pair, range);
		else
			return pair;
		return pair;
	}
}
