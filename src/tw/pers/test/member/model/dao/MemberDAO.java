package tw.pers.test.member.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tw.pers.test.member.model.bean.Member;

public class MemberDAO {
	private Connection conn;

	public MemberDAO(Connection conn) {
		this.conn = conn;
	}

	public void addMember(Member m) throws SQLException {
		final String SQL = "INSERT INTO Member(name, age, photo) VALUES(?, ?, ?)";

		PreparedStatement preState = conn.prepareStatement(SQL);

		preState.setString(1, m.getName());
		preState.setInt(2, m.getAge());
		preState.setBytes(3, m.getPhoto());

		preState.execute();

		preState.close();

	}

	public Member findMemberByID(Integer id) throws SQLException {
		final String SQL = "SELECT * FROM [Member] WHERE [id] = ?";

		PreparedStatement preState = conn.prepareStatement(SQL);
		preState.setInt(1, id);
		ResultSet rs = preState.executeQuery();

		Member m = new Member();
		if (rs.next()) {
			m.setId(rs.getInt("id"));
			m.setName(rs.getString("name"));
			m.setAge(rs.getInt("age"));
			m.setPhoto(rs.getBytes("photo"));
		} else {
			throw new SQLException("Member not found.");
		}

		rs.close();
		preState.close();

		return m;

	}

	public void deleteMemberByID(Integer id) throws SQLException {
		final String SQL = "DELETE FROM [Member] WHERE [id] = ?";

		PreparedStatement preState = conn.prepareStatement(SQL);
		preState.setInt(1, id);
		preState.execute();

		preState.close();

	}

	public void updateMember(Member m) throws SQLException {
		final String SQL = "UPDATE [Member] SET [name] = ?, [age] = ?, [photo] = ? WHERE [id] = ?";

		PreparedStatement preState = conn.prepareStatement(SQL);
		preState.setString(1, m.getName());
		preState.setInt(2, m.getAge());
		preState.setBytes(3, m.getPhoto());
		preState.setInt(4, m.getId());

		preState.executeUpdate();

		preState.close();

	}

	public List<Member> findAllMember() throws SQLException {
		final String SQL = "SELECT * FROM [Member]";

		PreparedStatement preState = conn.prepareStatement(SQL);
		ResultSet rs = preState.executeQuery();

		ArrayList<Member> mList = new ArrayList<>();
		while (rs.next()) {
			Member m = new Member();
			m.setId(rs.getInt("id"));
			m.setName(rs.getString("name"));
			m.setAge(rs.getInt("age"));
			m.setPhoto(rs.getBytes("photo"));
			mList.add(m);
		}

		rs.close();
		preState.close();

		return mList;
	}
}
