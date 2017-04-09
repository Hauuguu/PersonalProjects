//David Itkin, David Su, Josh Espinosa 
//383 AI Semester Project

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
public class Minimax{

	/** VERY IMPORTANT!!!! READ THESE TWO COMMENTS
	 *  TURN on this boolean to true to activate Alpha beta pruning!
	 *  ****Alpha Beta pruning sometimes causes the AI players to stack all pieces into one column, an unresolved error that started cropping up very recently.
	 */
	public static boolean alphabeta = false;
	/**
	 *  TURN on this boolean to true to activate One-player mode, if false it will be AI vs AI
	 *  WARNING: The AI may be tough to beat for a human player, so DO NOT PLAY AGAINST IT IF YOU DISLIKE LOSING!
	 */
	public static boolean p1vsAI = false;
	
	

	public static int depth;
	public static char player;
	public static String winner="";
	public static int turn;
	public static boolean skip=false;

	public static int wP1MyDouble3;
	public static int wP1OpDouble3;
	public static int wP1MySingle3;
	public static int wP1OpSingle3;
	public static int wP1MyDouble2;
	public static int wP1OpDouble2;
	public static int wP1MySingle2;
	public static int wP1OpSingle2;
	public static int wP1MyDouble1;
	public static int wP1OpDouble1;
	public static int wP2MyDouble3;
	public static int wP2OpDouble3;
	public static int wP2MySingle3;
	public static int wP2OpSingle3;
	public static int wP2MyDouble2;
	public static int wP2OpDouble2;
	public static int wP2MySingle2;
	public static int wP2OpSingle2;
	public static int wP2MyDouble1;
	public static int wP2OpDouble1;

	public static int myMove=0;

