package com.hl.entity.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="person_info")
public class Person {
	@Id
    @Column(name="person_id")
	@GenericGenerator(name="systemUUID",strategy="uuid")
	@GeneratedValue(generator="systemUUID")
	private String id;
	private String name;
	private int age;
	//集合属性，保留该对象关联的学校
	@ElementCollection(targetClass=String.class)
	@CollectionTable(name="school_info",joinColumns=@JoinColumn(name="person_id",nullable=false))
	@Column(name="school_name")
	@OrderColumn(name="list_order")
	private List<String> schools;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<String> getSchools() {
		return schools;
	}
	public void setSchools(List<String> schools) {
		this.schools = schools;
	}

}
