/**
 * Adrienne Hembrick
 * Intro to Data science
 * CMSC-435-001
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class a2 {

	/** Reformats CSV file so that it can be analyzed by the Java code */
	public static ArrayList<String[]> input(int option, String name){
		try {
			FileReader complete, oneMissing, twoMissing;
			BufferedReader csv;
			String[] file = new String[5];
			ArrayList<String[]> list = new ArrayList<String[]>();

			if(option == 1) {
				complete = new FileReader(name);
				csv = new BufferedReader(complete);	
			}
			else if(option == 2) {
				oneMissing = new FileReader("dataset_missing01.csv");
				csv = new BufferedReader(oneMissing);
			}		
			else {
				twoMissing = new FileReader("dataset_missing20.csv");
				csv = new BufferedReader(twoMissing);
			}
			String row = csv.readLine();

			while(row != null){
				file = row.split(",");
				list.add(file);
				row = csv.readLine();
			}

			csv.close();

			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException ex){	
			ex.printStackTrace();
		}
		return null;
	}

	/** Prints out the CSV file*/
	public static void output(ArrayList<String[]> text, String name) {
		try {
			File file = new File(name);
			PrintWriter write = new PrintWriter(file);
			StringBuilder lines = new StringBuilder();

			for(int i = 0; i < text.size(); i++) {
				for(int j=0; j< text.get(i).length; j++) {
					if(j < text.get(i).length-1) {
						lines.append(text.get(i)[j]);
						lines.append(",");		
					}
					else {
						lines.append(text.get(i)[j]);		
					}		
				}
				lines.append("\n");
			}
			write.write(lines.toString());
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** Prints out the MAE values for the respective file and method*/
	public static void print(String type, String name, double MAE) {
		if(type.equals("meancon")) {
			if(name.equals("dataset_missing01.csv"))
				System.out.printf("MAE_01_mean_conditional = %.5f %n", MAE/8796);
			else
				System.out.printf("MAE_20_mean_conditional = %.5f %n", MAE/8796);
		}
		else if (type.equals("mean")) {
			if(name.equals("dataset_missing01.csv"))
				System.out.printf("MAE_01_mean = %.5f %n", MAE/8796);
			else
				System.out.printf("MAE_20_mean = %.5f %n", MAE/8796);
		}
		else if(type.equals("oven")) {
			if(name.equals("dataset_missing01.csv"))
				System.out.printf("MAE_01_hd = %.5f %n", MAE/8796);
			else
				System.out.printf("MAE_20_hd = %.5f %n", MAE/8796);
		}
		else{
			if(name.equals("dataset_missing01.csv"))
				System.out.printf("MAE_01_hd_conditional = %.5f %n", MAE/8796);
			else
				System.out.printf("MAE_20_hd_conditional = %.5f %n", MAE/8796);
		}
	}

	/** Sees whether or not Binary is 'yes' or 'no for conditional imputation */
	public static boolean con(String so) {
		so = so.toUpperCase();

		if(so.equals("YES")) {
			return true;
		}
		return false;
	}

	/** Finds the absolute value of the difference between the missing value and the completed value 
	 * @param
	 * @return */
	public static double MAEBase(double missing, double complete){
		double val = missing - complete; 
		if(val < 0)
			return val * -1;
		return val;	
	}

	/** Parses the string into a double or returns one if not a number */
	public static double parse(ArrayList<String[]> text, int get, int index) {
		double value = 0;

		try {
			value = Double.parseDouble(text.get(get)[index]);
		}catch (NumberFormatException e){
			return -1;
		}
		return value;
	}

	/** Finds the shortest distance in the table conditionally*/
	public static String theLargePan(ArrayList<String[]> text, int get, int index, boolean con) {
		double shortest = 100000000;
		double d = 0;
		int n = text.get(get).length;

		for(int i = 1; i < text.size(); i++) {
			if(i!=get && !text.get(i)[index].equals("?")){
				for(int j = 0; j < text.get(i).length-1; j++) {	
					if(con) {
						if((parse(text, i, j) >= 0 || parse(text, get, j) >= 0 || (get!=i && index!=j)) && (!text.get(get)[j].equals("?") && !text.get(i)[j].equals("?"))) {
							d = MAEBase(parse(text, i, j), parse(text, get, j));
						}
						else
							n--;

						n=text.get(get).length;
						if(d/n < shortest){
							shortest = d/n;
						}
					}else{
						if((parse(text, i, j) >= 0 || parse(text, get, j) >= 0 || (get!=i && index!=j)) && (!text.get(get)[j].equals("?") && !text.get(i)[j].equals("?"))) {
							d = MAEBase(parse(text, i, j), parse(text, get, j));
						}
						else
							n--;

						n=text.get(get).length;
						if(d/n < shortest){
							shortest = d/n;
						}
					}
				}
			}
		}
		return Double.toString(shortest);	
	}

	/** Finds the shortest distance in the table */
	public static String thePan(ArrayList<String[]> text, int get, int index) {
		double shortest = 100000000;
		double d = 0;
		int n = text.get(get).length;

		for(int i = 1; i < text.size(); i++) {
			if(i!=get && !text.get(i)[index].equals("?")){
				for(int j = 0; j < text.get(i).length-1; j++) {	
					if((parse(text, i, j) >= 0 || parse(text, get, j) >= 0 || (get!=i && index!=j)) && (!text.get(get)[j].equals("?") && !text.get(i)[j].equals("?"))) {
						d = MAEBase(parse(text, i, j), parse(text, get, j));
					}
					else
						n--;
				}
				n=text.get(get).length;
				if(d/n < shortest){
					shortest = d/n;
				}
			}	
		}

		return Double.toString(shortest);
	}


	/** Calculates the mean of columns and outputs them into '?' spaces  */
	public static void mean(ArrayList<String[]> aL, ArrayList<String[]> complete, String name, String name2) {
		double MAE = 0;
		double x = 0;
		int n = aL.size();
		String term = "";

		for(int j = 0 ; j < aL.get(0).length-1; j++) {
			for(int i = 0 ; i < aL.size(); i++) {
				if(parse(aL,i,j) < 0) 
					n--;
				else
					x += parse(aL,i,j);		
			}

			x /= n;

			term = Double.toString(x);

			for(int i = 0 ; i < aL.size(); i++) {
				if (aL.get(i)[j].equals("?")) {
					aL.get(i)[j] = term;		
					MAE += MAEBase(Double.parseDouble(aL.get(i)[j]), Double.parseDouble(complete.get(i)[j]));
				}		
			}
			x=0;
			n = aL.size();

		}
		print("mean",name, MAE);	
		output(aL,name2);
	}

	/** Calculates the mean of columns condtionally and outputs them into '?' spaces  */
	public static void meanCon(ArrayList<String[]> aL, ArrayList<String[]> complete, String name, String name2) {
		double MAE = 0;
		double yes = 0;
		double no = 0;
		int y = 0;
		int n = 0;

		for(int j = 0 ; j < aL.get(1).length-1; j++) {
			for(int i = 1 ; i < aL.size(); i++) {

				if(parse(aL,i,j)< 0 && con(aL.get(i)[aL.get(i).length-1])) {
					y--;
				}
				else if(parse(aL,i,j)< 0) {
					n--;
				}	
				else if(con(aL.get(i)[aL.get(i).length-1])) {
					yes += parse(aL,i,j);
					y++;
				}
				else {
					no += parse(aL,i,j);;
					n++;
				}
			}

			yes /= y;
			no /= n;

			String term = Double.toString(yes);
			String term2 = Double.toString(yes);

			for(int i = 1 ; i < aL.size(); i++) {
				if(aL.get(i)[j].equals("?") && con(aL.get(i)[aL.get(i).length-1]) == true ) {
					aL.get(i)[j] = term;	
					MAE+= MAEBase(Double.parseDouble(aL.get(i)[j]), Double.parseDouble(complete.get(i)[j]));
				}
				else if (aL.get(i)[j].equals("?")) {
					aL.get(i)[j] = term2;	
					MAE+= MAEBase(Double.parseDouble(aL.get(i)[j]), Double.parseDouble(complete.get(i)[j]));
				}	

			}
		}
		print("meancon",name, MAE);
		output(aL,name2);
	}

	/** Calculates the hot deck of the CSV file */
	public static void hotCrossedBuns(ArrayList<String[]> aL, ArrayList<String[]> complete, String name, String name2) {
		ArrayList<String> dubs = new ArrayList<String>();
		double MAE = 0;
		int n = 0;

		for(int j = 0; j < aL.get(0).length-1; j++){
			for(int i = 0 ; i < aL.size(); i++) {
				if(aL.get(i)[j].equals("?"))
					dubs.add(thePan(aL,i,j));
			}
		}

		for(int j = 0; j < aL.get(0).length-1; j++){
			for(int i = 0 ; i < aL.size(); i++) {
				if(aL.get(i)[j].equals("?")) {
					aL.get(i)[j] = dubs.get(n);
					MAE += MAEBase(Double.parseDouble(aL.get(i)[j]), Double.parseDouble(complete.get(i)[j]));
					n++;
				}
			}
		}	
		print("oven",name, MAE);
		output(aL,name2);
	}

	/** Calculates the hot deck of the CSV file conditionally */
	public static void hotCrossedBunsCon(ArrayList<String[]> aL, ArrayList<String[]> complete, String name, String name2) {
		ArrayList<String> yah = new ArrayList<String>();
		ArrayList<String> nay = new ArrayList<String>();

		double MAE = 0;
		int n = 0;
		int y = 0;

		for(int j = 0; j < aL.get(0).length-1; j++){
			for(int i = 0 ; i < aL.size(); i++) {
				if(aL.get(i)[j].equals("?"))
					if(con(aL.get(i)[aL.get(0).length-1]))
						yah.add(theLargePan(aL,i,j,true));
					else
						nay.add(theLargePan(aL,i,j,false));
			}
		}
		for(int j = 0; j < aL.get(0).length-1; j++){
			for(int i = 0 ; i < aL.size(); i++) {
				if(aL.get(i)[j].equals("?")) {
					if(con(aL.get(i)[aL.get(0).length-1])){
						aL.get(i)[j] = yah.get(y);
						MAE += MAEBase(Double.parseDouble(aL.get(i)[j]), Double.parseDouble(complete.get(i)[j]));
						y++;
					}
					else{
						aL.get(i)[j] = nay.get(n);
						MAE += MAEBase(Double.parseDouble(aL.get(i)[j]), Double.parseDouble(complete.get(i)[j]));
						n++;


					}
				}
			}
		}	
		print("ovenCon",name, MAE);	
		output(aL,name2);
	}



	public static void main (String [] args) {
		mean(input(1,"dataset_missing01.csv"),input(1,"dataset_complete.csv"),"dataset_missing01.csv","V00866330_missing01_imputed_mean.csv");
		meanCon(input(1,"dataset_missing01.csv"),input(1,"dataset_complete.csv"),"dataset_missing01.csv","V00866330_missing01_imputed_mean_conditional.csv");
		hotCrossedBuns(input(1,"dataset_missing01.csv"),input(1,"dataset_complete.csv"),"dataset_missing01.csv","V00866330_missing01_imputed_hd.csv");
		hotCrossedBunsCon(input(1,"dataset_missing01.csv"),input(1,"dataset_complete.csv"),"dataset_missing01.csv","Vnumber_missing20_imputed_hd_conditional.csv");

		mean(input(1,"dataset_missing20.csv"),input(1,"dataset_complete.csv"),"dataset_missing20.csv","V00866330_missing20_imputed_mean.csv");
		meanCon(input(1,"dataset_missing20.csv"),input(1,"dataset_complete.csv"),"dataset_missing20.csv","V00866330_missing20_imputed_mean_conditional.csv");
		hotCrossedBuns(input(1,"dataset_missing20.csv"),input(1,"dataset_complete.csv"),"dataset_missing20.csv","V00866330_missing20_imputed_hd.csv");
		hotCrossedBunsCon(input(1,"dataset_missing20.csv"),input(1,"dataset_complete.csv"),"dataset_missing20.csv","Vnumber_missing20_imputed_hd_conditional.csv");
	}

}