	public static void main(String[] args) throws IOException {
		long timerStart = System.currentTimeMillis();


		//if up to 21 arguments are supplied those are the weights for feature 1, feature 2, feature 3 etc for each players
		// if not then they're given random values 1-100, corresponding negatives for the opponent's features.
		/**
		 * CODE FOR UNDERSTANDING FEATURE WEIGHTS
		 * 1. Int variables starting in 'w' are WEIGHTS
		 * 2. Weight variables containing 'My' describe the turn player's features weights
		 * 3. Weight variables containing 'Op' describe the opponent's feature weights
		 * 4. All weight variables correspond to VALID CHAINS (game pieces of the same color, linked horizontally,
		 * 		vertically, or diagonally, either unblocked on one or both ends.
		 * 5. Variables containing 'single' are singly unblocked (unblocked by an opponent's piece or wall on one end)
		 * 6. Variables containing 'double' are doubly unblocked (unblocked by an opponent's piece or wall on BOTH ends)
		 * 7. Variables containing 'P1' are player 1's idea of the weights
		 * 8. Variables containing 'P2' are player 2's idea of the weights, making them different and comparing performance 
		 * 		reveals something about which weights work better than others.
		 */
		Random gen = new Random();
		depth = Integer.valueOf(args[0]);

		wP1MyDouble3 = gen.nextInt(100);
		if (args.length>=2)
			wP1MyDouble3 = Integer.valueOf(args[1]);

		wP1OpDouble3 = (-1)*gen.nextInt(100);
		if (args.length>=3)
			wP1OpDouble3 = Integer.valueOf(args[2]);

		wP1MySingle3 = gen.nextInt(100);
		if (args.length>=4)
			wP1MySingle3 = Integer.valueOf(args[3]);

		wP1OpSingle3 = (-1)*gen.nextInt(100);
		if (args.length>=5)
			wP1OpSingle3 = Integer.valueOf(args[4]);

		wP1MyDouble2 = gen.nextInt(100);
		if (args.length>=6)
			wP1MyDouble2 = Integer.valueOf(args[5]);

		wP1OpDouble2 = (-1)*gen.nextInt(100);
		if (args.length>=7)
			wP1OpDouble2 = Integer.valueOf(args[6]);

		wP1MySingle2 = gen.nextInt(100);
		if (args.length>=8)
			wP1MySingle2 = Integer.valueOf(args[7]);

		wP1OpSingle2 = (-1)*gen.nextInt(100);
		if (args.length>=9)
			wP1OpSingle2 = Integer.valueOf(args[8]);

		wP1MyDouble1 = gen.nextInt(100);
		if (args.length>=10)
			wP1MyDouble1 = Integer.valueOf(args[9]);

		wP1OpDouble1 = (-1)*gen.nextInt(100);
		if (args.length>=11)
			wP1OpDouble1 = Integer.valueOf(args[10]);

		wP2MyDouble3 = gen.nextInt(100);
		if (args.length>=12)
			wP2MyDouble3 = Integer.valueOf(args[11]);

		wP2OpDouble3 = (-1)*gen.nextInt(100);
		if (args.length>=13)
			wP2OpDouble3 = Integer.valueOf(args[12]);

		wP2MySingle3 = gen.nextInt(100);
		if (args.length>=14)
			wP2MySingle3 = Integer.valueOf(args[13]);

		wP2OpSingle3 = (-1)*gen.nextInt(100);
		if (args.length>=15)
			wP2OpSingle3 = Integer.valueOf(args[14]);

		wP2MyDouble2 = gen.nextInt(100);
		if (args.length>=16)
			wP2MyDouble2 = Integer.valueOf(args[15]);

		wP2OpDouble2 = (-1)*gen.nextInt(100);
		if (args.length>=17)
			wP2OpDouble2 = Integer.valueOf(args[16]);

		wP2MySingle2 = gen.nextInt(100);
		if (args.length>=18)
			wP2MySingle2 = Integer.valueOf(args[17]);

		wP2OpSingle2 = (-1)*gen.nextInt(100);
		if (args.length>=19)
			wP2OpSingle2 = Integer.valueOf(args[18]);

		wP2MyDouble1 = gen.nextInt(100);
		if (args.length>=20)
			wP2MyDouble1 = Integer.valueOf(args[19]);

		wP2OpDouble1 = (-1)*gen.nextInt(100);
		if (args.length>=21)
			wP2OpDouble1 = Integer.valueOf(args[20]);

		//the following if statements handle errors
		if(wP1MyDouble3<0 || wP1MyDouble3>100 || wP1MySingle3<0 || wP1MySingle3>100 || wP1MyDouble2<0 || wP1MyDouble2>100 || wP1MySingle2<0 || wP1MySingle2>100 || wP1MyDouble1<0 || wP1MyDouble1>100 || wP2MyDouble3<0 || wP2MyDouble3>100 || wP2MySingle3<0 || wP2MySingle3>100 || wP2MyDouble2<0 || wP2MyDouble2>100 || wP2MySingle2<0 || wP2MySingle2>100 || wP2MyDouble1<0 || wP2MyDouble1>100){
			System.err.println("Please make sure all turn player's features are weighted between 0 and 100 inclusive");
			System.exit(0);
		}
		if(wP1OpDouble3>0 || wP1OpDouble3<(-100) || wP1OpSingle3>0 || wP1OpSingle3<(-100) || wP1OpDouble2>0 || wP1OpDouble2<(-100) || wP1OpSingle2>0 || wP1OpSingle2<(-100) || wP1OpDouble1>0 || wP1OpDouble1<(-100) || wP2OpDouble3>0 || wP2OpDouble3<(-100) || wP2OpSingle3>0 || wP2OpSingle3<(-100) || wP2OpDouble2>0 || wP2OpDouble2<(-100) || wP2OpSingle2>0 || wP2OpSingle2<(-100) || wP2OpDouble1>0 || wP2OpDouble1<(-100)){
			System.err.println("Please make sure all opponent player's features are weighted between 0 and -100 inclusive");
			System.exit(0);
		}

		Minimax mm = new Minimax();
		char[][] mainstate = new char[6][7];

		for(int a=0;a<6;a++){
			for (int b=0;b<7;b++)
				mainstate[a][b]='E';
		}


		board mainBoard = mm.new board(mainstate, 'X');

		int maxMoves = 42;

		InputStreamReader inStream = new InputStreamReader (System.in);
		BufferedReader userInput = new BufferedReader (inStream);
		String inputMove = "";
		//game loop that governs player 1 vs AI
		if(p1vsAI){
			System.out.println("Please enter X to go first or O to go second, then press Enter");
			String inputPlayer = userInput.readLine();

			if(inputPlayer.equalsIgnoreCase("X")){
				mainBoard.setPlayer('X');
				mainBoard.setHuman('X');
			}
			else if (inputPlayer.equalsIgnoreCase("O")){
				mainBoard.setHuman('O');
			}
			else if (!inputPlayer.equalsIgnoreCase("X") || !inputPlayer.equalsIgnoreCase("O")){
				System.out.println("Please Enter X or O, case insensitive, then press Enter. No other inputs accepted");
				inputPlayer = userInput.readLine();
				if(inputPlayer.equalsIgnoreCase("X"))
					mainBoard.setPlayer('X');
				else if (inputPlayer.equalsIgnoreCase("O"))
					mainBoard.setPlayer('O');
			}

			p1VSAIloop:
				while(winner==""){
					turn++;
					System.out.println("\n -- Turn "+turn+" --\n");
					if(turn==1 && mainBoard.getHuman()=='O'){
						Integer[] intArray = mm.gameTree(mainBoard, depth);
						mm.nextMove(intArray, false, alphabeta, mainBoard);
						mainBoard.move(myMove);
						mainBoard.printBoard();
						mainBoard.calculateStateWeight(true);
						mainBoard.nexturn();

					}

					System.out.println("\nPlease enter an integer between 0 and 6 (INCLUSIVE) for your move, then press Enter\n");
					inputMove = userInput.readLine();
					if(Integer.valueOf(inputMove) != null && Integer.valueOf(inputMove)>-1 && Integer.valueOf(inputMove)<7){
						mainBoard=mainBoard.move(Integer.valueOf(inputMove));
						if(skip){
							System.out.println("\nPlease move into a column that isn't full!\n");
							mainBoard.printBoard();
						}
						else if(!skip){

							mainBoard.printBoard();
							mainBoard.calculateStateWeight(true);
							mainBoard.nexturn();
							if(winner!="")
								break p1VSAIloop;
						}

					}
					if(!skip){
						System.out.println("\n  Opponent's Response \n");
						Integer[] intArray = mm.gameTree(mainBoard, depth);
						mm.nextMove(intArray, false, alphabeta, mainBoard);
						mainBoard.move(myMove);
						mainBoard.printBoard();
						mainBoard.calculateStateWeight(true);
						if(winner!="")
							break p1VSAIloop;
						mainBoard.nexturn();
					}
					skip = false;

				}
		}

		//game loop that governs AI vs AI play
		if(p1vsAI==false){
			gameloop:
			for(int mov = 0; mov<42; mov++){
				System.out.println("\n -- Turn "+(mov+1)+"  "+"Player "+mainBoard.getPlayer()+" --\n");
				Integer[] intArray = mm.gameTree(mainBoard, depth);

				mm.nextMove(intArray, false, alphabeta, mainBoard);
				mainBoard.move(myMove);

				
				mainBoard.printBoard();
				mainBoard.calculateStateWeight(true);
				mainBoard.nexturn();
				if(""!=winner){
					break gameloop;
				}
			}

			long timerEnd = System.currentTimeMillis();
			System.out.print("The runtime for this program");
			if (alphabeta)
				System.out.print(" with AlphaBeta pruning ");
			else
				System.out.print(" without AlphaBeta pruning ");
			System.out.println("is: \n\t" + (((double)(timerEnd - timerStart))*(1.0/1000)) + " seconds or");
			System.out.println("\t"+((((double)(timerEnd - timerStart))*(1.0/1000))/maxMoves) + " seconds per move.");
		}
	}

