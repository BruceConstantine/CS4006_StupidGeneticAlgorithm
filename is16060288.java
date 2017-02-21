import java.util.Scanner;

public class is16060288{
	
	public static void main(String []args){
		//new ParameterSetter();
		///JUST A TEST CLASS AND GO..
	}
	
}

class ParameterSetter{
	public static void setParas(int G, int P, int S, int M, int C ){
		
		// Scanner in = new Scanner(System.in);
		// System.out.println(" G P S M C");
		// G_GenetaionsNum = in.nextInt();
		
	}
}

class GenerateData{
	
	int G_GenetaionsNum;
	int P_PopulationSize;
	int S_StudentNum;
	int M_TotalModules;
	int C_inCourseModules;
	
	int [][] StuTimetable;
	int [][] ExamTimetable;
	
	float [] costFxResult_Arr;
	
	GenerateData(){}
	
	GenerateData(int G, int P, int S, int M, int C){
		G_GenetaionsNum		= G;
		P_PopulationSize	= P;
		S_StudentNum		= S;
		M_TotalModules		= M;
		C_inCourseModules	= C;
	}
	
	public void outFile(String fileName){
		
	}
	
	private /*public*/ void generate_StuTimetable(){
		//assert( S_StudentNum != 0 && C_inCourseModules != 0 );
		//stuTimetable = new int[S_StudentNum][C_inCourseModules];
	}
	
	private /*public*/ void generate_ExamTimetable(){
		
	}
	
 
}

class Fx_fitness{
	public static void calculate(/*parameters needed*/){
		
	}
}
	
	
// POOR DESIGN
class RandomSelectionHelper{
	boolean ModuleState_Arr[] ; // The module has been chosed in table is TRUE, stand for Do NOT make this number into Account. 
	public static boolean HasBeenGenerated(){
		return ;
	}
	
	public int getAProperExam(){
		return ;
	}
	
	//The following is a poor method in name , which is same as the boolean varable array in data field.
	private int randomNumGenerate(){
		return ;
	}
}


