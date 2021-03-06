
import java.util.Vector;

// sql classes that i need
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;

// time stuff
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/*
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 * 
 * NOTE: once you call the Query class, you MUST call
 * .close(), or else you WILL get a resource leak!
 */
public class Query {
	private static Connection conn;
	
	// DO NOT FORGE TO CALL .CLOSE()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void close() throws SQLException {
		if (conn != null)
			conn.close();
	}
	
	String IP_ADDRESS_DB = "database-1.ct0zvlj1qp3l.us-east-2.rds.amazonaws.com";
	int PORT_DB = 3306;

	String USERNAME_DB = "admin";
	String PASSWORD_DB = "sasouniscool";
	
	Query() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		if (conn == null) {
			conn = DriverManager.getConnection("jdbc:mysql://" + IP_ADDRESS_DB + ":"+ PORT_DB 
					+ "/chess?user=" + USERNAME_DB + "&password=" + PASSWORD_DB);
		}
	}

	
	// i am on the fence about declaring this as void.... should be boolean but im not sure
	public boolean autheticate(String username, String password) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT username, password FROM User "
					+ "WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next()) 
				return false;
			// check invalid password
			if (!rs.getString("password").equals(password)) 
				return false;
			
			return true;
		} catch (SQLException sqle) {
			// wtf do i do here? twiddle my thumbs? idk
			sqle.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public Vector<User> getTopPlayers(int threshold) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM User ORDER BY elo DESC");
			rs = ps.executeQuery();
			Vector<User> topPlayers = new Vector<User>();
			while (rs.next()) {
				if (topPlayers.size() >= threshold) 
					break;
			
				topPlayers.add(
						new User(
								rs.getInt("user_id"),
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("firstname"),
								rs.getString("lastname"),
								rs.getString("date_created"),
								rs.getInt("elo"),
								rs.getInt("num_wins"),
								rs.getInt("num_losses"),
								rs.getInt("num_ties"),
								rs.getInt("num_games")
								)
						);
			}
			return topPlayers;
		} catch (ParseException pe) {
			pe.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	
	public Vector<Game> getPlayerGames(String username) {
		
			Vector<Game> games = new Vector<Game>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			User player = searchUser(username);
			int id = player.id;
			try {
				String query = "SELECT * FROM Game WHERE white_player_id=? or black_player_id=? ORDER BY game_id DESC";
				System.out.println(query +" | " + id);
				ps = conn.prepareStatement(query);
				ps.setInt(1, id);
				ps.setInt(2, id);
				rs = ps.executeQuery();
				
				while(rs.next()) {
					games.add(new Game(
							rs.getInt("game_id"), 
							rs.getInt("white_player_id"), 
							rs.getInt("black_player_id"), 
							rs.getString("game_status"), 
							rs.getTime("start_time"), 
							rs.getTime("end_time"), 
							rs.getDate("start_time"), 
							rs.getDate("end_time"), 
							rs.getInt("white_player_elo"), 
							rs.getInt("black_player_elo")));
				}
				
				if(rs != null) 
					rs.close();
				if(ps != null)
					ps.close();
			} catch (SQLException sqle) {
				System.out.println("SQLE: " + sqle);
			}

			return games;
	}
	
	
	public User searchUser(String key, String value) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			System.out.println("SELECT * FROM User n WHERE " +key + "="+ value);
			
			ps = conn.prepareStatement("SELECT * FROM User "
					+ "WHERE ?=?");
			ps.setString(1, key);
			ps.setString(2, value);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next())  {
				return null;
			}
			
			return new User(
					rs.getInt("user_id"),
					rs.getString("username"),
					rs.getString("password"),
					rs.getString("firstname"),
					rs.getString("lastname"),
					rs.getString("date_created"),
					rs.getInt("elo"),
					rs.getInt("num_wins"),
					rs.getInt("num_losses"),
					rs.getInt("num_ties"),
					rs.getInt("num_games")
					);
			
		} catch(ParseException pe) {
			pe.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public User searchUser(int id) {
		
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			System.out.println("SELECT * FROM User n WHERE user_id="+ id);
			
			ps = conn.prepareStatement("SELECT * FROM User "
					+ "WHERE user_id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next())  {
				return null;
			}
							
			return new User(
					rs.getInt("user_id"),
					rs.getString("username"),
					rs.getString("password"),
					rs.getString("firstname"),
					rs.getString("lastname"),
					rs.getString("date_created"),
					rs.getInt("elo"),
					rs.getInt("num_wins"),
					rs.getInt("num_losses"),
					rs.getInt("num_ties"),
					rs.getInt("num_games")
					);
			
		} catch(ParseException pe) {
			pe.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public User searchUser(String username) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if(username.toLowerCase().equals("userisguest")) {
			return null;
		}
		
		try {
			ps = conn.prepareStatement("SELECT * FROM User "
					+ "WHERE username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next()) 
				return null;
			
			return new User(
					rs.getInt("user_id"),
					rs.getString("username"),
					rs.getString("password"),
					rs.getString("firstname"),
					rs.getString("lastname"),
					rs.getString("date_created"),
					
					rs.getInt("elo"),
					rs.getInt("num_wins"),
					rs.getInt("num_losses"),
					rs.getInt("num_ties"),
					rs.getInt("num_games")
					);
		} catch(ParseException pe) {
			pe.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			// wtf do i do here? twiddle my thumbs? idk
			sqle.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
		
	public void updatePlayerGamesPlayed(String whitePlayerUsername, String blackPlayerUsername, 
										String whiteGameState, long timeDiff) {
		System.out.println("updatePlayerGamesPlayed: " + whiteGameState);
		User white = searchUser(whitePlayerUsername);
		User black = searchUser(blackPlayerUsername);
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement gu = null;
		String winOrLoss = "";
		try {		
			
			if(whiteGameState.toLowerCase().equals("win")) {
				winOrLoss = "win";
				if(white != null) {
					ps = conn.prepareStatement("UPDATE User SET num_wins=num_wins+1, "
							+ "num_games=num_games+1 WHERE user_id=?;");
					ps.setInt(1, white.id);
					ps.executeUpdate();
				}
				
				if(black != null) {
					ps2 = conn.prepareStatement("UPDATE User SET num_losses=num_losses+1, "
							+ "num_games=num_games+1 WHERE user_id=?;");
					ps2.setInt(1, black.id);
					ps2.executeUpdate();
				}
				

			} else if (whiteGameState.toLowerCase().equals("loss")) {
				winOrLoss = "loss";
				if(black != null) {
					ps = conn.prepareStatement("UPDATE User SET num_wins=num_wins+1, "
							+ "num_games=num_games+1 WHERE user_id=?;");
					ps.setInt(1, black.id);
					ps.executeUpdate();
				}
				
				if(white != null ) {
					ps2 = conn.prepareStatement("UPDATE User SET num_losses=num_losses+1, "
							+ "num_games=num_games+1 WHERE user_id=?;");
					ps2.setInt(1, white.id);
					ps2.executeUpdate();
				}
			} else {
				winOrLoss = "tie";
				if(black != null) {
					ps = conn.prepareStatement("UPDATE User SET num_ties=num_ties+1, "
							+ "num_games=num_games+1 WHERE user_id=?;");
					ps.setInt(1, black.id);
					ps.executeUpdate();
				}
		
				if(white != null) {
					ps2 = conn.prepareStatement("UPDATE User SET num_ties=num_ties+1, "
							+ "num_games=num_games+1 WHERE user_id=?;");
					ps2.setInt(1, white.id);
					ps2.executeUpdate();
				}
			
			}
			
			SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Long timeNow = new Date().getTime();
			Long timeAtStart = timeNow - timeDiff;
			int whiteId = 0; 
			int blackId = 0;
			int whiteElo = 1000;
			int blackElo = 1000;
			
			if(white != null) {
				whiteId = white.id;
				whiteElo = white.elo;
			}
			
			if(black != null) {
				blackId = black.id;
				blackElo = black.elo;
			}
			
			
			//Create a new entry for the game played in the game table of the DB
			System.out.println(String.format("INSERT INTO Game(white_player_id, black_player_id, "
					+ "game_status, start_time, end_time, white_player_elo, black_player_elo) "
					+ "VALUES (%s, %s, %s, %s, %s, %s, %s)", 
					whiteId,blackId,winOrLoss,dateSdf.format(timeAtStart),dateSdf.format(timeNow),
					whiteElo,blackElo));
			
			
			gu =  conn.prepareStatement("INSERT INTO Game(white_player_id, black_player_id, "
					+ "game_status, start_time, end_time, white_player_elo, black_player_elo) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
			gu.setInt(1, whiteId);
			gu.setInt(2, blackId);
			gu.setString(3, winOrLoss);
			gu.setString(4, dateSdf.format(timeAtStart));
			gu.setString(5, dateSdf.format(timeNow));
			gu.setInt(6, whiteElo);
			gu.setInt(7, blackElo);
			gu.executeUpdate();
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				if (ps != null) ps.close();
				if (ps2 != null) ps2.close();
				if(gu != null) gu.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public void updateElo(String winningPlayersUsername, String LosingPlayersUsername) {
		System.out.println("Update Elo");
		
		User winner = searchUser(winningPlayersUsername);
		User loser = searchUser(LosingPlayersUsername);
		
		int wElo = 1000;
		int lElo = 1000;
		
		if(winner != null) {
			 wElo = winner.elo;
		}
		
		if(loser != null ) {
			lElo = loser.elo;
		}

		Double winningProbability =  1.0 / (1.0 + Math.pow(10.0,(lElo*1.0 - wElo*1.0)/400.0));
		//Double losingProbability = 1.0 / (1.0 + Math.pow(10.0,(wElo*1.0 - lElo*1.0)/400.0));
		
		Double multiplier = 100.0;

        int winningPlayerNewElo = (int)(multiplier * (1-winningProbability));
        int losingPlayerNewElo = (int)(multiplier * (1-winningProbability)*-1.0);
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			
			if(winner != null) {
				ps = conn.prepareStatement("UPDATE User SET elo=elo+? WHERE user_id=?;");
				ps.setInt(1, winningPlayerNewElo);
				ps.setInt(2, winner.id);
				ps.executeUpdate();
			}
			
			if(loser != null) {
				ps2 = conn.prepareStatement("UPDATE User SET elo=elo+? WHERE user_id=?;");
				ps2.setInt(1, losingPlayerNewElo);
				ps2.setInt(2, loser.id);
				ps2.executeUpdate();
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			try {
				if (ps != null) ps.close();
				if (ps2 != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
		
        
	}
	
	
	public int getTotalPlayers() {
		System.out.println("SELECT COUNT(user_id) FROM User");
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			
			ps = conn.prepareStatement("SELECT COUNT(user_id) FROM User;");
			rs = ps.executeQuery();
			
			// check invalid username
			if (!rs.next())  {
				return -1;
			}
			
			return rs.getInt("COUNT(user_id)");
		
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return -1;
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	
	public void createAccount(String username, String email, String password, 
							  String firstname, String lastname)
	{
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO \n"
					+ "User(username, email, password, firstname, lastname, date_created, elo, num_wins, num_losses,\n"
					+ "num_ties, num_games) \n"
					+ "VALUES\n"
					+ "(?, ?, ?, ?, ?, ?, 1000, 0, 0, 0, 0)");
			
			ps.setString(1, username);
			ps.setString(2, email);
			ps.setString(3, password);
			ps.setString(4, firstname);
			ps.setString(5, lastname);
			ps.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			ps.executeUpdate();

		} catch (SQLException sqle) {
			// wtf do i do here? twiddle my thumbs? idk
			sqle.printStackTrace();
			//return null;
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException sqle2) {
				sqle2.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		// le epic unit tests
		
		// authenticate
		Query q = null;
		try {  
			
			q = new Query(); 
			
			Long start = System.currentTimeMillis();
			q.getTopPlayers(10);
			Long end = System.currentTimeMillis();
			System.out.println(end-start);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe){
			
		}
		
		// getTopPlayers works!
		Vector<User> v = q.getTopPlayers(100);
		for (int i = 0; i < v.size(); ++i) {
			System.out.println(v.get(i).elo);
		}
		
		// NOTE: if you run this line of code twice, you will get a runtime error because I made each 'username' unique!
		// q.createAccount("user", "pass", "first", "last");
	}
}

/*
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
 * 
 */