	//Iteratively call calculate feature weights over all returned game states to build the tree 
	//that MiniMax is performed on in the nextMove method.
	public Integer[] gameTree(board b, int depth){

		ArrayList<board> bigList = new ArrayList<board>();
		char player = b.getPlayer();
		char turnPlayer = player;
		bigList.add(b);

		for (int d = 0;d<depth; d++){	
			ArrayList<board> nextLevel = null;

			//Switch player every depth
			if (player == 'X' && d > 0)
				player = 'O';
			else if(player == 'O' && d > 0)
				player = 'X';

			//populates nextLevel arraylist with new set of board configurations for the next move
			nextLevel = new ArrayList<board>();
			for (int pos = 0;pos<bigList.size();pos++){	
				for(int col=0;col<7;col++){
					board temp = bigList.get(pos).duplicateBoard(player);
					temp.move(col);
					nextLevel.add(temp);			
				}
			}
			bigList = nextLevel;
		}
		//makes an array of game state values corresponding to all possible gamestates.
		Integer[] gameStateValues = new Integer[bigList.size()];
		for(int y = 0; y < bigList.size(); y++){
			if(bigList.get(y)!=null){
				bigList.get(y).setPlayer(turnPlayer);
				gameStateValues[y] = bigList.get(y).calculateStateWeight(false);
			}
		}
		//Iteratively call calculate feature weights over all returned game states to build the 'minimax' tree
		return gameStateValues;
	}



	//recursive MiniMax method. but you have to pass it the biggest bottom array.
	//returns the size of the array to keep itself going

