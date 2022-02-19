package com.sitechecker.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.sitechecker.header.MyHash;
import com.sitechecker.utils.Utils;

public class MySQLConnection implements AutoCloseable, Serializable {

    /**
     * 
     * 
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String database = "websitechecker";
    private final MysqlDataSource data;
    private String timeZone;

    private static MySQLConnection myDb;

    public static MySQLConnection getInstance() {
	if (myDb == null) {
	    myDb = new MySQLConnection();
	    myDb.DBInit();
	}
	return myDb;
    }

    public MySQLConnection() {
	data = new MysqlDataSource();
	File file = Utils.getSetupFile();
	try {
	    BufferedReader bufferreader = new BufferedReader(new FileReader(file));

	    String line = "";

	    line = bufferreader.readLine();

	    String[] token = line.split(",");
	    String user = Utils.simpleDec(token[0]);
	    data.setUser(user);

	    String pass = Utils.simpleDec(token[1].toString());

	    data.setPassword(pass);
	    String server = Utils.simpleDec(token[2].toString());

	    data.setServerName(server);
	    String port = Utils.simpleDec(token[3].toString());

	    data.setPort(Integer.parseInt(port));

	    timeZone = Utils.simpleDec(token[4].toString());

	    data.setDatabaseName(database);

	    bufferreader.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public int DBInit() {

	try {
	    data.setServerTimezone(timeZone);
	} catch (SQLException e1) {
	    e1.printStackTrace();
	}

	try {
	    Connection connection = data.getConnection();
	    if (connection.isValid(2000))
		connection.close();
	    return 1;

	} catch (SQLException e) {

	    e.printStackTrace();
	    return -1;
	}
    }

    public int isvalid() {
	Connection conn = null;

	try {
	    conn = data.getConnection();
	    if (conn.isValid(2000))
		return 1;
	    else
		return -1;
	} catch (SQLException e) {
	    return -1;
	} finally {
	    try {
		if (conn != null)
		    conn.close();
		;
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
    }

    public int saveSite(String id, String siteName, String type, Date date) {
	String query = "insert into targets(id_user, servername,type,data)" + "values(?,?,?,?);";

	Connection conn = null;
	PreparedStatement stmt = null;
	Timestamp sqlDate = Timestamp.from(date.toInstant());

	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setString(1, id);
	    stmt.setString(2, siteName);
	    stmt.setString(3, type);
	    stmt.setTimestamp(4, sqlDate);
	    return stmt.executeUpdate();
	} catch (Exception e) {
	    e.printStackTrace();
	    return -1;
	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    public int checkInsertInfo(int statusCode, String servername) {

	String query = "insert into serverinfo(id,status)" + "values((select id  from targets where servername =?"
		+ ")," + "?);";
	Connection conn = null;
	PreparedStatement stmt = null;

	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setString(1, servername);
	    stmt.setInt(2, statusCode);

	    return stmt.executeUpdate();
	} catch (Exception e) {
	    e.printStackTrace();
	    return -1;
	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    public int checkEmail(String email) {
	String query = "select exists(select email from users where email= ? );";
	Connection conn = null;
	int re = 0;
	ResultSet result = null;
	PreparedStatement stmt = null;

	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setString(1, email);
	    result = stmt.executeQuery();
	    result.next();
	    re = Integer.parseInt(result.getString(1));

	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;

	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return re;

    }

    public int checkServerOwner(String id_user) {
	String query = "select exists(select servername from targets where id_user=?);";
	Connection conn = null;
	int re = 0;
	ResultSet result = null;
	PreparedStatement stmt = null;

	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setString(1, id_user);
	    result = stmt.executeQuery();
	    result.next();
	    re = Integer.parseInt(result.getString(1));

	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;

	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return re;

    }

    public int deleteServer(String id) {

	Connection conn = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();

	    String query = "delete from targets where id = ?";

	    stmt = conn.prepareStatement(query);
	    stmt.setString(1, id);

	    return stmt.executeUpdate();

	} catch (Exception e) {
	    e.printStackTrace();
	    return -1;
	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    public int registerSimpleUser(String username, String email, String password) {
	Timestamp now = new Timestamp(new Date().getTime());
	Connection conn = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();

	    String hash = MyHash.getSaltedHash(password);

	    String query = "INSERT INTO users(username,email,password,registerdate)" + "values(?,?,?,?);";

	    stmt = conn.prepareStatement(query);
	    stmt.setString(1, username);
	    stmt.setString(2, email);
	    stmt.setString(3, hash);
	    stmt.setTimestamp(4, now);
	    return stmt.executeUpdate();

	} catch (Exception e) {
	    e.printStackTrace();
	    return -1;
	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    public List<String> logSimpleUser(String email, String password) {

	String query = "Select id,username, password,email,rank from users where email= ?;";
	Connection conn = null;
	List<String> li = null;
	ResultSet result = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setString(1, email);
	    result = stmt.executeQuery();

	    if (result.next()) {
		String rid = result.getString("id");
		String rusername = result.getString("username");
		String rpassword = result.getString("password");
		String remail = result.getString("email");

		String rrank = result.getString("rank");

		if (MyHash.check(password, rpassword)) {
		    li = new ArrayList<>();
		    li.add(rid);
		    li.add(rusername);
		    li.add(remail);
		    li.add(rrank);
		    return li;
		} else {

		    // pass don;t match
		    li = new ArrayList<>();
		    return li;
		}

	    } else {
		// no user in db.
		li = new ArrayList<>();
		li.add("0");
		return li;
	    }

	} catch (Exception e) {

	    e.printStackTrace();
	    // error
	    return null;
	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();

	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    public int getUserTotalSites(String id) {

	String query = "select count(servername) from targets where id_user= ?;";
	Connection conn = null;
	int re = 0;
	ResultSet result = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setString(1, id);
	    result = stmt.executeQuery();
	    if (result.next())
		re = Integer.parseInt(result.getString(1));

	} catch (Exception e) {

	    e.printStackTrace();
	    // error
	    return -1;
	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();

	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return re;

    }

    public int getTotalUsers() {

	String query = "select count(email) from users";
	Connection conn = null;
	int re = 0;
	ResultSet result = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    result = stmt.executeQuery();
	    if (result.next())
		re = Integer.parseInt(result.getString(1));

	} catch (Exception e) {

	    e.printStackTrace();
	    // error
	    return -1;
	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();

	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return re;

    }

    public int getServerCount() {

	String query = "select count(id) from targets;";
	Connection conn = null;
	int re = 0;
	ResultSet result = null;
	try {
	    conn = data.getConnection();
	    result = conn.prepareStatement(query).executeQuery();
	    result.next();
	    re = Integer.parseInt(result.getString(1));

	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;

	} finally {
	    try {
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return re;

    }

    public List<List<String>> getTargetInfo() {
	String query = "select id, servername,type,data from targets order by id;";
	Connection conn = null;
	ResultSet res = null;
	PreparedStatement stmt = null;

	List<List<String>> result = new ArrayList<>();
	List<String> ids = new ArrayList<>();
	List<String> servernames = new ArrayList<>();
	List<String> type = new ArrayList<>();
	List<String> data2 = new ArrayList<>();
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    res = stmt.executeQuery();
	    while (res.next()) {
		ids.add(res.getString("id"));
		servernames.add(res.getString("servername"));
		type.add(res.getString("type"));
		data2.add(res.getString("data"));
	    }
	    result.add(ids);
	    result.add(servernames);
	    result.add(type);
	    result.add(data2);
	    return result;
	} catch (Exception e) {

	    e.printStackTrace();
	    // error
	    return null;
	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    res.close();
		if (conn != null)
		    conn.close();

	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    public int getStatus(String ID) {
	String query = "select status from serverinfo where id=?;";
	Connection conn = null;
	int re = 0;
	ResultSet result = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setInt(1, Integer.parseInt(ID));
	    result = stmt.executeQuery();
	    boolean is = result.next();
	    if (is)
		re = Integer.parseInt(result.getString(1));

	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;

	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return re;

    }

    public int countDown() {
	String query = "select count(t.status) from serverinfo t where "
		+ "t.status=(select max(t2.status) from serverinfo t2 where t2.status=!200 and t2.status!=301);";
	Connection conn = null;
	int re = 0;
	ResultSet result = null;
	try {
	    conn = data.getConnection();
	    result = conn.prepareStatement(query).executeQuery();
	    result.next();
	    re = Integer.parseInt(result.getString(1));

	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;

	} finally {
	    try {
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return re;
    }

    public int count200() {
	String query = "select count( distinct id) from serverinfo where status=200 OR status=301;";
	Connection conn = null;
	int re = 0;
	ResultSet result = null;
	try {
	    conn = data.getConnection();
	    result = conn.prepareStatement(query).executeQuery();
	    result.next();
	    re = Integer.parseInt(result.getString(1));

	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;

	} finally {
	    try {
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return re;
    }

    public List<String> getLastCheckDate(String id) {
	String query = "select max(date) as date,max(status) as status from serverinfo where id=? group by id;";
	Connection conn = null;
	List<String> check = new ArrayList<String>();
	ResultSet result = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setInt(1, Integer.parseInt(id));
	    result = stmt.executeQuery();

	    while (result.next()) {
		check.add(result.getString("date"));
		check.add(result.getString("status"));
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;

	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return check;

    }

    public List<List<String>> getHistory(String id) {

	String query = "select date,status from serverinfo where id=?;";
	Connection conn = null;
	ResultSet res = null;
	PreparedStatement stmt = null;
	List<List<String>> result = new ArrayList<>();
	List<String> dates = new ArrayList<>();
	List<String> status = new ArrayList<>();
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setInt(1, Integer.parseInt(id));
	    res = stmt.executeQuery();
	    while (res.next()) {
		dates.add(res.getString("date"));
		status.add(res.getString("status"));
	    }
	    result.add(dates);
	    result.add(status);

	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;

	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (res != null)
		    res.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return result;

    }

    public List<String> getDistinctStatus(String id) {
	String query = "select distinct status from serverinfo where id=?;";
	Connection conn = null;
	List<String> cods = new ArrayList<>();
	ResultSet result = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setInt(1, Integer.parseInt(id));
	    result = stmt.executeQuery();
	    while (result.next()) {
		cods.add(result.getString("status"));
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;

	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return cods;

    }

    public int getDistinctStatusCount(String id, String code) {
	String query = "select count(status) from serverinfo where id= ? and status= ?";
	Connection conn = null;
	int countCode = 0;
	ResultSet result = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    stmt.setInt(1, Integer.parseInt(id));
	    stmt.setString(2, code);
	    result = stmt.executeQuery();
	    while (result.next()) {
		countCode = result.getInt(1);
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;

	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return countCode;

    }

    public List<Integer> getDistinctIds() {
	String query = "select distinct id from serverinfo;";
	Connection conn = null;
	List<Integer> cods = new ArrayList<>();
	ResultSet result = null;
	PreparedStatement stmt = null;
	try {
	    conn = data.getConnection();
	    stmt = conn.prepareStatement(query);
	    result = stmt.executeQuery();
	    while (result.next()) {
		cods.add(result.getInt("id"));
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	    return null;

	} finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (result != null)
		    result.close();
		if (conn != null)
		    conn.close();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return cods;

    }

    // bad bad bad
    public static Connection checkServer(String server, String user, String pass, int port, String timeZone) {
	MysqlDataSource source = new MysqlDataSource();
	source.setServerName(server);
	source.setUser(user);
	source.setPassword(pass);
	source.setPort(port);

	Connection conn = null;
	try {
	    source.setServerTimezone(timeZone);
	    conn = source.getConnection();
	    conn.isValid(2000);
	    return conn;
	} catch (SQLException e) {
	    System.out.println(e.getLocalizedMessage());
	    return null;
	}
    }

    public void destroy() {
	myDb = null;
	System.out.println("null mysql");

    }

    @Override
    public void close() throws Exception {

	data.setAutoClosePStmtStreams(true);
	// resultSet.close();
	// connection.close();

    }

}
