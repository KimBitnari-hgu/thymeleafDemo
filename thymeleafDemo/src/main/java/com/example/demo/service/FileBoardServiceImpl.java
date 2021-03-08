package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.FileBoardVO;
import com.example.demo.bean.FileVO;
import com.example.demo.mapper.FileBoardMapper;

@Service
public class FileBoardServiceImpl implements FileBoardService {

	@Autowired
	FileBoardMapper fileBoardMapper;
	
	//Create
	//게시글 생성 
	@Override
	public int createFileBoard(FileBoardVO fileBoard) {
		return fileBoardMapper.createFileBoard(fileBoard);
	}
	//파일 업로드
	@Override
	public int createFile(FileVO file) {
		return fileBoardMapper.createFile(file);
	}
		
		
	//Read
	// 게시글 리스트 출력
	@Override
	public List<FileBoardVO> readFileBoardList() {
		return fileBoardMapper.readFileBoardList();
	}
	//게시글 세부내용 보기 
	@Override
	public FileBoardVO readFileBoardDetail(int b_no) {
		return fileBoardMapper.readFileBoardDetail(b_no);
	}
	//파일 다운로드
	@Override
	public FileVO readFileDetail(int b_no) {
		return fileBoardMapper.readFileDetail(b_no);
	}
		
		
	//Update
	//게시글 수정 
	@Override
	public int updateFileBoard(FileBoardVO fileBoard) {
		return fileBoardMapper.updateFileBoard(fileBoard);
	}
		
		
	//Delete
	//게시글 삭제 
	@Override
	public int deleteFileBoard(int b_no) {
		return fileBoardMapper.deleteFileBoard(b_no);
	}
}
