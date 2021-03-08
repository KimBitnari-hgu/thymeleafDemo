package com.example.demo.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data		//setter()/getter(), toString() 등 작업을 자동으로 생성해 준다.
@NoArgsConstructor
@AllArgsConstructor
public class FileBoardVO {

	private int b_no;
	private String title;
	private String content;
	private String writer;
	private Date reg_date;
}
