/**
 * @author 	   :	ZHIKANG TIAN 
 * @student ID :	16060288
 * */


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
							)
							.outFile("AI17.txt"); 
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
				(G > 0 && P > 0 && S > 0 && M > 0 && C > 0 )
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
		
	GenerateData(int G, int P, int S, int M, int C){
		G_GenetaionsNum		= G;
		P_PopulationSize	= P;
		S_StudentNum		= S;
		M_TotalModules		= M;
		C_inCourseModules	= C;
		D_ExamDays 			= M_TotalModules / numOfSessionEachDay;
	
		// Three Big Area
		
		generate_StuTimetable();
		
		generate_ExamTimetable(numOfSessionEachDay);	
		
		costFxResult_Arr = Fx_fitness.calculate(StuTimetable, ExamTimetable);
	}
	
	// output result from memory(array) to disk(file)
	public void outFile(String fileName){
		try{
			File file = new File(fileName);	//read target file 
			if( !file.exists() ){
				System.out.println("File: "+ fileName +" does not exist!....\nCreating....\n");
				file.createNewFile();
				System.out.println("File is created now, which is on the dir:  "+ file.getAbsolutePath());
			}
			PrintWriter out = new PrintWriter(file);
			
			//*** Student Timetable Output ***
			for(int stu_index = 1; stu_index <= S_StudentNum ; stu_index++){
				out.print("Student "+ stu_index +":");
				for(int module_index = 0 ; module_index < C_inCourseModules; module_index++){
					out.print( " M" + StuTimetable[stu_index - 1][module_index] );
				}
				out.println();
			}
			out.println();
			
			//*** Exam Timetable Output ***
			for(int ord_index = 1; ord_index <= P_PopulationSize ; ord_index++){
				
				out.print("Ord "+ ord_index +":\t");
				
				for(int sessionPart = 0; sessionPart < 2 ; sessionPart ++){
				
					for(int module_index = 1 ; module_index <= D_ExamDays; module_index++){
						out.print( " m" + ExamTimetable[ord_index - 1][sessionPart][module_index - 1] );
					}
					if(sessionPart == 0) {
						out.println();
						out.print("\t");
					}
				}
				out.println(" : cost: " + costFxResult_Arr[ord_index - 1]);
			}
		
			//***END output
			out.close();
			System.out.print("File Successfully output.");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
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
 
}

class Fx_fitness{ 
	public static int[] calculate(int[][] stuTimetable, int [][][] ExamTimetable){

		int P_PopulationSize	= ExamTimetable.length;
		int SessionNumEachDay	= ExamTimetable[0].length;
		int D_ExamDays			= ExamTimetable[0][0].length;
		int []FxCostList 		= new int[ P_PopulationSize ]  ;
		
		for(int p = 0 ; p < P_PopulationSize ; p++){
			Set<Integer> thisPopulation_overlappingStuName_Set = new HashSet<Integer>(); 
			
			for(int day = 0 ; day < D_ExamDays; day++){
				
				Set<Integer> today_overlappingStuName_Set = new HashSet<Integer>();	// potential name list, but just for one day .	
				
				for(int session = 0 ; session < SessionNumEachDay ; session++){
					int ExamCode = ExamTimetable[p][session][day];
					// for each module exam , check whether it has overlapping for all student.
					checkOverlapStuNameList_withExamCode(ExamCode, stuTimetable, ExamTimetable[p],today_overlappingStuName_Set );
				}	
				thisPopulation_overlappingStuName_Set.addAll(today_overlappingStuName_Set);
			}
			FxCostList [p] = thisPopulation_overlappingStuName_Set.size();	
		}
		return FxCostList ;
	}

	private static void checkOverlapStuNameList_withExamCode(int examCode , int[][] stuTimetable, int [][] A_ExamTimetable, Set<Integer> potentialNameList) {
		
		// 'if' brance is going to generate the potential student name list 
		if( potentialNameList.isEmpty() ){		
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
	 * 				e.g. initial 2D array is  							[  [1, 2], [3, 4] ]  
	 * 					 And Random Sequence is 2 3 1 1 for example, the element array[0][0] shuold swap with the second one in array, the element array[0][1] swap with the third, and array[1][0] swap with the first (array[0][0] ) and the array[1][1] swap with the first array[0][0]
	 * 					 the sequence after shuffled should be:			[  [4, 3], [2, 1] ]  
	 */
	private static HashSet<String> shuffleIndex_generationSequence_Set = new HashSet<String>();	
		
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
}