	//also accepts an alphaBeta parameter (aB)
	public int nextMove(Integer[] prevMoves, boolean max, boolean aB, board b){

		if(prevMoves.length==1){
			return prevMoves.length;
		}
		//we must keep in mind what happens when there arent 7 possible moves to make.
		Integer[] curMoves = new Integer[prevMoves.length/7];
		int index=0;
		int section = 0;
		int divide = 0;
		int move=0;
		curMoves[section] = prevMoves[index];

		//this while loop takes the array for a given layer of moves and selects the min or max state from each. 
		lop:
			while (index<prevMoves.length){
				if(divide==7){
					section++;
					divide=0;
					curMoves[section]=prevMoves[index];
				}
				if(max){
					if(index>=prevMoves.length || section>=curMoves.length)
						break lop;

					if(aB && section>0){
						int owl=0;
						int secCount=0;
						//going through all other sections, comparing current value to previous sections.

						for(secCount=0;secCount<section;secCount++){
							if(prevMoves[index]!=null && curMoves[section]!=null && prevMoves[index] >= curMoves[section]){
								owl++;
							}
						}
						if (owl==secCount){
							curMoves[section]=prevMoves[index];
							index=index+(7-divide);
							divide=0;
							section++;							
						}
						if(index>=prevMoves.length || section>=curMoves.length){
							break lop;
						}
					}
					if(prevMoves[index]!=null && curMoves[section]!=null && prevMoves[index]>curMoves[section]){
						curMoves[section] = prevMoves[index];

						if(prevMoves.length==7)
							move = index;
					}
				}
				else if(!max){
					if(index>=prevMoves.length || section>=curMoves.length){
						break lop;
					}
					if(aB && section>0){
						int owl=0;
						int secCount=0;

						//going through all other sections, comparing current value to previous sections.

						for(secCount=0;secCount<section;secCount++){
							if(prevMoves[index]!=null && curMoves[section]!=null && prevMoves[index] <= curMoves[section]){
								owl++;
							}
						}
						if (owl==secCount){
							curMoves[section]=prevMoves[index];
							index=index+(7-divide);
							divide=0;
							section++;

						}
						if(index>=prevMoves.length || section>=curMoves.length){
							break lop;
						}
					}
					if(prevMoves[index] != null && curMoves[section]!=null){
						if(prevMoves[index] < curMoves[section]){
							curMoves[section] = prevMoves[index];
							if(prevMoves.length==7)
								move = index;
						}
					}
				}
				divide++;
				index++;	
			}
		
		//this code makes sure the 'move' picked is legitimate  - aka, that it doesn't lead to the AI placing a piece into a full column
		if(prevMoves.length==7){
			if(b.getState()[5][move]=='E'){
				myMove=move;
			}
			//iterates until it finds the next best thing to the previous best, and it isn't a move into a full column
			else if(b.getState()[5][move]!='E'){
				Integer upperbound = prevMoves[move];
				Integer curBest = 0;
				int curBestIndex =0;

				lo:
				for(int j=0;j<7;j++){
					if(prevMoves[j]<upperbound){
						curBest = prevMoves[j];
						curBestIndex = j;
						if(b.getState()[5][move]=='E')
							break lo;
					}
				}
						
				move = curBestIndex;
				
				myMove = move;
			}
		}
		
		
		if(max)
			max=false;
		else if(!max)
			max=true;

		return nextMove(curMoves, max, aB, b);
	}

	public class board{
		private char[][] state = new char[6][7];
		//the locks allow us to discount squares that already were counted for chains 
		private boolean[] unlock =  new boolean[7]; 
		private boolean[][] unlock2D = new boolean[6][7];
		char human;


		boolean firstEndBlocked = false;
		char chainchar='E';
		//the current player.. 'X' if the turn player is P1, 'O' if the turn player is P2
		private char player;
		public board(char[][] st, char play){

			state=st;
			player=play;
			//initializing unlocks to true aka unlocked
			for(int a=0;a<7;a++){
				for(int b=0;b<6;b++){
					unlock2D[b][a]=true;
				}
				unlock[a]=true;
			}

		}
		public char getPlayer(){
			return player;
		}
		public void setHuman(char person){
			human = person;

		}
		public void setPlayer(char player){
			if (player=='X' || player=='O');
			this.player = player;
		}
		public void p1(){
			this.player='X';
		}
		public void p2(){
			this.player='O';
		}
		//changes the turn player, called when a move is made.
		public void nexturn(){
			if(this.player=='X'){
				p2();
			}
			else
				p1();
		}
		public char[][] getState(){
			return state;
		}
		public board getBlankBoard(){
			char[][] newState = new char[6][7];

			for(int a=0;a<6;a++){
				for (int b=0;b<7;b++)
					newState[a][b]='E';
			}

			return new board(newState, 'X');
		}
		public board duplicateBoard(char player){
			board newBoard = getBlankBoard();

			for (int row = 0; row < 6; row++){
				for (int col = 0; col < 7; col++){
					newBoard.set(row,col,this.getState()[row][col]);
				}
			}
			newBoard.player = player;
			return newBoard;
		}
		/**
		 * Method to set a value of the board
		 * @param row - The row to set
		 * @param col - The column to set
		 * @param value - The player value of that spot
		 */
		public void set(int row, int col, char value){
			this.getState()[row][col] = value;
		}

		//move into a column, 'X' for p1, 'O' for p2
		public board move(int col){
			if(state[5][col]!='E' && human!=player){
				return null;
			}
			else if(state[5][col]!='E' && human==player){
				skip=true;
				return this;
			}

			search:
				for(int row=0;row<6;row++){
					if(state[row][col]=='E'){
						state[row][col]=player;
						break search;
					}
				}
			return this;
		}

