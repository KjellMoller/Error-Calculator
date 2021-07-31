package codes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Calculator {
	
	public static String equation = "";
	public static String[] operators = new String[21];
	public static String[] equationPlace = new String[21];
	public static String[] values = new String[21];
	public static double[] Values = new double[21];
	public static String[] uncertainties = new String[21];
	public static double[] Uncertainties = new double[21];
	
	public static String fileStream() throws Exception{
		
		//Read Equation From File
		File inputEquation = new File("Equation");
		Scanner fileReader = new Scanner(inputEquation);
		String equation = "";
		int lines = 0;
		while(fileReader.hasNextLine())
		{
		fileReader.nextLine();
		lines++;
		}
		equation = Files.readAllLines(Paths.get("Equation")).get(lines-1);
		fileReader.close();
		return(equation);
		
	}
	
	public static void splitEquation(String S2){

		//Find Strings Before And After Operator and Operator
		for(int x = 0; x < 21; x++) {
			equationPlace[x] = null;
			operators[x] = null;
			Values[x] = 0.0;
			Uncertainties[x] = 0.0;
			uncertainties[x] = null;
			values[x] = null;
		}
		int x = 0;
		int y = 0;
		boolean operatorFound = false;
		
		while(x < S2.length()) {
			operatorFound = false;
			
			if(x == 0 && S2.charAt(x) >= '0' && S2.charAt(x) <= '9') {
				int t = x;
				equationPlace[y] = "";
				while(S2.charAt(t) != ')') {
					equationPlace[y] += S2.charAt(t);
					t++;
				}
				y++;
				operatorFound = false;
			}			
			
		if(S2.charAt(x) == '+' || (S2.charAt(x) == '-' && (S2.charAt(x+1) <= '0' || S2.charAt(x+1) >= '9'))|| S2.charAt(x) == '*' || S2.charAt(x) == '/' || S2.charAt(x) == '^') {
			operators[y] = "";
			operators[y] += S2.charAt(x);	
			y++;
			operatorFound = true;
		}
		else if((S2.charAt(x) == ')' && S2.charAt(x-1) == ' ') || (S2.charAt(x) == '(' && S2.charAt(x+1) != '±')) {
			operators[y] = "";
			operators[y] += S2.charAt(x);
			y++;
			operatorFound = true;
		}
		
		if(operatorFound == true)
		{

			int q = x+1;
			while(q < S2.length() && S2.charAt(q) == ' ')
				q++;
			
			if(S2.charAt(q) >= '0' && S2.charAt(q) <= '9' ) {
				equationPlace[y] = "";
				do{
        			equationPlace[y]+=S2.charAt(q);
        			q++;
        		}while(S2.charAt(q) != ')');
        		equationPlace[y]+=S2.charAt(q);
        		y++;
			}
			if(S2.charAt(q) == '-' && S2.charAt(q+1) >= '0' && S2.charAt(q+1) <= '9') {
				equationPlace[y] = "";
				do{
        			equationPlace[y]+=S2.charAt(q);
        			q++;
        		}while(S2.charAt(q) != ')');
        		equationPlace[y]+=S2.charAt(q);
        		y++;
			}
			operatorFound = false;
		}
		
			x++;
		}
		/*
		for(int j = 0; j < 21; j++) {
			System.out.println(equationPlace[j]);
		}
		
		System.out.println();
		
		for(int j = 0; j < 21; j++) {
			System.out.println(operators[j]);
		}
		
		System.out.println();
		*/
		
		
		//Convert to uncertainties and values
		for(int k = 0; k < 21; k++) {
			int r = 0;
		if(equationPlace[k] == null) {
			continue;
		}
		else if(equationPlace[k] != null)
				values[k] = "";
				uncertainties[k] = "";
				
				for(int l = 0; l < equationPlace[k].length(); l++)
				{
					if(equationPlace[k].charAt(l) == '±')
						break;
					r++;
				}
			
			for(int b = 0; b < r; b++)
			{
				values[k] += equationPlace[k].charAt(b);
			}
			for(int c = r+1; c < equationPlace[k].length(); c++)
			{
				uncertainties[k] += equationPlace[k].charAt(c);
			}
			
		}
		
		String useful = "";
		for(int j = 0; j < 21; j++) {
			if(values[j] != null)
			{
				useful = "";
				for(int c = 0; c < values[j].length(); c++)
				{
					if(values[j].charAt(c) == ' ' || values[j].charAt(c) == '(')
						break;
					useful += values[j].charAt(c);
				}
			Values[j] = Double.parseDouble(useful);
			}
			if(uncertainties[j] != null)
			{
				useful = "";
				for(int c = 0; c < uncertainties[j].length(); c++)
				{
					 if(uncertainties[j].charAt(c) == ' ' || uncertainties[j].charAt(c) == ')')
                         continue;
					useful += uncertainties[j].charAt(c);
				}
				
				Uncertainties[j] = Double.parseDouble(useful);
			}
			
			//System.out.println(values[j]);
		}
		/*
		System.out.println();
		
		for(int j = 0; j < 21; j++) {

				System.out.println(Uncertainties[j]);
		}
		System.out.println();
		for(int j = 0; j < 21; j++) {
				System.out.println(Values[j]);
		}
		*/
	}
	
	public static String solve(String equation1) {
		splitEquation(equation1);
		int answerSolved = 0;
		boolean operationComplete = false;
		String answer = null;
		Integer openBracket = (Integer)null; 
		Integer closeBracket = (Integer)null;
		String eqnAnswer = "";
		String valuesPlace = "";
		String uncertaintiesPlace = "";
		boolean change = false;
		boolean operatorTrue = false;
		int operationAt = 0;
		int completionCounter = 0;
		int bracketCount = 0;
		
		do
		{
			operationComplete = false;
			operationAt = 0;
			completionCounter = 0;
			int q = 0;
			
			//Powers
			for(int t = 0; t < 21; t++)
			{
				if(operators[t] == null)
					continue;
				if(operators[t].charAt(0) == '^')
					operatorTrue = true;
				operationAt = t;
			}
			if(operationComplete == false && operatorTrue == true) {
				for(int a = 0; a < 21; a++) {
					if(operators[a] == null)
						continue;
					if(operators[a].charAt(0) == '^') {
					q = a-1;
					eqnAnswer = power(Values[a-1],Values[a+1],Uncertainties[a-1]);
					break;
					}
					}
				
				if(eqnAnswer != null) {
				int r = 0;
				
					valuesPlace = "";
					uncertaintiesPlace = "";
					
					for(int l = 0; l < eqnAnswer.length(); l++)
					{
						if(eqnAnswer.charAt(l) == ' ')
							break;
						r++;
					}
				
				for(int b = 0; b < r; b++)
				{
					valuesPlace += eqnAnswer.charAt(b);
				}
				for(int c = r+1; c < eqnAnswer.length(); c++)
				{
					uncertaintiesPlace += eqnAnswer.charAt(c);
				}
				Values[q] = Double.parseDouble(valuesPlace);
				Uncertainties[q] = Double.parseDouble(uncertaintiesPlace);
				Values[q+2] = 0;
				Uncertainties[q+2] = 0;
				operators[q+1] = null;
				eqnAnswer = null;
				}
				operationComplete = true;
				}
			operatorTrue = false;
			
			//Division
			for(int t = 0; t < 21; t++)
			{
				if(operators[t] == null)
					continue;
				if(operators[t].charAt(0) == '/')
					operatorTrue = true;
				operationAt = t;
			}
			if(operationComplete == false && operatorTrue == true) {
				for(int a = 0; a < 21; a++) {
					if(operators[a] == null)
						continue;
					if(operators[a].charAt(0) == '/') {
					q = a-1;
					eqnAnswer = divide(Values[a-1],Values[a+1],Uncertainties[a-1],Uncertainties[a+1]);
					break;
					}
					}
				
				if(eqnAnswer != null) {
				int r = 0;
				
					valuesPlace = "";
					uncertaintiesPlace = "";
					
					for(int l = 0; l < eqnAnswer.length(); l++)
					{
						if(eqnAnswer.charAt(l) == ' ')
							break;
						r++;
					}
				
				for(int b = 0; b < r; b++)
				{
					valuesPlace += eqnAnswer.charAt(b);
				}
				for(int c = r+1; c < eqnAnswer.length(); c++)
				{
					uncertaintiesPlace += eqnAnswer.charAt(c);
				}
				Values[q] = Double.parseDouble(valuesPlace);
				Uncertainties[q] = Double.parseDouble(uncertaintiesPlace);
				Values[q+2] = 0;
				Uncertainties[q+2] = 0;
				operators[q+1] = null;
				eqnAnswer = null;
				}
				operationComplete = true;
				}
			operatorTrue = false;
			
			//Multiplication
			for(int t = 0; t < 21; t++)
			{
				if(operators[t] == null)
					continue;
				if(operators[t].charAt(0) == '*')
					operatorTrue = true;
				operationAt = t;
			}
			if(operationComplete == false && operatorTrue == true) {
				for(int a = 0; a < 21; a++) {
					if(operators[a] == null)
						continue;
					if(operators[a].charAt(0) == '*') {
					q = a-1;
					eqnAnswer = multiply(Values[a-1],Values[a+1],Uncertainties[a-1],Uncertainties[a+1]);
					break;
					}
					}
				
				if(eqnAnswer != null) {
				int r = 0;
				
					valuesPlace = "";
					uncertaintiesPlace = "";
					
					for(int l = 0; l < eqnAnswer.length(); l++)
					{
						if(eqnAnswer.charAt(l) == ' ')
							break;
						r++;
					}
				
				for(int b = 0; b < r; b++)
				{
					valuesPlace += eqnAnswer.charAt(b);
				}
				for(int c = r+1; c < eqnAnswer.length(); c++)
				{
					uncertaintiesPlace += eqnAnswer.charAt(c);
				}

				Values[q] = Double.parseDouble(valuesPlace);
				Uncertainties[q] = Double.parseDouble(uncertaintiesPlace);
				Values[q+2] = 0;
				Uncertainties[q+2] = 0;
				operators[q+1] = null;
				eqnAnswer = null;
				}
				operationComplete = true;
				}
			operatorTrue = false;
			
			//Subtraction
			for(int t = 0; t < 21; t++)
			{
				if(operators[t] == null)
					continue;
				if(operators[t].charAt(0) == '-')
					operatorTrue = true;
				operationAt = t;
			}
			if(operationComplete == false && operatorTrue == true) {
				for(int a = 0; a < 21; a++) {
					if(operators[a] == null)
						continue;
					if(operators[a].charAt(0) == '-') {
					q = a-1;
					eqnAnswer = subtract(Values[a-1],Values[a+1],Uncertainties[a-1],Uncertainties[a+1]);
					break;
					}
					}
				
				if(eqnAnswer != null) {
				int r = 0;
				
					valuesPlace = "";
					uncertaintiesPlace = "";
					
					for(int l = 0; l < eqnAnswer.length(); l++)
					{
						if(eqnAnswer.charAt(l) == ' ')
							break;
						r++;
					}
				
				for(int b = 0; b < r; b++)
				{
					valuesPlace += eqnAnswer.charAt(b);
				}
				for(int c = r+1; c < eqnAnswer.length(); c++)
				{
					uncertaintiesPlace += eqnAnswer.charAt(c);
				}
				Values[q] = Double.parseDouble(valuesPlace);
				Uncertainties[q] = Double.parseDouble(uncertaintiesPlace);
				Values[q+2] = 0;
				Uncertainties[q+2] = 0;
				operators[q+1] = null;
				eqnAnswer = null;
				}
				operationComplete = true;
				}
			operatorTrue = false;
			
			//Addition
			for(int t = 0; t < 21; t++)
			{
				if(operators[t] == null)
					continue;
				if(operators[t].charAt(0) == '+')
					operatorTrue = true;
				operationAt = t;
			}
			if(operationComplete == false && operatorTrue == true) {
			for(int a = 0; a < 21; a++) {
				if(operators[a] == null)
					continue;
				if(operators[a].charAt(0) == '+') {
				q = a-1;
				eqnAnswer = add(Values[a-1],Values[a+1],Uncertainties[a-1],Uncertainties[a+1]);
				break;
				}
				}
			
			if(eqnAnswer != null) {
			int r = 0;
			
				valuesPlace = "";
				uncertaintiesPlace = "";
				
				for(int l = 0; l < eqnAnswer.length(); l++)
				{
					if(eqnAnswer.charAt(l) == ' ')
						break;
					r++;
				}
			
			for(int b = 0; b < r; b++)
			{
				valuesPlace += eqnAnswer.charAt(b);
			}
			for(int c = r+1; c < eqnAnswer.length(); c++)
			{
				uncertaintiesPlace += eqnAnswer.charAt(c);
			}
			Values[q] = Double.parseDouble(valuesPlace);
			Uncertainties[q] = Double.parseDouble(uncertaintiesPlace);
			Values[q+2] = 0;
			Uncertainties[q+2] = 0;
			operators[q+1] = null;
			eqnAnswer = null;
			}
			operationComplete = true;
			}
			operatorTrue = false;
		/*
			for(int a = 0; a < 21; a++)
				System.out.println(operators[a]);
			System.out.println();
				for(int a = 0; a < 21; a++)
				System.out.println(Uncertainties[a]);
				System.out.println();
				for(int a = 0; a < 21; a++)
				System.out.println(Values[a]);
				System.out.println();
		*/
				//Condensing
				if(operationAt >1) {
				for(int b = operationAt-2; b < 19; b++)
				{
					operators[b] = operators[b+2];
					Values[b] = Values[b+2];
					Uncertainties[b] = Uncertainties[b+2];
				}
				operators[19] = null;
				operators[20] = null;
				Values[19] = 0;
				Values[20] = 0;
				Uncertainties[19] = 0;
				Uncertainties[20] = 0;
				}
			/*	
			for(int a = 0; a < 21; a++)
				System.out.println(operators[a]);
			System.out.println();
			for(int a = 0; a < 21; a++)
				System.out.println(Values[a]);
			System.out.println();
			for(int a = 0; a < 21; a++)
				System.out.println(Uncertainties[a]);
		*/
			
			
			for(int b = 0; b < 21; b++)
			{
				//System.out.println(Values[b]);
				if(Values[b] != 0) {
					completionCounter++;
				}
			}
			if(completionCounter == 1) {
			answerSolved +=1;
			}
			
		}while(answerSolved == 0);
		
		//System.out.println(Values[0] + " (±" + Uncertainties[0] + ")");
		equation1 = (Values[0] + " (±" + Uncertainties[0] + ")");
		return equation1;
	}
	
	public static String add(double number1, double number2, double uncertainty1, double uncertainty2) {

		String answer = "";
		double valueAnswer = number1+number2;
		double uncertaintyTotal;
		answer = Double.toString(valueAnswer);
		answer += ' ';
		
		String useful = "";
		uncertaintyTotal = (uncertainty1*number1) + (uncertainty2*number2);
		if(uncertaintyTotal < 0)
			uncertaintyTotal*=-1;
		useful = Double.toString(uncertaintyTotal);
		
		for(int a = 0; a < useful.length(); a++)
			answer += useful.charAt(a);
			
		return answer;
		
	}
	
	public static String subtract(double number1, double number2, double uncertainty1, double uncertainty2) {

		String answer = "";
		double valueAnswer = number1-number2;
		double uncertaintyTotal;
		answer = Double.toString(valueAnswer);
		answer += ' ';
		
		String useful = "";
		uncertaintyTotal = (uncertainty1*number1) + (uncertainty2*number2);
		if(uncertaintyTotal < 0)
			uncertaintyTotal*=-1;
		useful = Double.toString(uncertaintyTotal);
		
		for(int a = 0; a < useful.length(); a++)
			answer += useful.charAt(a);
			
		return answer;
		
	}
	
	public static String multiply(double number1, double number2, double uncertainty1, double uncertainty2) {

		String answer = "";
		double valueAnswer = number1*number2;
		double percentUncertainty1;
		double percentUncertainty2;
		double percentUncertaintyTotal;
		answer = Double.toString(valueAnswer);
		answer += ' ';
		
		String useful = "";
		percentUncertainty1 = uncertainty1/number1;
		percentUncertainty2 = uncertainty2/number2;
		percentUncertaintyTotal = percentUncertainty1 + percentUncertainty2;
		double uncertaintyAnswer = percentUncertaintyTotal * valueAnswer;
		if(uncertaintyAnswer < 0)
			uncertaintyAnswer*=-1;
		useful = Double.toString(uncertaintyAnswer);
		for(int a = 0; a < useful.length(); a++)
			answer += useful.charAt(a);
			
		return answer;
		
	}
	
	public static String divide(double number1, double number2, double uncertainty1, double uncertainty2) {

		String answer = "";
		double valueAnswer = number1/number2;
		double percentUncertainty1;
		double percentUncertainty2;
		double percentUncertaintyTotal;
		answer = Double.toString(valueAnswer);
		answer += ' ';
		
		String useful = "";
		percentUncertainty1 = uncertainty1/number1;
		percentUncertainty2 = uncertainty2/number2;
		percentUncertaintyTotal = percentUncertainty1 + percentUncertainty2;
		double uncertaintyAnswer = percentUncertaintyTotal * valueAnswer;
		if(uncertaintyAnswer < 0)
			uncertaintyAnswer*=-1;
		useful = Double.toString(uncertaintyAnswer);
		for(int a = 0; a < useful.length(); a++)
			answer += useful.charAt(a);
			
		return answer;
		
	}
	
	public static String power(double number1, double number2, double uncertainty1) {
		
		String answer = "";
		double valueAnswer = Math.pow(number1, number2); 
		double percentUncertainty1;
		answer = Double.toString(valueAnswer);
		answer += ' ';
		
		String useful = "";
		percentUncertainty1 = uncertainty1/number1;
		double uncertaintyAnswer = percentUncertainty1 * number2;
		uncertaintyAnswer*=valueAnswer;
		useful = Double.toString(uncertaintyAnswer);
		for(int a = 0; a < useful.length(); a++)
			answer += useful.charAt(a);
			
		return answer;

	}
	
	
	public static void main(String[] args) throws Exception {
		equation = (fileStream());
		//System.out.println();
		String newEquation = "";
		newEquation = equation;
		System.out.println(newEquation);
		int finito = 0;
		String answer = "";
		boolean solved = false;
		do {
			//System.out.println(newEquation);
			splitEquation(newEquation);
			
		//Find innermost brackets
		Integer[] openBracket = new Integer[21];
		Integer[] closedBracket = new Integer[21];
		
		for(int a = 0; a < 21; a++) {
			openBracket[a] = (Integer)null;
			closedBracket[a] = (Integer)null;
		}
		int stillBracket = 0;
		
		for(int a = 0; a < 21; a++) {
			if(operators[a] != null && operators[a].charAt(0) == ')')
				closedBracket[a] = a;
			if(operators[a] != null && operators[a].charAt(0) == '(') {
				openBracket[a] = a;
				stillBracket++;
			}
			//System.out.println(openBracket[a]);
			//System.out.println(closedBracket[a]);
		}
		
		//System.out.println(stillBracket);
		if(stillBracket > 0) {
			//System.out.println("test");
			int counter = 0;
			int stringBegin = 0;
			int stringEnd = 0;
			for(int x = 0; x < 21; x++) {
				if(operators[x] != null && operators[x].charAt(0) == '(')
				{
					counter++; 
					if(counter == stillBracket) {
						stringBegin = x;
						for(int y = x; y < 21; y++) {
							if(operators[y] != null && operators[y].charAt(0) == ')')
								stringEnd = y;
						}
					}
				}
			}
			
			System.out.println(stringBegin + " " + stringEnd);
			
			String S1 = "";
			String S2 = "";
			String S3 = "";
			
			for(int x = 0; x < stringBegin; x++) {
				if(Values[x] != 0) {
					S1 += (Values[x] + " (±" + Uncertainties[x] + ") ");
				}
				if(operators[x] != null) {
					S1 += (operators[x] + " ");
				}
			}
			for(int x = stringBegin+1; x < stringEnd; x++) {
				if(Values[x] != 0) {
					S2 += (Values[x] + " (±" + Uncertainties[x] + ") ");
				}
				if(operators[x] != null) {
					S2 += (operators[x] + " ");
				}
			}
			for(int x = stringEnd+1; x < 21; x++) {
				if(Values[x] != 0) {
					S3 += (Values[x] + " (±" + Uncertainties[x] + ") ");
				}
				if(operators[x] != null) {
					S3 += (" " + operators[x] + " ");
				}
			}
			String useful = solve(S2);
			newEquation = "";
			newEquation += S1;
			newEquation += useful;
			newEquation+= S3;
			for(int a = 0; a < 21; a++) {
				equationPlace[a] = null;
				operators[a] = null;
				Values[a] = 0.0;
				Uncertainties[a] = 0.0;
			}

			System.out.println(newEquation);
		}
		else{
			finito = 0;
			solve(newEquation);
			for(int x = 0; x < 21; x++) {
				if(Values[x] != 0) {
					finito++;
				}
			}
			if(finito ==1) {
				solved = true;
				answer = (Values[0] + " (±" + Uncertainties[0] + ")");
			}
		}
		
		}while(solved == false);
		
		System.out.println(answer);
		
	}
	
}
