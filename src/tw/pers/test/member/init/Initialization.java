package tw.pers.test.member.init;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tw.pers.test.member.model.bean.Member;
import tw.pers.test.member.model.dao.MemberDAO;
import tw.pers.test.member.util.ConnectionFactory;

public class Initialization {

	public static void Initialize() {
		try (Connection conn = ConnectionFactory.getConnection()) {

			MemberDAO mDAO = new MemberDAO(conn);

			Statement state = conn.createStatement();

			state.execute(
					"CREATE TABLE IF NOT EXISTS Member(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, age INTEGER, photo BLOB)");

			state.close();

			if (conn.createStatement().executeQuery("SELECT [id] FROM [Member]").next()) {
				return;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					Initialization.class.getClassLoader().getResourceAsStream("initialization_data/Member.json")));
			List<Member> mList = new Gson().fromJson(br, new TypeToken<List<Member>>() {
			}.getType());
			br.close();

			mList.forEach(m -> {
				try {
					BufferedInputStream bis = new BufferedInputStream(Initialization.class.getClassLoader()
							.getResourceAsStream("initialization_data/user-type-B-" + m.getId() + ".png"));

					m.setPhoto(bis.readAllBytes());

					bis.close();

					mDAO.addMember(m);

				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