		public int calculateStateWeight(boolean conclusive){
			int total=0;
			boolean verlok = false;
			int[] fts = new int[12];
			//finding vertical chains

			for(int col=0;col<7;col++){
				for(int row=0;row<5;row++){
					if(this.state[row][col]!='E' && !verlok){
						chainchar=this.state[row][col];

						if(this.state[row+1][col]==chainchar){

							//vertical chain found, length 2 or more
							if(row<4){
								//if the chain ends there, record it 		
								if(this.state[row+2][col]=='E'&&chainchar==player){
									fts[6]++;
									verlok=true;
								}
								else if(this.state[row+2][col]=='E'&&chainchar!=player){
									fts[7]++;
									verlok=true;
								}
								else if(this.state[row+2][col]==chainchar){
									//vertical chain found, length 3 or more
									if(row<3){
										//if the chain ends there, record it
										if(this.state[row+3][col]=='E'&&chainchar==player){
											fts[2]++;
											verlok=true;
										}
										else if(this.state[row+3][col]=='E'&&chainchar!=player){
											fts[3]++;
											verlok=true;
										}
										else if(this.state[row+3][col]==chainchar&&chainchar==player){
											fts[10]++;
											verlok=true;
										}
										else if(this.state[row+3][col]==chainchar&&chainchar!=player){
											fts[11]++;
											verlok=true;
										}
									}
								}
							}
						}
					}
					//unlock the column
					if(row==4)
						verlok=false;
				}
			}

			horloop:
				for (int row=0;row<6;row++){
					for(int col=0;col<6;col++){
						if(this.state[row][col]!='E' && unlock[col]){
							chainchar=this.state[row][col];
							//checking the first end for possible block
							if(col>0 && this.state[row][col-1]=='E'){
								if(this.state[row][col-1]=='E')
									firstEndBlocked=false;
								else
									firstEndBlocked=true;
							}
							else
								firstEndBlocked = true;


							//checking mydouble1 chain
							if(this.state[row][col+1]=='E' && !firstEndBlocked && chainchar==player){
								fts[8]++;
								continue;
							}

							//checking opdouble1 chain
							if(this.state[row][col+1]=='E' && !firstEndBlocked && chainchar!=player){
								fts[9]++;
								continue;
							}


							if(this.state[row][col+1]==chainchar && unlock[col+1]){
								if(col==5 && !firstEndBlocked){
									if(chainchar==player)
										fts[6]++;
									else if(chainchar!=player)
										fts[7]++;
									unlock[col+1]=false;
								}	
								//horizontal chain found, length 2 or more
								if(col<5){

									if(this.state[row][col+2]=='E'&&chainchar==player){
										if(firstEndBlocked)
											fts[6]++;
										else
											fts[4]++;

										unlock[col+1]=false;
									}
									else if(this.state[row][col+2]=='E'&&chainchar!=player){
										if(firstEndBlocked)
											fts[7]++;
										else
											fts[5]++;

										unlock[col+1]=false;
									}

									else if(this.state[row][col+2]==chainchar && unlock[col+2]){
										//horizontal chain found, length 3 or more
										if(col==4 && !firstEndBlocked){
											if(chainchar==player)
												fts[2]++;
											else if(chainchar!=player)
												fts[3]++;
											unlock[col+1]=false;
											unlock[col+2]=false;	
										}
										if(col<4){
											//if chain ends there, and it's MY chain, record it
											if(this.state[row][col+3]=='E'&&chainchar==player){
												if(firstEndBlocked)
													fts[2]++;
												else
													fts[0]++;
											}
											//if chain ends there, and it's OP chain, record it
											else if(this.state[row][col+3]=='E'&&chainchar!=player){
												if(firstEndBlocked)
													fts[3]++;
												else
													fts[1]++;
											}
											//if chain length 3 is found, we lock the row for the next 3 iterations of 2nd main forloop
											unlock[col+2]=false;
											if(col==3){
												if(this.state[row][col+3]==chainchar && chainchar==player){
													fts[10]++;
													unlock[col+3]=false;
												}
												else if(this.state[row][col+3]==chainchar && chainchar!=player){
													fts[11]++;
													unlock[col+3]=false;
												}
											}
											if(col<3 && unlock[col+3]){

												if(this.state[row][col+3]==chainchar&&chainchar==player){
													fts[10]++;
													unlock[col+3]=false;
												}
												else if(this.state[row][col+3]==chainchar&&chainchar!=player){
													fts[11]++;
													unlock[col+3]=false;
												}
											}

										}
									}
								}

							}
						}
					}
				}
			SEloopLower:
				//top to bottom checking diagonals that go NW to SE
				for (int dec=0; dec<3; dec++){
					for(int row=5;row>0;row--){
						for(int col=0;col<6;col++){
							row=row-dec;
							//making sure we never go out of bounds with the row while changing the diagonal we're checking
							if(row<1)
								continue;
							if(this.state[row][col]!='E' && unlock2D[row][col]){
								chainchar=this.state[row][col];
								//checking the first end for possible block
								if(col>0 && row <5){
									if(this.state[row+1][col-1]=='E')
										firstEndBlocked=false;
									else
										firstEndBlocked=true;
								}
								else
									firstEndBlocked = true;

								//checking for chain of length 2 NW to SE
								if(this.state[row-1][col+1]==chainchar  && unlock2D[row-1][col+1]){
									//diagonal chain found of length 2 or higher, NW to SE
									if(row==1 && col>1 && !firstEndBlocked){
										if(chainchar==player)
											fts[6]++;
										else if(chainchar!=player)
											fts[7]++;
										unlock2D[row-1][col+1]=false;
									}
									if(row>1 && col<5){
										//if the chain ends there, record it 	
										if(this.state[row-2][col+2]=='E'&&chainchar==player){
											if(firstEndBlocked)
												fts[6]++;
											else
												fts[4]++;
											unlock2D[row-1][col+1]=false;
										}
										else if(this.state[row-2][col+2]=='E'&&chainchar!=player){
											if(firstEndBlocked)
												fts[7]++;
											else
												fts[5]++;
											unlock2D[row-1][col+1]=false;
										}
										else if(this.state[row-2][col+2]==chainchar  && unlock2D[row-2][col+2]){
											//diagonal chain found, length 3 or more, NW to SE
											if(row==2 && col>0 && !firstEndBlocked){
												if(chainchar==player)
													fts[2]++;
												else if(chainchar!=player)
													fts[3]++;
												unlock2D[row-2][col+2]=false;
											}
											if(row>2 && col<4){
												//if chain ends there, and it's MY chain, record it
												if(this.state[row-3][col+3]=='E'&&chainchar==player){
													if(firstEndBlocked)
														fts[2]++;
													else
														fts[0]++;
												}
												else if(this.state[row-3][col+3]=='E'&&chainchar!=player){
													if(firstEndBlocked)
														fts[3]++;
													else
														fts[1]++;
												}

												unlock2D[row-1][col+1]=false;
												unlock2D[row-2][col+2]=false;
												//checking for length 4 chains aka wins
												if(this.state[row-3][col+3]==chainchar&&chainchar==player  && unlock2D[row-3][col+3]){
													fts[10]++;
													unlock2D[row-3][col+3]=false;
												}
												else if(this.state[row-3][col+3]==chainchar&&chainchar!=player  && unlock2D[row-3][col+3]){
													fts[11]++;
													unlock2D[row-3][col+3]=false;
												}		
											}
										}
									}
								}
							}
						}
					}
				}
				SEloopUpper:
					//top to bottom checking diagonals that go NW to SE, in the upper part of the board
					for (int inc=0; inc<3; inc++){
						for(int row=5;row>0;row--){
							for(int col=1;col<6;col++){
								col=col+inc;
								//making sure we never go out of bounds with the column while changing the diagonal we're checking
								if(col>5)
									continue;
								if(this.state[row][col]!='E' && unlock2D[row][col]){
									chainchar=this.state[row][col];
									//checking the first end for possible block
									if(col>0 && row <5){
										if(this.state[row+1][col-1]=='E')
											firstEndBlocked=false;
										else
											firstEndBlocked=true;
									}
									else
										firstEndBlocked=true;

									//checking for chain of length 2 NW to SE
									if(this.state[row-1][col+1]==chainchar && unlock2D[row-1][col+1]){

										//diagonal chain found of length 2 or higher, NW to SE
										if(col==5 && row<4 && !firstEndBlocked){
											if(chainchar==player)
												fts[6]++;
											else if(chainchar!=player)
												fts[7]++;
											unlock2D[row-1][col+1]=false;

										}
										if(col<5 && row>2){
											//if the chain ends there, record it 	
											if(this.state[row-2][col+2]=='E'&&chainchar==player){
												if(firstEndBlocked)
													fts[6]++;
												else
													fts[4]++;
												unlock2D[row-1][col+1]=false;
											}
											else if(this.state[row-2][col+2]=='E'&&chainchar!=player){
												if(firstEndBlocked)
													fts[7]++;
												else
													fts[5]++;
												unlock2D[row-1][col+1]=false;
											}
											else if(this.state[row-2][col+2]==chainchar && unlock2D[row-2][col+2]){
												//diagonal chain found, length 3 or more, NW to SE
												if(col==4 && row<5 && !firstEndBlocked){
													if(chainchar==player)
														fts[2]++;
													else if(chainchar!=player)
														fts[3]++;
													unlock2D[row-2][col+2]=false;

												}
												if(col<4 && row>3){
													//if chain ends there, and it's MY chain, record it
													if(this.state[row-3][col+3]=='E'&&chainchar==player){
														if(firstEndBlocked)
															fts[2]++;
														else
															fts[0]++;
														unlock2D[row-2][col+2]=false;
													}
													else if(this.state[row-3][col+3]=='E'&&chainchar!=player){
														if(firstEndBlocked)
															fts[3]++;
														else
															fts[1]++;
														unlock2D[row-2][col+2]=false;
													}
													unlock2D[row-1][col+1]=false;
													unlock2D[row-2][col+2]=false;
													//checking for length 4 chains aka wins
													if(this.state[row-3][col+3]==chainchar&&chainchar==player && unlock2D[row-3][col+3]){
														fts[10]++;
														unlock2D[row-3][col+3]=false;
													}
													else if(this.state[row-3][col+3]==chainchar&&chainchar!=player&& unlock2D[row-3][col+3]){
														fts[11]++;
														unlock2D[row-3][col+3]=false;
													}		
												}
											}
										}
									}
								}
							}
						}
					}
				for(int a=0;a<7;a++){
					for(int b=0;b<6;b++){
						unlock2D[b][a]=true;
					}
					unlock[a]=true;
				}




				NEloopLower:
					//top to bottom checking diagonals that go SW to NE
					for (int inc=0; inc<3; inc++){
						for(int row=0;row<5;row++){
							for(int col=1;col<6;col++){
								col=col+inc;
								//making sure we never go out of bounds with the column while changing the diagonal we're checking
								if(col>5)
									continue;
								if(this.state[row][col]!='E' && unlock2D[row][col]){
									chainchar=this.state[row][col];
									//checking the first end for possible block
									if(col>1 && row>0){
										if(this.state[row-1][col-1]=='E')
											firstEndBlocked=false;
										else
											firstEndBlocked=true;
									}
									else
										firstEndBlocked=true;

									//checking for chain of length 2 SW to NE
									if(this.state[row+1][col+1]==chainchar && unlock2D[row+1][col+1]){
										//diagonal chain found of length 2 or higher, SW to NE
										if(col==5 && row>1 && !firstEndBlocked){
											if(chainchar==player)
												fts[6]++;
											else if(chainchar!=player)
												fts[7]++;
											unlock2D[row+1][col+1]=false;

										}	
										if(col<5 && row<4){
											//if the chain ends there, record it 	
											if(this.state[row+2][col+2]=='E'&&chainchar==player){
												if(firstEndBlocked)
													fts[6]++;
												else
													fts[4]++;
											}
											else if(this.state[row+2][col+2]=='E'&&chainchar!=player){
												if(firstEndBlocked)
													fts[7]++;
												else
													fts[5]++;
											}
											//diagonal chain found, length 3 or more, SW to NE
											else if(this.state[row+2][col+2]==chainchar && unlock2D[row+2][col+2]){
												if(col==4 && row>0 && !firstEndBlocked){
													if(chainchar==player){
														fts[2]++;
													}
													else if(chainchar!=player){
														fts[3]++;
													}
													unlock2D[row+1][col+1]=false;
													unlock2D[row+2][col+2]=false;
												}	
												if(row<3 && col<4){
													//if chain ends there, and it's MY chain, record it
													if(this.state[row+3][col+3]=='E'&&chainchar==player){
														if(firstEndBlocked){

															fts[2]++;
														}
														else
															fts[0]++;
													}
													else if(this.state[row+3][col+3]=='E'&&chainchar!=player){
														if(firstEndBlocked)
															fts[3]++;
														else
															fts[1]++;
													}

													unlock2D[row+1][col+1]=false;
													unlock2D[row+2][col+2]=false;
													//checking for length 4 chains aka wins
													if (unlock2D[row+3][col+3]){
														if(this.state[row+3][col+3]==chainchar&&chainchar==player){
															fts[10]++;
															unlock2D[row+3][col+3]=false;
														}
														else if(this.state[row+3][col+3]==chainchar&&chainchar!=player){
															fts[11]++;
															unlock2D[row+3][col+3]=false;
														}		
													}
												}
											}
										}
									}
								}
							}
						}
					}

				NEloopUpper:
					//going bottom to top checking for diagonal chains in the direction SW to NE
					for (int inc=0; inc<3; inc++){
						for(int row=0;row<6;row++){
							for(int col=0;col<6;col++){
								row=row+inc;
								//making sure we never go out of bounds with the row while changing the diagonal we're checking
								if(row>4)
									continue;
								if(this.state[row][col]!='E' && unlock2D[row][col]){
									chainchar=this.state[row][col];
									//checking the first end for possible block
									if(col>0 && row >0){
										if(this.state[row-1][col-1]=='E')
											firstEndBlocked=false;
										else
											firstEndBlocked=true;
									}
									else
										firstEndBlocked=true;

									//checking for chain of length 2 SW  to NE
									if(this.state[row+1][col+1]==chainchar && unlock2D[row+1][col+1]){
										//diagonal chain found of length 2 or higher, SW to NE
										if(col<5 && col>1 && row==4 && !firstEndBlocked){
											if(chainchar==player)
												fts[6]++;
											else if(chainchar!=player)
												fts[7]++;
											unlock2D[row+1][col+1]=false;
										}	
										if(row<4 && col<5){
											//if the chain ends there, record it 	
											if(this.state[row+2][col+2]=='E'&&chainchar==player){
												if(firstEndBlocked)
													fts[6]++;
												else
													fts[4]++;
												unlock2D[row+1][col+1]=false;
											}
											else if(this.state[row+2][col+2]=='E'&&chainchar!=player){
												if(firstEndBlocked)
													fts[7]++;
												else
													fts[5]++;
												unlock2D[row+1][col+1]=false;
											}
											else if(this.state[row+2][col+2]==chainchar && unlock2D[row+2][col+2]){
												//diagonal chain found, length 3 or more, SW to NE
												if(col<5 && col>0 && row==3 && !firstEndBlocked ){
													if(chainchar==player)
														fts[2]++;
													else if(chainchar!=player)
														fts[3]++;
													unlock2D[row+1][col+1]=false;
													unlock2D[row+2][col+2]=false;
												}	
												if(row<3 && col<4){
													//if chain ends there, and it's MY chain, record it
													if(this.state[row+3][col+3]=='E'&&chainchar==player){
														if(firstEndBlocked)
															fts[2]++;
														else
															fts[0]++;
													}
													else if(this.state[row+3][col+3]=='E'&&chainchar!=player){
														if(firstEndBlocked)
															fts[3]++;
														else
															fts[1]++;
													}
													unlock2D[row+1][col+1]=false;
													unlock2D[row+2][col+2]=false;
													//checking for length 4 chains aka wins
													if(this.state[row+3][col+3]==chainchar&&chainchar==player){
														fts[10]++;
														unlock2D[row+3][col+3]=false;
													}
													else if(this.state[row+3][col+3]==chainchar&&chainchar!=player){
														fts[11]++;
														unlock2D[row+3][col+3]=false;
													}		
												}
											}
										}
									}
								}
							}
						}
					}
					//calculating the state total values
					if(this.player=='X'){
						total+= fts[0]*wP1MyDouble3 + 
								fts[1]*wP1OpDouble3 +
								fts[2]*wP1MySingle3 +
								fts[3]*wP1OpSingle3 +
								fts[4]*wP1MyDouble2 +
								fts[5]*wP1OpDouble2 +
								fts[6]*wP1MySingle2 +
								fts[7]*wP1OpSingle2 +
								fts[8]*wP1MyDouble1 +
								fts[9]*wP1OpDouble1 +
								//win states and loss states have intense values
								fts[10]*1000000 +
								fts[11]*(-1000000);
						if(conclusive){
							if(fts[10]>0)
								winner="Player 1";
							else if(fts[11]>0)
								winner="Player 2";

						}
					}
					else if(this.player=='O'){
						total+= fts[0]*wP2MyDouble3 + 
								fts[1]*wP2OpDouble3 +
								fts[2]*wP2MySingle3 +
								fts[3]*wP2OpSingle3 +
								fts[4]*wP2MyDouble2 +
								fts[5]*wP2OpDouble2 +
								fts[6]*wP2MySingle2 +
								fts[7]*wP2OpSingle2 +
								fts[8]*wP2MyDouble1 +
								fts[9]*wP2OpDouble1 +
								//win states and loss states have intense values
								fts[10]*1000000 +
								fts[11]*(-1000000);
						if(conclusive){
							if(fts[10]>0)
								winner="Player 2";
							else if(fts[11]>0)
								winner="Player 1";
						}
					}

					if (conclusive){
						System.out.print("\nThe weight of the above board is: "+ total+" for Player "+this.player+"\n\n" );
					}
					if(conclusive && (fts[10]>0 || fts[11]>0)){
						System.out.println("The Winner Is: "+winner+"\n");
					}

					return total;
		}
		public char getHuman(){
			return human;
		}




		public void printBoard(){
			for(int q = 5; q >= 0; q--){
				System.out.print("| ");
				for(int u = 0; u < 7; u++){			
					System.out.print(state[q][u] + " | ");
				}
				System.out.println();

			}

		}
	}

}


