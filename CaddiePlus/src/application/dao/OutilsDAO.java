package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import application.Connexion;
import java.sql.SQLException;

public class OutilsDAO {
	public static int  getNextAutoIncrement(String nomTable) throws SQLException{
		Connection conn = Connexion.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES "
		        									   +"WHERE TABLE_SCHEMA =? AND TABLE_NAME =?;");
		pstmt.setString(1, "CaddiePlusDataBase");
		pstmt.setString(2, nomTable);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()==true) {
			return rs.getInt("AUTO_INCREMENT");
		}else {
			return 1;
		}
	}
}
