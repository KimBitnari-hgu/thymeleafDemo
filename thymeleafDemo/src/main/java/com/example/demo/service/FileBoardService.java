package com.example.demo.service;

import java.util.List;

import com.example.demo.bean.FileBoardVO;
import com.example.demo.bean.FileVO;

public interface FileBoardService {

	//Create
	//게시글 생성 
	int createFileBoard(FileBoardVO fileBoard);
	//파일 업로드
	int createFile(FileVO file);
		
		
	//Read
	// 게시글 리스트 출력
	List<FileBoardVO> readFileBoardList();
	//게시글 세부내용 보기 
	FileBoardVO readFileBoardDetail(int b_no);
	//파일 다운로드
	FileVO readFileDetail(int b_no);
		
		
	//Update
	//게시글 수정 
	int updateFileBoard(FileBoardVO fileBoard);
		
		
	//Delete
	//게시글 삭제 
	int deleteFileBoard(int b_no);
}
