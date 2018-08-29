//Author: Aidan Campbell
//Description: ISU, plays a game of "Deal or no Deal"
//Date Created: 06/09/2017
//Date Revised: 06/20/2017

import java.util.ArrayList;//Importing necessary classes for game mechanics, GUI, and files
import java.util.Random;
import java.text.NumberFormat;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class DealOrNoDealCAM

{

   public static void main(String[] args)
   
   {
   
      double[] briefcases = new double[26];//Stores value of each of the 26 briefcases
      double[] impValues = new double[6];//Stores values needed for multiple methods(since an array does not require a return statement)
		String[] display = new String[26];//Stores what the moneyboard will display on the JFrameDisplay JFrame
			
      ArrayList<Double> amounts = new ArrayList<Double>();//Stores amounts of each briefcase in ascending order; used to assign values to each briefcase randomly
      amounts.add(0.01);amounts.add(1.00);amounts.add(5.00);amounts.add(10.00);amounts.add(25.00);amounts.add(50.00);amounts.add(75.00);amounts.add(100.00);amounts.add(200.00);amounts.add(300.00);amounts.add(400.00);amounts.add(500.00);amounts.add(750.00);
      amounts.add(1000.00);amounts.add(5000.00);amounts.add(10000.00);amounts.add(25000.00);amounts.add(50000.00);amounts.add(75000.00);amounts.add(100000.00);amounts.add(200000.00);amounts.add(300000.00);amounts.add(400000.00);amounts.add(500000.00);amounts.add(750000.00);amounts.add(1000000.00);
      
		for(int i = 0; i < 26; i++)//Specifies what each value of the display array will show
		{
         double value = amounts.get(i);
			if(value == 1000000.00)//I used multiple if - else - ifs because different char combos result in the values not being aligned on the JFrame
			{
				display[i] = String.format("%-1s %20s", "$", (int)value);//In the show, all of the values, except 0.01, are shown without decimal places
			}
         else if(i == 0)
         {
            display[i] = String.format("%-1s %23s", "$", value);
         }
         else if(value < 1000 && value >= 100)
         {
            display[i] = String.format("%-1s %23s", "$", (int)value);
         }
         else if(value < 100 && value >= 10)
         {
            display[i] = String.format("%-1s %24s", "$", (int)value);
         }
         else if(value < 10)
         {
            display[i] = String.format("%-1s %25s", "$", (int)value);
         }
         else if(value < 100000 && value >= 10000)
         {
            display[i] = String.format("%-1s %22s", "$", (int)value);
         }
         else if(value < 10000)
         {
            display[i] = String.format("%-1s %23s", "$", (int)value);
         }
         else
         {
            display[i] = String.format("%-1s %21s", "$", (int)value);
         }
         
		}
			
      Random ran = new Random();//random is used to assign values to each case
      boolean deal = false;//Stores if the user has made a deal with the banker
      int temp = 0;
      
      for(int i = 0; i < 26; i++)//Assigns a value randomly picked from the arraylist to each briefcase 
      {
        temp = ran.nextInt(26 - i);//generates a random int from 0 to 1 less than the amount of briefcases left to assign
        briefcases[i] = amounts.get(temp);//gets value of that random index
        amounts.remove(temp);//removing an element ensures that no 2 cases will have the same amount
      }
      
      //Beginning the game    
      JOptionPane.showMessageDialog(null, "Welcome to Deal or No Deal!\nBegin the game by picking a\nbriefcase to keep.\nYou can choose to sell this briefcase\nto the banker or keep\nthe amount of money inside.");
      temp = JFrameDisplay(briefcases, impValues, display);//Asks user for briefcase choice
      impValues[0] = temp;//Setting the briefcase number and value to the impValues array as both values are needed throughtout the program
      impValues[1] = briefcases[temp - 1];
      briefcases[temp - 1] = 0;//Setting the briefcase value to 0 informs that this case should not be displayed
      JOptionPane.showMessageDialog(null, "Your personal case is case number " + (int)impValues[0] +".");//Informing the user of his/her choice
		      
      while(deal != true)//As long as a deal has not been reached, the game will continue
      {
         if(impValues[4] != 9)//A player can only select briefcases a certain amount of times before the end of the game
         {
            deal = ManageCasesDeals(briefcases, impValues, display);//Goes to method that controls the user's choice of cases to open and deals
         }
         else
         {
            deal = EndGame(briefcases, impValues, display);//Goes to method where the user can switch or keep cases
         } 
      }
      
      HighScores(impValues);//After the game is over, goes to method displaying and entering high scores
      JOptionPane.showMessageDialog(null, "Thanks for playing.");//Closing message
      System.exit(0);//Closing JOptionPane
   }
   
   /**
   *Manages the selecting briefcases and deal offers (JOptionPane dialog, necessary calculations and method invocations)
   *pre: impValues[4] != 9, deal != true, impValues[0] > 0 && <= 26
   *post: returns true or false 
   */
   
   public static boolean ManageCasesDeals(double[] cases, double[] values, String[] board)
   
   {
   
      NumberFormat money = NumberFormat.getCurrencyInstance();//To format money values
      Random ran = new Random();
      
      double total = values[1];//total is used in the calculation of the average; the average includes the user's case as its value is still in play
      double bankOffer = 0;//bankOffer is the bankers offer to buy the user's case; 
      int casesLeft = 1;//cases remaining in play (includes user's case)
      values[5] = 0;//Stores the bankdeal; is initialized as 0 to specify that a deal has not been accepted/made (which controls the JFrame layout)
      
      int userPicks = 6 - (int)values[4];//amount of cases a user can choose this round; values[4] stores the amount of rounds that have past
      if(userPicks < 1)
      {
         userPicks = 1;//user picks cannot be less than 1 per round
      }
      
      JOptionPane.showMessageDialog(null, "You can open " + userPicks + " briefcase(s).");//Informs user of how many cases he/she can open
      
      for(int i = 0; i < userPicks; i++)//for the amount of cases that can be open this round
      {
         
         int choice = JFrameDisplay(cases, values, board);//Goes to method where the user clicks on briefcases using JFrame
         
         JOptionPane.showMessageDialog(null, "Briefcase " + choice + " contains " + money.format(cases[choice - 1]) + ".");//Informs user of case choice value
			for(int p = 0; p < 26; p++)//for loop finds the case choice value corresponding with the board value to delete it (since it is out of play)
			{
				if(board[p] != " ")//A space signifies that a value is no longer in play
				{
					String temp = board[p];//Modifying the string to just get the number part
					temp = temp.substring(2);
					temp = temp.trim();
					if(cases[choice - 1] == 0.01)//the 0.01 is a double while the other values are ints on the board
					{
						board[0] = " ";
						p = 26;//ends for loop as only one value changes per case choice
					}
					else if(p > 0 && (int)cases[choice - 1] == Integer.parseInt(temp))
					{
						board[p] = " ";
						p = 26;
					}
				}
			}
		
         cases[choice - 1] = 0;//Signifies that this case has been chosen
      }
      
      for(int i = 0; i < 26; i++)
      {
         if(cases[i] > 0)
         {
            total += cases[i];//Calculates total of remaining values
            casesLeft += 1;//Calculates remaining cases
            values[2] = (int)(i + 1);//values[2] is the case number of the final case to be opened
         }
      }
      
      JOptionPane.showMessageDialog(null, "The Banker is calling...");//Creates suspense...
  
      //Calculates a bank offer based off of average of remaining values
      if(values[4] >= 8)//when few values remain, the normal calculation can produce weird results (ex: a deal lower than the lowest amount left)
      {
         bankOffer = ((total/(double)casesLeft)*0.9);
      }
      else//regular calculation; deals start off smaller earlier in the game
      {
          bankOffer = ((total/(double)casesLeft)*((double)ran.nextInt(501)/10000 + 0.80))  - ((((double)(9 - values[4])/(double)15))*(total/(double)casesLeft));
      }
         
      values[5] = bankOffer;//stores bank offer in array as if user accepts, it will be sent to the high scores method; values[5] is the user's winnings
      
      int reaction = JFrameDisplay(cases, values, board);//Goes to JFrame method and asks if deal or no deal (returns 1 for deal; 0 for no deal)
      
      values[4] += 1;//Signifies that another round of picking cases + deals has passed
      
      if(values[4] == 9)//There are 9 rounds of picking cases
      {
         values[3] = total - values[1];//values[3] is the value of the final case
      }
            
      if(reaction != 1)
      {
         return(false);//states that a deal has not been reached
      }
      else if(bankOffer >= values[1])//States that a deal has been reached and displays winnings; displays appropriate message based on if the deal is higher than the value in the original briefcase
      {
         JOptionPane.showMessageDialog(null, "Congratulations, you won " + money.format(bankOffer) + ".\nYour case, number " + (int)values[0] + ", contained " + money.format(values[1]) + ".");
         return(true);//Ends game since deal has been reached
      }
      else
      {
         JOptionPane.showMessageDialog(null, "You won " + money.format(bankOffer) + ".\nUnfortunately, your case, number " + (int)values[0] + ", contained " + money.format(values[1]) + ".");
         return(true);
      }
    
   }
   
   /**
   * Manages the end of a game and the choice of switch/keep (JOptionPane dialog)
   * pre: deal != true, impValues[4] == 9, impValues[1] > 0, impValues[3] > 0, impValues[0] > 0 && <= 26, impValues[2] > 0 && <= 26
   * post: returns true
   */
   
   public static boolean EndGame(double[] amounts, double[] important, String[] boArd)
   
   {
   
      NumberFormat money = NumberFormat.getCurrencyInstance();
      //Informing user of option to switch or keep cases
      JOptionPane.showMessageDialog(null, "Since there are only two cases left,\nyou have the option to switch your\ncase with the one remaining or \nkeep your original case. \nYou will take home the amount of money \nin the case you choose.");
      int choice = JFrameDisplay(amounts, important, boArd);//Goes to method for user to click on frame; 1 is to switch cases, 0 is to stay with the original
            
      if(choice == 1 && important[3] < important[1])//Shows the user his/her winnings; displays appropriate message based on which case contains more money
      {
         JOptionPane.showMessageDialog(null, "The case you choose, case " + (int)important[2] + ", contained " + money.format(important[3]) + ".\nUnfortunately, your original case, case " + (int)important[0] + ", contained " + money.format(important[1]) + ".");
         important[5] = important[3];//important[5] is used for the highscore; the value of the case is the user's score
      }
      else if(choice == 1)
      {
         JOptionPane.showMessageDialog(null, "You choose correctly!\nYou won " + money.format(important[3]) + ".\nYour original case, case " + (int)important[0] + ", contained " + money.format(important[1]) + ".");
         important[5] = important[3];
      }
      else if(choice == 0 && important[1] < important[3])
      { 
         JOptionPane.showMessageDialog(null, "Your original case, case " + (int)important[0] + ", contained " + money.format(important[1]) + ".\nUnfortunately, the other case, case " + (int)important[2] + ", contained " + money.format(important[3]) + ".");
         important[5] = important[1];
      }
      else if(choice == 0)
      {
         JOptionPane.showMessageDialog(null, "You choose correctly!\nYou won " + money.format(important[1]) + ".\nThe other case, case " + (int)important[2] + ", contained " + money.format(important[3]) + ".");
         important[5] = important[1];
      }
      
      return(true);//A deal has not been reached but it notifies the main method that the game is over
      
   }
   
   /*
   * Reads and writes to/from a file containing the highscores; displays updated highscores in a JTable or creates new highscore list
   * pre: deal == true, impValues[5] > 0
   * post: writes to file the updated high scores list
   */
   
	public static void HighScores(double[] imPortant)
	
	{
      File highScores = new File("1HighScores.txt");//Creates object that refers to the file containing the highscores
      FileReader in;//Declaring objects necessary for file reading
      BufferedReader readFile;
      FileWriter out;//Declaring objects necessary for file writing
      BufferedWriter writeFile;
      String lineOfText;//Used in reading the file
      JFrame frame = new JFrame();//Creates new JFrame to display the highscores
      Color gold = Color.decode("#FFDF00");//Gets the colour gold
      NumberFormat money = NumberFormat.getCurrencyInstance();
      int places[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};//array that stores the placement (1st, etc.) of each score; necessary in displaying a tie
      int ties = 0;//used to keep the same placement for tied scores
      
      String[] names = new String[16];//String array used for storing and sorting high score names; text file stores 15 scores but 16 elements are necessary to include the new user name
      double[] scores = new double[16];//Double array used for storing and sorting high score scores
      
      try
      {
			if(highScores.exists())//If the highscores file cannot be found, program will create a new list
			{
        		in = new FileReader(highScores);//Opens reading streams
         	readFile = new BufferedReader(in);
				for(int i = 0; i < 15; i++)//Reads a maximum of 15 names and scores
				{
					lineOfText = readFile.readLine();//reads a name
           		names[i] = lineOfText;
               if(lineOfText != null)//If less than 15 scores are present, the program will need to stop reading at a point
               {
            	   lineOfText = readFile.readLine();//reads a score
            	   scores[i] = Double.parseDouble(lineOfText);
               }
         	
				}
         	readFile.close();//Closes reading streams
         	in.close();
			}
			else
			{
				highScores.createNewFile();//creates a new highscore file
			}
      } catch(FileNotFoundException e) {//catches possible exceptions
      } catch(IOException e) {}
      
      scores[15] = imPortant[5];//declares last element as user's score
      
      if(scores[15] >= scores[14])//Informs user that they got a highscore if they beat or tied the 15th place score
      {
         JOptionPane.showMessageDialog(null, "Congratulations! Your score is in the top 15 all time scores!");
         String name = JOptionPane.showInputDialog(null, "Name", "Please Enter Your Name", JOptionPane.QUESTION_MESSAGE);//Asks for high score name
         names[15] = name;//declares last element as user's name
      }
       
      if(scores[14] == scores[15])//if scores 15 ties scores 14, scores 15 is placed in the highscores list
      {
         scores[14] = 0;
      } 
       
      for(int a = 0; a < 15; a++)//Sorts highscores in descending order using bubble sort
      {
         for(int b = 0; b < 15; b++)
         {
            if(scores[b] < scores[b + 1])
            {
               double temp = scores[b];
               scores[b] = scores[b + 1];
               scores[b + 1] = temp;
               String tempS = names[b];//Since the names are associated with the scores, if a score switches its respective name must also switch
               names[b] = names[b + 1];
               names[b + 1] = tempS;
            }
            else if(a == 14 && scores[b] == scores[b + 1])//last iteration of loop (everything is essentially in order)
            {
               ties += 1;
               places[b + 1] = places[b];//the next placement is the same as the previous in a tie
            }
         }
      }
        
      JOptionPane.showMessageDialog(null, "Top 15 Highscores: ");//Informs user that the top 10 highscores will be displayed
      
      //Formulates JTable with all highscores in descending order of score
      String[][] data = {{String.valueOf(places[0]),names[0], money.format(scores[0])},{String.valueOf(places[1]),names[1], money.format(scores[1])},{String.valueOf(places[2]),names[2], money.format(scores[2])},{String.valueOf(places[3]),names[3], money.format(scores[3])},{String.valueOf(places[4]),names[4], money.format(scores[4])},
                        {String.valueOf(places[5]),names[5], money.format(scores[5])},{String.valueOf(places[6]),names[6], money.format(scores[6])},{String.valueOf(places[7]),names[7], money.format(scores[7])},{String.valueOf(places[8]),names[8], money.format(scores[8])},{String.valueOf(places[9]),names[9], money.format(scores[9])},
                        {String.valueOf(places[10]),names[10], money.format(scores[10])},{String.valueOf(places[11]),names[11], money.format(scores[11])},{String.valueOf(places[12]),names[12], money.format(scores[12])},{String.valueOf(places[13]),names[13], money.format(scores[13])},{String.valueOf(places[14]),names[14], money.format(scores[14])}};
      String[] column = {"Ranking", "Name", "Winnings"};
      JTable t = new JTable(data,column);
      t.setBounds(0,0,750,450);//Setting boundaries of the table
      t.setRowHeight(30);//Setting the row height
      t.setFont(new Font("Arial", Font.BOLD, 21));//Setting the table font
      t.setBackground(gold);//Setting the background colour
      JTableHeader h = t.getTableHeader();//Allows me to modify the header colour, size, font, etc.
      h.setPreferredSize(new Dimension(250, 30));
      h.setBackground(Color.BLACK);//Different colours to show that it is a header
      h.setForeground(Color.WHITE);
      h.setFont(new Font("Arial", Font.BOLD, 21));
      JScrollPane sp = new JScrollPane(t);
      frame.add(sp);
      frame.setSize(800,500);//Sets size of JFrame
      frame.setVisible(true);//Makes it visible
         
      while(frame.isVisible())//ensures that the program will pause while the frame is open
      {
         try
         {  
            Thread.sleep(250); 
         }
         catch(InterruptedException e) {}
      }
         
      try
      {
         out = new FileWriter(highScores);//open writing streams
         writeFile = new BufferedWriter(out);
         for(int i = 0; i < 15; i++)//Writes top 15 names and scores in high scores file
         {
            if(scores[i] != 0)//Will stop writing if less than 15 names are saved
				{
					writeFile.write(names[i]);//writes name
           		writeFile.newLine();
            	writeFile.write(String.valueOf(scores[i]));//writesscore
            	writeFile.newLine();
				}
         }
         writeFile.close();//Close writing streams
         out.close();
      } catch(IOException e) {}//Carch possible exception            
		
	}
   
   /**
   * Displays a JFrame and uses GUI for user input on choosing briefcases, and deals
   * pre: deal != true, impValues[5] >= 0, impValues[0] <= 26 && >= 1
   * post: returns the value of the briefcase/outcome of deal or switch/keep 
   */
   
   public static int JFrameDisplay(double[] caSes, double[] vaLues, String[] diSplay)
   
   {
      
      NumberFormat money = NumberFormat.getCurrencyInstance();
      final JFrame f = new JFrame();//Creates JFrame; is declared as final to allow manipulation in ActionListener
      JPanel p1 = new JPanel();//Creates first panel; stores the left JTable
      p1.setBounds(20,10,180,450);//Setting p1 dimensions and location
      f.add(p1);
      JPanel p2 = new JPanel();//Creates second panel; stores the right JTable
      p2.setBounds(880,10,180,450);//setting dimensions and location
      f.add(p2);
      final int[] caseChoice = {0};//this array will store the case chose/deal or no deal/switch or stay; in order to be manipulated by a button, it must be a final array
      Color gold = Color.decode("#FFDF00");//Making gold available for use
      
      JLabel header2 = new JLabel("Choose a briefcase to open");//Creating header for when user is picking cases
      header2.setBounds(410, 30, 300, 30);//Setting location and dimensions
      header2.setFont(new Font("Arial", Font.BOLD, 20));//Setting font
      JLabel header = new JLabel("Choose a briefcase to keep");//when user is choosing a personal case
      header.setBounds(410, 30, 300, 30);
      header.setFont(new Font("Arial", Font.BOLD, 20));
      JLabel bottom = new JLabel((int)vaLues[0] + " is your briefcase");//reminding user of their case
      bottom.setBounds(447, 405, 300, 30);
      bottom.setFont(new Font("Arial", Font.BOLD, 20));
      
      JLabel bank = new JLabel("The Banker is offering: ");//used during bank deals
      bank.setBounds(340, 0, 500, 100);
      bank.setFont(new Font("Arial", Font.BOLD, 36));
      JLabel offer = new JLabel(money.format(vaLues[5]));//displays bank offer in big red letters
      offer.setBounds(420, 100, 500, 100);
      offer.setFont(new Font("Arial", Font.BOLD, 48));
      offer.setForeground(Color.RED);
      
      JLabel end = new JLabel("Would you like to ");//at the end of the game
      end.setBounds(390, 75, 600, 50);
      end.setFont(new Font("Arial", Font.BOLD, 36));
      end.setForeground(Color.RED);
      JLabel end2 = new JLabel("keep your case or switch?");
      end2.setBounds(310, 125, 600, 50);
      end2.setFont(new Font("Arial", Font.BOLD, 36));
      end2.setForeground(Color.RED);
      
      //Sets values for the first table (lowest 13 values), will display a space if value is out of play
      String[][] data = {{diSplay[0]},{diSplay[1]},{diSplay[2]},{diSplay[3]},{diSplay[4]},{diSplay[5]},{diSplay[6]},{diSplay[7]},{diSplay[8]},{diSplay[9]},{diSplay[10]},{diSplay[11]},{diSplay[12]}};
      String[] column = {""};//no header
      JTable t1 = new JTable(data,column);
      t1.setRowHeight(33);//Setting dimensions
      t1.getColumn("").setMinWidth(180);
      t1.setFont(new Font("Arial", Font.BOLD, 20));//Setting font
      t1.setBackground(gold);//Creating background colour
      t1.setBounds(20,10,180,450);//Setting location and dimensions of table
      p1.add(t1);
      
      //Sets values for second table (highest 13 values)
      String[][] data2 = {{diSplay[13]},{diSplay[14]},{diSplay[15]},{diSplay[16]},{diSplay[17]},{diSplay[18]},{diSplay[19]},{diSplay[20]},{diSplay[21]},{diSplay[22]},{diSplay[23]},{diSplay[24]},{diSplay[25]}};
      String[] column2 = {""};
      JTable t2 = new JTable(data2,column2);
      t2.setRowHeight(33);
      t2.getColumn("").setMinWidth(180);
      t2.setFont(new Font("Arial", Font.BOLD, 20));
      t2.setBackground(gold);
      t2.setBounds(880,10,180,450);
      p2.add(t2);
      
      if(vaLues[0] != 0 && vaLues[5] == 0 && vaLues[4] != 9)//chooses which headers to show based on if there's a deal, if the user has chosen a case and by the round number
      {
         f.add(bottom); f.add(header2);
      }
      else if(vaLues[5] == 0 && vaLues[4] != 9)
      {
         f.add(header);
      }
      else if(vaLues[4] != 9)
      {
         JButton deal = new JButton("DEAL!");//Creates deal button for user to press
         deal.setBounds(330, 300, 200, 75);
         deal.setFont(new Font("Arial", Font.BOLD, 30));//Setting font of text on button
         deal.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){caseChoice[0] = 1; f.dispose();}});//in this case, returning 1 means a deal has been reached
         f.add(deal);//adding button to frame
         JButton nodeal = new JButton("NO DEAL!");
         nodeal.setBounds(550, 300, 200, 75);
         nodeal.setFont(new Font("Arial", Font.BOLD, 30));
         nodeal.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){caseChoice[0] = 0; f.dispose();}});//value 0 means no deal; dispose closes the frame when a button is clicked
         f.add(nodeal);
         f.add(bank); f.add(offer);//adds appropriate headers to frame
      }
      else
      {
         JButton swi = new JButton("SWITCH");//adds switch/keep buttons
         swi.setBounds(330, 300, 200, 75);
         swi.setFont(new Font("Arial", Font.BOLD, 30));
         swi.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){caseChoice[0] = 1; f.dispose();}});//value 1 means switch
         f.add(swi);
         JButton keep = new JButton("KEEP");
         keep.setBounds(550, 300, 200, 75);
         keep.setFont(new Font("Arial", Font.BOLD, 30));
         keep.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){caseChoice[0] = 0; f.dispose();}});//value 0 means keep
         f.add(keep);
         f.add(end); f.add(end2);//adds appropriate headers to frame
      }
      
      //displays briefcase buttons if there is no deal offer and if there are more than 2 cases left
      if(vaLues[5] == 0 && vaLues[4] != 9) {
      if(caSes[0] > 0)//if the case has already been opened, the button will not appear
      {
         JButton c1 = new JButton("1");//defining button for briefcase 1
         c1.setBounds(275, 330, 80, 60);//setting location (layout of buttons is based on show)
         c1.setFont(new Font("Arial", Font.BOLD, 25));//setting font of the number on the button
         c1.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 1; f.dispose();}});//value signifies that user has chosen this case
         f.add(c1);//adds this button to the JFrame
      }
      if(caSes[1] > 0)//repeats this for all 26 cases
      {
         JButton c2 = new JButton("2");
         c2.setBounds(365, 330, 80, 60);
         c2.setFont(new Font("Arial", Font.BOLD, 25));
         c2.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 2; f.dispose();}});
         f.add(c2);
      }
      if(caSes[2] > 0)
      {
         JButton c3 = new JButton("3");
         c3.setBounds(455, 330, 80, 60);
         c3.setFont(new Font("Arial", Font.BOLD, 25));
         c3.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 3; f.dispose();}});
         f.add(c3);
      }
      if(caSes[3] > 0)
      {
         JButton c4 = new JButton("4");
         c4.setBounds(545, 330, 80, 60);
         c4.setFont(new Font("Arial", Font.BOLD, 25));
         c4.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 4; f.dispose();}});
         f.add(c4);
      }
      if(caSes[4] > 0)
      {
         JButton c5 = new JButton("5");
         c5.setBounds(635, 330, 80, 60);
         c5.setFont(new Font("Arial", Font.BOLD, 25));
         c5.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 5; f.dispose();}});
         f.add(c5);
      }
      if(caSes[5] > 0)
      {
         JButton c6 = new JButton("6");
         c6.setBounds(725, 330, 80, 60);
         c6.setFont(new Font("Arial", Font.BOLD, 25));
         c6.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 6; f.dispose();}});
         f.add(c6);
      }
      if(caSes[6] > 0)
      {
         JButton c7 = new JButton("7");
         c7.setBounds(230, 250, 80, 60);
         c7.setFont(new Font("Arial", Font.BOLD, 25));
         c7.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 7; f.dispose();}});
         f.add(c7);
      }
      if(caSes[7] > 0)
      {
         JButton c8 = new JButton("8");
         c8.setBounds(320, 250, 80, 60);
         c8.setFont(new Font("Arial", Font.BOLD, 25));
         c8.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 8; f.dispose();}});
         f.add(c8);
      }
      if(caSes[8] > 0)
      {
         JButton c9 = new JButton("9");
         c9.setBounds(410, 250, 80, 60);
         c9.setFont(new Font("Arial", Font.BOLD, 25));
         c9.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 9; f.dispose();}});
         f.add(c9);
      }
      if(caSes[9] > 0)
      {
         JButton c10 = new JButton("10");
         c10.setBounds(500, 250, 80, 60);
         c10.setFont(new Font("Arial", Font.BOLD, 25));
         c10.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 10; f.dispose();}});
         f.add(c10);
      }
      if(caSes[10] > 0)
      {
         JButton c11 = new JButton("11");
         c11.setBounds(590, 250, 80, 60);
         c11.setFont(new Font("Arial", Font.BOLD, 25));
         c11.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 11; f.dispose();}});
         f.add(c11);
      }
      if(caSes[11] > 0)
      {
         JButton c12 = new JButton("12");
         c12.setBounds(680, 250, 80, 60);
         c12.setFont(new Font("Arial", Font.BOLD, 25));
         c12.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 12; f.dispose();}});
         f.add(c12);
      }
      if(caSes[12] > 0)
      {
         JButton c13 = new JButton("13");
         c13.setBounds(770, 250, 80, 60);
         c13.setFont(new Font("Arial", Font.BOLD, 25));
         c13.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 13; f.dispose();}});
         f.add(c13);
      }
      if(caSes[13] > 0)
      {
         JButton c14 = new JButton("14");
         c14.setBounds(230, 170, 80, 60);
         c14.setFont(new Font("Arial", Font.BOLD, 25));
         c14.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 14; f.dispose();}});
         f.add(c14);
      }
      if(caSes[14] > 0)
      {
         JButton c15 = new JButton("15");
         c15.setBounds(320, 170, 80, 60);
         c15.setFont(new Font("Arial", Font.BOLD, 25));
         c15.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 15; f.dispose();}});
         f.add(c15);
      }
      if(caSes[15] > 0)
      {
         JButton c16 = new JButton("16");
         c16.setBounds(410, 170, 80, 60);
         c16.setFont(new Font("Arial", Font.BOLD, 25));
         c16.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 16; f.dispose();}});
         f.add(c16);
      }
      if(caSes[16] > 0)
      {
         JButton c17 = new JButton("17");
         c17.setBounds(500, 170, 80, 60);
         c17.setFont(new Font("Arial", Font.BOLD, 25));
         c17.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 17; f.dispose();}});
         f.add(c17);
      }
      if(caSes[17] > 0)
      {
         JButton c18 = new JButton("18");
         c18.setBounds(590, 170, 80, 60);
         c18.setFont(new Font("Arial", Font.BOLD, 25));
         c18.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 18; f.dispose();}});
         f.add(c18);
      }
      if(caSes[18] > 0)
      {
         JButton c19 = new JButton("19");
         c19.setBounds(680, 170, 80, 60);
         c19.setFont(new Font("Arial", Font.BOLD, 25));
         c19.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 19; f.dispose();}});
         f.add(c19);
      }
      if(caSes[19] > 0)
      {
         JButton c20 = new JButton("20");
         c20.setBounds(770, 170, 80, 60);
         c20.setFont(new Font("Arial", Font.BOLD, 25));
         c20.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 20; f.dispose();}});
         f.add(c20);
      }
      if(caSes[20] > 0)
      {
         JButton c21 = new JButton("21");
         c21.setBounds(275, 90, 80, 60);
         c21.setFont(new Font("Arial", Font.BOLD, 25));
         c21.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 21; f.dispose();}});
         f.add(c21);
      }
      if(caSes[21] > 0)
      {
         JButton c22 = new JButton("22");
         c22.setBounds(365, 90, 80, 60);
         c22.setFont(new Font("Arial", Font.BOLD, 25));
         c22.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 22; f.dispose();}});
         f.add(c22);
      }
      if(caSes[22] > 0)
      {
         JButton c23 = new JButton("23");
         c23.setBounds(455, 90, 80, 60);
         c23.setFont(new Font("Arial", Font.BOLD, 25));
         c23.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 23; f.dispose();}});
         f.add(c23);
      }
      if(caSes[23] > 0)
      {
         JButton c24 = new JButton("24");
         c24.setBounds(545, 90, 80, 60);
         c24.setFont(new Font("Arial", Font.BOLD, 25));
         c24.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 24; f.dispose();}});
         f.add(c24);
      }
      if(caSes[24] > 0)
      {
         JButton c25 = new JButton("25");
         c25.setBounds(635, 90, 80, 60);
         c25.setFont(new Font("Arial", Font.BOLD, 25));
         c25.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 25; f.dispose();}});
         f.add(c25);
      }
      if(caSes[25] > 0)
      {
         JButton c26 = new JButton("26");
         c26.setBounds(725, 90, 80, 60);
         c26.setFont(new Font("Arial", Font.BOLD, 25));
         c26.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ caseChoice[0] = 26; f.dispose();}});
         f.add(c26);
      }
      }
      
      f.setLayout(null);//sets frame layout as no layout (I "formatted" using panels and setBounds)
      f.setSize(1080, 500);//setting size of frame
      f.setVisible(true);//setting the frame to be visible
      f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//setting close operation
      
      while(f.isVisible())//ensures that execution pauses until user has clicked a button
      {
         try
         {  
            Thread.sleep(250); 
         }
         catch(InterruptedException e) {}
      }
      
      return(caseChoice[0]);//returns the case choice/ deal or no deal/ switch or keep to calling invocation
      
   }
												
}
        
