package tw.pers.test.member.model.bean;

import java.util.Arrays;
import java.util.Objects;

public class Member {

	private Integer id;
	private String name;
	private Integer age;
	private byte[] photo;

	public Member() {
		super();
	}

	public Member(String name) {
		super();
		this.name = name;
	}

	public Member(String name, Integer age, byte[] photo) {
		super();
		this.name = name;
		this.age = age;
		this.photo = photo;
	}

	public Member(Integer id, String name, Integer age, byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.photo = photo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "id=" + id + ", " + name + " | ";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		return Objects.equals(id, other.id);
	}

}
