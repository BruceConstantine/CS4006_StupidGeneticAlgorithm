import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

/*import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
*/

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
		///JUST A TEST CLASS AND GO..
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
		}
		catch(Exception e){	// if $#@ inputed.
			//e.printStackTrace();
			System.out.println("***Please Enter a positive Integer!***\n");
			ParameterGetter.getUserInput( );		// BUG When Ctrl+C.
		}
		finally{
			if( G > 0 && P > 0 && S > 0 && M > 0 && C > 0 ) {}
			else { 
				System.out.println("***Please Enter a positive Integer!***\n");
				ParameterGetter.getUserInput(); 
			}
			in.close();
		}
	}
	//*** getter method***//
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
	
	int G_GenetaionsNum;
	int P_PopulationSize;
	int S_StudentNum;
	int M_TotalModules;
	int C_inCourseModules;
	int D_ExamDays;				/* What if total Modules is an odd? SEE GenerateData Constructor*/
	
	int [][] StuTimetable;		// The first record from index ZERO 0 rather than 1 !
	int [][][] ExamTimetable;		
								
	float [] costFxResult_Arr;
		
	GenerateData(int G, int P, int S, int M, int C){
		G_GenetaionsNum		= G;
		P_PopulationSize	= P;
		S_StudentNum		= S;
		M_TotalModules		= M;
		C_inCourseModules	= C;
		
		//### D_ExamDays!!!
		if( M_TotalModules % 2 == 0 )
			D_ExamDays = M_TotalModules/2;
		else
			D_ExamDays = M_TotalModules/2 + 1;
		//### END
		
		generate_StuTimetable();
		generate_ExamTimetable();
		
	}
	
	public void outFile(String fileName){
		try{
			File file = new File(fileName);	//read target file 
			if( !file.exists() ){
				System.out.println("File: "+ fileName +" does not exist!....\nCreating....\n");
				file.createNewFile();
				System.out.println("File is created now, which is on the dir:  "+ file.getAbsolutePath());
			}
			//BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream(file));
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
				
				out.print("Ord "+ ord_index +":");
				
				for(int sessionPart = 0; sessionPart < 2 ; sessionPart ++){
				
					for(int module_index = 1 ; module_index <= D_ExamDays; module_index++){
						out.print( " m" + ExamTimetable[ord_index - 1][sessionPart][module_index - 1] );
					}

					out.println();
				}

				out.println(" : cost: " + costFxResult_Arr[ord_index - 1]);
			}
			
			//***END output
			
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//finally ?
	}
	
	private /*public*/ void generate_StuTimetable(){
		StuTimetable = new int[S_StudentNum][C_inCourseModules];
		int index = 0;
		while( index < S_StudentNum){
			StuTimetable[index] = Algo.generate_AStuTimeTable( C_inCourseModules, M_TotalModules ) ;
			index++;
		}
	}
	
	private /*public*/ void generate_ExamTimetable(){
		ExamTimetable = new int[P_PopulationSize][2][ D_ExamDays ];
	}
	
 
}

class Fx_fitness{
	public static void calculate(/*parameters needed*/){
		
	}
}


// POOR DESIGN
class Algo{
	boolean ModuleState_Arr[] ; // The module has been chosed in table is TRUE, stand for Do NOT make this number into Account. 
	public static boolean HasBeenGenerated(){
		return true;
	}
	
	public static int [] generate_AStuTimeTable(int C_inCourseModules, int M_TotalModules){
		return null;
	}
	
	public int getAProperExam(){
		return 1;
	}
	
	//The following is a poor method in name , which is same as the boolean varable array in data field.
	private int randomNumGenerate(){
		return 1;
	}
}
